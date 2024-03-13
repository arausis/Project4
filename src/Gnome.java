import processing.core.PImage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Gnome extends Entity implements obstacle{

    public static final String GNOME_KEY = "gnome";
    public static final int GNOME_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 0;
    public static final int GNOME_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 1;
    public static final int GNOME_PARSE_PROPERTY_COUNT = 2;
    private int cooldown = -1;
    public Gnome(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod) {
        super(id, position, images, animationPeriod, behaviorPeriod);
    }
    public void executeActivity(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        Optional<Entity> gnomeTarget = world.findNearest(getPosition(), new ArrayList<>(List.of(shroomDude.class)));
        cooldown -= 1;

        if (gnomeTarget.isPresent()) {
            Point tgtPos = gnomeTarget.get().getPosition();

            if (moveTo(world, gnomeTarget.get(), scheduler)) {
                Point direction = getDirection(tgtPos);
                Point harpoonStart = new Point(position.x + direction.x, position.y + direction.y);
                if (world.getOccupant(harpoonStart).isEmpty() && cooldown <= 0) {
                    String imageKey = "";
                    if (direction.equals(new Point(1, 0))) {
                        imageKey = Harpoon.HARPOON_KEY + "_R";
                    } else if (direction.equals(new Point(0, 1))) {
                        imageKey = Harpoon.HARPOON_KEY + "_D";
                    } else if (direction.equals(new Point(-1, 0))) {
                        imageKey = Harpoon.HARPOON_KEY + "_L";
                    } else if (direction.equals(new Point(0, -1))) {
                        imageKey = Harpoon.HARPOON_KEY + "_U";
                    }

                    Harpoon harpoon = new Harpoon(Harpoon.HARPOON_KEY,
                            harpoonStart,
                            imageLibrary.get(imageKey),
                            0.25, 0.1, direction);
                    world.addEntity(harpoon);
                    harpoon.scheduleActions(scheduler, world, imageLibrary);
                    cooldown = 8;
                }
                else if (world.getOccupant(harpoonStart).isPresent() && world.getOccupant(harpoonStart).get() instanceof shroomDude){
                    world.removeEntity(scheduler, world.getOccupant(harpoonStart).get());
                }
            }
        }

        scheduleBehavior(scheduler, world, imageLibrary);
    }

    public void updateImage() {
        imageIndex = imageIndex + 1;
    }
    private Point getDirection(Point target){
        int deltax = (target.x - this.position.x);
        int deltay = (target.y - this.position.y);
        if(deltax == 0){
            deltay /= Math.abs(deltay);
        }
        else{
            deltax /= Math.abs(deltax);
        }
        return new Point(deltax, deltay);
    }

    private boolean cleanShot(Point target, World world){
        int deltax = Math.abs(this.position.x - target.x);
        int deltay = Math.abs(this.position.y - target.y);
        if((deltax == 0 && deltay < 6) || (deltay == 0 && deltax < 6)){
            Point direction = getDirection(target);
            for(int i = Stream.of(deltax, deltay).max(Comparator.comparingInt(b -> (int)b)).get(); i > 1; i--){
                if(world.getOccupant(new Point(this.position.x + direction.x, this.position.y + direction.y)).isPresent()){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean moveTo(World world, Entity target, EventScheduler scheduler) {
        if (this.cleanShot(target.getPosition(), world)) {
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

