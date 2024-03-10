public class Animation extends Action{
    private int repeatCount;

    /**
     * Constructs an Action object with specified characteristics.
     * In the base program, this is not called directly.
     * Instead, the encapsulated 'create' method are used to create specific kinds.
     *
     * @param kind The type that determines instance logic and categorization.
     * @param entity The entity enacting the action.
     * @param world The world in which the action occurs.
     * @param imageLibrary The image data that may be used by the action.
     * @param repeatCount The number of animation repeats. A zero indicates indefinite repeats.
     */
    public Animation(Entity entity, World world, ImageLibrary imageLibrary, int repeatCount) {
        super(entity, world, imageLibrary);
        this.repeatCount = repeatCount;
    }

    public void execute(EventScheduler scheduler) {
        executeAnimation(scheduler);
    }
    public void executeAnimation(EventScheduler scheduler) {
        entity.updateImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(entity, new Animation(this.entity, this.world, this.imageLibrary,Math.max(this.repeatCount - 1, 0)), entity.getAnimationPeriod());
        }
    }
}
