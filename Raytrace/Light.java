package Raytrace;

// Testing pull request.

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
     * @param v         The position of a point light or the direction to a directional light
     * @param r         Intensity of red colour
     * @param g         Intensity of green colour
     * @param b         Intensity of blue colour
     */
    Light(int type, Vector3D v, float r, float g, float b) {
        lightType = type;
        intensityRed = r;
        intensityGreen = g;
        intensityBlue = b;
        if (type != AMBIENT) {
            lightVector = v;
            if (type == DIRECTIONAL) {
                lightVector.normalize();
            }
        }
    }
}
