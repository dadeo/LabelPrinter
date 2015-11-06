package com.github.dadeo.labelprinter.gui

import javax.swing.*
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent

class ToolBar extends JToolBar {
    private ToolBarListener toolBarListener

    ToolBar() {
        // Remove border if you want toolbar dockable after dragging.
//        setBorder BorderFactory.createEtchedBorder()

//        setFloatable(false)

        def createActionButton = { String name, String path, Action action, KeyStroke keyStroke ->
            ImageIcon icon = Utils.createIcon(path)

            JButton button = new JButton(action)
            button.setToolTipText(name)
            button.setIcon(icon)

            button.getActionMap().put("perform$name", action);
            button.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(keyStroke, "perform$name");

            add button
        }

        Action performSave = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                toolBarListener?.saveEventOccurred()
            }
        }

        Action performRefresh = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                toolBarListener?.refreshEventOccurred()
            }
        }

        Action performPrint = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                toolBarListener?.printEventOccurred()
            }
        }

        createActionButton('Save',
                           '/com/github/dadeo/labelprinter/images/Save16.gif',
                           performSave,
                           KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.META_MASK))

        createActionButton('Refresh',
                           '/com/github/dadeo/labelprinter/images/Refresh16.gif',
                           performRefresh,
                           KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.META_MASK))

        createActionButton('Print',
                           '/com/github/dadeo/labelprinter/images/Print16.gif',
                           performPrint,
                           KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.META_MASK))

    }

    void setToolBarListener(ToolBarListener listener) {
        this.toolBarListener = listener
    }


}
