import java.util.*;
import processing.core.PImage;

/** Represents an entity that exists in the virtual world. */
public abstract class Entity {

    // Constant save file column positions for properties required by all entities.
    public static final int ENTITY_PROPERTY_KEY_INDEX = 0;
    public static final int ENTITY_PROPERTY_ID_INDEX = 1;
    public static final int ENTITY_PROPERTY_POSITION_X_INDEX = 2;
    public static final int ENTITY_PROPERTY_POSITION_Y_INDEX = 3;
    public static final int ENTITY_PROPERTY_COLUMN_COUNT = 4;


    /** Entity's identifier that often includes the corresponding 'key' constant. */
    protected String id;

    /** Entity's x/y position in the world. */
    protected Point position;

    /** Entity's inanimate (singular) or animation (multiple) images. */
    protected List<PImage> images;

    /** Index of the element from 'images' used to draw the entity. */
    protected int imageIndex;

    /** Positive (non-zero) time delay between the entity's animations. */
    protected double animationPeriod;

    /** Positive (non-zero) time delay between the entity's behaviors. */
    protected double behaviorPeriod;



    public Entity(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.animationPeriod = animationPeriod;
        this.behaviorPeriod = behaviorPeriod;
    }


    /** Helper method for testing. Preserve this functionality for all kinds of entities. */
    public String log(){
        if (id.isEmpty()) {
            return null;
        } else {
            return String.format("%s %d %d %d", id, position.x, position.y, imageIndex);
        }
    }

    /** Called when an animation action occurs. */
    public void updateImage() {
            throw new UnsupportedOperationException("updateImage not supported for %s");
    }

    /** Called to begin animation and/or behavior for an entity. */
    public void scheduleActions(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
    }

    /** Begins all animation updates for the entity. */
    public void scheduleAnimation(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
        scheduler.scheduleEvent(this, new Animation(this, world, imageLibrary, 0), animationPeriod);
    }

    /** Schedules a single behavior update for the entity. */
    public void scheduleBehavior(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
        scheduler.scheduleEvent(this, new Behavior(this, world, imageLibrary), behaviorPeriod);
    }

    /** Performs the entity's behavior logic. */
    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        executeActivity(world, imageLibrary, scheduler);
    }

    public void executeActivity(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {}



    public double getAnimationPeriod() {
                return animationPeriod;
    }


    public int getImageIndex() {
        return imageIndex;
    }

    public List<PImage> getImages() {
        return images;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
