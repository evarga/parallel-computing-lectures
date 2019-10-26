package com.example.bankaccount;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccountV5 implements BankAccount {
    // We use proper locking, so we don't have data races anymore.
    private int balance;

    @Override
    public synchronized int getBalance() {
        return balance;
    }

    @Override
    public synchronized void setBalance(int x) {
        balance = x;
    }

    @Override
    public synchronized void withdraw(int amount) throws InsufficientFundsException {
        int b = getBalance();

        if (amount > b)
            throw new InsufficientFundsException();

        setBalance(b - amount);
    }

    // ... other synchronized operations like deposit, etc.
}
