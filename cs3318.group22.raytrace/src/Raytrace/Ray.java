package cs3318.group22.raytrace.src.Raytrace;

import java.awt.*;
import java.util.List;


public class Ray {
    private static final float MAX_T = Float.MAX_VALUE;
    Vector3D origin;
    Vector3D direction;

    private float t;
    Renderable object;


    // getters and setters

    /**
     * Initialise ray with constructor parameters.
     *
     * @param eye origin
     * @param dir direction
     */
    Ray(Vector3D eye, Vector3D dir) {
        origin = new Vector3D(eye);
        direction = Vector3D.normalize(dir);
    }

    Vector3D getOrigin() {
        return origin;
    }

    void setOrigin(Vector3D origin) {
        this.origin = origin;
    }

    Vector3D getDirection() {
        return direction;
    }

    void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    Renderable getObject() {
        return object;
    }

    void setObject(Renderable o) {
        this.object = o;
    }

    float getT() {
        return t;
    }

    void setT(float t) {
        this.t = t;
    }

    /**
     * Check if light from camera hits all the objects.
     *
     * @param objects List of objects
     * @return True if the object is not null
     */
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

    /**
     * Specifies the shade of shape.
     *
     * @param lights     List of lights
     * @param objects    List of objects
     * @param background Background colour
     * @return Object shade
     */
    Color Shade(List<Object> lights, List<Object> objects, Color background) {
        return object.Shade(this, lights, objects, background);
    }

    public String toString() {
        return ("ray origin = " + origin + "  direction = " + direction + "  t = " + t);
    }
}