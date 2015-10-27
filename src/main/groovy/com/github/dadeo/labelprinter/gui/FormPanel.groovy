package com.github.dadeo.labelprinter.gui

import javax.swing.*
import javax.swing.border.Border
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent

class FormPanel extends JPanel {
    private JLabel line1Label
    private JTextField line1Field
    private JLabel line2Label
    private JTextField line2Field
    private JLabel line3Label
    private JTextField line3Field
    private JLabel line4Label
    private JTextField line4Field
    private JLabel occupationLabel
    private JLabel ageCategoryLabel
    private JLabel employmentLabel
    private JTextField occupationField
    private JButton addBtn
    private FormListener formListener
    private JList ageList
    private JComboBox employmentCombo
    private JLabel citizenLabel
    private JCheckBox citizenCheck
    private JTextField taxField
    private JLabel taxLabel
    private JLabel genderLabel
    private JRadioButton maleRadio
    private JRadioButton femaleRadio
    private ButtonGroup genderGroup

    FormPanel() {
        createComponents()
        layoutComponents()
        reset()
    }

    protected void createComponents() {
        Dimension dim = getPreferredSize()
        dim.width = 275
        setPreferredSize(dim)
        setMinimumSize(dim)

        line1Label = new JLabel("Line 1: ")
        line2Label = new JLabel("Line 2: ")
        line3Label = new JLabel("Line 3: ")
        line4Label = new JLabel("Line 4: ")
        line1Field = new JTextField(10)
        line2Field = new JTextField(10)
        line3Field = new JTextField(10)
        line4Field = new JTextField(10)

        def mnemonic = { JLabel label, JComponent field, int key ->
            label.displayedMnemonic = key
            label.labelFor = field
        }

        mnemonic line1Label, line1Field, KeyEvent.VK_1
        mnemonic line2Label, line2Field, KeyEvent.VK_2
        mnemonic line3Label, line3Field, KeyEvent.VK_3
        mnemonic line4Label, line4Field, KeyEvent.VK_4

        addBtn = new JButton("Add")

        addBtn.addActionListener { ActionEvent e ->
            if (formListener)
                formListener.formEventOccurred(new FormEvent(this,
                                                             line1Field.text,
                                                             line2Field.text,
                                                             line3Field.text,
                                                             line4Field.text))

        }

        Border innerBorder = BorderFactory.createTitledBorder("Add Person")
        Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5)
        setBorder BorderFactory.createCompoundBorder(outerBorder, innerBorder)
    }

    protected void layoutComponents() {
        setLayout new GridBagLayout()
        GridBagConstraints gc = new GridBagConstraints()
        gc.gridy = 0

        //////////////////////////// First Row ////////////////////////////
        gc.weightx = 1
        gc.weighty = 0

        gc.gridx = 0
        gc.fill = GridBagConstraints.NONE
        gc.anchor = GridBagConstraints.LINE_END
        gc.insets = new Insets(0, 0, 0, 5)
        add line1Label, gc

        gc.gridx = 1
        gc.fill = GridBagConstraints.HORIZONTAL
        gc.anchor = GridBagConstraints.LINE_START
        gc.insets = new Insets(0, 0, 0, 0)
        add line1Field, gc

        //////////////////////////// Second Row ////////////////////////////
        ++gc.gridy

        gc.weighty = 0.1

        gc.gridx = 0
        gc.fill = GridBagConstraints.NONE
        gc.anchor = GridBagConstraints.LINE_END
        gc.insets = new Insets(0, 0, 0, 5)
        add line2Label, gc

        gc.gridx = 1
        gc.gridy = 1
        gc.fill = GridBagConstraints.HORIZONTAL
        gc.anchor = GridBagConstraints.LINE_START
        gc.insets = new Insets(0, 0, 0, 0)
        add line2Field, gc

        //////////////////////////// Third Row ////////////////////////////
        ++gc.gridy

        gc.weightx = 1
        gc.weighty = 0.1

        gc.gridx = 0
        gc.fill = GridBagConstraints.NONE
        gc.anchor = GridBagConstraints.LINE_END
        gc.insets = new Insets(0, 0, 0, 5)
        add line3Label, gc

        gc.gridx = 1
        gc.fill = GridBagConstraints.HORIZONTAL
        gc.anchor = GridBagConstraints.LINE_START
        gc.insets = new Insets(0, 0, 0, 0)
        add line3Field, gc

        //////////////////////////// Next Row ////////////////////////////
        ++gc.gridy

        gc.weightx = 1
        gc.weighty = 0.1

        gc.gridx = 0
        gc.fill = GridBagConstraints.NONE
        gc.anchor = GridBagConstraints.LINE_END
        gc.insets = new Insets(0, 0, 0, 5)
        add line4Label, gc

        gc.gridx = 1
        gc.fill = GridBagConstraints.HORIZONTAL
        gc.anchor = GridBagConstraints.LINE_START
        gc.insets = new Insets(0, 0, 0, 0)
        add line4Field, gc

        //////////////////////////// Next Row ////////////////////////////
        ++gc.gridy

        gc.weightx = 1
        gc.weighty = 4.5

        gc.gridx = 1
        gc.anchor = GridBagConstraints.FIRST_LINE_START
        gc.insets = new Insets(10, 0, 0, 0)
        add addBtn, gc
    }

    void setFormListener(FormListener formListener) {
        this.formListener = formListener
    }

    void reset() {
        line1Field.text = ""
        line2Field.text = ""
        line3Field.text = ""
        line4Field.text = ""
    }

    void requestFocus() {
        line1Field.requestFocus()
    }
}