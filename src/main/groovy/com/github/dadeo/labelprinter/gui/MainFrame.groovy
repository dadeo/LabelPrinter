package com.github.dadeo.labelprinter.gui

import com.apple.eawt.Application
import com.apple.eawt.QuitHandler
import com.github.dadeo.labelprinter.controller.Controller

import javax.swing.*
import javax.swing.filechooser.FileFilter
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.prefs.Preferences

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW

class MainFrame extends JFrame {

    private ToolBar toolBar
    private TablePanel tablePanel
    private FormPanel formPanel
    private JFileChooser fileChooser
    private Controller controller
    private Preferences preferences
    private JSplitPane splitPane

    MainFrame(String version) {
        super("Label Maker Pro - $version")

        JFrame thisFrame = this

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

        Action performSetFocus = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                tablePanel.stopEditing()
                formPanel.requestFocus()
            }
        }

        ((JPanel) this.contentPane).getActionMap().put("performSetFocus", performSetFocus);
        ((JPanel) this.contentPane).getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.META_MASK), "performSetFocus");

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

        Application.application.setQuitHandler(new QuitHandler() {
            @Override
            public void handleQuitRequestWith(com.apple.eawt.AppEvent.QuitEvent qe, com.apple.eawt.QuitResponse qr) {
                int action = JOptionPane.showConfirmDialog(MainFrame.this, "Do you really want to exit?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)
                if (action == JOptionPane.OK_OPTION) {
                    thisFrame.windowListeners.each { it.windowClosing(new WindowEvent(thisFrame, 0)) }
                    qr.performQuit();
                } else {
                    qr.cancelQuit();
                }
            }
        });

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
        menuBar.add(windowMenu)

        menuBar
    }
}