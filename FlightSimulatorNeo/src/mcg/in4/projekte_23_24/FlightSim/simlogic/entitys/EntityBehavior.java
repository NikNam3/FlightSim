package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys;

import mcg.in4.projekte_23_24.FlightSim.engine.structures.Scene;

public abstract class EntityBehavior {
    protected Scene scene;
    protected int hostId;

    public EntityBehavior(){}

    public EntityBehavior(Scene scene, int host){
        this.scene  = scene;
        this.hostId = host;
    }

    protected final <T> T getComponent(Class<T> cls){
        return scene.getComponent(hostId, cls);
    }

    protected final <T> boolean hasComponent(Class<T> cls){
        return scene.hasComponent(hostId, cls);
    }

    protected final <T> void addComponent(T component, Class<T> cls){
        scene.addComponent(hostId, component, cls);
    }

    public final void bind(Scene scene, int host){
        this.scene  = scene;
        this.hostId = host;
    }


    /**
     * This method is called every frame
     * @param deltaTime The time since the last frame in seconds.
     */
    public void onUpdate(float deltaTime){}
    /**
     * This method is called when the entity is created
     */
    public void onCreate(){}
}
