package Raytrace;

import java.awt.*;
import java.util.List;

public class Ray {
    protected static final float MAX_T = Float.MAX_VALUE;
    Vector3D origin;
    Vector3D direction;

    protected float t;
    Renderable object;

    public float getT() {
        return t;
    }
    public void setT(float t) {
        this.t = t;
    }

    public Ray(Vector3D eye, Vector3D dir) {
        origin = new Vector3D(eye);
        direction = Vector3D.normalize(dir);
    }

    public boolean trace(List<Object> objects) {
        t = MAX_T;
        object = null;
        for (Object objList : objects) {
            Renderable object = (Renderable) objList;
            object.intersect(this);
        }
        return (object != null);
    }

    // The following method is not strictly needed, and most likely
    // adds unnecessary overhead, but I prefered the syntax
    //
    //            ray.Shade(...)
    // to
    //            ray.object.Shade(ray, ...)
    //
    public final Color Shade(List<Object> lights, List<Object> objects, Color bgnd) {
        return object.Shade(this, lights, objects, bgnd);
    }

    public String toString() {
        return ("ray origin = "+origin+"  direction = "+direction+"  t = "+t);
    }
}