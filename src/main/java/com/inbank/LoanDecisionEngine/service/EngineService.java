package com.inbank.LoanDecisionEngine.service;

import com.inbank.LoanDecisionEngine.dto.DecisionData;
import com.inbank.LoanDecisionEngine.dto.InputData;
import com.inbank.LoanDecisionEngine.dto.Segmentation;
import com.inbank.LoanDecisionEngine.utils.ExternalRegister;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EngineService {

    public DecisionData decide(InputData inputData) {

        if (!usersInputIsNull(inputData)) {
            return new DecisionData(null,null,false,
                    "Invalid input data");
        }

        String explanation = checkConstraints(inputData);
        if (explanation != null) {
            return new DecisionData(null,null,false, explanation);
        }


        BigDecimal creditModifier = new BigDecimal(1000);
        if (ExternalRegister.getPersonStatus(inputData.getPersonalCode()) == Segmentation.debt){
            return new DecisionData(null,null,false,
                    "A person has debt");
        }
        if (ExternalRegister.getPersonStatus(inputData.getPersonalCode()) == Segmentation.segment_1){
            creditModifier = new BigDecimal(100);
        }
        if (ExternalRegister.getPersonStatus(inputData.getPersonalCode()) == Segmentation.segment_2){
            creditModifier = new BigDecimal(300);
        }
        if (ExternalRegister.getPersonStatus(inputData.getPersonalCode()) == Segmentation.segment_3){
            creditModifier = new BigDecimal(1000);
        }

        BigDecimal creditScore = creditModifier
                .divide(inputData.getInputSum(),10, RoundingMode.HALF_UP).
                multiply(new BigDecimal(inputData.getLoanPeriod())).setScale(2,RoundingMode.HALF_UP);

        if (creditScore.compareTo(new BigDecimal(1)) < 0){
            //not approved
            //return largest POSSIBLE sum of credit
            //If a suitable loan amount is not found within the selected period =>
            // => find suitable period for the requested loan amount
            //If both tries were not successful => Try to find the highest possible loan amount for a new period
            BigDecimal largestSum = creditModifier.multiply(new BigDecimal(inputData.getLoanPeriod()));

            if (largestSum.compareTo(new BigDecimal(2000)) < 0){
                BigDecimal newSuitablePeriod = inputData.getInputSum()
                        .divide(creditModifier,10,RoundingMode.HALF_UP);
                if (newSuitablePeriod.compareTo(new BigDecimal(60)) > 0){

                    BigDecimal newLoanAmount = null;
                    BigDecimal highestLoanAmount = new BigDecimal(1999);
                    Integer newLoanPeriod = null;
                    boolean foundPossibleLoanAndPeriod = false;
                    for (int i = 12; i < 61;i++){
                        newLoanAmount = creditModifier.multiply(new BigDecimal(i));
                        if (newLoanAmount.compareTo(new BigDecimal(2000)) >= 0 &&
                                newLoanAmount.compareTo(new BigDecimal(10000)) <= 0 &&
                                newLoanAmount.compareTo(highestLoanAmount) > 0){
                            highestLoanAmount = newLoanAmount;
                            newLoanPeriod = i;
                            foundPossibleLoanAndPeriod = true;
                        }
                    }
                    if (foundPossibleLoanAndPeriod){
                        return new DecisionData(highestLoanAmount, newLoanPeriod, false,
                                "Possible loan amount is: " + newLoanAmount+ " € and period is: "
                                        + newLoanPeriod + " months");
                    }
                    else{
                        return new DecisionData(inputData.getInputSum(), inputData.getLoanPeriod(), false,
                                "Unfortunately, it is not possible to find any possible loan amount and period");
                    }

                }
                else{
                    int newPeriod = newSuitablePeriod.setScale(0, RoundingMode.CEILING).intValue();
                    return new DecisionData(inputData.getInputSum(), newPeriod, false,
                            "A possible loan amount is not found for the selected period," +
                                    " a new suitable period for loan amount of " + inputData.getInputSum() +
                                    " € is: " + newPeriod +" months.");
                }
            }
            else {
                return new DecisionData(largestSum, inputData.getLoanPeriod(), false,
                        "The largest sum which we would approve for " +inputData.getLoanPeriod()+
                                " months period is: " + largestSum +" €");
            }

        }
        if (creditScore.compareTo(new BigDecimal(1)) > 0){
            //approved
            //check for larger sum
            BigDecimal largerSum = creditModifier.multiply(new BigDecimal(inputData.getLoanPeriod()));

            if (largerSum.compareTo(new BigDecimal(10000)) > 0){
                if ((inputData.getInputSum().compareTo(new BigDecimal(10000)) != 0)){
                    return new DecisionData(new BigDecimal(10000), inputData.getLoanPeriod(), true,
                            "For "+inputData.getLoanPeriod()+" month loan period," +
                                    " we can approve a larger sum: 10000 €");
                }
            }
            else{
                return new DecisionData(largerSum, inputData.getLoanPeriod(), true,
                        "For "+inputData.getLoanPeriod()+" month loan period," +
                                " we can approve a larger sum: "+ largerSum +" €");
            }
        }

        //approved
        //it is already the largest sum
        return new DecisionData(inputData.getInputSum(), inputData.getLoanPeriod(), true,
                "You are allowed to take a loan amount of " + inputData.getInputSum() +
                        " € for a period of " + inputData.getLoanPeriod()+" months");


    }

    private boolean usersInputIsNull(InputData inputData) {
        if (inputData == null ||
                inputData.getPersonalCode() == null ||
                inputData.getLoanPeriod() == null ||
                inputData.getInputSum() == null) {
            return false;
        }
        return true;
    }
    private String checkConstraints(InputData inputData){

        if (inputData.getInputSum().compareTo(new BigDecimal(2000)) < 0){
            return "Minimum loan amount can be 2000 €";
        }
        if (inputData.getInputSum().compareTo(new BigDecimal(10000)) > 0){
            return "Maximum loan amount can be 10000 €";
        }
        if (inputData.getLoanPeriod() < 12){
            return "Minimum loan period can be 12 months";
        }
        if (inputData.getLoanPeriod() > 60){
            return "Maximum loan period can be 60 months";
        }
        if (inputData.getPersonalCode() < 0){
            return "Personal code cannot be negative";
        }

        return null;
    }

}
