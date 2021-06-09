/**
 * [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package up.edu.isgc.raytracer.lights;

import up.edu.isgc.raytracer.Intersection;
import up.edu.isgc.raytracer.Vector3D;

import java.awt.*;

/**
 *
 * @author Jafet Rodríguez,Sebastián Ochoa Osuna
 */
public class DirectionalLight extends Light {
    private Vector3D direction;

    /**
     * Directional Light constructor
     * @param position Directional light position
     * @param direction Directional light direction
     * @param color Directional light color
     * @param intensity Directional light intensity
     */
    public DirectionalLight(Vector3D position, Vector3D direction, Color color, double intensity){
        super(position, color, intensity);
        setDirection(Vector3D.normalize(direction));
    }

    /**
     * gets direction position
     * @return
     */
    public Vector3D getDirection() {
        return direction;
    }

    /**
     *
     * @param direction direction to set
     */
    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    /**
     * Gets dotporduct of Normal of object and Light
     * @param intersection intersection between object and light
     * @return dot product
     */
    @Override
    public float getNDotL(Intersection intersection) {
        return (float)Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.scalarMultiplication(getDirection(), -1.0)), 0.0);
    }
}
