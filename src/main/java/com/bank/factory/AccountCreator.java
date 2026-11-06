package com.bank.factory;

import com.bank.model.Account;
import com.bank.model.Customer;

// Creator class of the Factory Method pattern
public abstract class AccountCreator {
    private static int nextNumber = 1001;

    // The factory method. Subclasses decide which account to create.
    protected abstract Account createAccount(String id, Customer owner);

    // Common steps for every new account
    public Account openAccount(Customer owner) {
        String id = "KZ" + nextNumber++;
        Account account = createAccount(id, owner);
        System.out.println("[Factory] " + getClass().getSimpleName() + " created " + account.getType()
                + " account " + id + " for " + owner.getFullName()
                + " with strategy " + account.getInterestStrategy().getName());
        return account;
    }
}
