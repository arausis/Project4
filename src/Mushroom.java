import processing.core.PImage;

import java.util.*;

public class Mushroom extends Entity implements obstacle{
    public static final String MUSHROOM_KEY = "mushroom";
    public static final int MUSHROOM_PARSE_BEHAVIOR_PERIOD_INDEX = 0;
    public static final int MUSHROOM_PARSE_PROPERTY_COUNT = 1;
    public Mushroom(String id, Point position, List<PImage> images, double behaviorPeriod) {
        super(id, position, images, 0.0, behaviorPeriod);
    }
    public void scheduleActions(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
        scheduleBehavior(scheduler, world, imageLibrary);
    }

    /** Executes Mushroom specific Logic. */
    public void executeActivity(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        List<Point> adjacentPositions = new ArrayList<>(List.of(
                new Point(position.x - 1, position.y),
                new Point(position.x + 1, position.y),
                new Point(position.x, position.y - 1),
                new Point(position.x, position.y + 1)
        ));
        Collections.shuffle(adjacentPositions);

        List<Point> mushroomBackgroundPositions = new ArrayList<>();
        List<Point> mushroomEntityPositions = new ArrayList<>();
        for (Point adjacentPosition : adjacentPositions) {
            Random rand = new Random();
            int r = rand.nextInt(5);
            if (r == 1 && world.inBounds(adjacentPosition) && !world.isOccupied(adjacentPosition) && world.hasBackground(adjacentPosition)) {
                Entity sdude = new shroomDude(shroomDude.shroomDude_KEY , adjacentPosition,
                        imageLibrary.get(shroomDude.shroomDude_KEY ), 0.1, 0.3);

                world.addEntity(sdude);
                sdude.scheduleActions(scheduler, world, imageLibrary);
                break;
            }
        }

        if (!mushroomBackgroundPositions.isEmpty()) {
            Point position = mushroomBackgroundPositions.get(0);

            Background background = new Background("grass_mushrooms", imageLibrary.get("grass_mushrooms"), 0);
            world.setBackgroundCell(position, background);
        } else if (!mushroomEntityPositions.isEmpty()) {
            Point position = mushroomEntityPositions.get(0);

            Entity mushroom = new Mushroom(MUSHROOM_KEY, position, imageLibrary.get(MUSHROOM_KEY), behaviorPeriod * 4.0);

            world.addEntity(mushroom);
            mushroom.scheduleActions(scheduler, world, imageLibrary);
        }

        scheduleBehavior(scheduler, world, imageLibrary);
    }
}
