package Raytrace;

// Testing pull request.

class Light {
    public static final int AMBIENT = 0;
    public static final int DIRECTIONAL = 1;
    public static final int POINT = 2;

    protected int lightType;
    protected Vector3D lightVector;           // the position of a point light or
                                     // the direction to a directional light
    protected float intensityRed;
    protected float intensityGreen;
    protected float intensityBlue;                // intensity of the light source

    //getter and setters
    public int getLightType() {
        return lightType;
    }
    public void setLightType(int lightType) {
        this.lightType = lightType;
    }

    public Vector3D getLightVector() {
        return lightVector;
    }
    public void setLightVector(Vector3D lightVector) {
        this.lightVector = lightVector;
    }

    public float getIntensityRed() {
        return intensityRed;
    }
    public void setIntensityRed(float intensityRed) {
        this.intensityRed = intensityRed;
    }

    public float getIntensityBlue() {
        return intensityBlue;
    }
    public void setIntensityBlue(float intensityBlue) {
        this.intensityBlue = intensityBlue;
    }

    public float getIntensityGreen() {
        return intensityGreen;
    }
    public void setIntensityGreen(float intensityGreen) {
        this.intensityGreen = intensityGreen;
    }


    public Light(int type, Vector3D v, float r, float g, float b) {
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
