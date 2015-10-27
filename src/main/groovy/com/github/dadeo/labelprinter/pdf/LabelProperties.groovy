package com.github.dadeo.labelprinter.pdf

import groovy.transform.Immutable

@Immutable
class LabelProperties {
    float width
    float height
    int columns
    int rows
}