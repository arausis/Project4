/** A scheduled action to be carried out by a specific entity. */
public abstract class Action {
    /**
     * Enumerated type defining different kinds of actions that entities take in the world.
     * Specific values are assigned to the action's 'kind' instance variable at initialization.
     * There are two types of actions: animations (image updates) and behaviors (logic updates).
     */
    public enum ActionKind {
        ANIMATION,
        BEHAVIOR
    }

    /** Type that determines instance logic and categorization. */
    /** Entity enacting the action. */
    protected final Entity entity;
    /** World in which the action occurs. */
    protected final World world;
    /** Image data that may be used by the action. */
    protected final ImageLibrary imageLibrary;
    /** Number of animation repeats. A zero indicates indefinite repeats. */


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
    public Action(Entity entity, World world, ImageLibrary imageLibrary) {
        this.entity = entity;
        this.world = world;
        this.imageLibrary = imageLibrary;
    }

    /**
     * Returns a new 'Behavior' type action.
     * Constructor arguments provide hints to data necessary for a subclass.
     *
     * @param entity The entity enacting the behavior.
     * @param world The world in which the behavior occurs.
     * @param imageLibrary The image data that may be used by the behavior.
     *
     * @return A new Action object configured as a(n) 'Behavior'.
     */

    /**
     * Returns a new 'Animation' type action.
     * Constructor arguments provide hints to data necessary for a subclass.
     *
     * @param entity The entity that the animation is applied to.
     *  @param repeatCount The number of animation repeats. A zero indicates indefinite repeats.
     *
     * @return A new Action object configured as a(n) 'Animation'.
     */
    /** Called when the action's scheduled time occurs. */
    public abstract void execute(EventScheduler scheduler);

}
