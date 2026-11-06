package com.bank.factory;

import com.bank.model.Account;
import com.bank.model.CreditAccount;
import com.bank.model.Customer;
import com.bank.strategy.LoanInterestStrategy;

public class CreditAccountCreator extends AccountCreator {
    private static final double CREDIT_LIMIT = 500_000;
    private static final double CREDIT_RATE = 24.0;

    @Override
    protected Account createAccount(String id, Customer owner) {
        Account account = new CreditAccount(id, owner, CREDIT_LIMIT);
        account.setInterestStrategy(new LoanInterestStrategy(CREDIT_RATE));
        return account;
    }
}
