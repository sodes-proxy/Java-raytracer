/**
 * [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package up.edu.isgc.raytracer.lights;

import up.edu.isgc.raytracer.Intersection;
import up.edu.isgc.raytracer.Vector3D;

import java.awt.*;

/**
 * @author Jafet Rodríguez, Sebastián Ochoa Osuna
 */
public class PointLight extends Light {

    /**
     * PointLight Constructor
     * @param position light position
     * @param color light color
     * @param intensity light intensity
     */
    public PointLight(Vector3D position, Color color, double intensity) {
        super(position, color, intensity);
    }

    /**
     * Gets dotporduct of Normal of object and Light
     * @param intersection intersection between object and light
     * @return dot product
     */
    @Override
    public float getNDotL(Intersection intersection) {
        return (float) Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.normalize(Vector3D.substract(getPosition(), intersection.getPosition()))), 0.0);
    }
}
