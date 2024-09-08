package br.com.rsdconsultoria.rdcoin;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Scanner;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import br.com.rsdconsultoria.rdcoin.infra.p2p.AuthService;
import br.com.rsdconsultoria.rdcoin.infra.p2p.Node;
import br.com.rsdconsultoria.rdcoin.infra.p2p.P2PNetwork;
import br.com.rsdconsultoria.rdcoin.util.CryptoUtil;
import br.com.rsdconsultoria.rdcoin.util.PubPrivKeysLoader;

public class Main {
    public static void main(String[] args) throws Exception {

        // var newPrivateKey = CryptoUtil.generatePrivateKeyFromPassword("1234");
        // System.out.println(newPrivateKey);

        // var keyPair = CryptoUtil.generateKeyPairFromPrivateKey(newPrivateKey);
        // System.out.printf("Private: %s \n\n",
        // Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        // System.out.printf("Public: %s \n\n",
        // Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));

        // var privateKey = PubPrivKeysLoader.loadPrivateKey(
        // "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCNxfbmshOXjlwuH1WwJ6om3a/spc/yjrOCHV0ECs4jT0PDzj1qSyu/6CyPyd0jawnmtFjQF2+JYDdU6Lyt1Iyv6CIObRBuX9fB4OGNRBvsLoS3HQpAmi+A/8kvvYvwy/1aJCC66SWzl29G04esJaIWbjfZZ07V2evOjfDlC3L8s1RB/4sFExzBNNjHqpRx0ldLT1dQu6U0WzKrRRDnBEwbEWhsNJkUlk9v1K/yImL4/aW1JAGynpz0IyEfH0WvXjbMS+xH6g+wKCGADtwk7inOOjcpMHIatDAsoweyejZHKwP/uPS7VKqavd29cgad/2quV9Th/t0e76IvO+cRkLFLAgMBAAECggEAAp4zbZXzCxGdUPPrk8HEjTLH/kYfLYR5euw3fOae+qnxDDTK9fAgOH5jrETdaSHSrS7xYOfxFSFXHdTgy99b7B4Mp78kPLvwv2suq+DxGKhqc1/9Tq1KNhPP6RDGYvjxW1b4pie+AxX6mRBsHFkWW1tHrr0PCJ+Dw9NJ+oDLa8n0HnQcpuVItWEC9+a9JlQji4wq8Tb0hkLgQjmJZ1pCSkHlYBQCH/ueW+LxxB4yVWn0ATdPPchyp4OcEyueRnhzVJKNXoagfDXAnqSFXj7YGWNh8xG+9BhL+FKtx2J/qmfozNG+RiP+UMTc9wAIgT5UrY7SPdMu4IChdwrA4QbKeQKBgQDG9tlfzBkk5IX27/YRF5C8U7zFE+XNVAKNHYzoVLvlfPqt8pPITQoGoXLdRepUJClZ1ZvNplqUFbfjIP04TlPVhEFCLYNg1nnYNshyq7XtYqGUX3VBWkLj5S3jHapJ+0QURJ/TvBOMVuqvvA0W327VaFrZpMTALegFh86D5A1orQKBgQC2ahyAIZgccb9vdnX4869rOfiDI0LhpVPs3KpRa8/8Zvcz5t5UUGGGNrtVY2qanJW4ZbSyvOvRCBPsN6aiBvwJqJx3h5iwBvD/i8tWp2qX1sj2tteS2miRqJWSJKPJPn78m3bnZkH+q2WpJd0GJqHyE4mGi+m1RWjXJje3alXo1wKBgFamnxjnjHPSyr022JWtC63ZUjcKaWS5F7tZiUqNzJ8vhQVIxi/PzXQB0EKp9lnavd+P9iDkOEKSCtnyPhXNlxlAViN6KuN7xOjzQu0d90eBUxmJUUm7A7K41vk4rDc4JPgmwuy11lWibOdIIPM7L8vlfNuYtM91rrzp3nj2UxipAoGBAJIHACnSuuKKbKXePzK8YYGFNtxELhZVM4o6XF4x4J4+D0cNWo5Eis3WosEIhR6gm3qycKVld6IuxUQkiNDMppdU020cr66639KzJmSIQVysJNTcKlb1YSC5xILpsfQ4g4/VJKq5wbsNmqR+GyIBa0NPQJjlCIKEvhj+DzxVc9ChAoGAJDeJWfGCVZfaiwco2cTbvdldCcuy1HD7qawicganrO3WtbYhhR+SYzjeNlJcXTPiHrPu7l379uqfSUUZB7++n/yFPGjVhvjaF0y2YTepAWsK+axpkgR/npPQNVrq6rFse1Sd4ieSZFcbSUNQnn+wfx5gwGgD2/WiwyVHp1nNgNY=");
        // var message = CryptoUtil.calculateSHA512("teste de teste");
        // var signature = CryptoUtil.signMessage(message, privateKey);

        // System.out.printf("Message: %s \n\n", message);
        // System.out.printf("Signature: %s \n\n", signature);

        // var publicKey = PubPrivKeysLoader.loadPublicKey(
        // "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjcX25rITl45cLh9VsCeqJt2v7KXP8o6zgh1dBArOI09Dw849aksrv+gsj8ndI2sJ5rRY0BdviWA3VOi8rdSMr+giDm0Qbl/XweDhjUQb7C6Etx0KQJovgP/JL72L8Mv9WiQguukls5dvRtOHrCWiFm432WdO1dnrzo3w5Qty/LNUQf+LBRMcwTTYx6qUcdJXS09XULulNFsyq0UQ5wRMGxFobDSZFJZPb9Sv8iJi+P2ltSQBsp6c9CMhHx9Fr142zEvsR+oPsCghgA7cJO4pzjo3KTByGrQwLKMHsno2RysD/7j0u1Sqmr3dvXIGnf9qrlfU4f7dHu+iLzvnEZCxSwIDAQAB");

        // var valid = CryptoUtil.verifyMessage(message, signature, publicKey);

        // System.out.println(valid);

        //////// Código para rede P2P
        // Registrar o provedor BouncyCastle
        Security.addProvider(new BouncyCastleProvider());
        // Cria um gerador de par de chaves para o algoritmo RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); // Tamanho da chave
        // Gerar uma chave AES
        KeyGenerator secretKeyGen = KeyGenerator.getInstance("AES");
        secretKeyGen.init(256); // ou 128, 192 bits
        SecretKey secretKey = secretKeyGen.generateKey();

        // Gera o par de chaves (pública e privada)
        KeyPair pair = keyGen.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();

        // Gera um par de chaves para criptografia RSA
        KeyPair keyPair1 = pair;
        KeyPair keyPair2 = pair;

        P2PNetwork network = new P2PNetwork();

        // Gera tokens JWT para os nós
        String token1 = AuthService.generateToken("node1");
        String token2 = AuthService.generateToken("node2");

        // Adiciona nós à rede
        int port = Integer.parseInt(args[0]);
        Node nodeLocal = new Node("localhost", port, keyPair1.getPublic(), keyPair1.getPrivate(), token1, secretKey);
        nodeLocal.setItsMe(true);
        network.addNode(nodeLocal, keyPair1.getPublic());

        // Inicia nós em threads separadas
        new Thread(() -> {
            try {
                network.startNode(port, keyPair1.getPrivate());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            var message = scanner.nextLine();

            if (message.startsWith("add-node")) {
                var params = message.split(" ");
                network.addNode(new Node("localhost", Integer.parseInt(params[1]), keyPair1.getPublic(),
                        keyPair1.getPrivate(), token1,
                        secretKey), keyPair1.getPublic());
            } else if (message.startsWith("exit")) {
                System.exit(0);
            } else {
                network.broadcastMessage(message);
            }
        }
    }
}