package br.com.rsdconsultoria.rdcoin.dto;

/**
 * A "reason" why a block was invalid, suitable for determining whether the
 * provider of the block should be banned/ignored/disconnected/etc.
 * These are much more granular than the rejection codes, which may be more
 * useful for some other use-cases.
 */
public enum BlockValidationResult {
  BLOCK_RESULT_UNSET, // !< initial value. Block has not yet been rejected
  BLOCK_CONSENSUS, // !< invalid by consensus rules (excluding any below reasons)
  /**
   * Invalid by a change to consensus rules more recent than SegWit.
   * Currently unused as there are no such consensus rule changes, and any
   * download
   * sources realistically need to support SegWit in order to provide useful data,
   * so differentiating between always-invalid and invalid-by-pre-SegWit-soft-fork
   * is uninteresting.
   */
  BLOCK_RECENT_CONSENSUS_CHANGE,
  BLOCK_CACHED_INVALID, // !< this block was cached as being invalid and we didn't store the reason why
  BLOCK_INVALID_HEADER, // !< invalid proof of work or time too old
  BLOCK_MUTATED, // !< the block's data didn't match the data committed to by the PoW
  BLOCK_MISSING_PREV, // !< We don't have the previous block the checked one is built on
  BLOCK_INVALID_PREV, // !< A block this one builds on is invalid
  BLOCK_TIME_FUTURE, // !< block timestamp was > 2 hours in the future (or our clock is bad)
  BLOCK_CHECKPOINT, // !< the block failed to meet one of our checkpoints
  BLOCK_HEADER_LOW_WORK // !< the block header may be on a too-little-work chain
}
