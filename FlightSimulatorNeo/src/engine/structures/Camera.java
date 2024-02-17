package engine.structures;

public class Camera {
    public float fov;
    public float nearClip;
    public float farClip;

    public Camera(float fov, float nClip, float fClip){
        this.fov      = fov;
        this.nearClip = nClip;
        this.farClip  = fClip;

    }
}
