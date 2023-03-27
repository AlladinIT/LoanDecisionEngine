package com.inbank.LoanDecisionEngine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecisionData {
    private BigDecimal outputSum;
    private Integer suitablePeriod;
    private Boolean approved;
    private String explanation;
}
