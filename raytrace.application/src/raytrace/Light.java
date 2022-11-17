package raytrace;

// Testing pull request.

/**
 * A class representing a light.
 *
 */
class Light {
    private static final int AMBIENT = 0;
    private static final int DIRECTIONAL = 1;
    private static final int POINT = 2;

    private int lightType;
    private Vector3D lightVector;
    private float intensityRed;
    private float intensityGreen;
    private float intensityBlue;                // intensity of the light source

    //getter and setters
    public static int getAMBIENT(){
        return AMBIENT;
    }

    public static int getDIRECTIONAL(){
        return DIRECTIONAL;
    }

    public static int getPOINT(){
        return POINT;
    }

    public int getLightType() {
        return lightType;
    }

    public Vector3D getLightVector() {
        return lightVector;
    }

    public float getIntensityRed() {
        return intensityRed;
    }

    public float getIntensityBlue() {
        return intensityBlue;
    }

    public float getIntensityGreen() {
        return intensityGreen;
    }

    /**
     * Initialises light with constructor parameters.
     *
     * @param type      Value of light type
     * @param vector    The position of a point light or the direction to a directional light
     * @param redValue  Intensity of red colour
     * @param greenValue         Intensity of green colour
     * @param blueValue         Intensity of blue colour
     */
    Light(int type, Vector3D vector, float redValue, float greenValue, float blueValue) {
        lightType = type;
        intensityRed = redValue;
        intensityGreen = greenValue;
        intensityBlue = blueValue;
        if (type != AMBIENT) {
            lightVector = vector;
            if (type == DIRECTIONAL) {
                lightVector.normalize();
            }
        }
    }
}
