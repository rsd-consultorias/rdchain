package br.com.rsdconsultoria.rdcoin.dto;

/**
 * A "reason" why a transaction was invalid, suitable for determining whether
 * the
 * provider of the transaction should be banned/ignored/disconnected/etc.
 */
public enum TxValidationResult {
    TX_RESULT_UNSET, // !< initial value. Tx has not yet been rejected
    TX_CONSENSUS, // !< invalid by consensus rules
    /**
     * Invalid by a change to consensus rules more recent than SegWit.
     * Currently unused as there are no such consensus rule changes, and any
     * download
     * sources realistically need to support SegWit in order to provide useful data,
     * so differentiating between always-invalid and invalid-by-pre-SegWit-soft-fork
     * is uninteresting.
     */
    TX_RECENT_CONSENSUS_CHANGE,
    TX_INPUTS_NOT_STANDARD, // !< inputs (covered by txid) failed policy rules
    TX_NOT_STANDARD, // !< otherwise didn't meet our local policy rules
    TX_MISSING_INPUTS, // !< transaction was missing some of its inputs
    TX_PREMATURE_SPEND, // !< transaction spends a coinbase too early, or violates locktime/sequence
                        // locks
    /**
     * Transaction might have a witness prior to SegWit
     * activation, or witness may have been malleated (which includes
     * non-standard witnesses).
     */
    TX_WITNESS_MUTATED,
    /**
     * Transaction is missing a witness.
     */
    TX_WITNESS_STRIPPED,
    /**
     * Tx already in mempool or conflicts with a tx in the chain
     * (if it conflicts with another tx in mempool, we use MEMPOOL_POLICY as it
     * failed to reach the RBF threshold)
     * Currently this is only used if the transaction already exists in the mempool
     * or on chain.
     */
    TX_CONFLICT,
    TX_MEMPOOL_POLICY, // !< violated mempool's fee/size/descendant/RBF/etc limits
    TX_NO_MEMPOOL, // !< this node does not have a mempool so can't validate the transaction
    TX_RECONSIDERABLE, // !< fails some policy, but might be acceptable if submitted in a (different)
                       // package
    TX_UNKNOWN, // !< transaction was not validated because package failed
}
