package mcg.in4.projekte_23_24.FlightSim.simlogic.components;

import mcg.in4.projekte_23_24.FlightSim.engine.structures.Texture2D;

/**
 * This class represents a component which holds the mesh data of an object.
 * It contains the glId of the mesh and the vertexCount of the mesh, as well as the textureMaps of the mesh.
 * @author Vincent Lahmann
 * @version 1.0
 * @since 1.0
 * @see Texture2D
 */

public class Mesh {
    public int glId;
    public int vertexCount;
    public Texture2D[] textureMaps;


    /**
     * This constructor creates a Mesh with the given glId and vertexCount.
     * @param glId is the glId of the mesh
     * @param vertexCount is the vertexCount of the mesh
     * @author Vincent Lahmann
     */
    public Mesh(int glId, int vertexCount){
        this.glId = glId;
        this.vertexCount = vertexCount;
    }

    /**
     * This constructor creates a Mesh with the default values.
     * @author Vincent Lahmann
     */
    public Mesh(){
        this.glId        = 0;
        this.vertexCount = 0;
    }

}
