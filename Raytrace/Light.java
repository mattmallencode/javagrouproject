package Raytrace;

// Testing pull request.

class Light {
    public static final int AMBIENT = 0;
    public static final int DIRECTIONAL = 1;
    public static final int POINT = 2;

    protected int lightType;
    protected Vector3D lightVector;
    protected float intensityRed;
    protected float intensityGreen;
    protected float intensityBlue;                // intensity of the light source

    //getter and setters
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
