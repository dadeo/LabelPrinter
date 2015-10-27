package com.github.dadeo.labelprinter.gui

import javax.swing.*
import java.awt.*

class Utils {
    static ImageIcon createIcon(String path) {
        URL url = getClass().getResource(path)
        if (url == null)
            System.err.println("Unable to load image from $path")
        else
            new ImageIcon(url)
    }

    static Font createFont(String path) {
        URL url = getClass().getResource(path)
        if (url == null)
            System.err.println("Unable to load font from $path")
        else
            url.withInputStream { is ->
                try {
                    Font.createFont(Font.TRUETYPE_FONT, is)
                } catch (FontFormatException e) {
                    System.err.println 'Bad format in font file: $path'
                } catch (IOException e) {
                    System.err.println 'Bad format in font file: $path'
                }
            }
    }
}