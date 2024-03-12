package mcg.in4.projekte_23_24.FlightSim.engine.base;

import mcg.in4.projekte_23_24.FlightSim.engine.components.general.Transform;

import static mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d.*;

/**
 * Helper methods for the mcg.in4.projekte_23_24.FlightSim.engine
 *
 * @version 1.0
 * @since 1.0
 *
 * @author Vincent Lahmann
 */
public class Utils {
    /**
     * Calculates the model matrix of a transform
     * @param transform The transform object
     * @return the model matrix
     *
     * @author Vinent Lahmann
     */
    public static float[][] modelMatrix(Transform transform){
        if(transform == null)
            return mat4();
        return mul(transform.matrixPosition, transform.matrixRotation);
    }

    /**
     * Calculates the model matrix an entity<br>Returns an identity matrix if the entity has no transform component attached
     * @param scene Scene which hosts the entity
     * @param entity The entity in question
     * @return The model matrix of the entity
     *
     * @author Vinent Lahmann
     */
    public static float[][] modelMatrix(Scene scene, int entity){
        if(!scene.hasComponent(entity, Transform.class))
            return mat4();
        Transform transform = scene.getComponent(entity, Transform.class);
        return modelMatrix(transform);
    }

    /**
     * Calculates the model matrix of an entity in world space<br>
     * Recursively iterates the entities parents<br>
     * Returns an identity matrix if neither the entity nor its parents have a transform component attached
     * @param scene Scene which hosts the entity
     * @param entity The entity in question
     * @return The model matrix of the entity
     *
     * @author Vinent Lahmann
     */
    public static float[][] getWorldSpaceModel(Scene scene, int entity){
        float[][] nodeModel = modelMatrix(scene, entity);
        if(!scene.hasParent(entity))
            return nodeModel;

        float[][] parentModel = getWorldSpaceModel(scene, scene.getParent(entity));
        return mul(parentModel, nodeModel);
    }
}
