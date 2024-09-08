package br.com.rsdconsultoria.rdcoin.infra.p2p;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

public class P2PNetwork {
    private List<Node> nodes = new ArrayList<>();
    private Map<String, PublicKey> publicKeys = new HashMap<>();

    public void addNode(Node node, PublicKey publicKey) {
        nodes.add(node);
        publicKeys.put(node.getAddress() + ":" + node.getPort(), publicKey);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void broadcastMessage(String message) throws IOException, GeneralSecurityException {
        for (Node node : nodes) {
            if (!node.isItsMe()) {
                PublicKey recipientPublicKey = publicKeys.get(node.getAddress() + ":" + node.getPort());
                node.sendMessage(message, recipientPublicKey);
            }
        }
    }

    public void startNode(int port, PrivateKey privateKey) throws IOException, GeneralSecurityException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            InputStream in = socket.getInputStream();
            byte[] tokenBytes = new byte[256];
            // in.read(tokenBytes);
            String token = new String(tokenBytes).trim();

            if (AuthService.validateToken(token)) {
                byte[] encryptedMessage = in.readAllBytes();
                Node node = findNodeByPort(port);
                if (node != null) {
                    String message = node.receiveMessage(encryptedMessage);
                    System.out.println("Node: " + port + " - Received message: " + message);
                }
            } else {
                System.out.println("Invalid token received.");
            }

            in.close();
            socket.close();
        }
    }

    private Node findNodeByPort(int port) {
        for (Node node : nodes) {
            if (node.getPort() == port) {
                return node;
            }
        }
        return null;
    }
}
