package com.example.bankaccount;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class BankAccountTestBase<T extends BankAccount> {
    // You may want to increase the number of trials if tests are failing. Try to set it to a
    // lower value (like, 100) to demonstrate how classical testing doesn't work with multi-threaded programs.
    protected static final int NUM_TRIALS = 5000;
    protected static final AtomicInteger exceptionCounter = new AtomicInteger();

    protected final T bankAccount;

    protected BankAccountTestBase(T bankAccount) {
        this.bankAccount = bankAccount;
    }

    @BeforeClass
    public static void setupExceptionNHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (e instanceof InsufficientFundsException)
                    exceptionCounter.incrementAndGet();
            }
        });
    }

    @Before
    public void resetExceptionCounter() {
        exceptionCounter.set(0);
    }

    @Test(expected = InsufficientFundsException.class)
    public void repeatedlyWithdrawMoneySequentially() {
        for (int i = 0; i < NUM_TRIALS; i++) {
            bankAccount.setBalance(150);
            bankAccount.withdraw(100);
            bankAccount.withdraw(100);
        }
    }

    protected final Runnable businessLogic = new Runnable() {
        @Override
        public void run() {
            bankAccount.withdraw(100);
        }
    };

    protected void repeatedlyWithdrawMoneyInParallel() throws InterruptedException {
        for (int i = 0; i < NUM_TRIALS; i++) {
            final Thread t1 = new Thread(businessLogic);
            final Thread t2 = new Thread(businessLogic);

            bankAccount.setBalance(150);
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        }
    }
}