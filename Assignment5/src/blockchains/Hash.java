package blockchains;

import java.util.Arrays;

public class Hash {

  // +--------+-------------------------------------------------------
  // | Fields |
  // +--------+

  private byte[] data;


  // +--------------+-------------------------------------------------
  // | Constructors |
  // +--------------+

  public Hash(byte[] data) {
    this.data = data;
  }


  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  public byte[] getData() {
    return data;
  }

  public boolean isValid() {
    return (this.data[0] == 0) && (this.data[1] == 0) && (this.data[2] == 0);
  }

  public String toString() {
    String result = new String();
    for (Byte b : data)
      result += String.format("%02x", Byte.toUnsignedInt(b));
    return result;
  }

  public boolean equals(Object other) {
    return other instanceof Hash && Arrays.equals(this.data, ((Hash) other).getData());
  }
}
