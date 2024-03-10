import processing.core.PImage;

import java.util.List;

public abstract class Resourceful extends Entity{
    protected int resourceCount;

    protected int resourceLimit;
    public Resourceful(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod, int resourceCount, int resourceLimit) {
        super(id, position, images, animationPeriod, behaviorPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }
}
