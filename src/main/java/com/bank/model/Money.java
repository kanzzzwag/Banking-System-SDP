package com.bank.model;

import java.util.Locale;

// Helper for printing amounts in KZT
public final class Money {
    private Money() {
    }

    public static String kzt(double amount) {
        return String.format(Locale.US, "%,.2f KZT", amount);
    }
}
