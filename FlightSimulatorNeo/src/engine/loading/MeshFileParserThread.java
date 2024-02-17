package engine.loading;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class MeshFileParserThread extends Thread{
    private static class DataBuffer{
        float[] positions;
        float[] uvs;
        float[] normals;
    }

    private final DataBuffer  buffer;
    private final String      file;
    private Exception         exception;

    MeshFileParserThread(String file){
        this.file          = file;
        this.exception     = null;
        this.buffer        = new DataBuffer();
    }

    @Override
    public void run() {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(file), Charset.defaultCharset());
        } catch (IOException e) {
            exception = e;
            return;
        }

        List<Float> posPool  = new ArrayList<>();
        List<Float> normPool = new ArrayList<>();
        List<Float> uvPool   = new ArrayList<>();

        List<Float> posData  = new ArrayList<>();
        List<Float> normData = new ArrayList<>();
        List<Float> uvData   = new ArrayList<>();

        for(String line : lines) {
            String[] blocks = line.split(" ");
            String token = blocks[0];
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
        }

        buffer.positions = new float[posData.size()];
        for(int idx = 0; idx < posData.size(); idx++) {
            buffer.positions[idx] = posData.get(idx);
        }

        buffer.normals = new float[normData.size()];
        for(int idx = 0; idx < normData.size(); idx++) {
            buffer.normals[idx] = normData.get(idx);
        }

        buffer.uvs = new float[uvData.size()];
        for(int idx = 0; idx < uvData.size(); idx++) {
            buffer.uvs[idx] = uvData.get(idx);
        }
    }

    float[] getPositionData(){
        if(isAlive())
            throw new IllegalStateException();
        return buffer.positions;
    }

    float[] getNormalData(){
        if(isAlive())
            throw new IllegalStateException();
        return buffer.normals;
    }

    float[] getUvData(){
        if(isAlive())
            throw new IllegalStateException();
        return buffer.uvs;
    }

    String getFile(){
        return file;
    }

    boolean hasFailed(){
        return exception != null;
    }

    Exception getException(){
        return exception;
    }
}
