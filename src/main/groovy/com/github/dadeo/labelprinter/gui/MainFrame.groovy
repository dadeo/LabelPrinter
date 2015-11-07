package com.github.dadeo.labelprinter.gui

import com.github.dadeo.labelprinter.controller.Controller

import javax.swing.*
import javax.swing.filechooser.FileFilter
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.prefs.Preferences

class MainFrame extends JFrame {

    private ToolBar toolBar
    private TablePanel tablePanel
    private FormPanel formPanel
    private JFileChooser fileChooser
    private Controller controller
    private Preferences preferences
    private JSplitPane splitPane

    MainFrame() {
        super("Label Maker Pro")

        layout = new BorderLayout()

        toolBar = new ToolBar()
        formPanel = new FormPanel()
        tablePanel = new TablePanel()
        preferences = Preferences.userRoot().node("labelMakerPro")

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tablePanel)
        splitPane.oneTouchExpandable = true

        controller = new Controller()

        tablePanel.setData(controller.findAllLabels())
        tablePanel.setPersonTableListener(new LabelTableListener() {
            @Override
            void labelUpdated(int row) {
                controller.updateLabel(row)
            }

            void rowDeleted(int row) {
                controller.removeLabel(row)
            }
        })

        fileChooser = new JFileChooser()
        fileChooser.fileFilter = new FileFilter() {
            @Override
            boolean accept(File f) {
                f.isDirectory() || f.name.endsWith('.per')
            }

            @Override
            String getDescription() {
                "Person database files(*.per)"
            }
        }

        toolBar.toolBarListener = new ToolBarListener() {
            @Override
            void saveEventOccurred() {
                try {
                    tablePanel.save()
                    controller.save()
                } catch (e) {
                    e.printStackTrace()
                    JOptionPane.showMessageDialog(MainFrame.this, "Cannot save to the database.", "Database Save Problem", JOptionPane.ERROR_MESSAGE)
                }
            }

            @Override
            void refreshEventOccurred() {
                try {
                    refreshPersonData()
                } catch (e) {
                    e.printStackTrace()
                    JOptionPane.showMessageDialog(MainFrame.this, "Cannot refresh from the database.", "Database Refresh Problem", JOptionPane.ERROR_MESSAGE)
                }
            }

            @Override
            void printEventOccurred() {
                controller.print()
            }

        }

        formPanel.formListener = { FormEvent e ->
            controller.addLabel(e)
            tablePanel.refresh()
            formPanel.reset()
            formPanel.requestFocus()
        }

        String user = preferences.get("user", "sa")
        String password = preferences.get("password", "")
        int port = preferences.getInt("port", 3306)
        controller.configure(port, user, password)

        add(toolBar, BorderLayout.PAGE_START)
        add(splitPane, BorderLayout.CENTER)

        this.JMenuBar = createMenuBar()

        restorePreviousSizeAndLocation(preferences)
        setMinimumSize(new Dimension(1200, 400))

        defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE

        addWindowListener(new WindowAdapter() {
            @Override
            void windowClosing(WindowEvent e) {
                Point windowLocation = MainFrame.this.location
                preferences.putInt('MainPanel.location.x', windowLocation.x as int)
                preferences.putInt('MainPanel.location.y', windowLocation.y as int)

                def currentSize = MainFrame.this.size
                preferences.putDouble('MainPanel.width', currentSize.width)
                preferences.putDouble('MainPanel.height', currentSize.height)

                dispose()
                System.gc()
            }
        })

        refreshPersonData()

        visible = true

        formPanel.requestFocus()
    }

    protected void restorePreviousSizeAndLocation(Preferences preferences) {
        location = new Point(preferences.getInt('MainPanel.location.x', 0), preferences.getInt('MainPanel.location.y', 0))

        Dimension lastSize = new Dimension((int) preferences.getDouble('MainPanel.width', 1200),
                                           (int) preferences.getDouble('MainPanel.height', 400))
        setSize(lastSize)
    }

    private void refreshPersonData() {
        controller.refresh()
        tablePanel.refresh()
    }

    private JMenuBar createMenuBar() {

        JMenuItem exportDataItem = new JMenuItem("Export Data...")
        exportDataItem.addActionListener { ActionEvent e ->
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    controller.saveData(fileChooser.selectedFile)
                } catch (error) {
                    JOptionPane.showMessageDialog(this, "Could not save data to file.", "Error", JOptionPane.ERROR_MESSAGE)
                    error.printStackTrace()
                }

            }
        }
        JMenuItem importDataItem = new JMenuItem("Import Data...")
        importDataItem.addActionListener { ActionEvent e ->
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    controller.loadData(fileChooser.selectedFile)
                    tablePanel.refresh()
                } catch (error) {
                    JOptionPane.showMessageDialog(this, "Could not load data from file.", "Error", JOptionPane.ERROR_MESSAGE)
                    error.printStackTrace()
                }
            }
        }
        JMenuItem exitItem = new JMenuItem("Exit")
        exitItem.addActionListener { ActionEvent e ->
            int action = JOptionPane.showConfirmDialog(this, "Do you really want to exit?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION)
            if (action == JOptionPane.OK_OPTION) {
                windowListeners.each { it.windowClosing(new WindowEvent(this, 0)) }
            }
        }

        JMenu fileMenu = new JMenu("File")
        fileMenu.add(exportDataItem)
        fileMenu.add(importDataItem)
        fileMenu.addSeparator()
        fileMenu.add(exitItem)

        importDataItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.META_MASK)
        exportDataItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.META_MASK)
        exitItem.mnemonic = KeyEvent.VK_X
        exitItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK)
        fileMenu.mnemonic = KeyEvent.VK_F

        JMenu showMenu = new JMenu("Show")
        JMenuItem showFormItem = new JCheckBoxMenuItem("Person Form")
        showFormItem.selected = true
        showFormItem.addActionListener { ActionEvent e ->
            JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.source
            if (menuItem.isSelected()) {
                splitPane.dividerLocation = (int) formPanel.minimumSize.width
            }
            formPanel.visible = menuItem.isSelected()
        }
        showMenu.add(showFormItem)

        JMenu windowMenu = new JMenu("Window")
        windowMenu.add(showMenu)

        JMenuBar menuBar = new JMenuBar()
        menuBar.add(fileMenu)
        menuBar.add(windowMenu)

        menuBar
    }
}