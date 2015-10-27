package com.github.dadeo.labelprinter.model

import groovy.transform.Canonical

@Canonical
class Label implements Serializable {
    long id
    String line1
    String line2
    String line3
    String line4
    Boolean printed
}