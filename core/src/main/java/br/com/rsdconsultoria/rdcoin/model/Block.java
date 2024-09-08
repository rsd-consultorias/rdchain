package br.com.rsdconsultoria.rdcoin.model;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private String blockVersion;
    private String sequence;
    private String previousHash;
    private String timestamp;
    private String hash;
    private String validator;
    private String signature;
    private List<Transaction> transactions;

    public Block() {
        this.transactions = new ArrayList<Transaction>();
        this.setBlockVersion("v1");
    }

    public String getBlockVersion() {
        return blockVersion;
    }

    public void setBlockVersion(String version) {
        this.blockVersion = version;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    @Override
    public String toString() {
        return getBlockVersion() + getSequence() + getPreviousHash() + getTimestamp() + getValidator();
    }
}
