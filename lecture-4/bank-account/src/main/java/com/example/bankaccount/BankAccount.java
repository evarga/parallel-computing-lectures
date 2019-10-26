package com.example.bankaccount;

public interface BankAccount {
    int getBalance();

    void setBalance(int x);

    void withdraw(int amount) throws InsufficientFundsException;
}
