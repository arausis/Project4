public class growingMushroom extends Action{

    private Mushroom shroom;
    public growingMushroom(World world, ImageLibrary imageLibrary, Mushroom shroom){
        super(shroom, world, imageLibrary);
        this.shroom = shroom;

    }
    public void execute(EventScheduler scheduler){
        if(!world.isOccupied(this.shroom.position)) {
            world.addEntity(this.shroom);
            this.shroom.scheduleActions(scheduler, world, imageLibrary);
        }
        else{
            scheduler.scheduleEvent(new Water("none", new Point(0,0), imageLibrary.get("none")),
                    new growingMushroom(world, imageLibrary,
                            this.shroom), 0.1 );
        }

    }
}
