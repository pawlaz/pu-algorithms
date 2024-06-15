import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    public Outcast(final WordNet wordnet) {
        if (wordnet == null) {
            throw new IllegalArgumentException("Argemnts can't be null");
        }
        this.wordnet = wordnet;
    }

    private int getWordDistance(final String word, final String[] nouns) {
        int distanceSum = 0;
        for (String noun : nouns) {
            int distance = wordnet.distance(word, noun);
            if (distance > 0) {
                distanceSum += distance;
            }
        }
        return distanceSum;
    }

    public String outcast(final String[] nouns) {
        if (nouns == null) {
            throw new IllegalArgumentException("Argemnts can't be null");
        }

        int maxDistance = 0;
        String result = null;
        for (String word : nouns) {
            int distance = getWordDistance(word, nouns);
            if (distance > maxDistance) {
                maxDistance = distance;
                result = word;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // Args: synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt
        // outcast5.txt: table
        // outcast8.txt: bed
        // outcast11.txt: potato

        String resourcesPath = "./resources/wordnet/";
        WordNet wordnet = new WordNet(resourcesPath + args[0], resourcesPath + args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(resourcesPath + args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
