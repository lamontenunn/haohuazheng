import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {

    public static void main(String[] args) throws IOException {
        List<String> airportCodes = readAirportCodes("airport_codes.txt");
        int[][] distanceMatrix = readDistanceMatrix("distance_matrix.txt", airportCodes);

        findShortestPath(airportCodes, distanceMatrix, "HNL", "PVD");
        findShortestPath(airportCodes, distanceMatrix, "SFO", "MIA");
        findShortestPath(airportCodes, distanceMatrix, "LAX", "MIA");

    }

    public static List<String> readAirportCodes(String filename) throws IOException {
        List<String> airportCodes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                airportCodes.add(line.trim());
            }
        }
        return airportCodes;
    }

    public static int[][] readDistanceMatrix(String filename, List<String> airportCodes) throws IOException {
        int[][] distanceMatrix = new int[airportCodes.size()][airportCodes.size()];

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            int rowIndex = 0;
            String line;
            while ((line = br.readLine()) != null) {
                int[] row = Arrays.stream(line.trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
                distanceMatrix[rowIndex++] = row;
            }
        }
        return distanceMatrix;
    }

    // Find shortest path via Dijkstra's algo
    public static void findShortestPath(List<String> airportCodes, int[][] distanceMatrix, String start, String end) {
        int startIndex = airportCodes.indexOf(start);
        int endIndex = airportCodes.indexOf(end);

        int[] distances = new int[airportCodes.size()];
        int[] previous = new int[airportCodes.size()];

        for (int i = 0; i < distances.length; i++) {
            distances[i] = Integer.MAX_VALUE;
            previous[i] = -1;
        }

        distances[startIndex] = 0;
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.offer(new Node(startIndex, 0));

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            int currentAirport = currentNode.airport;
            int currentDistance = currentNode.distance;

            if (currentAirport == endIndex) {
                break;
            }

            for (int i = 0; i < airportCodes.size(); i++) {
                int distance = distanceMatrix[currentAirport][i];
                if (distance != Integer.MAX_VALUE && currentDistance + distance < distances[i]) {
                    distances[i] = currentDistance + distance;
                    previous[i] = currentAirport;
                    queue.offer(new Node(i, distances[i]));
                }
            }
        }

        System.out.println("Start: " + start);
        System.out.println("End: " + end);
        System.out.println("The shortest distance between " + start + " and " + end + " is " + distances[endIndex] + " miles.");
        System.out.print("The shortest route between " + start + " and " + end + " is\n");

        List<String> path = new ArrayList<>();
        int current = endIndex;
        while (current != -1) {
            path.add(0, airportCodes.get(current));
            current = previous[current];
        }

        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println("\n");
    }

    static class Node implements Comparable<Node> {
        int airport;
        int distance;

        public Node(int airport, int distance) {
            this.airport = airport;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
}

            
