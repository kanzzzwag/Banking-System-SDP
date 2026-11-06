package com.bank.strategy;

// Used for credit accounts and loans. Here the bank charges the client.
public class LoanInterestStrategy implements InterestStrategy {
    private final double annualRate;

    public LoanInterestStrategy(double annualRate) {
        this.annualRate = annualRate;
    }

    @Override
    public double calculateMonthlyInterest(double debt) {
        if (debt <= 0) {
            return 0;
        }
        return debt * annualRate / 100 / 12;
    }

    @Override
    public double getAnnualRate() {
        return annualRate;
    }

    @Override
    public String getName() {
        return "Loan " + annualRate + "%";
    }
}
