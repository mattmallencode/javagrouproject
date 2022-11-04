package Raytrace;

// Testing pull request.

class Light {
    public static final int AMBIENT = 0;
    public static final int DIRECTIONAL = 1;
    public static final int POINT = 2;

    public int lightType;
    public Vector3D lvec;           // the position of a point light or
    // the direction to a directional light
    public float ir, ig, ib;        // intensity of the light source

    public Light(int type, Vector3D v, float r, float g, float b) {
        lightType = type;
        ir = r;
        ig = g;
        ib = b;
        if (type != AMBIENT) {
            lvec = v;
            if (type == DIRECTIONAL) {
                lvec.normalize();
            }
        }
    }
}
