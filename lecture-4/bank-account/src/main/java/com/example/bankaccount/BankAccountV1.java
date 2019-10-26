package com.example.bankaccount;

public class BankAccountV1 implements BankAccount {
    private int balance;

    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    public void setBalance(int x) {
        balance = x;
    }

    @Override
    public void withdraw(int amount) throws InsufficientFundsException {
        int b = getBalance();

        // This is inserted to provoke bad interleaving without changing the semantics.
        Thread.yield();

        if (amount > b)
            throw new InsufficientFundsException();

        setBalance(b - amount);
    }

    // ... other operations like deposit, etc.
}
