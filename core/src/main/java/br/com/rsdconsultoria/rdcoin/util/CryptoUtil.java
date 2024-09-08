package br.com.rsdconsultoria.rdcoin.util;

import java.math.BigInteger;
import java.security.*;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;

public class CryptoUtil {
    // Função para gerar uma chave privada a partir de uma senha
    public static byte[] generatePrivateKey(String passPhrase) throws Exception {
        byte[] salt = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec((passPhrase + new java.util.Date()).toCharArray(), salt, 100000, 512);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] key = skf.generateSecret(spec).getEncoded();
        return key;
    }

    // Função para gerar um par de chaves RSA a partir de uma chave privada derivada
    public static KeyPair generateKeyPairFromPrivateKey(byte[] privateKey) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        return pair;
    }

    // Função para assinar uma mensagem
    public static String signMessage(String message, PrivateKey privateKey) throws Exception {
        Signature sign = Signature.getInstance("SHA512withRSA");
        sign.initSign(privateKey);
        sign.update(message.getBytes("UTF-8"));
        byte[] signature = sign.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    // Função para verificar uma assinatura
    public static boolean verifyMessage(String message, String signature, PublicKey publicKey) throws Exception {
        Signature verify = Signature.getInstance("SHA512withRSA");
        verify.initVerify(publicKey);
        verify.update(message.getBytes("UTF-8"));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return verify.verify(signatureBytes);
    }

    public static String calculateSHA512(String input) {
        try {
            // Obtém uma instância do algoritmo SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            
            // Calcula o hash
            byte[] messageDigest = md.digest(input.getBytes());
            
            // Converte o byte array em um BigInteger
            BigInteger no = new BigInteger(1, messageDigest);
            
            // Converte o BigInteger para uma string hexadecimal
            String hashtext = no.toString(16);
            
            // Adiciona zeros à esquerda para garantir que o hash tenha 128 caracteres
            while (hashtext.length() < 128) {
                hashtext = "0" + hashtext;
            }
            
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
