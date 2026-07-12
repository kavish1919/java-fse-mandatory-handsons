package com.cognizant.account.model;

/**
 * Account POJO representing bank account details (`number`, `type`, `balance`).
 */
public class Account {

    private String number;
    private String type;
    private double balance;

    public Account() {
    }

    public Account(String number, String type, double balance) {
        this.number = number;
        this.type = type;
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account [number=" + number + ", type=" + type + ", balance=" + balance + "]";
    }
}
