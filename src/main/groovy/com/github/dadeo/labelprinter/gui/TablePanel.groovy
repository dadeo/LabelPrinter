package com.github.dadeo.labelprinter.gui

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.List

class TablePanel extends JPanel {
    private JTable table
    private LabelTableModel tableModel
    private JPopupMenu popup
    LabelTableListener personTableListener

    TablePanel() {
        JMenuItem removeItem = new JMenuItem("Delete row")
        removeItem.addActionListener { ActionEvent e ->
            int index = table.selectionModel.getMinSelectionIndex()
            if (personTableListener) {
                personTableListener.rowDeleted(index)
                tableModel.fireTableRowsDeleted(index, index)
            }
        } as ActionListener

        popup = new JPopupMenu()
        popup.add(removeItem)

        tableModel = new LabelTableModel()
        table = new JTable(tableModel)
        table.tableHeader.defaultRenderer = new LabelTableCellHeaderRenderer()
        table.rowHeight = 25
        table.getColumnModel().getColumn(0).setMaxWidth(50)
        table.getColumnModel().getColumn(1).setMinWidth(150)
        table.getColumnModel().getColumn(2).setMinWidth(150)
        table.getColumnModel().getColumn(3).setMinWidth(150)
        table.getColumnModel().getColumn(4).setMinWidth(150)
        table.getColumnModel().getColumn(1).setPreferredWidth(100)
        table.getColumnModel().getColumn(2).setPreferredWidth(100)
        table.getColumnModel().getColumn(3).setPreferredWidth(100)
        table.getColumnModel().getColumn(4).setPreferredWidth(100)
        table.addMouseListener(new MouseAdapter() {
            @Override
            void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.point)
                table.selectionModel.setSelectionInterval(row, row)
                if (e.button == MouseEvent.BUTTON3) {
                    popup.show(table, e.x, e.y)
                }
            }
        })
        layout = new BorderLayout()
        add(new JScrollPane(table), BorderLayout.CENTER)
    }

    void setData(List<Label> data) {
        tableModel.data = data
        refresh()
    }

    void refresh() {
        tableModel.fireTableDataChanged()
    }

}