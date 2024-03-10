import processing.core.PImage;

import java.util.List;

public class Stump extends Entity implements obstacle{
    public static final String STUMP_KEY = "stump";
    public static final int STUMP_PARSE_PROPERTY_COUNT = 0;
    public Stump(String id, Point position, List<PImage> images) {
        super(id, position, images, 0.0, 0.0);
    }
}
