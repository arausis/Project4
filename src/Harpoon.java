import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Harpoon extends Entity{
    public static final String HARPOON_KEY = "harpoon";
    public static final int HARPOON_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 0;
    public static final int HARPOON_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 1;
    public static final int HARPOON_PARSE_PROPERTY_COUNT = 2;
    private final Point direction;

    public Harpoon(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod, Point direction) {
        super(id, position, images, animationPeriod, behaviorPeriod);
        this.direction = direction;
    }
    public void executeActivity(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        Optional<Entity> next = moveTo(world, scheduler);
        if (next.isPresent()){
            world.removeEntity(scheduler, next.get());
            world.removeEntity(scheduler, this);
        }
        scheduleBehavior(scheduler, world, imageLibrary);
    }

    public void updateImage() {
        imageIndex = imageIndex + 1;
    }

    public Optional<Entity> moveTo(World world, EventScheduler scheduler) {
        Point nextPos = new Point(position.x + direction.x, position.y + direction.y);
        Optional<Entity> occupant = world.getOccupant(nextPos);
        if(world.inBounds(nextPos)){
            if (occupant.isEmpty()) {
                world.moveEntity(scheduler, this, nextPos);
            }
        }
        else{
            world.removeEntity(scheduler, this);
        }
        return occupant;
    }
    public void scheduleActions(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
        scheduleAnimation(scheduler, world, imageLibrary);
        scheduleBehavior(scheduler, world, imageLibrary);
    }
    public Point nextPosition(World world, Point destination) {

        // A pathing strategy instantiation
        PathingStrategy pathingStrategy = new AStarPathingStrategy();

        Predicate<Point> canPassThrough = (x) -> (!world.getOccupant(x).isPresent() || !(world.getOccupant(x).get() instanceof obstacle));

        BiPredicate<Point, Point> withinReach = (x, y) -> x.manhattanDistanceTo(y) == 1;

        Function<Point, Stream<Point>> potentialNeighbors = (p) -> Stream.of(
                        new Point(p.x, p.y + 1), new Point(p.x, p.y -1), new Point(p.x + 1, p.y) ,new Point(p.x - 1, p.y ))
                .filter(q -> world.inBounds(q));

        List<Point> path = pathingStrategy.computePath(new Point(position.x, position.y), destination, canPassThrough, withinReach, potentialNeighbors);
        if(path.isEmpty()){
            return new Point(position.x, position.y);
        }
        else {
            return path.get(0);
        }
    }

}
