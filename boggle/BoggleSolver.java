import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;
import java.util.HashSet;

public class BoggleSolver
{
    private final HashSet<String> dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException();
        }

        this.dictionary = new HashSet<>(dictionary.length);
        Collections.addAll(this.dictionary, dictionary);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }

        HashSet<String> validWords = new HashSet<>();

        // make a copy of boggle board
        char[][] boggleBoard = new char[board.rows()][board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                boggleBoard[i][j] = board.getLetter(i ,j);
            }
        }

        //remains to be done...

        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }

        String convertWord = convertQu(word);
        if (convertWord.length() < 3 || !dictionary.contains(convertWord)) {
            return 0;
        }

        return switch (convertWord.length()) {
            case 3, 4 -> 1;
            case 5 -> 2;
            case 6 -> 3;
            case 7 -> 5;
            default -> 11;
        };
    }

    private String convertQu(String word) {
        if (!word.contains("Q")) {
            return word;
        } else {
            int index = word.indexOf("Q");
            return word.substring(0, index) + "Q" + word.substring(index);
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
