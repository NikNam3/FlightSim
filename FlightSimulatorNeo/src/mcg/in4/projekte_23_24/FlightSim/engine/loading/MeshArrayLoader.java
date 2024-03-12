package mcg.in4.projekte_23_24.FlightSim.engine.loading;

import mcg.in4.projekte_23_24.FlightSim.engine.components.render.MeshArray;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.Material;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.Mesh;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL41.*;

/**
 * Class for loading structures on main thread
 *
 * @version 1.0
 * @since 1.0
 * @see MeshArray
 * @author Vincent Lahmann
 */
public class MeshArrayLoader {
    /**
     * Converts a Wavefront obj file to MeshArray<br>
     * One Material corresponds to one Mesh in the array
     * @param file Path of source file. Must be in wavefront obj format
     * @return Data from file as MeshArray
     * @throws Exception If an error occurs while loading
     *
     * @see MeshArray
     * @author Vincent Lahmann
     */
    public static MeshArray loadMesh(String file) throws Exception {
        String parentDir = getParentDirectory(file);

        List<String> lines;
        // parse all lines from file into list
        lines = Files.readAllLines(Paths.get(file), Charset.defaultCharset());

        // temporary data pool
        List<Float> posPool  = new ArrayList<>();
        List<Float> normPool = new ArrayList<>();
        List<Float> uvPool   = new ArrayList<>();

        // data sorted per face
        List<Float> posData  = new ArrayList<>();
        List<Float> normData = new ArrayList<>();
        List<Float> uvData   = new ArrayList<>();

        Map<String, Material> materialLibrary = null;
        String activeMaterialName = "";
        List<Mesh> submeshList = new ArrayList<>();

        for(String line : lines) {
            String[] blocks = line.split(" ");
            String token = blocks[0];
            if(token.equals("mtllib")){
                String path = blocks[1];
                materialLibrary = loadMtlLib(parentDir + "\\" + path);
            }


            if(token.equals("v")){
                posPool.add(Float.parseFloat(blocks[1]));
                posPool.add(Float.parseFloat(blocks[2]));
                posPool.add(Float.parseFloat(blocks[3]));
            }
            if(token.equals("vn")){
                normPool.add(Float.parseFloat(blocks[1]));
                normPool.add(Float.parseFloat(blocks[2]));
                normPool.add(Float.parseFloat(blocks[3]));
            }
            if(token.equals("vt")){
                uvPool.add(Float.parseFloat(blocks[1]));
                uvPool.add(Float.parseFloat(blocks[2]));
            }

            if(token.equals("f")){
                for(int i = 0; i < 3; i++){
                    String[] vertBlocks = blocks[i + 1].split("/");
                    int vertIdx = Integer.parseInt(vertBlocks[0]) - 1;

                    posData.add(posPool.get(vertIdx * 3));
                    posData.add(posPool.get(vertIdx * 3 + 1));
                    posData.add(posPool.get(vertIdx * 3 + 2));

                    if(!vertBlocks[1].isEmpty()){
                        int uvIdx = Integer.parseInt(vertBlocks[1]) - 1;
                        uvData.add(uvPool.get(uvIdx * 2));
                        uvData.add(uvPool.get(uvIdx * 2 + 1));
                    }
                    if(vertBlocks.length == 3){
                        int normalIdx = Integer.parseInt(vertBlocks[2]) - 1;
                        normData.add(normPool.get(normalIdx * 3));
                        normData.add(normPool.get(normalIdx * 3 + 1));
                        normData.add(normPool.get(normalIdx * 3 + 2));
                    }
                }
            }

            if(token.equals("usemtl")){
                if(!posData.isEmpty()){
                    // flush old mesh
                    submeshList.add(toMesh(posData, normData, uvData, materialLibrary.get(activeMaterialName)));
                    posData.clear();
                    normData.clear();
                    uvData.clear();
                }
                activeMaterialName = blocks[1];
            }
        }
        //flush data of last mesh
        Material material = null;
        if(materialLibrary != null)
            material = materialLibrary.get(activeMaterialName);
        submeshList.add(toMesh(posData, normData, uvData, material));
        Mesh[] submeshArray = new Mesh[submeshList.size()];
        submeshList.toArray(submeshArray);
        MeshArray result = new MeshArray();
        result.submeshes = submeshArray;
        return result;
    }

    /**
     * Converts data to Mesh
     * @param file Path of source file. Must be in wavefront obj format
     * @return Data from file as MeshArray
     * @throws Exception If an error occurs while loading
     *
     * @author Vincent Lahmann
     */
    private static Map<String, Material> loadMtlLib(String file) throws Exception{
        String parentPath = getParentDirectory(file);
        Map<String, Material> result = new HashMap<>();
        List<String> lines = Files.readAllLines(Paths.get(file), Charset.defaultCharset());

        String activeMaterialName = "";
        for(String line : lines){
            String[] blocks = line.split(" ");
            String token = blocks[0];
            if(token.equals("newmtl")){
                activeMaterialName = blocks[1];
                result.put(activeMaterialName, new Material());
            }
            if(token.equals("Kd")){
                float r = Float.parseFloat(blocks[1]);
                float g = Float.parseFloat(blocks[2]);
                float b = Float.parseFloat(blocks[3]);
                result.get(activeMaterialName).albedo = new float[]{r, g, b};
            }
            if(token.equals("map_Kd")){
                result.get(activeMaterialName).texture = TextureLoader.load(parentPath + "\\" + blocks[1]);
            }
        }

        return result;
    }

    /**
     * Returns the parent directory of a file
     * @param dir Path of file
     * @return Parent directory of file
     *
     * @author Vincent Lahmann
     */
    private static String getParentDirectory(String dir){
        return Paths.get(dir).getParent().toString();
    }

    /**
     * Converts position, normal and uv data to a Mesh
     * @param positionList  List of position data
     * @param normalList   List of normal data
     * @param uvList      List of uv data
     * @param material   Material of mesh
     * @return Mesh
     *
     * @author Vincent Lahmann
     */
    private static Mesh toMesh(List<Float> positionList, List<Float> normalList, List<Float> uvList, Material material){
        float[] positions = new float[positionList.size()];
        for(int idx = 0; idx < positionList.size(); idx++) {
            positions[idx] = positionList.get(idx);
        }

        float[] normals = new float[normalList.size()];
        for(int idx = 0; idx < normalList.size(); idx++) {
            normals[idx] = normalList.get(idx);
        }

        float[] uvs = new float[uvList.size()];
        for(int idx = 0; idx < uvList.size(); idx++) {
            uvs[idx] = uvList.get(idx);
        }

        int glId = glGenVertexArrays();
        glBindVertexArray(glId);
        addVb(positions, 0, 3, false, 0, 0);
        addVb(normals,   1, 3, false, 0, 0);
        addVb(uvs,       2, 2, false, 0, 0);

        int vertexCount = positions.length / 3;

        return new Mesh(glId, vertexCount, material);
    }

    /**
     * Adds a vertex buffer to the current vertex array object
     * @param data Data to add
     * @param index Index of vertex attribute
     * @param count Number of components per vertex attribute
     * @param normalized If the data should be normalized
     * @param stride Offset between consecutive vertex attributes
     * @param offset Offset of the first component of the first vertex attribute
     *
     * @author Vincent Lahmann
     */
    private static void addVb(float[] data, int index, int count, boolean normalized, int stride, int offset) {
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glEnableVertexAttribArray(index);
        glBufferData(GL_ARRAY_BUFFER, data , GL_STATIC_DRAW);
        glVertexAttribPointer(index, count, GL_FLOAT, normalized, stride, offset);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
