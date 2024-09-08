package br.com.rsdconsultoria.rdcoin.infra.p2p;

import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.zip.*;

public class Node {
    private String address;
    private int port;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private SecretKey secretKey;
    private String token;
    private boolean itsMe;

    public Node(String address, int port, PublicKey publicKey, PrivateKey privateKey, String token,
            SecretKey secretKey) {
        this.address = address;
        this.port = port;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.secretKey = secretKey;
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public boolean isItsMe() {
        return itsMe;
    }

    public void setItsMe(boolean itsMe) {
        this.itsMe = itsMe;
    }

    public void sendMessage(String message, PublicKey recipientPublicKey) throws IOException, GeneralSecurityException {
        Socket socket = new Socket(address, port);
        OutputStream out = socket.getOutputStream();
        byte[] compressedMessage = compress(message);
        // byte[] encryptedMessage = encrypt(compressedMessage, recipientPublicKey);
        // out.write(token.getBytes());
        // out.write(encryptedMessage);
        out.write(compressedMessage);
        out.close();
        socket.close();
    }

    public String receiveMessage(byte[] encryptedMessage) throws GeneralSecurityException, IOException {
        // byte[] decryptedMessage = decrypt(encryptedMessage);
        // return decompress(decryptedMessage);
        return decompress(encryptedMessage);
    }

    private byte[] compress(String data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(data.getBytes());
        gzipOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private String decompress(byte[] compressedData) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
        GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private byte[] encrypt(byte[] data, PublicKey key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        // Inicializar o vetor de inicialização (IV)
        byte[] iv = new byte[16];
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        return cipher.doFinal(data);
    }

    private byte[] decrypt(byte[] data) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }
}
