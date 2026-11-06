package com.bank.factory;

import com.bank.model.Account;
import com.bank.model.CheckingAccount;
import com.bank.model.Customer;
import com.bank.strategy.CheckingInterestStrategy;

public class CheckingAccountCreator extends AccountCreator {
    @Override
    protected Account createAccount(String id, Customer owner) {
        Account account = new CheckingAccount(id, owner);
        account.setInterestStrategy(new CheckingInterestStrategy());
        return account;
    }
}
