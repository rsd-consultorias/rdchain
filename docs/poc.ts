import * as crypto from 'node:crypto';
import * as sql from 'sqlite3';

export module DatabaseUtil {
    // const db = new sql.Database('blockchain.db');

    export function createDatabase() {
        let script = `
        CREATE TABLE IF NOT EXISTS CONFIGURATION (VERSION INTEGER);
        CREATE TABLE IF NOT EXISTS BLOCKS (VERSION INTEGER,SEQUENCE INTEGER,PREVIOUS_HASH CHAR(128),HASH CHAR(128),TIMESTAMP LONG,VALIDATOR CHAR(128),TRANSACTIONS TEXT,SIGNATURE TEXT);
        CREATE TABLE IF NOT EXISTS TRANSACTIONS (VERSION INTEGER,SEQUENCE INTEGER,HASH CHAR(128),SENDER CHAR(128),RECEIVER CHAR(128),AMOUNT LONG,TAX INTEGER,TIMESTAMP LONG,SIGNATURE TEXT);
        `;
        // db.exec(script);
    }

    export function addTx(transaction: Blockchain.Transaction) {
        // const stmt = db.prepare("INSERT INTO TRANSACTIONS (VERSION,SEQUENCE,HASH,SENDER,RECEIVER,AMOUNT,TAX,TIMESTAMP,SIGNATURE) VALUES(?,?,?,?,?,?,?,?,?);");
        // stmt.run(transaction.version, transaction.sequence!, transaction.hash!, transaction.sender, transaction.receiver, transaction.amount, transaction.tax!, transaction.timestamp, transaction.signature!);
        // stmt.finalize();
    }

    export function addBlock(block: Blockchain.Block) {
        // const stmt = db.prepare("INSERT INTO BLOCKS (VERSION,SEQUENCE,PREVIOUS_HASH,HASH,TIMESTAMP,VALIDATOR,TRANSACTIONS,SIGNATURE) VALUES (?,?,?,?,?,?,?,?);");
        // stmt.run(block.version, block.sequence, block.previousHash, block.hash!, block.timestamp, block.validator, JSON.stringify(block.transactions), block.signature!);
        // stmt.finalize();
    }

    export function closeDatabase() {
        // db.close();
    }

}

export module CryptoUtil {
    // Mapa para armazenar a relação entre hash e chave pública
    const publicKeyMap = new Map();

    // Função para gerar uma chave privada a partir de uma senha
    export function generatePrivateKeyFromPassword(password: string): Buffer {
        const salt = crypto.randomBytes(32); // Você pode armazenar e reutilizar o salt
        const key = crypto.pbkdf2Sync(`${password}${new Date()}`, salt, 100000, 64, 'sha512');
        // console.log(crypto.getHashes());
        return key;
    }

    // Função para gerar um par de chaves RSA a partir de uma chave privada derivada
    export function generateKeyPairFromPrivateKey(privateKey: Buffer) {
        const { publicKey, privateKey: rsaPrivateKey } = crypto.generateKeyPairSync('rsa', {
            modulusLength: 2048,
            privateKeyEncoding: {
                type: 'pkcs1',
                format: 'pem',
            },
            publicKeyEncoding: {
                type: 'pkcs1',
                format: 'pem'
            },
        });

        // Substituir a chave privada gerada pela chave privada derivada
        // rsaPrivateKey.key = privateKey;

        return { publicKey, privateKey: rsaPrivateKey };
    }

    // Função para gerar o hash da chave pública
    export function generatePublicKeyHash(publicKey: string): string {
        const hash = crypto.createHash('sha512');
        hash.update(publicKey);
        let publicKeyHash = hash.digest('hex');

        // Armazenar a chave pública no mapa
        publicKeyMap.set(publicKeyHash, publicKey);

        return publicKeyHash;
    }

    // Função para assinar uma mensagem
    export function signMessage(message: string, privateKey: string): string {
        const sign = crypto.createSign('SHA512');
        sign.update(message);
        sign.end();
        const signature = sign.sign(privateKey, 'base64');
        return signature;
    }

    // Função para verificar uma assinatura
    export function verifyMessage(message: string, signature: string, publicKey: string): boolean {
        const verify = crypto.createVerify('SHA512');
        verify.update(message);
        verify.end();
        return verify.verify(publicKey, signature, 'base64');
    }

    // Função para obter a chave pública a partir do hash
    function getPublicKeyFromHash(publicKeyHash: string): string | undefined {
        return publicKeyMap.get(publicKeyHash);
    }

    // Função para verificar uma assinatura usando o hash da chave pública
    export function verifyMessageWithHash(message: string, signature: string, publicKeyHash: string): boolean {
        const verify = crypto.createVerify('SHA512');
        verify.update(message);
        verify.end();
        const publicKey = getPublicKeyFromHash(publicKeyHash); // Função fictícia para obter a chave pública a partir do hash
        return verify.verify(publicKey!, signature, 'base64');
    }
}

export module Blockchain {

    export class Wallet {
        publicKey: string;
        privateKey: string;
        address?: string;
        amount: number;

        constructor(email: string, password: string, amount: number) {
            let chavePrivada = CryptoUtil.generatePrivateKeyFromPassword(`${email}${password}`);
            let chaves = CryptoUtil.generateKeyPairFromPrivateKey(chavePrivada);
            this.privateKey = chaves.privateKey;
            this.publicKey = chaves.publicKey;

            this.address = CryptoUtil.generatePublicKeyHash(this.publicKey);
            this.amount = amount;
        }

        calculateHash(sender: string, receiver: string, amount: number, timestamp: number): string {
            return crypto.createHash('sha512').update(`${sender}${receiver}${amount}${timestamp}`).digest('hex');
        }

        signTransaction(transaction: Transaction) {
            const hashTx = this.calculateHash(transaction.sender, transaction.receiver, transaction.amount, transaction.timestamp);
            return CryptoUtil.signMessage(hashTx, this.privateKey);
        }

        // Função para verificar a assinatura usando a chave pública
        verifySignature(message: string, signature: string): boolean {
            return CryptoUtil.verifyMessage(message, signature, this.publicKey);
        }
    }

    export type Transaction = {
        version: string;
        sequence?: number;
        hash?: string;
        sender: string;
        receiver: string;
        amount: number;
        tax?: number;
        timestamp: number;
        signature?: string;
    };

    export type Stake = {
        address: string;
        amount: number;
        wallet: Wallet;
    };

    export type Block = {
        version: string;
        sequence: number;
        previousHash: string;
        timestamp: number;
        transactions: Transaction[];
        hash?: string;
        validator: string;
        signature?: string;
    };

    export class Blockchain {
        chain: Block[];
        pendingTransactions: Transaction[];
        stakes: Stake[];
        wallets: Map<string, Wallet>;
        REWARD_TAX: number = 83;

        constructor() {
            this.chain = [];
            this.stakes = [];
            this.wallets = new Map();
            this.pendingTransactions = [];
            this.createGenesisBlock();
        }

        createGenesisBlock() {
            const genesisBlock: Block = {
                version: '1',
                sequence: 0,
                previousHash: '0',
                timestamp: Date.now(),
                transactions: [],
                hash: this.calculateHash(0, '0', Date.now(), [], 'system'),
                validator: 'system'
            };
            this.chain.push(genesisBlock);
        }

        calculateHash(index: number, previousHash: string, timestamp: number, transactions: Transaction[], validator: string): string {
            return crypto.createHash('sha512').update(index + previousHash + timestamp + transactions.reduce((x: string, y: Transaction) => x + y.hash, '') + validator).digest('hex');
        }

        calculateTransactionHash(sender: string, receiver: string, amount: number, timestamp: number): string {
            return crypto.createHash('sha512').update(`${sender}${receiver}${amount}${timestamp}`).digest('hex');
        }

        getLatestBlock(): Block {
            return this.chain[this.chain.length - 1];
        }

        addBlock(newBlock: Block) {
            newBlock.previousHash = this.getLatestBlock().hash!;
            newBlock.hash = this.calculateHash(newBlock.sequence, newBlock.previousHash, newBlock.timestamp, newBlock.transactions, newBlock.validator);
            this.chain.push(newBlock);
        }

        createTransaction(transaction: Transaction) {
            let wallet = this.wallets.get(transaction.sender)!;

            if (wallet.amount > (transaction.amount + this.REWARD_TAX)) {
                transaction.hash = this.calculateTransactionHash(transaction.sender, transaction.receiver, transaction.amount, transaction.timestamp);
                transaction.tax = this.REWARD_TAX;
                this.pendingTransactions.push(transaction);
                wallet.amount -= (transaction.amount + this.REWARD_TAX);
            } else {
                // TODO o que fazer com a transação rejeitada?
            }
        }

        addStake(stake: Stake) {
            this.stakes.push(stake);
        }

        updateStake(address: string, amount: number) {
            this.stakes.forEach(stake => {
                if (stake.address === address) {
                    stake.amount += amount;
                }
            });
        }

        selectValidator(): string {
            let random = Math.floor(Math.random() * this.stakes.length);
            return this.stakes[random].address;
        }

        addWallet(wallet: Wallet) {
            this.wallets.set(wallet.address!, wallet);
        }

        async auditAndConfirmPendingTransactions() {
            if (this.pendingTransactions.length > 0) {
                let transactionIndex = 0;
                const miner = this.selectValidator();
                const minerWallet = this.stakes.filter(x => x.address === miner)[0];
                const newBlock: Block = {
                    version: '1',
                    sequence: this.chain.length,
                    previousHash: this.getLatestBlock().hash!,
                    timestamp: Date.now(),
                    transactions: [],
                    validator: miner
                };

                this.pendingTransactions.forEach(transaction => {
                    transaction.sequence = transactionIndex;
                    newBlock.transactions.push(transaction);
                    DatabaseUtil.addTx(transaction);
                    transactionIndex += 1;
                });

                const rewardTransaction: Transaction = {
                    version: '1',
                    sequence: transactionIndex,
                    sender: 'system',
                    receiver: miner,
                    amount: this.pendingTransactions.reduce((tax, tx) => tax + tx.tax!, 0),
                    timestamp: Date.now()
                };
                rewardTransaction.hash = this.calculateTransactionHash(rewardTransaction.sender, rewardTransaction.receiver, rewardTransaction.amount, rewardTransaction.timestamp);
                rewardTransaction.signature = CryptoUtil.signMessage(rewardTransaction.hash!, minerWallet.wallet.privateKey);
                newBlock.transactions.push(rewardTransaction);
                DatabaseUtil.addTx(rewardTransaction);

                newBlock.hash = this.calculateHash(newBlock.sequence, newBlock.previousHash, newBlock.timestamp, newBlock.transactions, newBlock.validator);

                newBlock.signature = CryptoUtil.signMessage(newBlock.hash!, minerWallet.wallet.privateKey);

                this.chain.push(newBlock);
                DatabaseUtil.addBlock(newBlock);

                newBlock.transactions.forEach(transaction => {
                    if (this.wallets.has(transaction.receiver))
                        this.wallets.get(transaction.receiver)!.amount += transaction.amount;
                });

                this.pendingTransactions = [];
            }
        }
    }

    export type CurrencyPair = {
        base: string;
        quote: string;
        rate: number;
    };

    export class CurrencyGraph {
        private graph: Map<string, Map<string, number>>;

        constructor() {
            this.graph = new Map();
        }

        addCurrencyPair(pair: CurrencyPair) {
            if (!this.graph.has(pair.base)) {
                this.graph.set(pair.base, new Map());
            }
            this.graph.get(pair.base)!.set(pair.quote, pair.rate);
        }

        getExchangeRate(base: string, quote: string): number | null {
            if (this.graph.has(base) && this.graph.get(base)!.has(quote)) {
                return this.graph.get(base)!.get(quote)!;
            }
            return null;
        }
    }
}
/////
DatabaseUtil.createDatabase();
const blockChain = new Blockchain.Blockchain();
const wallet1 = new Blockchain.Wallet('sender@dominio.com', 'a72b91f5b60eb46a43e17a06c020e92166145049c610974b42dc287be990176b', 200000);
const wallet2 = new Blockchain.Wallet('receiver@dominio.com', 'a72b91f5b60eb46a43e17a06c020e92166145049c610974b42dc287be990176b', 0);
const walletMinerA = new Blockchain.Wallet('miner-a@dominio.com', 'a72b91f5b60eb46a43e17a06c020e92166145049c610974b42dc287be990176b', 0);
const walletMinerB = new Blockchain.Wallet('miner-b@dominio.com', 'a72b91f5b60eb46a43e17a06c020e92166145049c610974b42dc287be990176b', 0);
const walletMinerC = new Blockchain.Wallet('miner-c@dominio.com', 'a72b91f5b60eb46a43e17a06c020e92166145049c610974b42dc287be990176b', 0);

blockChain.addWallet(wallet1);
blockChain.addWallet(wallet2);
blockChain.addWallet(walletMinerA);
blockChain.addWallet(walletMinerB);
blockChain.addWallet(walletMinerC);

blockChain.addStake({ address: walletMinerA.address!, amount: 0, wallet: walletMinerA });
blockChain.addStake({ address: walletMinerB.address!, amount: 0, wallet: walletMinerB });
blockChain.addStake({ address: walletMinerC.address!, amount: 0, wallet: walletMinerC });
blockChain.wallets.forEach(wallet => console.log(`${wallet.address?.toUpperCase()}: ${(wallet.amount / 100).toFixed(2)}`));

let quantidade = 4;
let inicio: Date;
for (let j = 0; j < 10; j++) {
    inicio = new Date();
    for (let i = 0; i < quantidade; i++) {
        let tx: Blockchain.Transaction = {
            version: '1',
            sender: wallet1.address!,
            receiver: wallet2.address!,
            amount: 20000,
            timestamp: Date.now()
        };
        tx.signature = wallet1.signTransaction(tx);

        blockChain.createTransaction(tx);
    }

    blockChain.auditAndConfirmPendingTransactions().then().catch(console.error);
    let termino = new Date();
    let tempo = (termino.getTime() - inicio.getTime()) / 1000;
    console.log(`Tempo: ${tempo} s @ ${blockChain.getLatestBlock().transactions.length - 1} transações`);
}

let lastBlock = blockChain.getLatestBlock();
console.log(lastBlock);
lastBlock.transactions.pop();
let tx1 = lastBlock.transactions.pop()!;

// console.log(JSON.stringify(tx1, null, 2));
console.log(`Verificar chave carteira 1: ${CryptoUtil.verifyMessageWithHash(tx1.hash!, tx1.signature!, wallet1.address!)}`);
console.log(`Verificar chave carteira 2: ${CryptoUtil.verifyMessageWithHash(tx1.hash!, tx1.signature!, wallet2.address!)}`);
blockChain.wallets.forEach(wallet => console.log(`${wallet.address?.toUpperCase()}: ${(wallet.amount / 100).toFixed(2)}`));

console.log(wallet1.publicKey);

/*
-- Gerar uma chave privada em formato PEM:
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048 -aes-256-cbc -pass pass:1234

-- Converter a chave privada de PEM para DER:
openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key.pem -out private_key.der -nocrypt -pass pass:1234

-- Extrair a chave pública da chave privada em formato DER:
openssl rsa -inform DER -in private_key.der -pubout -outform DER -out public_key.der
*/

DatabaseUtil.closeDatabase();