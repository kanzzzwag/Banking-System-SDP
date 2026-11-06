package com.bank.adapter;

// "Third party" local card processing. We cannot change it.
// It works with amounts in tiyn (1 KZT = 100 tiyn) and returns status codes.
public class LocalCardProcessor {
    public static final int OK = 0;
    public static final int INVALID_CARD = 1;
    public static final int LIMIT_EXCEEDED = 2;

    private static final long MAX_TIYN = 200_000_000L; // 2 000 000 KZT

    public int pushToCard(String cardNumber, long amountTiyn) {
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            return INVALID_CARD;
        }
        if (amountTiyn > MAX_TIYN) {
            return LIMIT_EXCEEDED;
        }
        System.out.println("    (Card processor) " + amountTiyn + " tiyn pushed to card ****"
                + cardNumber.substring(12));
        return OK;
    }
}
