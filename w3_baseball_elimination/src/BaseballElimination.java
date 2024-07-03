import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BaseballElimination {
    private static final int SOURCE_INDEX = 0;
    private static final int SINK_INDEX = 1;

    private final String[] teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] games; // i games left to play against team j
    private final Map<String, Integer> teamIndex = new HashMap<>(); // reverse index
    private final Map<String, Set<String>> calculatedEliminations = new HashMap<>();


    public BaseballElimination(final String filename) {
        In in = new In(filename);
        int teamsCount = in.readInt();

        this.teams = new String[teamsCount];
        this.wins = new int[teamsCount];
        this.losses = new int[teamsCount];
        this.remaining = new int[teamsCount];
        this.games = new int[teamsCount][teamsCount];


        for (int i = 0; i < teamsCount; i++) {
            String name = in.readString();
            this.teams[i] = name;
            this.wins[i] = in.readInt();
            this.losses[i] = in.readInt();
            this.remaining[i] = in.readInt();
            for (int j = 0; j < teamsCount; j++) {
                this.games[i][j] = in.readInt();
            }
            this.teamIndex.put(name, i);
        }
    }

    private int teamToIndex(final String team) {
        Integer index = this.teamIndex.get(team);
        if (index == null) {
            throw new IllegalArgumentException("Team is not registered");
        }
        return index;
    }

    public int numberOfTeams() {
        return teams.length;
    }

    public Iterable<String> teams() {
        return () -> Arrays.stream(teams).iterator();
    }

    public int wins(final String team) {
        return this.wins[this.teamToIndex(team)];
    }

    public int losses(final String team) {
        return this.losses[this.teamToIndex(team)];
    }

    public int remaining(final String team) {
        return this.remaining[this.teamToIndex(team)];
    }

    public int against(final String team1, final String team2) {
        return this.games[this.teamToIndex(team1)][this.teamToIndex(team2)];
    }

    public boolean isEliminated(final String team) {
        return !getEliminators(team).isEmpty();
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(final String team) {
        return isEliminated(team) ? getEliminators(team) : null;
    }

    private Set<String> getEliminators(final String team) {
        int index = this.teamToIndex(team);
        return this.calculatedEliminations.computeIfAbsent(team, s -> calculateEliminators(index));
    }

    private Set<String> trivialElimination(int index) {
        int maxWins = this.wins[index] + this.remaining[index];
        Set<String> eliminators = new HashSet<>();
        for (int i = 0; i < this.numberOfTeams(); i++) {
            if (i != index && maxWins < wins[i]) {
                eliminators.add(this.teams[i]);
            }
        }
        return eliminators;
    }

    private Set<String> calculateEliminators(int index) {
        Set<String> eliminators = trivialElimination(index);
        if (!eliminators.isEmpty()) {
            return eliminators; // simple alg was able to find the solution
        }

        // calculate via FF
        int opponentTeams = this.numberOfTeams() - 1;
        int totalGames = this.numberOfTeams() * opponentTeams;
        int uniqueGames = totalGames / 2;
        int opponentsGames = uniqueGames - opponentTeams;
        int maxWins = this.wins[index] + this.remaining[index];

        FlowNetwork network = new FlowNetwork(2 + opponentsGames + opponentTeams);
        int gameEdgeIndex = opponentTeams + 2;
        for (int i = 0; i < this.numberOfTeams(); i++) {
            if (i == index) {
                continue;
            }

            int opponentIndex = i < index ? i + 2 : i + 1;
            FlowEdge teamToSink = new FlowEdge(opponentIndex, SINK_INDEX,
                                               maxWins - this.wins[i]); // Team -> Sink
            network.addEdge(teamToSink);

            for (int j = 0; j < i; j++) {
                if (j == index) {
                    continue;
                }

                int gamesCount = this.games[i][j];

                FlowEdge sourceToGame = new FlowEdge(SOURCE_INDEX, gameEdgeIndex, gamesCount);
                network.addEdge(sourceToGame);

                FlowEdge firstTeamWin = new FlowEdge(gameEdgeIndex, opponentIndex,
                                                     Double.POSITIVE_INFINITY);
                network.addEdge(firstTeamWin);

                int secondTeamIndex = j < index ? j + 2 : j + 1;
                FlowEdge secondTeamWin = new FlowEdge(gameEdgeIndex, secondTeamIndex,
                                                      Double.POSITIVE_INFINITY);
                network.addEdge(secondTeamWin);

                gameEdgeIndex++;
            }
        }

        FordFulkerson maxFlow = new FordFulkerson(network, SOURCE_INDEX, SINK_INDEX);
        for (int i = 0; i < this.numberOfTeams(); i++) {
            if (i == index) {
                continue;
            }

            int opponentIndex = i < index ? i + 2 : i + 1;
            if (maxFlow.inCut(opponentIndex)) {
                eliminators.add(teams[i]);
            }
        }

        return eliminators;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
