package com.example.bankaccount;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BankAccountV6Test {
    @Test(timeout = 6000)
    public void readBalancesInParallelWhileWithdrawExclusively() throws InterruptedException {
        // This test will fail (time out) with ordinary locks; we would surely need at least 12 seconds.
        final BankAccount bankAccount = new BankAccountV6();
        bankAccount.setBalance(150);

        final Runnable readerLogic = new Runnable() {
            @Override
            public void run() {
                bankAccount.getBalance();
            }
        };
        final Runnable writerLogic = new Runnable() {
            @Override
            public void run() {
                bankAccount.withdraw(100);
            }
        };

        Thread[] readers = new Thread[10];
        Thread writer = new Thread(writerLogic);

        for (int i = 0; i < readers.length; i++) {
            if (i == readers.length / 2)
                writer.start();
            readers[i] = new Thread(readerLogic);
            readers[i].start();
        }

        for (Thread reader: readers)
            reader.join();
        writer.join();
        assertEquals(50, bankAccount.getBalance());
    }
}
