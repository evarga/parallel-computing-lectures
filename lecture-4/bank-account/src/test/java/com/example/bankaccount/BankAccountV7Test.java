package com.example.bankaccount;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BankAccountV7Test extends BankAccountTestBase<BankAccountV7> {
    public BankAccountV7Test() {
        super(new BankAccountV7());
    }

    @Test
    public void waitInWithdrawUntilEnoughMoneyIsAvailable() throws InterruptedException {
        bankAccount.setBalance(50);

        final Thread withdrawAgent = new Thread(businessLogic);
        withdrawAgent.start();
        // Let the thread doing the withdraw get stuck.
        Thread.sleep(100);
        bankAccount.deposit(20);
        Thread.sleep(100);
        bankAccount.deposit(30);
        withdrawAgent.join();

        assertEquals(0, exceptionCounter.get());
        assertEquals(0, bankAccount.getBalance());
    }

    @Test
    @Override
    public void repeatedlyWithdrawMoneySequentially() {
        // This test makes no sense for our new class, so do nothing here.
    }
}
