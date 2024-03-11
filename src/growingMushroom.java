public class growingMushroom extends Action{

    private Mushroom shroom;
    public growingMushroom(World world, ImageLibrary imageLibrary, Mushroom shroom){
        super(shroom, world, imageLibrary);
        this.shroom = shroom;

    }
    public void execute(EventScheduler scheduler){
        world.addEntity(this.shroom);
    }
}
