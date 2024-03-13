package mcg.in4.projekte_23_24.FlightSim.engine.terrain;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.*;

/**
 * Loader class for standard textures
 */
class HostLoaderAlbedo extends mcg.in4.projekte_23_24.FlightSim.engine.terrain.HostLoadingProcess<ByteBuffer> {
    HostLoaderAlbedo(String file) {
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
        setLoadingResult(data, widthA[0], heightA[0]);
    }
}
