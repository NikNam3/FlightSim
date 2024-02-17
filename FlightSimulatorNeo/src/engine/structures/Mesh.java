package engine.structures;

public class Mesh {
    public int glId;
    public int vertexCount;
    public Texture2D[] textureMaps;

    public Mesh(int glId, int vertexCount){
        this.glId = glId;
        this.vertexCount = vertexCount;
    }

    public Mesh(){
        this.glId        = 0;
        this.vertexCount = 0;
    }

}
