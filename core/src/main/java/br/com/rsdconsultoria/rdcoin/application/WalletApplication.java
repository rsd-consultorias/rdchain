package br.com.rsdconsultoria.rdcoin.application;

import java.util.ArrayList;
import java.util.List;

import br.com.rsdconsultoria.rdcoin.model.Transaction;
import br.com.rsdconsultoria.rdcoin.model.Wallet;

public class WalletApplication {
    private Wallet currentWallet;
    private List<Transaction> unsentTransactions;

    public WalletApplication() {
        this.unsentTransactions = new ArrayList<Transaction>();
    }

    public Wallet createWallet(String walletname, String passPhrase) {
        return null;
    }

    public void upgrade() {
    }

    public Wallet loadWallet(String passPhrase) {
        setCurrentWallet(new Wallet());
        
        return this.getCurrentWallet();
    }

    public Wallet getCurrentWallet() {
        return currentWallet;
    }

    private void setCurrentWallet(Wallet currentWallet) {
        this.currentWallet = currentWallet;
    }

    public boolean createTransaction(String receiver, long amount, long fee) {
        var newTransaction = new Transaction(receiver, amount, fee);
        this.unsentTransactions.add(newTransaction);
        return false;
    }

    public boolean signAndSendTransactionsToNetwork() {
        this.unsentTransactions.clear();
        return false;
    }
}
