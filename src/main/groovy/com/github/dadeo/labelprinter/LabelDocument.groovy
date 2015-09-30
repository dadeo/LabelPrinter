package com.github.dadeo.labelprinter

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.printing.PDFPageable

import java.awt.print.PrinterJob

class LabelDocument {
    private PDDocument document

    LabelDocument(document) {
        this.document = document
    }

    void save(String filename) {
        document.save(filename)
    }

    void save(File file) {
        document.save(file)
    }

    void save(OutputStream stream) {
        document.save(stream)
    }

    void printWithDialog() {
        PrinterJob job = PrinterJob.getPrinterJob()
        job.setPageable(new PDFPageable(document))

        if (job.printDialog()) {
            job.print()
        }

    }

    void close() {
        document.close()
    }
}