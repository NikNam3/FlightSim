package mcg.in4.projekte_23_24.FlightSim.engine.components.physics;

import mcg.in4.projekte_23_24.FlightSim.engine.flightphysics.Surface;

import java.util.HashMap;
import java.util.Map;

/**
 * Component to store surface data for an entity
 * @version 1.0
 * @since 1.0
 * @author Nikolas Kühnlein
 */
public class SurfaceModel {
    public final Map<String, Surface> surfaces;

    /**
     * Default constructor
     * @author Nikolas Kühnlein
     */
    public SurfaceModel(){
        surfaces = new HashMap<>();
    }
}
