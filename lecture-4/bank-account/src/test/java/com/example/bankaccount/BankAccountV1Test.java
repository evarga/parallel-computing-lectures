package com.example.bankaccount;

import org.junit.Test;
import static org.junit.Assert.*;

public class BankAccountV1Test extends BankAccountTestBase<BankAccountV1> {
    public BankAccountV1Test() {
        super(new BankAccountV1());
    }

    @Test
    @Override
    public void repeatedlyWithdrawMoneyInParallel() throws InterruptedException {
        super.repeatedlyWithdrawMoneyInParallel();

        // This is crucial. If there is a mismatch, then we had at least one bad interleaving. Our test succeeds
        // when such an unlucky scenario is exposed.
        assertNotEquals(NUM_TRIALS, exceptionCounter.get());
    }
}