package Raytrace;


import java.awt.*;
import java.util.List;

/*
ka =  ambient reflection coefficient
kd = diffuse reflection coefficient
ks = specular reflection coefficient
kt = transmission coefficient
kr = reflectance coefficient
ns phong exponent
 */
public class Surface {
    protected float intrinsicRed, intrinsicGreen, intrinsicBlue;        // surface's intrinsic color
    protected float ambientReflectionCoefficient, diffuseReflectionCoefficient, specularReflectionCoefficient, phongExponent;    // constants for phong model
    protected float transmissionCoefficient, reflectanceCoefficient, nt;
    private static final float TINY = 0.001f;
    private static final float I255 = 0.00392156f;  // 1/255
    public float getIntrinsicRed() {
        return intrinsicRed;
    }

    public void setIntrinsicRed(float intrinsicRed) {
        this.intrinsicRed = intrinsicRed;
    }

    public float getIntrinsicGreen() {
        return intrinsicGreen;
    }

    public void setIntrinsicGreen(float intrinsicGreen) {
        this.intrinsicGreen = intrinsicGreen;
    }

    public float getIntrinsicBlue() {
        return intrinsicBlue;
    }

    public void setIntrinsicBlue(float intrinsicBlue) {
        this.intrinsicBlue = intrinsicBlue;
    }

    public float getAmbientReflectionCoefficient() {
        return ambientReflectionCoefficient;
    }

    public void setAmbientReflectionCoefficient(float ambientReflectionCoefficient) {
        this.ambientReflectionCoefficient = ambientReflectionCoefficient;
    }

    public float getDiffuseReflectionCoefficient() {
        return diffuseReflectionCoefficient;
    }

    public void setDiffuseReflectionCoefficient(float diffuseReflectionCoefficient) {
        this.diffuseReflectionCoefficient = diffuseReflectionCoefficient;
    }

    public float getSpecularReflectionCoefficient() {
        return specularReflectionCoefficient;
    }

    public void setSpecularReflectionCoefficient(float specularReflectionCoefficient) {
        this.specularReflectionCoefficient = specularReflectionCoefficient;
    }

    public float getPhongExponent() {
        return phongExponent;
    }

    public void setPhongExponent(float phongExponent) {
        this.phongExponent = phongExponent;
    }

    public float getTransmissionCoefficient() {
        return transmissionCoefficient;
    }

    public void setTransmissionCoefficient(float transmissionCoefficient) {
        this.transmissionCoefficient = transmissionCoefficient;
    }

    public float getReflectanceCoefficient() {
        return reflectanceCoefficient;
    }

    public void setReflectanceCoefficient(float reflectanceCoefficient) {
        this.reflectanceCoefficient = reflectanceCoefficient;
    }

    public float getNt() {
        return nt;
    }

    public void setNt(float nt) {
        this.nt = nt;
    }


    public Surface(float redValue, float greenValue, float blueValue, float ambient, float diffuse, float specular, float n, float reflectance, float transmission, float index) {
        intrinsicRed = redValue; intrinsicGreen = greenValue; intrinsicBlue = blueValue;
        ambientReflectionCoefficient = ambient; diffuseReflectionCoefficient = diffuse; specularReflectionCoefficient = specular; phongExponent = n;
        reflectanceCoefficient = reflectance*I255; transmissionCoefficient = transmission; nt = index;
    }

    Color Shade(Vector3D p, Vector3D n, Vector3D v, java.util.List<Object> lights, List<Object> objects, Color background) {
        float red = 0;
        float green = 0;
        float blue = 0;
        for (Object lightSources : lights) {
            Light light = (Light) lightSources;
            if (light.getLightType() == Light.AMBIENT) {
                red += ambientReflectionCoefficient * intrinsicRed *light.getIr();
                green += ambientReflectionCoefficient * intrinsicGreen *light.getIg();
                blue += ambientReflectionCoefficient * intrinsicBlue *light.getIb();
            } else {
                Vector3D l;
                if (light.getLightType() == Light.POINT) {
                    l = new Vector3D(light.getLvec().x - p.x, light.getLvec().y - p.y, light.getLvec().z - p.z);
                    l.normalize();
                } else {
                    l = new Vector3D(-light.getLvec().x, -light.getLvec().y, -light.getLvec().z);
                }

                // Check if the surface point is in shadow
                Vector3D pointOffset = new Vector3D(p.x + TINY*l.x, p.y + TINY*l.y, p.z + TINY*l.z);
                Ray shadowRay = new Ray(pointOffset, l);
                if (shadowRay.trace(objects))
                    break;

                float lambert = Vector3D.dot(n,l);
                if (lambert > 0) {
                    if (diffuseReflectionCoefficient > 0) {
                        float diffuse = diffuseReflectionCoefficient *lambert;
                        red += diffuse* intrinsicRed *light.getIr();
                        green += diffuse* intrinsicGreen *light.getIg();
                        blue += diffuse* intrinsicBlue *light.getIb();
                    }
                    if (specularReflectionCoefficient > 0) {
                        lambert *= 2;
                        float spec = v.dot(lambert*n.x - l.x, lambert*n.y - l.y, lambert*n.z - l.z);
                        if (spec > 0) {
                            spec = specularReflectionCoefficient *((float) Math.pow((double) spec, (double) phongExponent));
                            red += spec*light.getIr();
                            green += spec*light.getIg();
                            blue += spec*light.getIb();
                        }
                    }
                }
            }
        }

        // Compute illumination due to reflection
        if (reflectanceCoefficient > 0) {
            float t = v.dot(n);
            if (t > 0) {
                t *= 2;
                Vector3D reflect = new Vector3D(t*n.x - v.x, t*n.y - v.y, t*n.z - v.z);
                Vector3D pointOffset = new Vector3D(p.x + TINY*reflect.x, p.y + TINY*reflect.y, p.z + TINY*reflect.z);
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

        red = (red > 1f) ? 1f : red;
        green = (green > 1f) ? 1f : green;
        blue = (blue > 1f) ? 1f : blue;

        red = (red < 0) ? 0 : red;
        green = (green < 0) ? 0 : green;
        blue = (blue < 0) ? 0 : blue;

        return new Color(red, green, blue);
    }
}

