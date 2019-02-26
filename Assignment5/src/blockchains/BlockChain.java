package blockchains;

import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BlockChain {
  private static class Node {
    public Block b;
    public Node next;

    public Node(Block b, Node next) {
      this.b = b;
      this.next = next;
    }
  } // static class Node


  // +--------+-------------------------------------------------------
  // | Fields |
  // +--------+

  private Node first;
  private Node last;
  private int size;
  private int alice;
  private int bob;


  // +--------------+-------------------------------------------------
  // | Constructors |
  // +--------------+

  public BlockChain(int initial) throws NoSuchAlgorithmException {
    Block b = new Block(0, initial, null);
    this.first = new Node(b, null);
    this.last = this.first;
    this.size = 1;
    this.alice = initial;
    this.bob = 0;
  }


  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  public int getSize() {
    return this.size;
  }

  public Hash getHash() {
    return this.last.b.getHash();
  } // great pun

  public Node getFirst() {
    return this.first;
  }

  public Block mine(int amount) throws NoSuchAlgorithmException {
    return new Block(this.last.b.getNum() + 1, amount, this.last.b.getHash());
  }

  /**
   * Appends valid block to the end of BlockChain
   * 
   * @param blk
   * @throws IllegalArgumentException
   * @throws NoSuchAlgorithmException
   */
  public void append(Block blk) throws IllegalArgumentException, NoSuchAlgorithmException {
    Block temp = new Block(blk.getNum(), blk.getAmount(), blk.getPrevHash());
    if (!blk.getHash().equals(temp.getHash()))
      throw new IllegalArgumentException("Bad Block");

    this.last.next = new Node(blk, null);
    this.last = this.last.next;
    this.size++;

    this.alice += blk.getAmount();
    this.bob -= blk.getAmount();
  }

  public boolean isValidBlockChain() throws NoSuchAlgorithmException {
    if (this.alice < 0 || this.bob < 0)
      return false;

    Node n = this.first;
    MessageDigest md = MessageDigest.getInstance("sha-256");

    // regenerates hash to assure consistency
    while (n != null) {
      md.update(ByteBuffer.allocate(8).putInt(n.b.getNum()).putInt(n.b.getAmount()).array());
      if (n.b.getPrevHash() != null)
        md.update(n.b.getPrevHash().getData());
      md.update(ByteBuffer.allocate(8).putLong(n.b.getNonce()).array());
      Hash h = new Hash(md.digest());
      if (!h.equals(n.b.getHash()))
        return false;
      n = n.next;
    } // end while

    return true;
  }

  /**
   * removes last Block on BlockChain when it contains more than one Block)
   * 
   * @return true or false
   */
  public boolean removeLast() {
    if (this.size > 1) {
      Node temp = this.first;
      while (temp.next != this.last)
        temp = temp.next;
      this.alice -= this.last.b.getAmount();
      this.bob += this.last.b.getAmount();
      this.last = temp;
      this.last.next = null;
      this.size--;
      return true;
    } // end if
    return false;
  }

  public void printBalances() {
    PrintWriter pen = new PrintWriter(System.out, true);
    Node temp = this.first;
    while (temp != null) {
      pen.println(temp.b.toString());
      temp = temp.next;
    }
  }

  public void report() {
    PrintWriter p = new PrintWriter(System.out, true);
    p.println("Alice: " + this.alice + ", Bob: " + this.bob);
  }
}
