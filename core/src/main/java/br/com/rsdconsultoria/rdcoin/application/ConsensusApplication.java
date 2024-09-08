package br.com.rsdconsultoria.rdcoin.application;

import java.util.ArrayList;
import java.util.List;

import br.com.rsdconsultoria.rdcoin.dto.BlockValidationResult;
import br.com.rsdconsultoria.rdcoin.dto.TxValidationResult;
import br.com.rsdconsultoria.rdcoin.dto.ValidationState;
import br.com.rsdconsultoria.rdcoin.dto.ValidationState.ModeState;
import br.com.rsdconsultoria.rdcoin.model.Block;
import br.com.rsdconsultoria.rdcoin.model.Transaction;
import br.com.rsdconsultoria.rdcoin.util.MerkleTree;

public class ConsensusApplication {
    public static final float MAX_FEE_FACTOR = 0.015f;

    public static ValidationState<TxValidationResult> checkTransaction(Transaction transaction) {
        var result = new ValidationState<TxValidationResult>();
        result.setState(ModeState.M_VALID);
        result.setResult(TxValidationResult.TX_RESULT_UNSET);

        if (Long.valueOf(transaction.getAmount()) <= 0) {
            result.setState(ModeState.M_INVALID);
            result.setResult(TxValidationResult.TX_CONSENSUS);
            result.setMessage("amount-should-be-greater-than-zero");
        }

        if (Long.valueOf(transaction.getFee()) < 0) {
            result.setState(ModeState.M_INVALID);
            result.setResult(TxValidationResult.TX_CONSENSUS);
            result.setMessage("fee-should-be-positive");
        }

        if (Long.valueOf(transaction.getFee()) >= Long.valueOf(transaction.getAmount())) {
            result.setState(ModeState.M_INVALID);
            result.setResult(TxValidationResult.TX_CONSENSUS);
            result.setMessage("fee-should-be-less-than-amount");
        }

        if (Long.valueOf(transaction.getFee()) >= (Long.valueOf(transaction.getAmount()) * MAX_FEE_FACTOR)) {
            result.setState(ModeState.M_INVALID);
            result.setResult(TxValidationResult.TX_CONSENSUS);
            result.setMessage("fee-should-be-less-than-max-limit");
        }

        return result;
    }

    public static ValidationState<BlockValidationResult> checkBlock(Block block) {
        var result = new ValidationState<BlockValidationResult>();
        result.setState(ModeState.M_VALID);
        result.setResult(BlockValidationResult.BLOCK_RESULT_UNSET);

        try {
            // Calculando a raiz de Merkle
            List<byte[]> hashes = new ArrayList<>();
            block.getTransactions().forEach(tx -> hashes.add(MerkleTree.hexToBytes(tx.getHash())));
            byte[] merkleRoot = MerkleTree.calculateMerkleRoot(hashes);

            block.setHash(MerkleTree.bytesToHex(merkleRoot));

            // TODO: verify if sub tree is mutated
            // if (false) {
            //     result.setResult(BlockValidationResult.BLOCK_MUTATED);
            //     result.setState(ModeState.M_INVALID);
            //     result.setMessage("duplicated-transaction");
            // }
        } catch (Exception exception) {
            result.setState(ModeState.M_ERROR);
        }

        return result;
    }
}
