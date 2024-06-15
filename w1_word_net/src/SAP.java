import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private final Digraph graph;

    private class SearchResult {
        private int ancestor;
        private int pathLength;

        public SearchResult() {
            this.ancestor = -1;
            this.pathLength = -1;
        }

        public void updateIfBetter(int length, int vertex) {
            if (this.pathLength < 0 || this.pathLength > length) {
                this.ancestor = vertex;
                this.pathLength = length;
            }
        }
    }

    public SAP(final Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Argument can't be null");
        }
        this.graph = new Digraph(G);
    }

    @SafeVarargs
    private void validateArguments(final Iterable<Integer>... iterables) {
        for (Iterable<Integer> iter : iterables) {
            if (iter == null) {
                throw new IllegalArgumentException("Argument can't be null");
            }

            for (int i : iter) {
                validateRange(i);
            }
        }
    }

    private void validateRange(int... numbers) {
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] < 0 || numbers[i] > graph.V() - 1) {
                throw new IllegalArgumentException("Vertex " + numbers[i] + " is out of range");
            }
        }
    }

    private SearchResult searchAncestor(final BreadthFirstDirectedPaths vBfsObj,
                                        final BreadthFirstDirectedPaths wBfsObj) {
        SearchResult sr = new SearchResult();
        for (int i = 0; i < graph.V(); i++) {
            if (!vBfsObj.hasPathTo(i) || !wBfsObj.hasPathTo(i)) {
                continue;
            }

            int newPathLength = vBfsObj.distTo(i) + wBfsObj.distTo(i);
            sr.updateIfBetter(newPathLength, i);
        }
        return sr;
    }

    private SearchResult searchAncestor(int v, int w) {
        validateRange(v, w);
        BreadthFirstDirectedPaths vBfsObj = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wBfsObj = new BreadthFirstDirectedPaths(graph, w);
        return searchAncestor(vBfsObj, wBfsObj);
    }

    private SearchResult searchAncestor(final Iterable<Integer> v, final Iterable<Integer> w) {
        validateArguments(v, w);
        BreadthFirstDirectedPaths vBfsObj = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wBfsObj = new BreadthFirstDirectedPaths(graph, w);
        return searchAncestor(vBfsObj, wBfsObj);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return searchAncestor(v, w).pathLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return searchAncestor(v, w).ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(final Iterable<Integer> v, final Iterable<Integer> w) {
        return searchAncestor(v, w).pathLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(final Iterable<Integer> v, final Iterable<Integer> w) {
        return searchAncestor(v, w).ancestor;
    }

    public static void main(String[] args) {
    }
}
