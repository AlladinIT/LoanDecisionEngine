package com.inbank.LoanDecisionEngine.utils;

import com.inbank.LoanDecisionEngine.dto.Segmentation;

public class ExternalRegister {

    public static Segmentation getPersonStatus(Long personalCode) {
        if (IntegerRange.isInClosedRange(personalCode,0,99)) {
            return Segmentation.debt;
        }
        else if (IntegerRange.isInClosedRange(personalCode,100,199)) {
            return Segmentation.segment_1;
        }
        else if (IntegerRange.isInClosedRange(personalCode,200,299)){
            return Segmentation.segment_2;
        }
        else {
            return Segmentation.segment_3;
        }
    }
}
