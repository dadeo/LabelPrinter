package com.github.dadeo.labelprinter.pdf

import groovy.transform.Canonical

@Canonical
class LabelContent implements Cloneable {
    String line1
    String line2
    String line3
    String line4
}