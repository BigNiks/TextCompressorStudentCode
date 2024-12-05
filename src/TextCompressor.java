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
        String[] words = new String[4];
        int[] bwords = new int[4];
        bwords[0] = 0b00000001;
        bwords[1] = 0b00000010;
        bwords[2] = 0b00000100;
        bwords[3] = 0b00001000;
        words[0] = "the";
        words[1] = "and";
        words[2] = "a";
        words[3] = "be";
        String encrypted = BinaryStdIn.readString();
        int i = 0;
        int n = encrypted.length();
        while (i < n) {
            for (int j = 0; j < words.length; j++) {
                if (encrypted.substring(i, encrypted.indexOf(" ")).equals(words[j])) {
                    BinaryStdOut.write(bwords[j]);
                }
                else {
                    BinaryStdOut.write(encrypted.substring(i, encrypted.indexOf(" ")));
                }
            }
            i++;
        }
        BinaryStdOut.close();
    }

    private static void expand() {

        // TODO: Complete the expand() method
        String[] words = new String[10];
        int[] bwords = new int[10];
        bwords[0] = 0b00000001;
        bwords[1] = 0b00000010;
        bwords[2] = 0b00000100;
        bwords[3] = 0b00001000;
        words[0] = "the";
        words[1] = "and";
        words[2] = "a";
        words[3] = "be";
        int i = 0;
        int n = BinaryStdIn.readByte();
        String encrypted = BinaryStdIn.readString();
        while (i < n) {
            for (int j = 0; j < 4; j++) {
                if (encrypted.substring(i, i + 8).equals(bwords[i])) {
                    BinaryStdOut.write(words[i]);
                }
                else {
                    BinaryStdOut.write(encrypted.substring(i, i + 8));
                }
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
