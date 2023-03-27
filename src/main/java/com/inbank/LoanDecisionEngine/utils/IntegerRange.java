package com.inbank.LoanDecisionEngine.utils;

public class IntegerRange {
    public static boolean isInClosedRange(Long number, Integer lowerBound, Integer upperBound) {
        return (lowerBound <= number && number <= upperBound);
    }
}
