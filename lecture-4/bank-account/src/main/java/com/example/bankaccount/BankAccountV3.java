package com.example.bankaccount;

import java.util.concurrent.atomic.AtomicBoolean;

public class BankAccountV3 implements BankAccount {
    // Without marking this field volatile, we would also have data races.
    private volatile int balance;
    private final AtomicBoolean busy = new AtomicBoolean();

    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    public void setBalance(int x) {
        setBalance(x, false);
    }

    private void setBalance(int x, boolean isInsideNestedTransaction) {
        while (true)
            if (busy.compareAndSet(isInsideNestedTransaction, true)) {
                balance = x;
                busy.set(isInsideNestedTransaction);
                return;
            }
    }

    @Override
    public void withdraw(int amount) throws InsufficientFundsException {
        while (true)
            if (busy.compareAndSet(false, true))
                try {
                    int b = getBalance();

                    if (amount > b)
                        throw new InsufficientFundsException();

                    setBalance(b - amount, true);
                    return;
                } finally {
                    busy.set(false);
                }
    }

    // ... other operations like deposit, etc.
}
