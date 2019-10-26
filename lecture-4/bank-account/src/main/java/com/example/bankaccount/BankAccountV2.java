package com.example.bankaccount;

import java.util.concurrent.atomic.AtomicBoolean;

public class BankAccountV2 implements BankAccount {
    // Without marking this field volatile, we would also have data races.
    private volatile int balance;
    private final AtomicBoolean busy = new AtomicBoolean();

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
        while (true)
            if (busy.compareAndSet(false, true)) {
                int b = getBalance();

                if (amount > b)
                    throw new InsufficientFundsException();

                setBalance(b - amount);
                busy.set(false);
                return;
            }
    }

    // ... other operations like deposit, etc.
}
