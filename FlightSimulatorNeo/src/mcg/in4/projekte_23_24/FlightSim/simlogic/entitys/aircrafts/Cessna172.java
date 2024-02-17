package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.aircrafts;

import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Mesh;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Surfaces;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.Aircraft;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.EntityBehavior;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.Surface;

public class Cessna172 extends Aircraft {
    @Override
    public void onCreate() {
        // Create the aircraft's left wing Surface
        int leftOuterWing = scene.createEntity();
        scene.addComponent(leftOuterWing, new Transform(), Transform.class);
        scene.addComponent(leftOuterWing, new Surface(true), EntityBehavior.class);
        scene.addComponent(leftOuterWing, new Mesh(), Mesh.class);

        scene.adopt(hostId, leftOuterWing);
        scene.getComponent(leftOuterWing, EntityBehavior.class).bind(scene, leftOuterWing);


        int leftInnerWing = scene.createEntity();
        scene.addComponent(leftInnerWing, new Transform(), Transform.class);
        scene.addComponent(leftInnerWing, new Surface(true), EntityBehavior.class);
        scene.addComponent(leftInnerWing, new Mesh(), Mesh.class);

        scene.adopt(hostId, leftInnerWing);
        scene.getComponent(leftInnerWing, EntityBehavior.class).bind(scene, leftInnerWing);


        // Safe all Surfaces in the Surfaces component for easy access
        int[] surfaces = new int[2]; //  TODO add all surfacesIds
        surfaces[0] = leftOuterWing;
        surfaces[1] = leftInnerWing;

        scene.getComponent(hostId, Surfaces.class).surfacesIds = surfaces;


    }
}
