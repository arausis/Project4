public class Behavior extends Action{
    public Behavior(Entity entity, World world, ImageLibrary imageLibrary) {
        super(entity, world, imageLibrary);
    }

    public void execute(EventScheduler scheduler) {
        executeBehavior(scheduler);
    }
    public void executeBehavior(EventScheduler scheduler) {
        entity.executeBehavior(world, imageLibrary, scheduler);
    }
}
