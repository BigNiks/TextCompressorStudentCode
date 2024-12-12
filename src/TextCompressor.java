/******************************************************************************
 *  Compilation:  javac TextCompressor.java
 *  Execution:    java TextCompressor - < input.txt   (compress)
 *  Execution:    java TextCompressor + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   abra.txt
 *                jabberwocky.txt
 *                shakespeare.txt
 *                virus.txt
 *
 *  % java DumpBinary 0 < abra.txt
 *  136 bits
 *
 *  % java TextCompressor - < abra.txt | java DumpBinary 0
 *  104 bits    (when using 8-bit codes)
 *
 *  % java DumpBinary 0 < alice.txt
 *  1104064 bits
 *  % java TextCompressor - < alice.txt | java DumpBinary 0
 *  480760 bits
 *  = 43.54% compression ratio!
 ******************************************************************************/

/**
 *  The {@code TextCompressor} class provides static methods for compressing
 *  and expanding natural language through textfile input.
 *
 *  @author Zach Blick, Niko Madriz
 */
public class TextCompressor {

    private static void compress() {

        // TODO: Complete the compress() method
        TST tst = new TST();
        for (int i = 0; i < 255; i++) {
            char c = (char) i;
            String s = "";
            s += c;
            tst.insert(s, i);
        }
        String encrypted = BinaryStdIn.readString();
        int i = 0;
        int n = encrypted.length();
        String prefix = "";
        int prefixLength = 257;
        while (i < n) {
            prefix = tst.getLongestPrefix(encrypted, i);
            BinaryStdOut.write(tst.lookup(prefix), 8);
            if (i + prefix.length() < encrypted.length()) {
                tst.insert(encrypted.substring(i + prefix.length(), i + prefix.length() + 1), prefixLength);
                prefixLength++;
            }
             i += prefix.length();
        }
        BinaryStdOut.write(256);
        BinaryStdOut.close();
    }

    private static void expand() {

        // TODO: Complete the expand() method
        String[] map = new String[4096];
        for (int i = 0; i < 255; i++) {
            char c = (char) i;
            String s = "";
            s += c;
            map[i] = s;
        }
        int i = 0;
        int n = BinaryStdIn.readByte();
        String encrypted = BinaryStdIn.readString();
        String prefix = "";
        String lookahead = "";
        int pf = 257;
        while (i < n) {
            int code = BinaryStdIn.readInt(8);
            prefix = map[code];
            BinaryStdOut.write(prefix);
            int lookaheadCode = BinaryStdIn.readInt(8);
            if (!map[lookaheadCode].isEmpty()) {
                map[pf] = map[code] + map[lookaheadCode].substring(0, 1);
            }
            else {
                map[pf] = map[code] + map[lookaheadCode];
            }
            if (code == 256) {
                break;
            }
            i++;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
