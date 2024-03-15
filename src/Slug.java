import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Slug extends Entity implements obstacle{

    public static final String SLUG_KEY = "slug";
    public static final int SLUG_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 0;
    public static final int SLUG_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 1;
    public static final int SLUG_PARSE_PROPERTY_COUNT = 2;
    public Slug(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod) {
        super(id, position, images, animationPeriod, behaviorPeriod);
    }

    public void executeActivity(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        Optional<Entity> slugTarget = world.findNearest(getPosition(), new ArrayList<>(List.of(Mushroom.class)));

        if (slugTarget.isPresent()) {
            Point tgtPos = slugTarget.get().getPosition();

            if (moveTo(world, slugTarget.get(), scheduler, imageLibrary)) {
                Background background = new Background("grass", imageLibrary.get("grass"), 0);
                world.setBackgroundCell(tgtPos, background);
            }
        }

        scheduleBehavior(scheduler, world, imageLibrary);

    }

    public void updateImage() {
        imageIndex = imageIndex + 1;
    }

    public boolean moveTo(World world, Entity target, EventScheduler scheduler, ImageLibrary imageLibrary) {
        if (getPosition().adjacentTo(target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());
            if (!getPosition().equals(nextPos)) {
                if(world.grassType(position)){
                    //world.setBackgroundCell(position, new Background("slimeygrass", imageLibrary.get("slimeygrass"), 0));
                }
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
