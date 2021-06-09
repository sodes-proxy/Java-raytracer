/**
 * [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package up.edu.isgc.raytracer;

/**
 *
 * @author Jafet Rodríguez, Sebastián Ochoa Osuna
 */
public class Vector3D {

    private static final Vector3D ZERO = new Vector3D(0.0, 0.0, 0.0);
    private double x, y, z;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Vector 3D constructor
     * @param x
     * @param y
     * @param z
     */
    public Vector3D(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    /**
     * calculates dot product of two vectors
     * @param vectorA first vector
     * @param vectorB second vector
     * @return dot product
     */
    public static double dotProduct(Vector3D vectorA, Vector3D vectorB){
        return (vectorA.getX() * vectorB.getX()) + (vectorA.getY() * vectorB.getY()) + (vectorA.getZ() * vectorB.getZ());
    }

    /**
     * calculates new vector by the cross product operation
     * @param vectorA first vector
     * @param vectorB second vector
     * @return ortogonal vector
     */
    public static Vector3D crossProduct(Vector3D vectorA, Vector3D vectorB){
        return new Vector3D((vectorA.getY() * vectorB.getZ()) - (vectorA.getZ() * vectorB.getY()),
                (vectorA.getZ() * vectorB.getX()) - (vectorA.getX() * vectorB.getZ()),
                (vectorA.getX() * vectorB.getY()) - (vectorA.getY() * vectorB.getX()));
    }

    /**
     * calculates vector's magnitude
     * @param vectorA vector to calculate
     * @return magnitude value
     */
    public static double magnitude(Vector3D vectorA){
        return Math.sqrt(dotProduct(vectorA, vectorA));
    }

    /**
     * adds two vectors
     * @param vectorA first vector
     * @param vectorB second vector
     * @return new vector
     */
    public static Vector3D add(Vector3D vectorA, Vector3D vectorB){
        return new Vector3D(vectorA.getX() + vectorB.getX(), vectorA.getY() + vectorB.getY(), vectorA.getZ() + vectorB.getZ());
    }

    /**
     * substract two vectors
     * @param vectorA first vector
     * @param vectorB second vector
     * @return new vector
     */
    public static Vector3D substract(Vector3D vectorA, Vector3D vectorB){
        return new Vector3D(vectorA.getX() - vectorB.getX(), vectorA.getY() - vectorB.getY(), vectorA.getZ() - vectorB.getZ());
    }

    /**
     * normalizes a vector
     * @param vectorA vector to normalize
     * @return normalized vector
     */
    public static Vector3D normalize(Vector3D vectorA){
        double mag = Vector3D.magnitude(vectorA);
        return new Vector3D(vectorA.getX() / mag, vectorA.getY() / mag, vectorA.getZ() / mag);
    }

    /**
     * multiplies vector components by a number
     * @param vectorA vector to multiply
     * @param scalar multiplicative value
     * @return new vector
     */
    public static Vector3D scalarMultiplication(Vector3D vectorA, double scalar){
        return new Vector3D(vectorA.getX() * scalar, vectorA.getY() * scalar, vectorA.getZ() * scalar);
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
    }

    public Vector3D clone(){
        return new Vector3D(getX(), getY(), getZ());
    }

    public static Vector3D ZERO(){
        return ZERO.clone();
    }
}
