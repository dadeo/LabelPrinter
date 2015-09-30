package com.github.dadeo.labelprinter

import groovy.transform.Immutable

@Immutable
class LabelPageDescriptor {
    PageProperties page
    LabelProperties label
}