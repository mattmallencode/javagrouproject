package raytrace;


import java.awt.*;
import java.util.List;

/**
 * A class representing a surface.
 *
 */
public class Surface {
    private float intrinsicRed, intrinsicGreen, intrinsicBlue;        // surface's intrinsic color
    private float ambientReflectionCoefficient, diffuseReflectionCoefficient, specularReflectionCoefficient, phongExponent;    // constants for phong model
    private float transmissionCoefficient, reflectanceCoefficient, nt;
    private static final float TINY = 0.001f;
    private static final float ONE_OVER_255 = 0.00392156f;  // 1/255

    /**
     * Constructor for the Surface class.
     *
     * @param redValue degree of red colour in the Surface.
     * @param greenValue degree of green colour in the Surface.
     * @param blueValue degree of blueValue in the Surface.
     * @param ambientReflection amount of ambient light reflected from Surface.
     * @param diffuseReflection amount of diffuse reflection from Surface.
     * @param specularReflection amount of specular reflection from Surface.
     * @param phongExp - phong exponent of the Surface.
     * @param reflectance - amount of reflectance from Surface.
     * @param transmission - probability of light passing through the Surface.
     * @param index - the index of the Surface.
     */
    public Surface(float redValue, float greenValue, float blueValue, float ambientReflection, float diffuseReflection, float specularReflection, float phongExp, float reflectance, float transmission, float index) {
        intrinsicRed = redValue; intrinsicGreen = greenValue; intrinsicBlue = blueValue;
        ambientReflectionCoefficient = ambientReflection; diffuseReflectionCoefficient = diffuseReflection; specularReflectionCoefficient = specularReflection; phongExponent = phongExp;
        reflectanceCoefficient = reflectance* ONE_OVER_255; transmissionCoefficient = transmission; nt = index;
    }

    /**
     * Function to apply the illumination model.
     *
     * @param p p
     * @param n n
     * @param v v
     * @param lights the list of Lights.
     * @param objects the list of Objects.
     * @param background the background color.
     * @return colour with the illumination model applied.
     */
    Color Shade(Vector3D p, Vector3D n, Vector3D v, java.util.List<Object> lights, List<Object> objects, Color background) {
        float red = 0;
        float green = 0;
        float blue = 0;
        for (Object lightSources : lights) {
            Light light = (Light) lightSources;
            if (light.getLightType() == Light.getAMBIENT()) {
                red += ambientReflectionCoefficient * intrinsicRed *light.getIntensityRed();
                green += ambientReflectionCoefficient * intrinsicGreen *light.getIntensityGreen();
                blue += ambientReflectionCoefficient * intrinsicBlue *light.getIntensityBlue();
            } else {
                Vector3D l;

                if (light.getLightType() == Light.getPOINT()) {
                    l = new Vector3D(light.getLightVector().getX() - p.getX(), light.getLightVector().getY() - p.getY(), light.getLightVector().getZ() - p.getZ());

                    l.normalize();
                } else {
                    l = new Vector3D(-light.getLightVector().getX(), -light.getLightVector().getY(), -light.getLightVector().getZ());
                }

                // Check if the surface point is in shadow
                Vector3D pointOffset = new Vector3D(p.getX() + TINY*l.getX(), p.getY() + TINY*l.getY(), p.getZ() + TINY*l.getZ());
                Ray shadowRay = new Ray(pointOffset, l);
                if (shadowRay.trace(objects))
                    break;

                float lambert = Vector3D.dotMultiplication(n,l);
                if (lambert > 0) {
                    if (diffuseReflectionCoefficient > 0) {
                        float diffuse = diffuseReflectionCoefficient *lambert;
                        red += diffuse* intrinsicRed *light.getIntensityRed();
                        green += diffuse* intrinsicGreen *light.getIntensityGreen();
                        blue += diffuse* intrinsicBlue *light.getIntensityBlue();
                    }
                    if (specularReflectionCoefficient > 0) {
                        lambert *= 2;
                        float spec = v.dotMultiplication(lambert*n.getX() - l.getX(), lambert*n.getY() - l.getY(), lambert*n.getZ() - l.getZ());

                        if (spec > 0) {
                            spec = specularReflectionCoefficient *((float) Math.pow((double) spec, (double) phongExponent));
                            red += spec*light.getIntensityRed();
                            green += spec*light.getIntensityGreen();
                            blue += spec*light.getIntensityBlue();
                        }
                    }
                }
            }
        }

        // Compute illumination due to reflection
        if (reflectanceCoefficient > 0) {
            float t = v.dotMultiplication(n);
            if (t > 0) {
                t *= 2;
                Vector3D reflect = new Vector3D(t*n.getX() - v.getX(), t*n.getY() - v.getY(), t*n.getZ() - v.getZ());
                Vector3D pointOffset = new Vector3D(p.getX() + TINY*reflect.getX(), p.getY() + TINY*reflect.getY(), p.getZ() + TINY*reflect.getZ());
                Ray reflectedRay = new Ray(pointOffset, reflect);
                if (reflectedRay.trace(objects)) {
                    Color reflectedColor = reflectedRay.Shade(lights, objects, background);
                    red += reflectanceCoefficient *reflectedColor.getRed();
                    green += reflectanceCoefficient *reflectedColor.getGreen();
                    blue += reflectanceCoefficient *reflectedColor.getBlue();
                } else {
                    red += reflectanceCoefficient *background.getRed();
                    green += reflectanceCoefficient *background.getGreen();
                    blue += reflectanceCoefficient *background.getBlue();
                }
            }
        }

        // Add code for refraction here

        red = Math.min(red, 1f);
        green = Math.min(green, 1f);
        blue = Math.min(blue, 1f);

        red = (red < 0) ? 0 : red;
        green = (green < 0) ? 0 : green;
        blue = (blue < 0) ? 0 : blue;

        return new Color(red, green, blue);
    }
}