import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Topological;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class WordNet {
    private final ArrayList<String> synsets;
    private final HashMap<String, HashSet<Integer>> nounToSynsets;
    private final Digraph digraph;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsetsFile, String hypernymsFile) {
        checkNull(synsetsFile);
        checkNull(hypernymsFile);

        synsets = new ArrayList<>();
        nounToSynsets = new HashMap<>();

        readSynsets(synsetsFile);

        int n = synsets.size();
        digraph = new Digraph(n);

        readHypernyms(hypernymsFile);
        sap = new SAP(digraph);

        checkDAG();
    }

    private void checkDAG() {
        Topological check = new Topological(digraph);
        if (!check.hasOrder()) {
            throw new IllegalArgumentException();
        }
    }

    private void checkNull(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
    }

    private void readSynsets(String synsetsFile) {
        In in = new In(synsetsFile);
        String[] temp;
        while (!in.isEmpty()) {
            temp = in.readLine().split(",");
            synsets.add(temp[1]);
            for (String s : temp[1].split(" ")) {
                if (!nounToSynsets.containsKey(s)) {
                    HashSet<Integer> newNoun = new HashSet<>();
                    newNoun.add(Integer.parseInt(temp[0]));
                    nounToSynsets.put(s, newNoun);
                } else {
                    HashSet<Integer> oldNoun = nounToSynsets.get(s);
                    oldNoun.add(Integer.parseInt(temp[0]));
                }
            }
        }
    }

    private void readHypernyms(String hypernymsFile) {
        In in = new In(hypernymsFile);
        String[] temp;
        while (!in.isEmpty()) {
            temp = in.readLine().split(",");
            int v = Integer.parseInt(temp[0]);
            for (int i = 1; i < temp.length; i++) {
                digraph.addEdge(v, Integer.parseInt(temp[i]));
            }
        }
    }

    // returns all WordNet nouns

    public Iterable<String> nouns() {
        return nounToSynsets.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkNull(word);
        return nounToSynsets.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        return sap.length(nounToSynsets.get(nounA), nounToSynsets.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        return synsets.get(sap.ancestor(nounToSynsets.get(nounA), nounToSynsets.get(nounB)));
    }

    // do unit testing of this class
    // public static void main(String[] args) {
    //
    // }
}