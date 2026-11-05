package com.bank.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// One money operation. It is sent to all observers.
public class Transaction {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static int counter = 1;

    private final int id;
    private final TransactionType type;
    private final Account account;
    private final String target;
    private final double amount;
    private final LocalDateTime time;

    public Transaction(TransactionType type, Account account, String target, double amount) {
        this.id = counter++;
        this.type = type;
        this.account = account;
        this.target = target;
        this.amount = amount;
        this.time = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public Account getAccount() {
        return account;
    }

    public String getTarget() {
        return target;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        String text = "#" + id + " " + time.format(FORMAT) + " " + type + " " + Money.kzt(amount)
                + " account " + account.getId();
        if (target != null) {
            text += " -> " + target;
        }
        return text;
    }
}
