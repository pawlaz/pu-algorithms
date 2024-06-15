// TODO
// Corner cases.  Throw an IllegalArgumentException in the following situations:
// Any argument to the constructor or an instance method is null
// The input to the constructor does not correspond to a rooted DAG.
// Any of the noun arguments in distance() or sap() is not a WordNet noun.

import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class WordNet {
    private static final String FILE_SEPARATOR = ",";
    private static final String NOUNS_SEPARATOR = " ";
    private final Map<Integer, String> synsets;
    private final Map<String, Set<Integer>> nouns;
    // private final Digraph digraph;

    public WordNet(final String synsetsPath, final String hypernymsPath) {
        validateArguments(synsetsPath, hypernymsPath);

        this.synsets = new HashMap<>();
        this.nouns = new HashMap<>();

        parseSynsetsFile(synsetsPath);

    }

    private void validateArguments(final Object... args) {
        boolean isAnyNull = Arrays.stream(args).anyMatch(Objects::isNull);
        if (isAnyNull) {
            throw new IllegalArgumentException("Argument can't be null");
        }
    }

    private void parseSynsetsFile(final String synsetsPath) {
        In reader = new In(synsetsPath);
        while (reader.hasNextLine()) {
            String[] synsetDefinition = reader.readLine().split(FILE_SEPARATOR);
            int synsetId = Integer.parseInt(synsetDefinition[0]);
            String[] synsetNouns = synsetDefinition[1].split(NOUNS_SEPARATOR);

            synsets.put(synsetId, synsetDefinition[1]);
            for (String noun : synsetNouns) {
                Set<Integer> ids = nouns.computeIfAbsent(noun, s -> new HashSet<>());
                ids.add(synsetId);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        // topological sort
    }

    // is the word a WordNet noun?
    public boolean isNoun(final String word) {

    }

    // distance between nounA and nounB (defined below)
    public int distance(final String nounA, final String nounB) {

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(final String nounA, final String nounB) {

    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
