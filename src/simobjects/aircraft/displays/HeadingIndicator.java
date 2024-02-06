package simobjects.aircraft.displays;

import graphics.Mesh;
import math.Mat3;
import math.Math3D;
import math.Vec3;



public class HeadingIndicator extends Display {
    public HeadingIndicator(Vec3 relPos, Mat3 relRot, Mesh backgroundMesh, Mesh pointerMesh) {
        super(relPos, relRot);

        DisplayElement background = new DisplayElement(backgroundMesh);
        DisplayElement pointer = new DisplayElement(pointerMesh);

        addDisplayElement(background); // Adds DisplayElement background to the Display at the index 0
        addDisplayElement(pointer); // Adds DisplayElement pointer to the Display at the index 1

        // In the Heading indicator the Background is a moving gyro. The pointer is the fixed part
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void Update() {
        assert Math3D.rotationMatrixToEulerAngles(aircraft.getRelRotation()) != null;

        getDisplayElement(0).setRotation(
                -Math3D.rotationMatrixToEulerAngles(
                        aircraft.getRelRotation()
                ).z
        );
    }
}
