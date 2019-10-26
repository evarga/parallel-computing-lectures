package com.example.bankaccount;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BankAccountV6 implements BankAccount {
    // We use proper locking, so we don't have data races anymore.
    private int balance;
    private final ReentrantReadWriteLock commonLock = new ReentrantReadWriteLock();
    // These locks may be needed by clients trying to implement thread-safe business logic that involves calling individual
    // methods of this class.
    public final Lock exclusiveLock = commonLock.writeLock();
    public final Lock sharedLock = commonLock.readLock();

    @Override
    public int getBalance() {
        try {
            sharedLock.lock();
            // Simulate a long lasting read transaction.
            Thread.sleep(1000);
            return balance;
        } catch (InterruptedException e) {
            return balance;
        } finally {
            sharedLock.unlock();
        }
    }

    @Override
    public void setBalance(int x) {
        try {
            exclusiveLock.lock();
            balance = x;
        } finally {
            exclusiveLock.unlock();
        }
    }

    @Override
    public void withdraw(int amount) throws InsufficientFundsException {
        try {
            exclusiveLock.lock();
            int b = getBalance();

            if (amount > b)
                throw new InsufficientFundsException();

            setBalance(b - amount);
        } finally {
            exclusiveLock.unlock();
        }
    }

    // ... other operations like deposit, etc.
}
