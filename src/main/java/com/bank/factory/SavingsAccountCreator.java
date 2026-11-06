package com.bank.factory;

import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.model.SavingsAccount;
import com.bank.strategy.SavingsInterestStrategy;

public class SavingsAccountCreator extends AccountCreator {
    @Override
    protected Account createAccount(String id, Customer owner) {
        Account account = new SavingsAccount(id, owner);
        account.setInterestStrategy(new SavingsInterestStrategy());
        return account;
    }
}
