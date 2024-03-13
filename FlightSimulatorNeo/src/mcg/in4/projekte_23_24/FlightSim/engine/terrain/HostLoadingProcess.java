package mcg.in4.projekte_23_24.FlightSim.engine.terrain;

/**
 * Base class for asynchronous texture loaders
 * @param <T> Type of data that will be read from the texture
 */
abstract class HostLoadingProcess<T> extends Thread{
    private int width;
    private int height;
    private final String file;
    private boolean successFlag;
    private T data;

    HostLoadingProcess(String file) {
        this.file   = file;
        successFlag = false;
    }

    @Override
    public abstract void run();

    /**
     * Helper method for setting all the loaded data at once
     * @param data Data that was loaded
     * @param width Width of the texture
     * @param height Height of the texture
     */
    protected final void setLoadingResult(T data, int width, int height){
        this.data   = data;
        this.width  = width;
        this.height = height;
        successFlag = true;
    }

    final int getTextureWidth() {
        return width;
    }

    final int getTextureHeight() {
        return height;
    }

    final T getTextureData() {
        return data;
    }

    final boolean wasSuccessful(){
        return successFlag;
    }

    final String getFile(){
        return file;
    }
}
