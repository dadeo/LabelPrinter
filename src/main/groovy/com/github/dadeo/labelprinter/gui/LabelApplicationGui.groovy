package com.github.dadeo.labelprinter.gui

import javax.swing.SwingUtilities


class LabelApplicationGui {

  static void main(args) {
    String version = this.getClass().getResource('/com/github/dadeo/labelprinter/version.txt').text
    SwingUtilities.invokeLater({
      new MainFrame(version)
    })
  }

}