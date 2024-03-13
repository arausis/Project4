public class growingMushroom extends Action{

    private Mushroom shroom;
    private int growthstate;
    public growingMushroom(World world, ImageLibrary imageLibrary, Mushroom shroom, int growthstate){
        super(shroom, world, imageLibrary);
        this.shroom = shroom;
        this.growthstate = growthstate;

    }
    public void execute(EventScheduler scheduler){
        if(this.growthstate == 3) {
            if (!world.isOccupied(this.shroom.position)) {
                world.addEntity(this.shroom);
                this.shroom.scheduleActions(scheduler, world, imageLibrary);
            } else {
                scheduler.scheduleEvent(new Water("none", new Point(0, 0), imageLibrary.get("none")),
                        new growingMushroom(world, imageLibrary,
                                this.shroom, this.growthstate), 0.1);
            }
        }
        else{
            Background background = new Background("grass_mushrooms" + growthstate, imageLibrary.get("grass_mushrooms" + (growthstate+1)), 0);
            world.setBackgroundCell(this.shroom.position, background);
            scheduler.scheduleEvent(new Water("none", new Point(0, 0), imageLibrary.get("none")),
                    new growingMushroom(world, imageLibrary,
                            this.shroom, this.growthstate+1), 1.5);
        }
    }
}
