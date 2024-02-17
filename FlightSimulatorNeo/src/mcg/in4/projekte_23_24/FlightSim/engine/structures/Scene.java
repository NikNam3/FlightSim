package mcg.in4.projekte_23_24.FlightSim.engine.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Scene {
    private final Map<Integer, Map<String, Object>> components;

    // Parent -> Children
    private final Map<Integer, List<Integer>> entityChildren;

    // Child -> Parent
    private final Map<Integer, Integer> entityParents;
    private int idCounter = 0;

    public Scene(){
        components     = new HashMap<>();
        entityChildren = new HashMap<>();
        entityParents  = new HashMap<>();
    }

    public boolean has(int entity){
        return components.containsKey(entity);
    }

    public int createEntity(){
        int id = idCounter++;
        components.put(id, new HashMap<>());
        entityChildren.put(id, new ArrayList<>());
        return id;
    }

    public void adopt(int parent, int child){
        makeRoot(child);
        entityParents.put(child, parent);
        entityChildren.get(parent).add(child);
    }

    public Set<Integer> getAll(){
        return components.keySet();
    }

    public void makeRoot(int entity){
        for(int child : entityChildren.get(entity)){
            entityParents.remove(child);
        }
        if(!hasParent(entity))
            return;
        int parent = entityParents.get(entity);
        entityChildren.get(parent).remove((Integer)entity);
        entityParents.remove(entity);
    }

    public boolean hasParent(int entity){
        return entityParents.containsKey(entity);
    }

    public int getParent(int entity){
        return entityParents.get(entity);
    }

    public List<Integer> getChildren(int parent){
        return entityChildren.get(parent);
    }

    public <T> void addComponent(int entity, T component, Class<T> cls){
        components.get(entity).put(cls.getName(), component);
    }

    public <T> T getComponent(int entity, Class<T> cls){
        return (T) components.get(entity).get(cls.getName());
    }

    public <T> boolean hasComponent(int entity, Class<T> cls){
        return components.get(entity).containsKey(cls.getName());
    }
}
