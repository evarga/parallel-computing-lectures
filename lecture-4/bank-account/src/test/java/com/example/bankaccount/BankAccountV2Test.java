package com.example.bankaccount;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BankAccountV2Test extends BankAccountTestBase<BankAccountV2> {
    public BankAccountV2Test() {
        super(new BankAccountV2());
    }

    @Test
    public void livelockWhenWithdrawMoneyInParallel() throws InterruptedException {
        final Thread t1 = new Thread(businessLogic);
        final Thread t2 = new Thread(businessLogic);
        final Thread t3 = new Thread(businessLogic);

        bankAccount.setBalance(150);
        t1.start();
        t2.start();
        t3.start();

        // Wait until threads handle the transactions. One of them will wait forever to acquire the busy flag.
        Thread.sleep(3000);

        // Never assume ordering of threads, i.e., you shouldn't expect that the last thread will be stuck here!
        assertTrue(t1.isAlive() || t2.isAlive() || t3.isAlive());
    }
}