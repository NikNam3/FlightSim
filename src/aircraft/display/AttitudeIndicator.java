package aircraft.display;

import graphics.Mesh;
import math.Vec3;

public class AttitudeIndicator extends Display {
    public AttitudeIndicator(Vec3 relPos, Vec3 relRot, Mesh backgroundMesh, Mesh pointerMesh) {
        super(relPos, relRot);

        DisplayElement background = new DisplayElement(backgroundMesh);
        background.setRelPos(new Vec3(0, -1, 0)); // TODO set the correct position of the Gyro Ball

        DisplayElement pointer = new DisplayElement(pointerMesh);

        addDisplayElement(background); // Adds DisplayElement moving background to the Display at the index 0
        addDisplayElement(pointer); // Adds not moving DisplayElement pointer to the Display at the index 1
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void Update() {
        getDisplayElement(0).setRelRot(new Vec3(
                -aircraft.getRelRotation().x,
                -aircraft.getRelRotation().y,
                -aircraft.getRelRotation().z)); // TODO add the correct rotation

        // Sets the Rotation of the Gyro to zero
    }
}
