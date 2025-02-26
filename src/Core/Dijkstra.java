package byow.Core;

import java.util.*;

public class Dijkstra {

    public double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public List<Double> getDistance(int[][] coordinates, int start) {

        int numVertices = coordinates.length;
        List<Double> distance = new ArrayList<>();
        boolean[] visited = new boolean[numVertices];
        int[] previous = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            distance.add(Double.POSITIVE_INFINITY);
        }

        distance.set(start, 0.0);

        for (int i = 0; i < numVertices - 1; i++) {
            int minVertex = findMinVertex(distance, visited);
            visited[minVertex] = true;

            for (int j = 0; j < numVertices; j++) {
                double edgeWeight = distance(coordinates[minVertex][0], coordinates[minVertex][1], coordinates[j][0], coordinates[j][1]);
                if (edgeWeight != 0 && !visited[j] && distance.get(minVertex) != Double.POSITIVE_INFINITY) {
                    double newDistance = distance.get(minVertex) + edgeWeight;
                    if (newDistance < distance.get(j)) {
                        distance.set(j, newDistance);
                        previous[j] = minVertex;
                    }
                }
            }
        }
        return distance;
    }

    public int findMinVertex(List<Double> distance, boolean[] visited) {
        int minVertex = -1;
        for (int i = 0; i < distance.size(); i++) {
            if (!visited[i] && (minVertex == -1 || distance.get(i) < distance.get(minVertex))) {
                minVertex = i;
            }
        }
        return minVertex;
    }

    public int createSmallestIndex(List<Double> distance, boolean[] connected) {
        double smallestIndex = Double.MAX_VALUE;
        for (int i = 0; i < distance.size(); i++) {
            if (distance.get(i) != 0 && !connected[i]) {
                double currentDistance = distance.get(i);
                if (currentDistance < smallestIndex) {
                    smallestIndex = currentDistance;
                }
            }
        }
        return distance.indexOf(smallestIndex);
    }
}