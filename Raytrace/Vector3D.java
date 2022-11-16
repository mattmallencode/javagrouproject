package Raytrace;

public class Vector3D {
    private float x, y, z;

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    /**
     * Vector3D constructor.
     *
     * @param x the x position of the vector.
     * @param y the y position of the vector.
     * @param z the z position of the vector.
     */
    public Vector3D(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
    }

    /**
     * Vector3D constructor.
     *
     * @param vector Vector3D object with x, y, and z coordinates.
     */
    public Vector3D(Vector3D vector) {
        x = vector.x;
        y = vector.y;
        z = vector.z;
    }

    /**
     * Dot multiplication method.
     *
     * @param B the other vector you want to multiply this vector with.
     *
     * @return the coordinates of this vector dot multiplied with the coordinates of the other vector.
     */
    public final float dotMultiplication(Vector3D B) {
        return (x*B.x + y*B.y + z*B.z);
    }

    /**
     * Dot multiplication method.
     *
     * @param Bx an x coordinate of another Vector3D instance.
     * @param By a y coordinate of another Vector3D instance.
     * @param Bz a z coordinate of another Vector3D instance.
     *
     * @return the coordinates of this vector dot multiplied with the input coordinates.
     */
    public final float dotMultiplication(float Bx, float By, float Bz) {
        return (x*Bx + y*By + z*Bz);
    }

    /**
     * Dot multiplication method.
     *
     * @param A First Vector3D instance.
     * @param B Second Vector3D instance.
     *
     * @return the coordinates of both vectors dot multiplied.
     */
    public static float dotMultiplication(Vector3D A, Vector3D B) {
        return (A.x*B.x + A.y*B.y + A.z*B.z);
    }

    /**
     * Cross product method.
     *
     * @param B another Vector3D instance.
     *
     * @return the cross product of this vector and vector B.
     */
    public final Vector3D cross(Vector3D B) {
        return new Vector3D(y*B.z - z*B.y, z*B.x - x*B.z, x*B.y - y*B.x);
    }

    /**
     * Cross product method.
     *
     * @param Bx x coordinate of another vector.
     * @param By y coordinate of another vector.
     * @param Bz z coordinate of another vector.
     *
     * @return the cross product of this vector's coordinates and the coordinates passed as input.
     */
    public final Vector3D cross(float Bx, float By, float Bz) {
        return new Vector3D(y*Bz - z*By, z*Bx - x*Bz, x*By - y*Bx);
    }

    /**
     * Cross product method.
     *
     * @param A first Vector3D instance.
     * @param B second Vector3D instance.
     *
     * @return the cross product of the two Vector3D instances.
     */
    public static Vector3D cross(Vector3D A, Vector3D B) {
        return new Vector3D(A.y*B.z - A.z*B.y, A.z*B.x - A.x*B.z, A.x*B.y - A.y*B.x);
    }

    /**
     * Get the length of this vector.
     *
     * @return the length of this vector.
     */
    public final float length( ) {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    /**
     * Get the length of vector A.
     *
     * @param A Vector3D instance.
     *
     * @return the length of Vector3D A.
     */
    static float length(Vector3D A) {
        return (float) Math.sqrt(A.x*A.x + A.y*A.y + A.z*A.z);
    }

    /**
     * Function to normalize this vector.
     */
    final void normalize( ) {
        float t = x*x + y*y + z*z;
        if (t != 0 && t != 1) t = (float) (1 / Math.sqrt(t));
        x *= t;
        y *= t;
        z *= t;
    }

    /**
     * Function to normalize the vector A.
     *
     * @param A Vector3D instance.
     *
     * @return the Vector3D A, now normalized.
     */
    static Vector3D normalize(Vector3D A) {
        float t = A.x*A.x + A.y*A.y + A.z*A.z;
        if (t != 0 && t != 1) t = (float)(1 / Math.sqrt(t));
        return new Vector3D(A.x*t, A.y*t, A.z*t);
    }

    /**
     * Return the string representation of this class instance.
     *
     * @return the string representation of this instance of the class.
     */
    public String toString() {
        return new String("["+x+", "+y+", "+z+"]");
    }
}