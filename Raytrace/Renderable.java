package Raytrace;

import java.awt.*;
import java.util.List;

/**
 * Renderable is the interface
 */
public interface Renderable {
    boolean intersect(Ray r);
    Color Shade(Ray ray, java.util.List<Object> lights, List<Object> objects, Color background);
    String toString();
}