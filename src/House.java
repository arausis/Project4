import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class House extends Resourceful implements obstacle{
    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_PARSE_PROPERTY_COUNT = 0;
    public House(String id, Point position, List<PImage> images) {
        super(id, position, images, 0.0, 0.0, 0, 5);
    }
    public void spawnDude(World world, ImageLibrary imageLibrary, EventScheduler scheduler){
        List<Point> adjacentPositions = new ArrayList<>(List.of(
                new Point(position.x - 1, position.y),
                new Point(position.x + 1, position.y),
                new Point(position.x, position.y - 1),
                new Point(position.x, position.y + 1)
        ));
        Collections.shuffle(adjacentPositions);

        for (Point adjacentPosition : adjacentPositions) {
            if (world.inBounds(adjacentPosition) && !world.isOccupied(adjacentPosition) && world.hasBackground(adjacentPosition)) {
                Entity newdude = new Dude(Dude.DUDE_KEY , adjacentPosition,
                        imageLibrary.get(Dude.DUDE_KEY ), 1.200, 0.300, 0, 2);
                world.addEntity(newdude);
                newdude.scheduleActions(scheduler, world, imageLibrary);
                break;
            }
        }
    }
}
