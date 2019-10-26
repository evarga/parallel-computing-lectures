package com.example.bankaccount;

public class BankAccountV7 implements BankAccount {
    // We use proper locking, so we don't have data races anymore.
    private int balance;
    // This alias better communicates the condition variable's purpose associated with the 'this' lock.
    private final Object arrivalOfMoney = this;

    @Override
    public synchronized int getBalance() {
        return balance;
    }

    @Override
    public synchronized void setBalance(int x) {
        balance = x;
        arrivalOfMoney.notify();
    }

    @Override
    public synchronized void withdraw(int amount) {
        int b = getBalance();

        while (amount > b) {
            try {
                System.out.println("We are lacking " + (amount - b) + " bucks...");
                arrivalOfMoney.wait();
                System.out.println("We have received some money...");
                b = getBalance();
            } catch (InterruptedException e) {
                return;
            }
        }

        setBalance(b - amount);
    }

    // This method should be part of the BankAccount interface (an opportunity to refactor code).
    public synchronized void deposit(int amount) {
        balance += amount;
        arrivalOfMoney.notify();
    }
}
