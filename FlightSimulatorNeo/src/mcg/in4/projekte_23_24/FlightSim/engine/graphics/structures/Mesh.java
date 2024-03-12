package mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures;

import static org.lwjgl.opengl.GL41.*;

/**
 * Wrapper for a VAO, its vertex count, and the material
 */

public class Mesh extends GLStructure {
    /**
     * Vertex count of the mesh
     */

    public Material material;
    public final int vertexCount;

    public Mesh(int glId, int vertexCount){
        super(glId);
        this.vertexCount = vertexCount;
    }

    public Mesh(int glId, int vertexCount, Material material){
        this(glId, vertexCount);
        this.material = material;
    }

    @Override
    public void makeActive() {
        glBindVertexArray(glApiId);
    }

    @Override
    public void clearFromDevice() {
        glDeleteVertexArrays(glApiId);
    }
}
