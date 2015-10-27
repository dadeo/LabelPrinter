package com.github.dadeo.labelprinter.gui

import sun.swing.table.DefaultTableCellHeaderRenderer

import javax.swing.*
import javax.swing.table.TableCellRenderer
import java.awt.*

class LabelTableCellHeaderRenderer implements TableCellRenderer {
    private DefaultTableCellHeaderRenderer delegate = new DefaultTableCellHeaderRenderer()
    private LabelTableModel model = new LabelTableModel()

    @Override
    Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        delegate.horizontalAlignment = model.justificationForHeading((String) value)
        delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
    }
}