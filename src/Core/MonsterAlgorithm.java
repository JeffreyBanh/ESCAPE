package byow.Core;
import java.util.*;

/**
 * @source ChatGPT
 **/
public class MonsterAlgorithm {
    public static class Node {
        public int x;
        public int y;
        public double cost;
        public double heuristic;
        public Node parent;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.cost = Double.POSITIVE_INFINITY;
            this.heuristic = Double.POSITIVE_INFINITY;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Node)) {
                return false;
            }
            Node other = (Node) obj;
            return this.x == other.x && this.y == other.y;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(x) ^ Integer.hashCode(y);
        }
    }


    public static List<Node> findPath(Node start, Node end, int[][] maze) {
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(node -> node.cost + node.heuristic));
        List<Node> closed = new ArrayList<>();

        start.cost = 0;
        start.heuristic = manhattanDistance(start, end);
        open.offer(start);

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current.equals(end)) {
                List<Node> path = new ArrayList<>();
                while (current != null) {
                    path.add(current);
                    current = current.parent;
                }
                return path;
            }

            closed.add(current);

            for (Node neighbor : getNeighbours(current, maze)) {
                if (closed.contains(neighbor)) {
                    continue;
                }

                double costFromStart = current.cost + manhattanDistance(current, neighbor);
                if (costFromStart < neighbor.cost) {
                    neighbor.cost = costFromStart;
                    neighbor.heuristic = manhattanDistance(neighbor, end);
                    neighbor.parent = current;

                    if (!open.contains(neighbor)) {
                        open.offer(neighbor);
                    }
                }
            }
        }

        return null;
    }

    private static double manhattanDistance(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private static List<Node> getNeighbours(Node node, int[][] maze) {
        List<Node> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};

        for (int[] dir : directions) {
            int x = node.x + dir[0];
            int y = node.y + dir[1];

            if (x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] == 0) {
                Node neighbor = new Node(x, y);
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    public static int[][] getVertex(Node start, Node end, int[][] maze) {
        int[][] res = new int[1][2];
        List<MonsterAlgorithm.Node> path = MonsterAlgorithm.findPath(start, end, maze);
        if (path != null) {
            Collections.reverse(path);
            if(path.size() == 1) {
                res[0][0] = path.get(0).x;
                res[0][1] = path.get(0).y;
            } else {
                res[0][0] = path.get(1).x;
                res[0][1] = path.get(1).y;
            }
        }
        return res;
    }

}


