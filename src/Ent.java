import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Ent extends Healthy implements obstacle{

    public static final String ENT_KEY = "ent";
    public static final int ENT_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 0;
    public static final int ENT_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 1;
    public static final int ENT_PARSE_PROPERTY_COUNT = 2;
    public Ent(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod) {
        super(id, position, images, animationPeriod, behaviorPeriod, 3);
    }

    public void executeActivity(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        Optional<Entity> entTarget = world.findNearest(getPosition(), new ArrayList<>(List.of(Dude.class)));

        if (entTarget.isPresent()) {
            Point tgtPos = entTarget.get().getPosition();

            if (moveTo(world, entTarget.get(), scheduler, imageLibrary) && world.grassType(tgtPos)) {
                Background background = new Background("bloodygrass", imageLibrary.get("bloodygrass"), 0);
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
                    Random rand = new Random();
                    int r = rand.nextInt(9);
                    switch(r){
                        case 1:
                            world.setBackgroundCell(position, new Background("footsteps1", imageLibrary.get("footsteps1"), 0));
                            health --;
                            break;
                        case 2:
                            world.setBackgroundCell(position, new Background("footsteps1", imageLibrary.get("footsteps2"), 0));
                            health--;
                            break;
                        case 3:
                            world.setBackgroundCell(position, new Background("footsteps1", imageLibrary.get("footsteps3"), 0));
                            health--;
                            break;
                        default:
                            break;
                    }
                    if(health == 0){
                        Entity tree = new Tree(
                                Tree.TREE_KEY + "_" + id,
                                position,
                                imageLibrary.get(Tree.TREE_KEY),
                                NumberUtil.getRandomDouble(Tree.TREE_RANDOM_ANIMATION_PERIOD_MIN, Tree.TREE_RANDOM_ANIMATION_PERIOD_MAX), NumberUtil.getRandomDouble(Tree.TREE_RANDOM_BEHAVIOR_PERIOD_MIN, Tree.TREE_RANDOM_BEHAVIOR_PERIOD_MAX),
                                NumberUtil.getRandomInt(Tree.TREE_RANDOM_HEALTH_MIN, Tree.TREE_RANDOM_HEALTH_MAX)
                        );

                        world.removeEntity(scheduler, this);

                        world.addEntity(tree);
                        tree.scheduleActions(scheduler, world, imageLibrary);
                        return false;
                    }
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
