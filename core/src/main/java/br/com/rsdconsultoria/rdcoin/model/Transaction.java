package br.com.rsdconsultoria.rdcoin.model;

import java.security.MessageDigest;

public class Transaction {
    private String transactioVersion;
    private String sequence;
    private String hash;
    private String sender;
    private String receiver;
    private String amount;
    private String fee;
    private String timestamp;
    private String signature;

    public Transaction(String receiver, long amount, long fee) {
        this.setReceiver(receiver);
        this.setAmount(String.valueOf((amount * 100)));
        this.setFee(String.valueOf((fee * 100)));

        try {
            this.computeHash();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getTransactioVersion() {
        return transactioVersion;
    }

    public void setTransactioVersion(String version) {
        this.transactioVersion = version;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return getTransactioVersion() + getSequence() + getHash() + getSender() + getReceiver() + getAmount() + getFee()
                + getTimestamp();
    }

    public void computeHash() throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(this.toString().getBytes());
        this.setHash(bytesToHex(digest.digest()));
    }

    // Método auxiliar para converter bytes em uma string hexadecimal
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
