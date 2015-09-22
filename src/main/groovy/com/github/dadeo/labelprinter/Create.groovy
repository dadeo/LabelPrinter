package com.github.dadeo.labelprinter

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.printing.PDFPageable

import java.awt.print.PrinterException
import java.awt.print.PrinterJob

/**
 * Examples of various different ways to print PDFs using PDFBox.
 */
final class Create {
    private Create() {
    }

    /**
     * Entry point.
     */
    static void main(String[] args) throws PrinterException, IOException {
        String filename = '/Users/dadeo/Desktop/my.pdf'
        def person = [
                name    : "Pinky Jones",
                address1: "PO Box 123",
                address2: "Polk City, IA 50220",
        ]

        float ppi = 72
        float pageTop = 11 * ppi
        float pageLeft = 0
        float marginY = 0.75 * ppi
        float marginX = 0.5 * ppi
        float labelWidth = 2.75 * ppi
        float labelHeight = 1.0 * ppi
        float lineHeight = 12
        float startY = pageTop - marginY
        float startX = pageLeft + marginX
        FPoint currentLine = new FPoint(startX, startY)
        FPoint currentPosition = currentLine
        PDDocument doc = new PDDocument();
        try {
            PDPage page = new PDPage();
            doc.addPage(page);

            PDFont font = PDType1Font.HELVETICA_BOLD;

            PDPageContentStream contents = new PDPageContentStream(doc, page);

            def stampAddress = { FPoint pos, p ->
                println "$pos.x, $pos.y"
                contents.beginText();
                contents.setFont(font, 12);
                contents.newLineAtOffset(pos.x, pos.y);
                contents.showText(p.name);
                contents.endText();

                contents.beginText();
                contents.newLineAtOffset(pos.x, (float) (pos.y - lineHeight));
                contents.showText(p.address1);
                contents.endText();

                contents.beginText();
                contents.newLineAtOffset(pos.x, (float) (pos.y - lineHeight * 2));
                contents.showText(p.address2);
                contents.endText();
            }

            10.times {
                3.times {
                    stampAddress currentPosition, person
                    currentPosition = currentPosition.adjust(labelWidth, 0)
                    return
                }
                currentLine = currentLine.adjust(0, -labelHeight)
                currentPosition = currentLine
            }

            contents.close();

            printWithDialog(doc)

            doc.save(filename);
        }
        finally {
            doc.close();
        }
    }

    private static void printWithDialog(PDDocument document) throws IOException, PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob()
        job.setPageable(new PDFPageable(document))

        if (job.printDialog()) {
            job.print()
        }
    }


}