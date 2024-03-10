import processing.core.PImage;

import java.util.List;

public class House extends Entity implements obstacle{
    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_PARSE_PROPERTY_COUNT = 0;
    public House(String id, Point position, List<PImage> images) {
        super(id, position, images, 0.0, 0.0);
    }
}
