import processing.core.PImage;

import java.util.List;

public class Sapling extends Healthy implements obstacle{
    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_PARSE_PROPERTY_COUNT = 0;
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final double SAPLING_BEHAVIOR_PERIOD = 2.0;
    public static final double SAPLING_ANIMATION_PERIOD = 0.01; // Very small to react to health changes
    public Sapling(String id, Point position, List<PImage> images) {
        super(id, position, images, SAPLING_ANIMATION_PERIOD, SAPLING_BEHAVIOR_PERIOD, 0);
    }

    /** Executes Sapling specific Logic. */
    public void executeActivity(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        health = health + 1;
        if (!transform(world, scheduler, imageLibrary)) {
            scheduleBehavior(scheduler, world, imageLibrary);
        }
    }
    public void updateImage() {
            if (health <= 0) {
                imageIndex = 0;
            } else if (health < SAPLING_HEALTH_LIMIT) {
                imageIndex = images.size() * health / SAPLING_HEALTH_LIMIT;
            } else {
                imageIndex = images.size() - 1;
            }
    }

    public void scheduleActions(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
        scheduleAnimation(scheduler, world, imageLibrary);
        scheduleBehavior(scheduler, world, imageLibrary);
    }

    /** Checks the Sapling's health and transforms accordingly, returning true if successful. */
    public boolean transform(World world, EventScheduler scheduler, ImageLibrary imageLibrary) {
        if (health <= 0) {
            Entity stump = new Stump(Stump.STUMP_KEY + "_" + id, position, imageLibrary.get(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (health >= SAPLING_HEALTH_LIMIT) {
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

            return true;
        }

        return false;
    }
}
