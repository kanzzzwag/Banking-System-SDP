package com.bank.adapter;

import java.util.Locale;

// "Third party" SWIFT service. We cannot change it.
// It works in US dollars and needs a BIC code and an IBAN.
public class InternationalSwiftService {
    private int referenceCounter = 5001;

    public String executeSwiftTransfer(String senderReference, String bicCode, String iban, double amountUsd) {
        if (bicCode.length() < 8 || iban.length() < 15) {
            throw new IllegalArgumentException("Invalid BIC or IBAN");
        }
        String reference = "SWIFT-" + referenceCounter++;
        System.out.printf(Locale.US, "    (SWIFT network) %s: %.2f USD sent to %s at %s for %s%n",
                reference, amountUsd, iban, bicCode, senderReference);
        return reference;
    }
}
