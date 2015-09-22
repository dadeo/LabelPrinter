package com.github.dadeo.labelprinter

import groovy.transform.Immutable

@Immutable
class FPoint {
    float x
    float y

    FPoint adjust(float xOffset, float yOffset) {
        new FPoint((float) (x + xOffset), (float) (y + yOffset))
    }
}