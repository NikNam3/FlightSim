package mcg.in4.projekte_23_24.FlightSim.engine.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The scene class is used to store all entities and their components
 * As well as the parent-child relationships between entities
 *
 * @author Vincent Lahmann
 */
public class Scene {
    private final Map<Integer, Map<String, Object>> components;

    // Parent -> Children
    private final Map<Integer, List<Integer>> entityChildren;

    // Child -> Parent
    private final Map<Integer, Integer> entityParents;
    private int idCounter = 0;

    /**
     * Constructor
     * @author Vincent Lahmann
     */
    public Scene(){
        components     = new HashMap<>();
        entityChildren = new HashMap<>();
        entityParents  = new HashMap<>();
    }

    /**
     * Check if an entity exists
     * @param entity Entity to check
     * @return true if the entity exists
     *
     * @author Vincent Lahmann
     */
    public boolean has(int entity){
        return components.containsKey(entity);
    }

    /**
     * Create a new entity and return its id
     * @return id of the new entity
     *
     * @author Theo Kamp
     */
    public int createEntity(){
        int id = idCounter++;
        components.put(id, new HashMap<>());
        entityChildren.put(id, new ArrayList<>());
        return id;
    }

    /**
     * Create a Parent - Child relationship between two entities
     * @param parent Parent entity
     * @param child Child entity
     *
     * @author Vincent Lahmann
     */
    public void adopt(int parent, int child){
        makeRoot(child);
        entityParents.put(child, parent);
        entityChildren.get(parent).add(child);
    }

    /**
     * Return all entities
     * @return Set of all entities
     *
     * @author Vincent Lahmann
     */
    public Set<Integer> getAll(){
        return components.keySet();
    }

    /**
     * Set an entity in a parent-child relationship to be a root entity
     * @param entity Entity to make root
     *
     * @author Vincent Lahmann
     */
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

    /**
     * Check if an entity has a parent
     * @param entity Entity to check
     * @return true if the entity has a parent
     *
     * @author Vincent Lahmann
     */
    public boolean hasParent(int entity){
        return entityParents.containsKey(entity);
    }

    /**
     * Get the parent of an entity
     * @param entity Entity to get the parent of
     * @return Parent entity
     *
     * @author Vincent Lahmann
     */
    public int getParent(int entity){
        return entityParents.get(entity);
    }

    /**
     * Get all children of an entity
     * @param parent Parent entity
     * @return List of childrenIds
     *
     * @author Vincent Lahmann
     */
    public List<Integer> getChildren(int parent){
        return entityChildren.get(parent);
    }

    /**
     * Add a component to an entity
     * @param entity Entity to add the component to
     * @param component Component to add
     * @param cls Class of the component
     * @param <T> Type of the component
     *
     * @author Vincent Lahmann
     */
    public <T> void addComponent(int entity, T component, Class<T> cls){
        components.get(entity).put(cls.getName(), component);
    }

    /**
     * Return a component of an entity
     * @param entity Entity to get the component from
     * @param cls Class of the component
     * @param <T> Type of the component
     * @return Component of the entity
     *
     * @author Vincent Lahmann
     */
    public <T> T getComponent(int entity, Class<T> cls){
        return (T) components.get(entity).get(cls.getName());
    }

    /**
     * Check if an entity has a specific component class
     * @param entity Entity to check
     * @param cls Class of the component
     * @param <T> Type of the component
     * @return true if the entity has the component
     *
     * @author Vincent Lahmann
     */
    public <T> boolean hasComponent(int entity, Class<T> cls){
        return components.get(entity).containsKey(cls.getName());
    }
}
