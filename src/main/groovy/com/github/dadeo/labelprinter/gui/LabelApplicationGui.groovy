package com.github.dadeo.labelprinter.gui

import javax.swing.SwingUtilities


class LabelApplicationGui {

  static void main(args) {
    SwingUtilities.invokeLater({
      new MainFrame()
    })
  }

}