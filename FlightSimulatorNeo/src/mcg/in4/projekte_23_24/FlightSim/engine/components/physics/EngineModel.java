package mcg.in4.projekte_23_24.FlightSim.engine.components.physics;

import mcg.in4.projekte_23_24.FlightSim.engine.flightphysics.Engine;

import java.util.HashMap;
import java.util.Map;

/**
 * Component to store engine data for an entity
 * @version 1.0
 * @since 1.0
 * @author Nikolas Kühnlein
 */
public class EngineModel {
    public final Map<String, Engine> engines;

    /**
     * Default constructor
     * @author Nikolas Kühnlein
     */
    public EngineModel(){
        engines = new HashMap<>();
    }
}
