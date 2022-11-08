package Raytrace;

class Light {
    public static final int AMBIENT = 0;
    public static final int DIRECTIONAL = 1;
    public static final int POINT = 2;

    protected int lightType;
    protected Vector3D lvec;           // the position of a point light or
                                     // the direction to a directional light
    protected float ir;
    protected float ig;
    protected float ib;                // intensity of the light source

    //getter and setters
    public int getLightType() {
        return lightType;
    }
    public void setLightType(int lightType) {
        this.lightType = lightType;
    }

    public Vector3D getLvec() {
        return lvec;
    }
    public void setLvec(Vector3D lvec) {
        this.lvec = lvec;
    }

    public float getIr() {
        return ir;
    }
    public void setIr(float ir) {
        this.ir = ir;
    }

    public float getIb() {
        return ib;
    }
    public void setIb(float ib) {
        this.ib = ib;
    }

    public float getIg() {
        return ig;
    }
    public void setIg(float ig) {
        this.ig = ig;
    }


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
