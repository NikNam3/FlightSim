package simobjects.aircraft.displays;

import graphics.Mesh;
import math.Mat3;
import math.Math3D;
import math.Vec3;

public class TurnIndicator extends Display{
    public TurnIndicator(Vec3 relPos, Mat3 relRot, Mesh backgroundMesh, Mesh pointerMesh) {
        super(relPos, relRot);


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void Update() {

    }
}
// TODO implement the TurnIndicator