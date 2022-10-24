import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Outcast {
    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new IllegalArgumentException();
        }

        int distance = Integer.MIN_VALUE;
        String outcast = null;
        for (String s : nouns) {
            int temp = distances(s, nouns);
            if (temp > distance) {
                distance = temp;
                outcast = s;
            }
        }

        return outcast;
    }

    private int distances(String noun, String[] nouns) {
        int distances = 0;
        for (String s : nouns) {
            distances += wordnet.distance(noun, s);
        }
        return distances;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}