/**
 * [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package up.edu.isgc.raytracer.objects;

import up.edu.isgc.raytracer.IIntersectable;
import up.edu.isgc.raytracer.Vector3D;

import java.awt.*;

/**
 *
 * @author Jafet , Sebastián Ochoa Osuna
 */
public abstract class Object3D implements IIntersectable {

    private Vector3D position;
    private Color color;

    /**
     * Getters and setters of 3D Object
     * **/
    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Object 3D constructor
     * @param position position of 3D object
     * @param color 3D object color
     */
    public Object3D(Vector3D position, Color color) {
        setPosition(position);
        setColor(color);
    }

}
