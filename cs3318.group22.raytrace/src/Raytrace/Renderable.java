package cs3318.group22.raytrace.src.Raytrace;

import java.awt.*;
import java.util.List;

/**
 * A interface representing a renderable object.
 *
 */
public interface Renderable {

    /**
     * This method checks if there is an intersection with ray.
     * Returns a boolean to verify whether an intersection occurred or not.
     *
     * @param r Ray instance that is checked for intersection
     * @return boolean to verify whether an intersection occurred or not.
     */
    boolean intersect(Ray r);

    /**
     * Supplies critical bits of geometric information for a class shader. It computes:
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
    Color Shade(Ray ray, java.util.List<Object> lights, List<Object> objects, Color background);

    /**
     * Overrides toString() to be specific to the class.
     *
     * @return String describes the instance of the class.
     */
    String toString();
}