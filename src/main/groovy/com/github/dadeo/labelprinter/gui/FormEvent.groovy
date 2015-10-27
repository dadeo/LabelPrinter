package com.github.dadeo.labelprinter.gui

class FormEvent extends EventObject {

    private final String line1
    private final String line2
    private final String line3
    private final String line4

    FormEvent(Object source, String line1, String line2, String line3, String line4) {
        super(source)
        this.line1 = line1
        this.line2 = line2
        this.line3 = line3
        this.line4 = line4
    }

    String getLine1() {
        return line1
    }

    String getLine2() {
        return line2
    }

    String getLine3() {
        return line3
    }

    String getLine4() {
        return line4
    }
}