import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class WordNet {
    private static final String COLUMNS_SEPARATOR = ",";
    private static final String NOUNS_SEPARATOR = " ";
    private final Map<Integer, String> synsetValues;
    private final Map<String, Set<Integer>> nouns;
    private final SAP sap;

    public WordNet(final String synsetsPath, final String hypernymsPath) {
        validateArguments(synsetsPath, hypernymsPath);

        this.synsetValues = new HashMap<>();
        this.nouns = new HashMap<>();
        parseSynsetsFile(synsetsPath);

        Digraph digraph = new Digraph(synsetValues.size());
        parseHypernymsFile(hypernymsPath, digraph);
        validateCycles(digraph);
        validateRoots(digraph);

        sap = new SAP(digraph);
    }

    private void validateArguments(final Object... args) {
        boolean isAnyNull = Arrays.stream(args).anyMatch(Objects::isNull);
        if (isAnyNull) {
            throw new IllegalArgumentException("Argument can't be null");
        }
    }

    private void validateCycles(final Digraph d) {
        DirectedCycle dc = new DirectedCycle(d);
        if (dc.hasCycle()) {
            throw new IllegalArgumentException("Cycles detected");
        }
    }

    private void validateRoots(final Digraph d) {
        int roots = 0;
        for (int i = 0; i < d.V(); i++) {
            if (d.outdegree(i) == 0) {
                roots++;
            }
        }
        if (roots != 1) {
            throw new IllegalArgumentException("Non-rooted graph detected");
        }
    }

    private void parseSynsetsFile(final String synsetsPath) {
        In reader = new In(synsetsPath);
        while (reader.hasNextLine()) {
            String[] synsetDefinition = reader.readLine().split(COLUMNS_SEPARATOR);
            int synsetId = Integer.parseInt(synsetDefinition[0]);
            String[] synsetNouns = synsetDefinition[1].split(NOUNS_SEPARATOR);

            synsetValues.put(synsetId, synsetDefinition[1]);
            for (String noun : synsetNouns) {
                Set<Integer> ids = nouns.computeIfAbsent(noun, s -> new HashSet<>());
                ids.add(synsetId);
            }
        }
    }

    private void parseHypernymsFile(final String hypernymsPath, final Digraph d) {
        In reader = new In(hypernymsPath);
        while (reader.hasNextLine()) {
            String[] hypernymsDefinition = reader.readLine().split(COLUMNS_SEPARATOR);
            int synsetId = Integer.parseInt(hypernymsDefinition[0]);

            for (int i = 1; i < hypernymsDefinition.length; i++) {
                int hypId = Integer.parseInt(hypernymsDefinition[i]);
                d.addEdge(synsetId, hypId);
            }
        }
    }

    public Iterable<String> nouns() {
        return Collections.unmodifiableSet(nouns.keySet());
    }

    public boolean isNoun(final String word) {
        validateArguments(word);
        return nouns.containsKey(word);
    }

    public int distance(final String nounA, final String nounB) {
        validateArguments(nounA, nounB);
        Set<Integer> aSynsetIds = nouns.get(nounA);
        Set<Integer> bSynsetIds = nouns.get(nounB);
        if (aSynsetIds == null || bSynsetIds == null) {
            return -1;
        }

        return sap.length(aSynsetIds, bSynsetIds);
    }

    // common ancestor of nounA and nounB in a shortest ancestral path
    public String sap(final String nounA, final String nounB) {
        validateArguments(nounA, nounB);
        Set<Integer> aSynsetIds = nouns.get(nounA);
        Set<Integer> bSynsetIds = nouns.get(nounB);
        if (aSynsetIds == null || bSynsetIds == null) {
            return null;
        }
        return synsetValues.get(sap.ancestor(aSynsetIds, bSynsetIds));
    }
    
    public static void main(String[] args) {
        String synsetsPath = "./resources/wordnet/synsets15.txt";
        String hypernymsPath = "./resources/wordnet/hypernyms15Path.txt";
        WordNet wordNet = new WordNet(synsetsPath, hypernymsPath);
        System.out.println(wordNet.distance("a", "n"));
        System.out.println(wordNet.sap("a", "n"));
    }
}
