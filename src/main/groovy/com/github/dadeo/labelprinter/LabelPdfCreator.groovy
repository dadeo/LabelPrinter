package com.github.dadeo.labelprinter

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font

import java.awt.print.PrinterException

final class LabelPdfCreator {

    /**
     * Entry point.
     */
    static void main(String[] args) throws PrinterException, IOException {
        String filename = '/Users/dadeo/Desktop/my.pdf'

        LabelContent person = new LabelContent(
                line1: "Pinky Jones",
                line2: "PO Box 123",
                line3: "Polk City, IA 50220",
                line4: "USA"
        )

        List<LabelContent> labels = (1..54).collect { LabelContent clone = (LabelContent) person.clone(); clone.line1 += " $it"; clone }

        LabelExclusionMatrix labelExclusionMatrix = new LabelExclusionMatrix()
                .exclude(0, 0, 0)
                .exclude(0, 1, 0)
                .exclude(0, 0, 1)
                .exclude(0, 1, 1)
                .exclude(1, 1, 4)
                .exclude(1, 1, 5)

        LabelPageDescriptor avery5160 = [
                page : [height: 11, marginX: 0.5, marginY: 0.75] as PageProperties,
                label: [width: 2.75, height: 1.0, columns: 3, rows: 10] as LabelProperties
        ]

        LabelPageDescriptor avery5163 = [
                page : [height: 11, marginX: 0.5, marginY: 0.75] as PageProperties,
                label: [width: 4.0, height: 2.0, columns: 2, rows: 5] as LabelProperties
        ]

        LabelDocument labelDocument = new LabelPdfCreator().createLabels(labels, avery5160, labelExclusionMatrix)

        try {
            labelDocument.printWithDialog()
            labelDocument.save(filename)
        }
        finally {
            labelDocument.close()
        }
    }

    LabelDocument createLabels(List<LabelContent> labelContent, LabelPageDescriptor labelDescriptor, LabelExclusionMatrix labelExclusionMatrix) {
        PDDocument doc = new PDDocument()

        try {
            while (labelContent)
                labelContent = createPage(doc, labelContent, labelDescriptor, labelExclusionMatrix)
        } catch (e) {
            doc.close()
            throw e
        }

        new LabelDocument(doc)
    }

    private List<LabelContent> createPage(PDDocument document, List<LabelContent> labels, LabelPageDescriptor descriptor, LabelExclusionMatrix labelExclusionMatrix) {
        float ppi = 72
        float pageTop = descriptor.page.height * ppi
        float pageLeft = 0
        float marginY = descriptor.page.marginY * ppi
        float marginX = descriptor.page.marginX * ppi
        float labelWidth = descriptor.label.width * ppi
        float labelHeight = descriptor.label.height * ppi
        float lineHeight = 12
        float startY = pageTop - marginY
        float startX = pageLeft + marginX
        FPoint currentLine = new FPoint(startX, startY)
        FPoint currentPosition = currentLine

        int pageIndex = document.pages.size()
        PDPage page = new PDPage()
        document.addPage(page)

        PDFont font = PDType1Font.HELVETICA_BOLD

        PDPageContentStream contents = new PDPageContentStream(document, page)

        int i = 0

        List<LabelContent> printedLabels = []

        def stampAddress = { FPoint pos, LabelContent content ->
            println content
            contents.beginText()
            contents.setFont(font, 12)
            contents.newLineAtOffset(pos.x, pos.y)
            contents.showText(content.line1)
            contents.endText()

            contents.beginText()
            contents.newLineAtOffset(pos.x, (float) (pos.y - lineHeight))
            contents.showText(content.line2)
            contents.endText()

            contents.beginText()
            contents.newLineAtOffset(pos.x, (float) (pos.y - lineHeight * 2))
            contents.showText(content.line3)
            contents.endText()

            contents.beginText()
            contents.newLineAtOffset(pos.x, (float) (pos.y - lineHeight * 3))
            contents.showText(content.line4)
            contents.endText()

            printedLabels << content
            ++i
        }

        descriptor.label.rows.times { int row ->
            descriptor.label.columns.times { int col ->
                if (!labelExclusionMatrix.isLabelExcluded(pageIndex, col, row)) {
                    LabelContent label = labels[i]
                    if (!label)
                        return []

                    stampAddress currentPosition, label
                }
                currentPosition = currentPosition.adjust(labelWidth, 0)
            }
            currentLine = currentLine.adjust(0, -labelHeight)
            currentPosition = currentLine
        }

        contents.close()

        labels - printedLabels
    }


}