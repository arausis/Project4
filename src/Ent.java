import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Ent extends Entity implements obstacle{

    public static final String ENT_KEY = "ent";
    public static final int ENT_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 0;
    public static final int ENT_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 1;
    public static final int Ent_PARSE_PROPERTY_COUNT = 2;
    public Ent(Tree tree, List<PImage> images, double animationPeriod, double behaviorPeriod) {
        super(ENT_KEY, tree.position, images, animationPeriod, behaviorPeriod);
    }

    public void executeActivity(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        Optional<Entity> entTarget = world.findNearest(getPosition(), new ArrayList<>(List.of(Dude.class)));

        if (entTarget.isPresent()) {
            Point tgtPos = entTarget.get().getPosition();

            if (moveTo(world, entTarget.get(), scheduler)) {
                Background background = new Background("grass_mushrooms", imageLibrary.get("grass_mushrooms"), 0);
                world.setBackgroundCell(tgtPos, background);
                world.removeEntity(scheduler, world.getOccupant(tgtPos).get());
            }
        }

        scheduleBehavior(scheduler, world, imageLibrary);

    }

    public void updateImage() {
        imageIndex = imageIndex + 1;
    }

    public boolean moveTo(World world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacentTo(target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());
            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
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
