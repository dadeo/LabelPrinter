package com.github.dadeo.labelprinter.gui

import javax.swing.*
import java.awt.*

class HightlightingBooleanCellEditor extends DefaultCellEditor {
    public HightlightingBooleanCellEditor() {
        super(new JCheckBox());
        JCheckBox checkBox = (JCheckBox) getComponent();
        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 int row, int column) {
        JCheckBox checkBox = super.getTableCellEditorComponent(table, value, isSelected, row, column)
        checkBox.setBorder(BorderFactory.createLineBorder(Color.black, 1))
    }
}