package br.com.rsdconsultoria.rdcoin.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {
    public static boolean isHashInMerkleRoot(byte[] hash, List<byte[]> hashes) throws Exception {
        byte[] merkleRoot = calculateMerkleRoot(hashes);
        return isHashPartOfMerkleRoot(hash, hashes, merkleRoot);
    }

    private static boolean isHashPartOfMerkleRoot(byte[] hash, List<byte[]> hashes, byte[] merkleRoot)
        throws Exception {
    if (hashes.size() == 1) {
        return MessageDigest.isEqual(hashes.get(0), merkleRoot) && MessageDigest.isEqual(hashes.get(0), hash);
    }

    List<byte[]> currentLevel = new ArrayList<>(hashes);
    boolean hashFound = false;

    while (currentLevel.size() > 1) {
        List<byte[]> nextLevel = new ArrayList<>();
        for (int i = 0; i < currentLevel.size(); i += 2) {
            byte[] hash1 = currentLevel.get(i);
            byte[] hash2 = (i + 1 < currentLevel.size()) ? currentLevel.get(i + 1) : hash1;
            byte[] combinedHash = combineHashes(hash1, hash2);
            nextLevel.add(combinedHash);

            // Verifica se o hash fornecido está presente
            if (MessageDigest.isEqual(hash, hash1) || MessageDigest.isEqual(hash, hash2)) {
                hashFound = true;
            }

            // Verifica se há duplicidade antes de detectar mutação
            if (MessageDigest.isEqual(hash1, hash2)
                    && !MessageDigest.isEqual(combinedHash, combineHashes(hash2, hash1))) {
                System.out.println("Mutação detectada nas subárvores!");
            }
        }
        currentLevel = nextLevel;
    }

    return hashFound && MessageDigest.isEqual(currentLevel.get(0), merkleRoot);
}


    private static byte[] combineHashes(byte[] hash1, byte[] hash2) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(hash1);
        digest.update(hash2);
        return digest.digest();
    }

    public static byte[] calculateMerkleRoot(List<byte[]> hashes) throws Exception {
        List<byte[]> currentLevel = new ArrayList<>(hashes);
        while (currentLevel.size() > 1) {
            List<byte[]> nextLevel = new ArrayList<>();
            for (int i = 0; i < currentLevel.size(); i += 2) {
                byte[] hash1 = currentLevel.get(i);
                byte[] hash2 = (i + 1 < currentLevel.size()) ? currentLevel.get(i + 1) : hash1;
                byte[] combinedHash = combineHashes(hash1, hash2);
                nextLevel.add(combinedHash);

                // Verifica se há duplicidade antes de detectar mutação
                if (MessageDigest.isEqual(hash1, hash2)
                        && !MessageDigest.isEqual(combinedHash, combineHashes(hash2, hash1))) {
                    System.out.println("Mutação detectada nas subárvores!");
                }
            }
            currentLevel = nextLevel;
        }
        return currentLevel.get(0);
    }

    public static void main(String[] args) throws Exception {
        List<byte[]> hashes = new ArrayList<>();
        hashes.add(hexToBytes("e7f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5"));
        hashes.add(hexToBytes("d2f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5"));
        hashes.add(hexToBytes("a3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5"));
        hashes.add(hexToBytes("b4f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5"));
        // hashes.add(hexToBytes("c5f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5"));
        hashes.add(hexToBytes("d6f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5"));

        hashes.add(hexToBytes("d6f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5"));
        hashes.add(hexToBytes("d6f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5"));
        hashes.add(hexToBytes("d6f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5"));
        hashes.add(hexToBytes("d6f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5"));
        // Adicione mais hashes conforme necessário

        byte[] merkleRoot = calculateMerkleRoot(hashes);

        byte[] hashToCheck = hexToBytes("e7f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5");
        boolean isInMerkleRoot = isHashInMerkleRoot(hashToCheck, hashes);
        boolean isNotInMerkleRoot = isHashInMerkleRoot(hexToBytes("c5f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5d3e8b5c3f5"), hashes);

        System.out.println("O hash está na raiz Merkle: " + isInMerkleRoot);
        System.out.println("O hash está na raiz Merkle: " + isNotInMerkleRoot);
        System.out.println("Raiz Merkle: " + bytesToHex(merkleRoot));
    }

    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
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
