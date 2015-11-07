package com.github.dadeo.labelprinter.gui

import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.EmptyBorder
import javax.swing.plaf.UIResource
import javax.swing.table.TableCellRenderer
import java.awt.*

class HighlightingBooleanCellRenderer extends JCheckBox implements TableCellRenderer, UIResource {
    private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    HighlightingBooleanCellRenderer() {
        super();
        setHorizontalAlignment(JLabel.CENTER);
        setBorderPainted(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setSelected((value != null && ((Boolean) value).booleanValue()));

        if (hasFocus) {
            setBorder(BorderFactory.createLineBorder(Color.black, 1));
        } else {
            setBorder(noFocusBorder);
        }

        return this;
    }
}
