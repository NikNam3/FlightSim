package mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures;

/**
 * Base class for all OpenGL structure classes
 */
public abstract class GLStructure {

    /**
     *  OpenGL id that is used to access the structure
     */
    protected final int glApiId;


    GLStructure(int glApiId){
        this.glApiId = glApiId;
    }

    /**
    *   Calls OpenGL and binds or uses the structure associated with glApiId
    */
    public abstract void makeActive();

    /**
     *   Deletes all associated data from the OpenGL device
     */
    public abstract void clearFromDevice();

}
