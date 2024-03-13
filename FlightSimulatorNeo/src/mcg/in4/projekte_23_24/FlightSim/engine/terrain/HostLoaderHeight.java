package mcg.in4.projekte_23_24.FlightSim.engine.terrain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.lwjgl.stb.STBImage.*;

/**
 * Loader class for textures containing floating point height data encoded in the r, g, b and a values
 */
class HostLoaderHeight extends mcg.in4.projekte_23_24.FlightSim.engine.terrain.HostLoadingProcess<float[]> {
    HostLoaderHeight(String file) {
        super(file);
    }

    @Override
    public void run() {
        stbi_set_flip_vertically_on_load(true);
        int[] widthA  = new int[1];
        int[] heightA = new int[1];
        ByteBuffer data = stbi_load(getFile(), widthA, heightA, new int[1], STBI_rgb_alpha);
        if(data == null){
            return;
        }

        // converting byte data to float array
        float[] dataFloat = new float[data.capacity() / 4];
        for(int idx = 0; idx < data.capacity() / 4; idx++) {
            byte r = (byte) (data.get(idx * 4) + 128);
            byte g = (byte) (data.get(idx * 4 + 1) + 128);
            byte b = (byte) (data.get(idx * 4 + 2) + 128);
            byte a = (byte) (data.get(idx * 4 + 3) + 128);

            float f = ByteBuffer.wrap(new byte[]{r, g, b, a}).order(ByteOrder.BIG_ENDIAN).getFloat();
            dataFloat[idx] = f;
        }
        setLoadingResult(dataFloat, widthA[0], heightA[0]);
    }
}
