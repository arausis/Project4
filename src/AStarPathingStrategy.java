import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {

    /**
     * Return a list containing a single point representing the next step toward a goal
     * If the start is within reach of the goal, the returned list is empty.
     *
     * @param start the point to begin the search from
     * @param end the point to search for a point within reach of
     * @param canPassThrough a function that returns true if the given point is traversable
     * @param withinReach a function that returns true if both points are within reach of each other
     * @param potentialNeighbors a function that returns the neighbors of a given point, as a stream
     */
    public List<Point> computePath(
            Point start,
            Point end,
            Predicate<Point> canPassThrough,
            BiPredicate<Point, Point> withinReach,
            Function<Point, Stream<Point>> potentialNeighbors
    ) {
        List<Node> ret= astar(withinReach, potentialNeighbors, canPassThrough, new HashSet<Node>(), new HashSet<Node>(), new Node(end), new Node(start) ).toList();
        List<Point> finalRet = new ArrayList<Point>();
        for(Node n : ret){
            finalRet.add((Point) n);
        }
        return finalRet;
    }


    public Stream<Node> astar(BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors,
                              Predicate<Point> canPassThrough, HashSet<Node> closedSet, HashSet<Node> openset, Node goal, Node s) {
        Stream<Point> adj = potentialNeighbors.apply(s);

        for (Object o : adj.toArray()) {

            Node current = new Node((Point) o);

            if(!canPassThrough.test(current)){
                continue;
            }

            if (withinReach.test(current, goal)){
                List<Node> path = new ArrayList<>();

                current.gscore = s.gscore + 1;
                current.previous = s;
                goal.gscore = current.gscore + 1;
                path.add(goal);
                path.add(current);

                while(current.previous != null){
                    path.add(current);
                    current = current.previous;
                }
                return path.stream().sorted(Comparator.comparingInt(x -> x.gscore));
            }

            if (!closedSet.contains(current)) {
                if (openset.contains(current)) {
                    if (current.gscore > s.gscore + 1) {
                        current.gscore = s.gscore + 1;
                        current.previous = s;
                        openset.remove(current);
                        openset.add(current);
                    }
                } else {
                    current.gscore = s.gscore + 1;
                    current.previous = s;
                    current.hscore = current.manhattanDistanceTo(goal);
                    openset.add(current);
                }
            }
        }
        closedSet.add(s);
        openset.remove(s);

        Optional<Node> next = openset.stream().min(Comparator.comparingInt(temp -> temp.hscore + temp.gscore));

        if (next.isPresent()) {
            return astar(withinReach, potentialNeighbors, canPassThrough, closedSet, openset, goal, next.get());
        } else {
            return Stream.of();
        }
    }

}
