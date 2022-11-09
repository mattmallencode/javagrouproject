package Raytrace;

import java.awt.*;
import java.util.List;

// An example "Renderable" object
public class Sphere implements Renderable {
    protected Surface surface;
    protected Vector3D center;
    protected float radius;
    protected float radSqr;

    public Sphere(Surface s, Vector3D c, float r) {
        surface = s;
        center = c;
        radius = r;
        radSqr = r * r;
    }

    public boolean intersect(Ray ray) {
        float dx = center.x - ray.origin.x;
        float dy = center.y - ray.origin.y;
        float dz = center.z - ray.origin.z;
        float v = ray.direction.dot(dx, dy, dz);

        // Do the following quick check to see if there is even a chance
        // that an intersection here might be closer than a previous one
        if (v - radius > ray.getT())
            return false;

        // Test if the ray actually intersects the sphere
        float t = radSqr + v * v - dx * dx - dy * dy - dz * dz;
        if (t < 0)
            return false;

        // Test if the intersection is in the positive
        // ray direction and it is the closest so far
        t = v - ((float) Math.sqrt(t));
        if ((t > ray.getT()) || (t < 0))
            return false;

        ray.setT(t); //= t;
        ray.object = this;
        return true;
    }

    public Color Shade(Ray ray, java.util.List<Object> lights, List<Object> objects, Color bgnd) {
        // An object shader doesn't really do too much other than
        // supply a few critical bits of geometric information
        // for a surface shader. It must must compute:
        //
        //   1. the point of intersection (p)
        //   2. a unit-length surface normal (n)
        //   3. a unit-length vector towards the ray's origin (v)
        //
        float px = ray.origin.x + ray.getT() * ray.direction.x;
        float py = ray.origin.y + ray.getT() * ray.direction.y;
        float pz = ray.origin.z + ray.getT() * ray.direction.z;

        Vector3D p = new Vector3D(px, py, pz);
        Vector3D v = new Vector3D(-ray.direction.x, -ray.direction.y, -ray.direction.z);
        Vector3D n = new Vector3D(px - center.x, py - center.y, pz - center.z);
        n.normalize();

        // The illumination model is applied
        // by the surface's Shade() method
        return surface.Shade(p, n, v, lights, objects, bgnd);
    }

    public String toString() {
        return ("sphere " + center + " " + radius);
    }
}