package com.github.dadeo.labelprinter.gui

import javax.swing.*
import javax.swing.table.DefaultTableCellRenderer
import java.awt.*

class HighlightingNumberCellRenderer extends DefaultTableCellRenderer {
    public HighlightingNumberCellRenderer() {
        super();
        setHorizontalAlignment(JLabel.RIGHT);
    }

    @Override
    Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)

        if (hasFocus) {
            c.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        } else {
            c.setBorder(noFocusBorder);
        }

        c
    }
}