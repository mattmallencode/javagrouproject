package Raytrace;

import java.awt.*;
import java.util.List;

public class Ray {
    protected static final float MAX_T = Float.MAX_VALUE;
    Vector3D origin;
    Vector3D direction;

    protected float t;
    Renderable object;

    // getters and setters
    public float getT() {
        return t;
    }
    public void setT(float t) {
        this.t = t;
    }

    Ray(Vector3D eye, Vector3D dir) {
        origin = new Vector3D(eye);
        direction = Vector3D.normalize(dir);
    }

    boolean trace(List<Object> objects) {
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
    final Color Shade(List<Object> lights, List<Object> objects, Color background) {
        return object.Shade(this, lights, objects, background);
    }

    public String toString() {
        return ("ray origin = "+origin+"  direction = "+direction+"  t = "+t);
    }
}