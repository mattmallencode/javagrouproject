package cs3318.group22.raytrace.src.Raytrace;

import java.awt.*;
import java.util.List;

// An example "Renderable" object

/**
 * A class representing a Sphere.
 *
 */
public class Sphere implements Renderable {
    private Surface surface;
    private Vector3D center;
    private float radius;
    private float radiusSquared;

    /**
     * A constructor for Sphere, that receives three parameters and instantiate four variables.
     *
     * @param surface Surface object
     * @param center Vector3d object
     * @param radius radius as a float
     */
    Sphere(Surface surface, Vector3D center, float radius) {
        this.surface = surface;
        this.center = center;
        this.radius = radius;
        radiusSquared = radius * radius;
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
    @Override
    public boolean intersect(Ray ray) {
        float dx = center.getX() - ray.getOrigin().getX();
        float dy = center.getY() - ray.getOrigin().getY();
        float dz = center.getZ() - ray.getOrigin().getZ();
        float v = ray.getDirection().dotMultiplication(dx, dy, dz);

        if (v - radius > ray.getT())
            return false;

        float t = radiusSquared + v * v - dx * dx - dy * dy - dz * dz;
        if (t < 0)
            return false;

        t = v - ((float) Math.sqrt(t));
        if ((t > ray.getT()) || (t < 0))
            return false;

        ray.setT(t); //= t;
        ray.setObject(this);
        return true;
    }

    /**
     * Supplies critical bits of geometric information for a sphere shader. It computes:
     * <ol>
     *     <li>the point of intersection {@code pointOfIntersection}</li>
     *     <li>a unit-length surface normal {@code normalUnitLength}</li>
     *     <li>a unit-length vector towards the ray's origin {@code vectorUnitLength}</li>
     * </ol>
     *
     * @param ray Ray object
     * @param lights a list object
     * @param objects an object
     * @param background color of the background
     * @return {@code surface.Shade} an illumination model
     */
    @Override
    public Color Shade(Ray ray, java.util.List<Object> lights, List<Object> objects, Color background) {

        // An object shader doesn't really do too much other than
        // supply a few critical bits of geometric information
        // for a surface shader. It must compute:
        //
        //   1. the point of intersection (p)
        //   2. a unit-length surface normal (n)
        //   3. a unit-length vector towards the ray's origin (v)
        //
        float px = ray.getOrigin().getX() + ray.getT() * ray.getDirection().getX();
        float pz = ray.getOrigin().getZ() + ray.getT() * ray.getDirection().getZ();
        float py = ray.getOrigin().getY() + ray.getT() * ray.getDirection().getY();

        Vector3D pointOfIntersection = new Vector3D(px, py, pz);
        Vector3D vectorUnitLength = new Vector3D(-ray.getDirection().getX(), -ray.getDirection().getY(), -ray.getDirection().getZ());
        Vector3D normalUnitLength = new Vector3D(px - center.getX(), py - center.getY(), pz - center.getZ());
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