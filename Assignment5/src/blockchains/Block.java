package blockchains;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {

  // +--------+-------------------------------------------------------
  // | Fields |
  // +--------+

  private int num;
  private int data;
  private long nonce;
  private Hash prev;
  private Hash h;


  // +--------------+-------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Constructs a block by mining the smallest nonce that produces a valid hash
   * 
   * @param num
   * @param amount
   * @param prevHash
   * @throws NoSuchAlgorithmException
   */

  public Block(int num, int amount, Hash prevHash) throws NoSuchAlgorithmException {
    this.num = num;
    this.data = amount;
    this.prev = prevHash;

    MessageDigest md = MessageDigest.getInstance("sha-256");
    boolean found = false;

    for (long i = 0; !found; i++) {
      // Each integer occupies 4 bytes, while each long occupies 8
      md.update(ByteBuffer.allocate(8).putInt(num).putInt(amount).array());
      if (prevHash != null)
        md.update(prevHash.getData());
      md.update(ByteBuffer.allocate(8).putLong(i).array());
      Hash h = new Hash(md.digest());
      if (h.isValid()) {
        this.nonce = i;
        this.h = h;
        found = true;
      }
    }
  }

  /**
   * Constructs a block from data given, provided that the nonce is correct
   * 
   * @param num
   * @param amount
   * @param prevHash
   * @param nonce
   * @throws NoSuchAlgorithmException
   */
  public Block(int num, int amount, Hash prevHash, long nonce) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("sha-256");
    // Each integer occupies 4 bytes, while each long occupies 8
    md.update(ByteBuffer.allocate(8).putInt(num).putInt(amount).array());
    if (prevHash != null)
      md.update(prevHash.getData());
    md.update(ByteBuffer.allocate(8).putLong(nonce).array());
    this.num = num;
    this.data = amount;
    this.prev = prevHash;
    this.nonce = nonce;
    this.h = new Hash(md.digest());
  }

  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  public int getNum() {
    return this.num;
  }

  public int getAmount() {
    return this.data;
  }

  public long getNonce() {
    return this.nonce;
  }

  public Hash getPrevHash() {
    return this.prev;
  }

  public Hash getHash() {
    return this.h;
  }

  public String toString() {
    String prev = "null";
    if (this.prev != null)
      prev = this.prev.toString();
    return "Block " + this.num + " (Amount: " + this.data + ", Nonce: " + this.nonce
        + ", prevHash: " + prev + ", hash: " + this.h.toString() + ")";
  }
}
