package br.com.rsdconsultoria.rdcoin.model;

public class Wallet {
    private String walletName;
    private String walletVersion;
    private String address;
    private Long balance;
    private Long unconfirmedBalance;

    private String publicKey;
    private String privateKey;

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletname) {
        this.walletName = walletname;
    }

    public String getWalletVersion() {
        return walletVersion;
    }

    public void setWalletVersion(String walletversion) {
        this.walletVersion = walletversion;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getUnconfirmedBalance() {
        return unconfirmedBalance;
    }

    public void setUnconfirmedBalance(Long unconfirmedBalance) {
        this.unconfirmedBalance = unconfirmedBalance;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
