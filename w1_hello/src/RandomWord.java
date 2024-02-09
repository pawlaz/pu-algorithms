import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

// Stop input on CTRL + D
public class RandomWord {
    public static void main(String[] args) {
        String champion = null;
        int i = 1;
        do {
            String warrior = StdIn.readString();
            if (StdRandom.bernoulli((double) 1 / i)) {
                champion = warrior;
            }
            i++;

        } while (!StdIn.isEmpty());
        StdOut.println(champion);
    }
}
