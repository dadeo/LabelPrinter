package com.github.dadeo.labelprinter.gui

import com.apple.eawt.Application
import com.apple.eawt.QuitHandler

import javax.swing.*

class LabelApplicationGui {

    static void main(args) {
        Application.application.setQuitHandler(new QuitHandler() {
            @Override
            public void handleQuitRequestWith(com.apple.eawt.AppEvent.QuitEvent qe, com.apple.eawt.QuitResponse qr) {
                int res = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the program?", "Quit ?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (res == JOptionPane.YES_OPTION)
                    qr.performQuit();
                else
                    qr.cancelQuit();
            }
        });

        String version = this.getClass().getResource('/com/github/dadeo/labelprinter/version.txt').text

        SwingUtilities.invokeLater({
            new MainFrame(version)
        })
    }

}