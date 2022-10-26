import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

public class BaseballElimination {
    private final int n;
    private final String[] teams;
    private final HashMap<String, Integer> teamId;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] games;
    private int total;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }

        In in = new In(filename);
        n = Integer.parseInt(in.readLine());
        teams = new String[n];
        teamId = new HashMap<>(n);
        wins = new int[n];
        losses = new int[n];
        remaining = new int[n];
        games = new int[n][n];

        for (int i = 0; i < n; i++) {
            String team = in.readString();
            teams[i] = team;
            teamId.put(team, i);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                games[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        ArrayList<String> temp = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            temp.add(teams[i]);
        }
        return temp;
    }

    // number of wins for given team
    public int wins(String team) {
        checkTeam(team);
        return wins[teamId.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        checkTeam(team);
        return losses[teamId.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkTeam(team);
        return remaining[teamId.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);
        return games[teamId.get(team1)][teamId.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        checkTeam(team);
        // Trivial elimination
        for (int i = 0; i < n; i++) {
            if (wins(team) + remaining(team) < wins[i]) {
                return true;
            }
        }

        FlowNetwork net = createNetwork(team);
        return (total != new FordFulkerson(net, 0, (n -  1) * n / 2 + n + 1).value());
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkTeam(team);
        if (!isEliminated(team)) {
            return null;
        }

        ArrayList<String> certificate = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (wins[i] > wins[teamId.get(team)] + remaining[teamId.get(team)]) {
                certificate.add(teams[i]);
                return certificate;
            }
        }

        int against = n * (n - 1) / 2;
        FlowNetwork network = createNetwork(team);
        FordFulkerson fordFulkerson = new FordFulkerson(network, 0, against + n + 1);
        for (int i = 0; i < n; i++) {
            if (!Objects.equals(teams[i], team) && fordFulkerson.inCut(against + i + 1)) {
                certificate.add(teams[i]);
            }
        }

        return certificate;
    }

    private FlowNetwork createNetwork(String team) {
        // Nontrivial elimination.
        // s: 0
        // games: 1 ~ n * (n - 1) / 2
        // teams: (n - 1) * n / 2 + 1 ~ (n - 1) * n / 2 + n
        // t: (n - 1) * n / 2 + n + 1
        int gameVertice = 1;
        int against = (n - 1) * n / 2;
        int teamVertice = 1 + against;
        total = 0;
        FlowNetwork net = new FlowNetwork(against + n + 2);

        // no need to exclude the parameter team since
        for (int i = 0; i < n; i++, teamVertice++) {
            for (int j = i + 1; j < n; j++, gameVertice++) {
                // add edges connected with game vertices
                net.addEdge(new FlowEdge(0, gameVertice, games[i][j]));
                net.addEdge(new FlowEdge(gameVertice, against + i + 1, Double.POSITIVE_INFINITY));
                net.addEdge(new FlowEdge(gameVertice, against + j + 1, Double.POSITIVE_INFINITY));
                total += games[i][j];
            }
            // add edges connected with t
            int capacity = wins(team) + remaining(team) - wins[teamVertice - 1 - against];
            if (capacity >= 0) {
                net.addEdge(new FlowEdge(teamVertice, against + n + 1, capacity));
            }
        }

        return net;
    }

    private void checkTeam(String team) {
        if (team == null || !teamId.containsKey(team)) {
            throw new IllegalArgumentException();
        }
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
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
