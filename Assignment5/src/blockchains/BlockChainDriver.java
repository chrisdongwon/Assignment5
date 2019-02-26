package blockchains;

import java.io.PrintWriter;
import java.util.Scanner;

public class BlockChainDriver {
  public static void main(String[] args) throws Exception {
    if (args.length != 1 || Integer.parseInt(args[0]) < 0)
      throw new Exception("Invalid Argument");

    BlockChain data = new BlockChain(Integer.parseInt(args[0]));
    PrintWriter p = new PrintWriter(System.out, true);
    Scanner s = new Scanner(System.in);

    boolean quit = false;
    do {
      data.printBalances();
      p.println("Command? ");
      s.reset();
      String input = s.next();

      if (input.equals("mine")) {
        p.println("Amount transferred? ");
        s.reset();
        int amt = s.nextInt();
        p.println("amount = " + amt + ", nonce = " + data.mine(amt).getNonce());
      } else if (input.equals("append")) {
        p.println("Amount transferred? ");
        s.reset();
        int amt = s.nextInt();
        p.println("Nonce? ");
        s.reset();
        long n = s.nextLong();
        data.append(new Block(data.getSize(), amt, data.getHash(), n));
      } else if (input.equals("remove")) {
        if (!data.removeLast())
          p.println("At least 1 block is necessary");
      } else if (input.equals("check")) {
        if (data.isValidBlockChain())
          p.println("Chain is valid!");
        else
          p.println("Chain is invalid!");
      } else if (input.equals("report"))
        data.report();
      else if (input.equals("help"))
        help();
      else if (input.equals("quit"))
        quit = true;
      else
        p.println("Invalid command");
      p.println();
    } while (!quit); // end do-while

    s.close();
  }

  private static void help() {
    PrintWriter p = new PrintWriter(System.out, true);
    p.println("Valid commands:");
    p.println("    mine: discovers the nonce for a given transaction");
    p.println("    append: appends a new block onto the end of the chain");
    p.println("    remove: removes the last block from the end of the chain");
    p.println("    check: checks that the block chain is valid");
    p.println("    report: reports the balances of Alice and Bob");
    p.println("    help: prints this list of commands");
    p.println("    quit: quits the program");
  }
}
