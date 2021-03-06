package com.github.dadeo.labelprinter.gui

import javax.swing.*
import java.awt.*
import java.awt.event.*
import java.util.List

class TablePanel extends JPanel {
    private JTable table
    private LabelTableModel tableModel
    private JPopupMenu popup
    LabelTableListener personTableListener

    TablePanel() {

        Action performDelete = new AbstractAction('Delete row') {
            public void actionPerformed(ActionEvent e) {
                int startIndex = table.selectionModel.getMinSelectionIndex()
                int lastIndex = table.selectionModel.getMaxSelectionIndex()

                if (startIndex == -1) return

                table.removeEditor()
                (startIndex..lastIndex).findAll {
                    table.selectionModel.isSelectedIndex(it)
                }.reverse().each { int index ->
                    personTableListener?.rowDeleted(index)
                    tableModel.fireTableRowsDeleted(index, index)
                }

                if (table.rowCount > startIndex)
                    table.selectionModel.setSelectionInterval(startIndex, startIndex)
                else if (table.rowCount > 0)
                    table.selectionModel.setSelectionInterval(startIndex - 1, startIndex - 1)
            }
        }

        popup = new JPopupMenu()
        popup.add(performDelete)

        tableModel = new LabelTableModel()
        tableModel.labelTableModelListener = new LabelTableModelListener() {
            @Override
            void labelUpdated(int row) {
                personTableListener?.labelUpdated(row)
            }
        }

        table = new JTable(tableModel)
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
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

        table.setDefaultRenderer(Number, new HighlightingNumberCellRenderer())
        table.setDefaultRenderer(Boolean, new HighlightingBooleanCellRenderer())
        table.setDefaultEditor(Boolean, new HightlightingBooleanCellEditor())

        table.addMouseListener(new MouseAdapter() {
            @Override
            void mousePressed(MouseEvent e) {
                if (e.button == MouseEvent.BUTTON3) {
                    popup.show(table, e.x, e.y)
                } else {
                    int row = table.getSelectedRow()
                    int column = table.getSelectedColumn()

                    if (row == -1 || column == -1) return

                    table.editCellAt(row, column)
                }
            }
        })

        table.addKeyListener(new KeyAdapter() {
            @Override
            void keyReleased(KeyEvent e) {
                int row = table.getSelectedRow()
                int column = table.getSelectedColumn()

                if (row == -1 || column == -1) return

                table.editCellAt(row, column)
            }
        })

        layout = new BorderLayout()
        add(new JScrollPane(table), BorderLayout.CENTER)


        table.getActionMap().put("performDelete", performDelete)
        table.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.META_MASK), "performDelete")
    }

    void setData(List<Label> data) {
        tableModel.data = data
        refresh()
    }

    void stopEditing() {
        if (table.isEditing())
            table.removeEditor()
    }

    void refresh() {
        stopEditing()
        tableModel.fireTableDataChanged()
    }

    @Override
    void requestFocus() {
        table.requestFocus()

        def forceToColumn = { it == -1 ? 0 : it }

        def forceToEditableColumn = { int row ->
            (0..<table.columnCount).find { table.isCellEditable(row, it) } ?: 0
        }

        if (table.rowCount > 0) {
            int row = forceToColumn(table.selectedRow)
            int column = table.selectedColumn == -1 ? forceToEditableColumn(row) : table.selectedColumn

            table.removeEditor()

            table.setRowSelectionInterval(row, row)
            table.editCellAt(row, column)
        }
    }

    List<Integer> getSelectedLabelIndices() {
        if (table.selectedRowCount <= 1)
            []
        else
            table.selectedRows as List
    }
}