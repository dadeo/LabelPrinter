package com.github.dadeo.labelprinter

import groovy.transform.Immutable

class LabelExclusionMatrix {
    private Map<LabelExclusion, Boolean> exclusions = [:].withDefault { false }

    LabelExclusionMatrix exclude(int page, int col, int row, boolean excludeValue = true) {
        exclusions[new LabelExclusion(page, col, row)] = excludeValue
        this
    }

    boolean isLabelExcluded(int page, int col, int row) {
        exclusions[new LabelExclusion(page, col, row)]
    }

    @Immutable
    private static class LabelExclusion {
        int page
        int col
        int row
    }
}