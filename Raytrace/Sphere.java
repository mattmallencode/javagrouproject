package Raytrace;

import java.awt.*;
import java.util.List;

// An example "Renderable" object

/**
 * An instance of Renderable
 *
 */
public class Sphere implements Renderable {
    protected Surface surface;
    protected Vector3D center;
    protected float radius;
    protected float radiusSquare;

    /**
     * A constructor for Sphere, that receives three parameters and instantiate four variables.
     *
     * @param s Surface object
     * @param c Vector3d object
     * @param r radius as a float
     */
    public Sphere(Surface s, Vector3D c, float r) {
        surface = s;
        center = c;
        radius = r;
        radiusSquare = r * r;
    }

    /**
     * Quick checks to see:
     * <ol>
     *     <li>a chance that intersection might be closer than a previous one;</li>
     *     <li>intersection between ray and sphere;</li>
     *     <li>intersection in the positive ray direction;</li>
     * </ol>
     * Returns {@code true} if the checks fails
     *
     * @param ray object which is to be tested
     * @return {@code true} if the check fails
     */
    public boolean intersect(Ray ray) {
        float dx = center.x - ray.origin.x;
        float dy = center.y - ray.origin.y;
        float dz = center.z - ray.origin.z;
        float v = ray.direction.dot(dx, dy, dz);


        if (v - radius > ray.getT())
            return false;

        float t = radiusSquare + v * v - dx * dx - dy * dy - dz * dz;
        if (t < 0)
            return false;

        t = v - ((float) Math.sqrt(t));
        if ((t > ray.getT()) || (t < 0))
            return false;

        ray.setT(t);
        ray.object = this;
        return true;
    }

    /**
     * Supplies critical bits of geometric information for a surface shader. It computes:
     * <ol>
     *     <li>the point of intersection {@code }</li>
     *     <li>a unit-length surface normal {@code }</li>
     *     <li>a unit-length vector towards the ray's origin {@code }</li>
     * </ol>
     *
     * @param ray Ray object
     * @param lights a list object
     * @param objects an object
     * @param background color of the background
     * @return {@code surface.Shade} an illumination model
     */
    public Color Shade(Ray ray, java.util.List<Object> lights, List<Object> objects, Color background) {

        float px = ray.origin.x + ray.getT() * ray.direction.x;
        float py = ray.origin.y + ray.getT() * ray.direction.y;
        float pz = ray.origin.z + ray.getT() * ray.direction.z;

        Vector3D pointOfIntersection = new Vector3D(px, py, pz);
        Vector3D vectorUnitLength = new Vector3D(-ray.direction.x, -ray.direction.y, -ray.direction.z);
        Vector3D normalUnitLength = new Vector3D(px - center.x, py - center.y, pz - center.z);
        normalUnitLength.normalize();


        return surface.Shade(pointOfIntersection, normalUnitLength, vectorUnitLength, lights, objects, background);
    }

    /**
     * Override method of toString().
     * Returns a string representation of Sphere which describes the center and its radius.
     *
     * @return {@code String} representation of Sphere
     */
    @Override
    public String toString() {
        return ("sphere " + center + " " + radius);
    }
}