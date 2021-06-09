package up.edu.isgc.raytracer.objects;

import up.edu.isgc.raytracer.Intersection;
import up.edu.isgc.raytracer.Ray;
import up.edu.isgc.raytracer.Vector3D;

import java.awt.*;

public class RefractionPlane extends Object3D {
    /**
     * Object 3D constructor
     *
     * @param position position of 3D object
     * @param color    3D object color
     */
    public RefractionPlane(Vector3D position, Color color) {
        super(position, color);
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        return null;
    }
}
