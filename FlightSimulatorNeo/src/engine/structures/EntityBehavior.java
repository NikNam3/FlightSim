package engine.structures;

public abstract class EntityBehavior {
    private Scene scene;
    private int hostId;

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

    public void onUpdate(float deltaTime){}
}
