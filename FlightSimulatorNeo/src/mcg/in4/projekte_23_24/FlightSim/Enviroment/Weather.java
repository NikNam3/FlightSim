package mcg.in4.projekte_23_24.FlightSim.Enviroment;

public class Weather {
    public static float[] getWindAtPosition(float[] position){
        return new float[]{-5,0,0}; // TODO magic number wind has 5m/s from south
    }

    public static float getAirDensityAtPosition(float[] position){
        return 1.225f; // TODO magic number air density at sea level
    }
}
