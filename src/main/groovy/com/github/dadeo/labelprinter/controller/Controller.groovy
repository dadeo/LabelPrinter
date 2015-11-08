package com.github.dadeo.labelprinter.controller

import com.github.dadeo.labelprinter.gui.FormEvent
import com.github.dadeo.labelprinter.model.H2Database
import com.github.dadeo.labelprinter.model.Label
import com.github.dadeo.labelprinter.pdf.*

class Controller {
    private H2Database database = new H2Database()

    void addLabel(FormEvent e) {
        Label p = new Label(line1: e.line1, line2: e.line2, line3: e.line3, line4: e.line4)
        database.addLabel(p)
    }

    void removeLabel(int index) {
        database.removeLabel(index)
    }

    void updateLabel(int index) {
        database.updateLabel(index)
    }

    List<Label> findAllLabels() {
        database.labels
    }

    void loadData(File dbFile) {
        database.loadFromFile(dbFile)
    }

    void saveData(File dbFile) {
        database.saveToFile(dbFile)
    }

    void refresh() {
        database.load()
    }

    void configure(int port, String user, String password) {
        database.configure(port, user, password)
    }

    void printLabels(List<Integer> indices) {
        List<Label> allLabels = findAllLabels()
        List<Label> labels = indices ? indices.collect { allLabels[it] } : allLabels


        List<LabelContent> labelContents = labels.collect {
            new LabelContent(line1: it.line1, line2: it.line2, line3: it.line3, line4: it.line4)
        }

        LabelExclusionMatrix labelExclusionMatrix = new LabelExclusionMatrix()
//                .exclude(0, 0, 0)
//                .exclude(0, 1, 0)
//                .exclude(0, 0, 1)
//                .exclude(0, 1, 1)
//                .exclude(1, 1, 4)
//                .exclude(1, 1, 5)

        LabelPageDescriptor avery5160 = [
            page : [height: 11, marginX: 0.5, marginY: 0.75] as PageProperties,
            label: [width: 2.75, height: 1.0, columns: 3, rows: 10] as LabelProperties
        ]

        LabelPageDescriptor avery5163 = [
            page : [height: 11, marginX: 0.5, marginY: 0.75] as PageProperties,
            label: [width: 4.0, height: 2.0, columns: 2, rows: 5] as LabelProperties
        ]

        LabelDocument labelDocument = new LabelPdfCreator().createLabels(labelContents, avery5160, labelExclusionMatrix)

        try {
            labelDocument.printWithDialog()
        }
        finally {
            labelDocument.close()
        }
    }
}