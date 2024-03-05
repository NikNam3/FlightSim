package mcg.in4.projekte_23_24.FlightSim.engine.structures;

public class HeightMap extends Texture2D{
    private float[] data;
    private int size;

    public HeightMap(int size, float[] data, int glId){
        super(glId);
        this.size  = size;
        this.data   = data;
    }

    public float sample(float u, float v){
        int i = (int) (u * size);
        int j = (int) (v * size);
        // TODO: Bilinear sampling umsetzen
        return data[i * size + j];
    }

    public void setData(float[] data) {
        this.data = data;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
