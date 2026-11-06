package com.bank.builder;

import com.bank.model.Customer;
import com.bank.model.Money;
import com.bank.strategy.InterestStrategy;
import com.bank.strategy.LoanInterestStrategy;

// Loan agreement has many fields and some of them are optional,
// so it is created only through LoanAgreement.Builder
public class LoanAgreement {
    private static int counter = 1;

    private final String number;
    private final Customer customer;
    private final double amount;
    private final int termMonths;
    private final double interestRate;
    private final String collateral; // optional
    private final String guarantor;  // optional
    private final InterestStrategy interestStrategy;

    private LoanAgreement(Builder builder) {
        this.number = "LN-" + counter++;
        this.customer = builder.customer;
        this.amount = builder.amount;
        this.termMonths = builder.termMonths;
        this.interestRate = builder.interestRate;
        this.collateral = builder.collateral;
        this.guarantor = builder.guarantor;
        this.interestStrategy = new LoanInterestStrategy(builder.interestRate);
    }

    // Simple flat interest: monthly interest from the strategy times the term
    public double getTotalInterest() {
        return interestStrategy.calculateMonthlyInterest(amount) * termMonths;
    }

    public double getMonthlyPayment() {
        return (amount + getTotalInterest()) / termMonths;
    }

    public String getNumber() {
        return number;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getAmount() {
        return amount;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public String getCollateral() {
        return collateral;
    }

    public String getGuarantor() {
        return guarantor;
    }

    @Override
    public String toString() {
        return number + ": " + customer.getFullName() + ", " + Money.kzt(amount) + ", " + termMonths
                + " months, " + interestRate + "% per year"
                + (collateral != null ? ", collateral: " + collateral : "")
                + (guarantor != null ? ", guarantor: " + guarantor : "")
                + ", monthly payment " + Money.kzt(getMonthlyPayment());
    }

    public static class Builder {
        private static final double BIG_LOAN = 5_000_000;

        private Customer customer;
        private double amount;
        private int termMonths;
        private double interestRate = 18.0; // default bank rate
        private String collateral;
        private String guarantor;

        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder termMonths(int termMonths) {
            this.termMonths = termMonths;
            return this;
        }

        public Builder interestRate(double interestRate) {
            this.interestRate = interestRate;
            return this;
        }

        public Builder collateral(String collateral) {
            this.collateral = collateral;
            return this;
        }

        public Builder guarantor(String guarantor) {
            this.guarantor = guarantor;
            return this;
        }

        // All checks are done here, so an invalid agreement can never exist
        public LoanAgreement build() {
            if (customer == null) {
                throw new IllegalStateException("Loan must have a customer");
            }
            if (amount < 50_000 || amount > 50_000_000) {
                throw new IllegalStateException("Loan amount must be from 50,000 to 50,000,000 KZT");
            }
            if (termMonths < 3 || termMonths > 120) {
                throw new IllegalStateException("Term must be from 3 to 120 months");
            }
            if (interestRate <= 0 || interestRate > 60) {
                throw new IllegalStateException("Interest rate must be from 0 to 60 percent");
            }
            if (amount > BIG_LOAN && collateral == null && guarantor == null) {
                throw new IllegalStateException("Loans above " + Money.kzt(BIG_LOAN)
                        + " need a collateral or a guarantor");
            }
            LoanAgreement agreement = new LoanAgreement(this);
            System.out.println("[Builder] Loan agreement created: " + agreement);
            return agreement;
        }
    }
}
