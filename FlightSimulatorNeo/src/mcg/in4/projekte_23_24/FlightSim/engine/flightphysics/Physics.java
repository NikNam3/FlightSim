package mcg.in4.projekte_23_24.FlightSim.engine.flightphysics;


import mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d;
import mcg.in4.projekte_23_24.FlightSim.engine.base.Scene;
import mcg.in4.projekte_23_24.FlightSim.engine.components.general.Transform;
import mcg.in4.projekte_23_24.FlightSim.engine.components.physics.EngineModel;
import mcg.in4.projekte_23_24.FlightSim.engine.components.physics.RigidBody;
import mcg.in4.projekte_23_24.FlightSim.engine.components.physics.SurfaceModel;

/**
 * This class handles the physics of every rigid body in the scene
 * It applies:  the force Gravity to every rigid body
 *              the force Thrust to every entity with an EngineModel
 *              the force Drag to every entity with a SurfaceModel
 *              the force Lift to every entity with a SurfaceModel containing wings
 *
 * @version 1.0
 * @since 1.0
 * @author Nikolas Kühnlein
 */
public class Physics {

    /**
     * The update method calls every Physics operation for the given entity, if it has a RigidBody and a Transform
     * @param scene the scene
     * @param entity the entity
     * @param deltaTime the time since the last frame
     *
     * @author Nikolas Kühnlein
     */
    public static void update(Scene scene, int entity, float deltaTime) {
        if (!scene.hasComponent(entity, RigidBody.class) && scene.hasComponent(entity, Transform.class)) {
            return;
        }
        RigidBody rigidBody = scene.getComponent(entity, RigidBody.class); // Get the RigidBody of the aircraft
        Transform transform = scene.getComponent(entity, Transform.class); // Get the Transform of the aircraft

        applyGravity(rigidBody, transform, deltaTime);
        applyThrust(scene, entity, rigidBody, transform, deltaTime);
        applyAerodynamicForces(scene, entity, rigidBody, transform, deltaTime);
        updateRigidBody(rigidBody, transform, deltaTime);

    }

    /**
     * Applies the force of gravity to the rigid body
     * This is done with the formula: F = m * g
     * where F is the force, m is the mass of the rigid body and g is the acceleration due to gravity (9.81 m/s^2)
     *
     * @param rigidBody the rigid body
     * @param transform the transform
     * @param deltaTime the time since the last frame
     *
     * @author Nikolas Kühnlein
     */
    private static void applyGravity(RigidBody rigidBody, Transform transform, float deltaTime) {
        float gravityForceMagnitude = 9.81f * rigidBody.mass;
        float[] gravityDirection = Math3d.vec3(0, -1, 0);
        float[] gravityForce = Math3d.mul(gravityDirection, gravityForceMagnitude);

        addForce(rigidBody, transform, gravityForce, Math3d.vec3(0,0,0), deltaTime);
    }

    /**
     * Adds the Thrust force to the rigid body if the entity has an EngineModel
     * First the RPM of the engines is updated
     * Then the thrust is calculated and added to the rigid body
     *
     * @param scene the scene in which the entity is
     * @param entity the entity to which the rigid body and EngineModel belong
     * @param rigidBody the rigid body of the entity
     * @param transform the transform of the entity
     * @param deltaTime the time since the last frame
     *
     * @author Nikolas Kühnlein
     */
    private static void applyThrust(Scene scene, int entity, RigidBody rigidBody, Transform transform, float deltaTime) {
        if (!scene.hasComponent(entity, EngineModel.class)) {
            return;
        }

        EngineModel engines = scene.getComponent(entity, EngineModel.class);

        for(Engine engine : engines.engines.values()) {
            if (engine.currentRPM < engine.setRPM) {
                engine.currentRPM = engine.currentRPM + engine.RPM_CHANGE_RATE * deltaTime;
            } else if (engine.currentRPM > engine.setRPM) {
                engine.currentRPM = engine.currentRPM - engine.RPM_CHANGE_RATE * deltaTime;
            }

            float thrust = calculateThrust(engine, rigidBody.velocity);

            addForce(rigidBody, transform, Math3d.mul(thrust, calculateNormalizedForwardVector(transform)), new float[] {0,0,0}, deltaTime);
        }
    }

    /**
     * Adds the aerodynamic forces for every surface to the rigid body if the entity has a SurfaceModel
     * The forces added are: Lift and Drag
     * Lift is only calculated for wing surfaces
     *
     * Lift is calculated with the formula: L = 0.5 * p * v^2 * S * Cl
     * Drag is calculated with the formula: D = 0.5 * p * v^2 * S * Cd
     *
     * where:   L is the lift force,
     *          D is the drag force,
     *          p is the air density,
     *          v is the velocity of the air relative to the surface,
     *          S is the surface area of the surface,
     *          Cl is the lift coefficient and
     *          Cd is the drag coefficient
     *
     * @param scene the scene in which the entity is
     * @param entity the entity to which the rigid body and SurfaceModel belong
     * @param rigidBody the rigid body of the entity
     * @param transform the transform of the entity
     * @param deltaTime the time since the last frame
     *
     * @author Nikolas Kühnlein
     */
    private static void applyAerodynamicForces(Scene scene, int entity, RigidBody rigidBody, Transform transform, float deltaTime) {
        if (!scene.hasComponent(entity, SurfaceModel.class)) {
            return;
        }

        SurfaceModel surfaces = scene.getComponent(entity, SurfaceModel.class);

        for(Surface surface: surfaces.surfaces.values()) {
            float[] offset = surface.offset;
            float[] offsetVector = calculateCOGOffsetVector(offset, rigidBody.cgOffset, transform);

            if (surface instanceof Wing wing) {
                addForce(rigidBody, transform, calculateWingLift(wing, rigidBody.velocity, transform), offsetVector, deltaTime);
            }
            addForce(rigidBody, transform, calculateSurfaceDrag(surface, rigidBody.velocity, transform), Math3d.vec3(0,0,0), deltaTime);

        }
    }

    /**
     * Updates the position and rotation of the rigid body
     * The position is updated with the formula: x = x + dv * t
     * The rotation is updated with the formula: R = R * dR * t
     *
     * where:   x is the position,
     *          dv is the velocity,
     *          t is the time since the last frame,
     *          R is the rotation matrix and
     *          dR is the rotation matrix for the angular velocity
     *
     * @param rigidBody the rigid body
     * @param transform the transform
     * @param deltaTime the time since the last frame
     *
     * @author Nikolas Kühnlein
     */
    private static void updateRigidBody(RigidBody rigidBody, Transform transform, float deltaTime) {
        transform.matrixPosition[0][3] = transform.matrixPosition[0][3] + rigidBody.velocity[0] * deltaTime;
        transform.matrixPosition[1][3] = transform.matrixPosition[1][3] + rigidBody.velocity[1] * deltaTime;
        transform.matrixPosition[2][3] = transform.matrixPosition[2][3] + rigidBody.velocity[2] * deltaTime;

        transform.matrixRotation = Math3d.mul(transform.matrixRotation, Math3d.rotationMatrix(
                rigidBody.angularVelocity[0]*deltaTime,
                rigidBody.angularVelocity[1]*deltaTime,
                rigidBody.angularVelocity[2]*deltaTime)
        );
    }

    /**
     * calculates the Lift force and Direction for a wing surface
     * @see #applyAerodynamicForces
     *
     * @param surface the wing surface
     * @param aircraftVelocity the velocity of the aircraft
     * @param aircraftTransform the transform of the aircraft
     * @return the Lift force and Direction
     *
     * @author Nikolas Kühnlein
     */
    private static float[] calculateWingLift(Wing surface, float[] aircraftVelocity, Transform aircraftTransform) {


        float   A = surface.area;
        float   p = 1.225f;
        float[] v = calculateAirflow(aircraftVelocity);
        float  Cl = surface.getCoefficientOfLift(
                calculateAngleOfAttack(surface, aircraftTransform, aircraftVelocity)
        );

        float   LiftMagnitude = (float) (0.5*Cl*A*p*Math3d.dot(v,v));
        float[] LiftDirection = Math3d.normalize(Math3d.rotateVec3(surface.normal, aircraftTransform.matrixRotation));

        return Math3d.mul(LiftDirection, LiftMagnitude);
    }

    /**
     * calculates the Drag force and Direction for a surface
     * @see #applyAerodynamicForces
     *
     * @param surface the surface
     * @param aircraftVelocity the velocity of the aircraft
     * @param aircraftTransform the transform of the aircraft
     * @return the Drag force and Direction
     *
     * @author Nikolas Kühnlein
     */
    private static float[] calculateSurfaceDrag(Surface surface, float[] aircraftVelocity, Transform aircraftTransform) {

        float   A = surface.area;
        float   p = 1.225f;
        float[] v = calculateAirflow(aircraftVelocity);
        float  Cd = surface.getCoefficientOfDrag(
                calculateAngleOfAttack(surface, aircraftTransform, aircraftVelocity)
        );

        float   DragMagnitude = (float) (-1*0.5*Cd*A*p*Math3d.dot(v,v));
        float[] DragDirection = Math3d.normalize(aircraftVelocity);

        return Math3d.mul(DragDirection, DragMagnitude);
    }

    /**
     * calculates the angle of attack meaning the angle between the airflow and the chord line of the surface which is an imaginary line from the leading edge to the trailing edge of the surface
     * This is done by taking the asin of the dot product of the airflow and the normal of the surface
     * Be aware that the airflow and the normal of the surface have to be normalized first
     *
     * @param surface the surface
     * @param aircraftTransform the transform of the aircraft
     * @param aircraftVelocity the velocity of the aircraft
     * @return the angle of attack
     *
     * @author Nikolas Kühnlein
     */
    private static float calculateAngleOfAttack(Surface surface, Transform aircraftTransform, float[] aircraftVelocity) {
        aircraftVelocity = Math3d.normalize(aircraftVelocity);
        float[] airflow = calculateAirflow(aircraftVelocity);
        float[] normal = Math3d.rotateVec3(surface.normal, aircraftTransform.matrixRotation);

        airflow = Math3d.normalize(airflow);
        normal = Math3d.normalize(normal);

        return (float) Math.asin(Math3d.dot(normal, airflow));


    }

    /**
     * calculates the movement of the air relative to the aircraft
     * This is done by subtracting the velocity of the aircraft from the velocity of the air
     * @param aircraftVelocity the velocity of the aircraft
     * @return the movement of the air relative to the aircraft
     *
     * @author Nikolas Kühnlein
     */
    private static float[] calculateAirflow(float[] aircraftVelocity) {
        float[] negativeVelocity = Math3d.mul(aircraftVelocity, -1);
        float[] wind = Math3d.vec3(0,0,0.001f);
        return Math3d.add(negativeVelocity, wind);
    }

    /**
     * calculates the normalized forward vector of the aircraft
     * This is done by multiplying the forward vector with the rotation matrix of the aircraft
     * @param transform the transform of the aircraft
     * @return the normalized forward vector of the aircraft
     *
     * @author Nikolas Kühnlein
     */
    private static float[] calculateNormalizedForwardVector(Transform transform) {
        float[] forward = new float[]{0, 0, -1, 0};
        return Math3d.normalize(
                Math3d.mul(transform.matrixRotation, forward)
        );

    }

    /**
     * calculates the offset to the center of gravity of the aircraft with respect to the aircraft rotation
     * this is done by rotating the offset and the center of gravity offset with the rotation matrix of the aircraft and adding them together
     *
     * @param offset the offset to the center of gravity
     * @param cogOffset the offset to the center of gravity of the aircraft
     * @param aircraftTransform the transform of the aircraft
     * @return the offset to the center of gravity of the aircraft with respect to the aircraft rotation
     *
     * @author Nikolas Kühnlein
     */
    private static float[] calculateCOGOffsetVector(float[] offset, float[] cogOffset, Transform aircraftTransform) {
        float[] rotatedOffset = Math3d.mul(aircraftTransform.matrixRotation, offset);
        float[] rotatedCogOffset = Math3d.mul(aircraftTransform.matrixRotation, cogOffset);
        return Math3d.add(rotatedOffset, rotatedCogOffset);


    }

    /**
     * calculates the thrustMagnitude of the engine
     *
     * Source: https://www.electricrcaircraftguy.com/2014/04/propeller-static-dynamic-thrust-equation-background.html
     * @param engine the engine
     * @param velocity the velocity of the aircraft
     * @return the thrust amount
     *
     * @author Nikolas Kühnlein
     */
    private static float calculateThrust(Engine engine, float[] velocity) {
        if (engine.currentRPM <= 0) { // Engine is off prevent division by zero
            return 0;
        }
        return (float) ((4.392e-8f * engine.currentRPM *
                (Math.pow(engine.DIAMETER * 39.3701f, 3.5f)  / Math.sqrt(engine.PITCH)) *
                (4.233e-4 * engine.currentRPM * engine.PITCH)));

    } // TODO incorporate velocity

    /**
     * Applies a force to the rigidBody
     * <p>
     * First the torque is calculated by taking the cross product of the offset and the force
     * Then the torque is transformed into the local space of the rigidBody
     * The acceleration and the angular acceleration are calculated by dividing the force and the torque by the mass and the moment of inertia of the rigidBody
     * Finally, the velocity and the angular velocity of the rigidBody are updated by adding the acceleration and the angular acceleration multiplied by the deltaTime
     * </p>
     *
     * @param rigidBody the rigidBody to apply the force to
     * @param transform the transform of the entity which the rigidBody is attached to
     * @param force the force vector to apply
     * @param offset the offset to the center of gravity of the entity
     * @param deltaTime the time between the last frame and the current frame
     *
     * @author Nikolas Kühnlein
     */
    private static void addForce(RigidBody rigidBody, Transform transform, float[] force, float[] offset, float deltaTime) {
        float[] torque = Math3d.cross(offset, force);
        torque = Math3d.mul(Math3d.inverse(transform.matrixRotation), torque);

        float[] acceleration = Math3d.div(force, rigidBody.mass);
        float[] angularAcceleration = Math3d.mul(Math3d.inverse(rigidBody.momentOfInertia), torque);

        rigidBody.velocity = Math3d.add(rigidBody.velocity, Math3d.mul(acceleration, deltaTime));
        rigidBody.angularVelocity = Math3d.add(rigidBody.angularVelocity, Math3d.mul(angularAcceleration, deltaTime));
    }
}
