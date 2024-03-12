package mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures;

/**
 * Base class for all OpenGL structure classes
 *
 * @version 1.0
 * @since 1.0
 * @author Vincent Lahmann
 */
public abstract class GLStructure {

    /**
     *  OpenGL id that is used to access the structure
     */
    protected final int glApiId;

    /**
     * Constructor
     * @param glApiId OpenGL id that is used to access the structure
     * @author Vincent Lahmann
     */
    GLStructure(int glApiId){
        this.glApiId = glApiId;
    }

    /**
     * Calls OpenGL and binds or uses the structure associated with glApiId
     * @author Vincent Lahmann
     */
    public abstract void makeActive();

    /**
     * Deletes all associated data from the OpenGL device
     * @author Vincent Lahmann
     */
    public abstract void clearFromDevice();

}
