package com.github.dadeo.labelprinter.gui

import com.github.dadeo.labelprinter.model.Label

import javax.swing.*
import javax.swing.table.AbstractTableModel

class LabelTableModel extends AbstractTableModel {
    LabelTableModelListener labelTableModelListener

    private List<Map<String, ?>> columnDefinitions = [
            [fieldName: 'id', headingName: 'ID', type: Integer, alignment: JLabel.CENTER],
            [fieldName: 'line1', headingName: 'Line 1', type: String, alignment: JLabel.LEFT],
            [fieldName: 'line2', headingName: 'Line 2', type: String, alignment: JLabel.LEFT],
            [fieldName: 'line3', headingName: 'Line 3', type: String, alignment: JLabel.LEFT],
            [fieldName: 'line4', headingName: 'Line 4', type: String, alignment: JLabel.LEFT],
            [fieldName: 'printed', headingName: 'Printed', type: Boolean, alignment: JLabel.CENTER],
    ]
    private List<Label> data = []

    void setData(List<Label> data) {
        this.data = data
    }

    @Override
    Class<?> getColumnClass(int columnIndex) {
        Map columnDefinition = columnDefinitions[columnIndex]
        columnDefinition.type
    }

    @Override
    void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (data == null) return

        Label label = data[rowIndex]
        switch (columnIndex) {
            case 1:
                label.line1 = value
                break
            case 2:
                label.line2 = value
                break
            case 3:
                label.line3 = value
                break
            case 4:
                label.line4 = value
                break
            case 5:
                label.printed = value
                break
        }

        labelTableModelListener?.labelUpdated(rowIndex)
    }

    @Override
    boolean isCellEditable(int rowIndex, int columnIndex) {
        [1, 2, 3, 4, 5].contains(columnIndex)
    }

    @Override
    int getRowCount() {
        data.size()
    }

    @Override
    int getColumnCount() {
        columnDefinitions.size()
    }

    @Override
    String getColumnName(int column) {
        columnDefinitions[column].headingName
    }

    @Override
    Object getValueAt(int rowIndex, int columnIndex) {
        Label label = data[rowIndex]
        Map columnDefinition = columnDefinitions[columnIndex]
        label[columnDefinition.fieldName]
    }

    int justificationForHeading(String heading) {
        Map<String, ?> definition = columnDefinitions.find { it.headingName == heading }
        if (definition)
            definition.alignment
        else
            JLabel.LEFT
    }
}