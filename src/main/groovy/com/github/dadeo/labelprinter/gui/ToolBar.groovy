package com.github.dadeo.labelprinter.gui

import javax.swing.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class ToolBar extends JToolBar implements ActionListener {
    private JButton saveButton
    private JButton refreshButton
    private JButton printButton
    private ToolBarListener toolBarListener

    ToolBar() {
        // Remove border if you want toolbar dockable after dragging.
//        setBorder BorderFactory.createEtchedBorder()

//        setFloatable(false)

        def createButton = { String name, String path ->
            ImageIcon icon = Utils.createIcon(path)

            JButton button = icon ? new JButton() : new JButton(name)
            button.setToolTipText(name)
            button.setIcon(icon)
            button.addActionListener(this)
            button
        }

        saveButton = createButton('Save', '/com/github/dadeo/labelprinter/images/Save16.gif')
        add saveButton

        refreshButton = createButton('Refresh', '/com/github/dadeo/labelprinter/images/Refresh16.gif')
        add refreshButton

        printButton = createButton('Print', '/com/github/dadeo/labelprinter/images/Print16.gif')
        add printButton
    }

    @Override
    void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.source

        if (!toolBarListener) return

        if (clicked == saveButton)
            toolBarListener.saveEventOccurred()
        else if (clicked == refreshButton)
            toolBarListener.refreshEventOccurred()
        else
            toolBarListener.printEventOccurred()
    }

    void setToolBarListener(ToolBarListener listener) {
        this.toolBarListener = listener
    }


}
