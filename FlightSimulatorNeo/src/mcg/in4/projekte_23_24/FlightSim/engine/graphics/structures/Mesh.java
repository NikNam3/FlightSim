package mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures;

import static org.lwjgl.opengl.GL41.*;

/**
 * Wrapper for a VAO, its vertex count, and the material
 * @version 1.0
 * @since 1.0
 * @see GLStructure
 * @author Vincent Lahmann
 */

public class Mesh extends GLStructure {
    /**
     * Vertex count of the mesh
     */

    public Material material;
    public final int vertexCount;

    /**
     * Constructor
     * @param glId OpenGL id of the mesh
     * @param vertexCount Vertex count of the mesh
     *
     * @author Vincent Lahmann
     */
    public Mesh(int glId, int vertexCount){
        super(glId);
        this.vertexCount = vertexCount;
    }

    /**
     * Constructor
     * @param glId OpenGL id of the mesh
     * @param vertexCount Vertex count of the mesh
     * @param material Material of the mesh
     *
     * @author Vincent Lahmann
     */
    public Mesh(int glId, int vertexCount, Material material){
        this(glId, vertexCount);
        this.material = material;
    }

    /**
     * @author Vincent Lahmann
     */
    @Override
    public void makeActive() {
        glBindVertexArray(glApiId);
    }

    /**
     * @author Vincent Lahmann
     */
    @Override
    public void clearFromDevice() {
        glDeleteVertexArrays(glApiId);
    }
}
