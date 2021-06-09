/**
 * [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package up.edu.isgc.raytracer;

/**
 *
 * @author Jafet Rodríguez,sebastián Ochoa
 */
public class Ray {

    private Vector3D origin;
    private Vector3D direction;

    /**
     * Ray constructor
     * @param origin origin position
     * @param direction direction position
     */
    public Ray(Vector3D origin, Vector3D direction) {
        setOrigin(origin);
        setDirection(direction);
    }

    /**
     * gets origin position
     * @return
     */
    public Vector3D getOrigin() {
        return origin;
    }

    /**
     *
     * @param origin sets origin position
     */
    public void setOrigin(Vector3D origin) {
        this.origin = origin;
    }

    /**
     * gets direction position
     * @return
     */
    public Vector3D getDirection() {
        return Vector3D.normalize(direction);
    }

    /**
     *
     * @param direction sets direction position
     */
    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }
}
