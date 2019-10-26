package com.example.bankaccount;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccountV4 implements BankAccount {
    // We use proper locking, so we don't have data races anymore.
    private int balance;
    // The lock may be needed by clients trying to implement thread-safe business logic that involves calling individual
    // methods of this class.
    public final Lock lock = new ReentrantLock();

    @Override
    public int getBalance() {
        try {
            lock.lock();
            return balance;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setBalance(int x) {
        try {
            lock.lock();
            balance = x;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void withdraw(int amount) throws InsufficientFundsException {
        try {
            lock.lock();
            int b = getBalance();

            if (amount > b)
                throw new InsufficientFundsException();

            setBalance(b - amount);
        } finally {
            lock.unlock();
        }
    }

    // ... other operations like deposit, etc.
}
