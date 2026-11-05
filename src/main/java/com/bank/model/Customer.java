package com.bank.model;

// Simple bank client
public class Customer {
    private final String id;
    private final String fullName;
    private final String phone;

    public Customer(String id, String fullName, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return id + " " + fullName + " (" + phone + ")";
    }
}
