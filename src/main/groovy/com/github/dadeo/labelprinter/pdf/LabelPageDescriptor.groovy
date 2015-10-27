package com.github.dadeo.labelprinter.pdf

import groovy.transform.Immutable

@Immutable
class LabelPageDescriptor {
    PageProperties page
    LabelProperties label
}