package com.example.bankaccount;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BankAccountV3Test extends BankAccountTestBase<BankAccountV3> {
    public BankAccountV3Test() {
        super(new BankAccountV3());
    }

    @Test
    @Override
    public void repeatedlyWithdrawMoneyInParallel() throws InterruptedException {
        super.repeatedlyWithdrawMoneyInParallel();

        // This is crucial. If there is a mismatch, then we had at least one bad interleaving. Our test succeeds
        // when such an unlucky scenario is not exposed. This test is an opposite to the variant V1.
        assertEquals(NUM_TRIALS, exceptionCounter.get());
    }
}