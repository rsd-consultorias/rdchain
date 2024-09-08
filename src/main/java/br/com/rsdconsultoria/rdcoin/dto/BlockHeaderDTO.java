package br.com.rsdconsultoria.rdcoin.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.rsdconsultoria.rdcoin.model.Block;

public class BlockHeaderDTO {
    private String blockHash;
    private long sequence;

    private List<String> transactionHashes;

    public BlockHeaderDTO(Block block) {
        transactionHashes = new ArrayList<String>();
        this.blockHash = block.getHash();
        sequence = Long.valueOf(block.getSequence());
        block.getTransactions().forEach(tx -> transactionHashes.add(tx.getHash()));
    }

    public String getBlockHash() {
        return blockHash;
    }

    public List<String> getTransactionHashes() {
        return transactionHashes;
    }

    public long getSequence() {
        return sequence;
    }
}
