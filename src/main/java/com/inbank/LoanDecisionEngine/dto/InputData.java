package com.inbank.LoanDecisionEngine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputData {
    private Long personalCode;
    private BigDecimal inputSum;
    private Integer loanPeriod;
}
