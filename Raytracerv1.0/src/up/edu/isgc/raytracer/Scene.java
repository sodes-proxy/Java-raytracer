/**
 * [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package up.edu.isgc.raytracer;

import up.edu.isgc.raytracer.lights.Light;
import up.edu.isgc.raytracer.objects.Camera;
import up.edu.isgc.raytracer.objects.Object3D;

import java.util.ArrayList;

/**
 *
 * @author Jafet Rodríguez,sebastián Ochoa Osuna
 */
public class Scene {

    private Camera camera;
    private ArrayList<Object3D> objects;
    private ArrayList<Light> lights;

    /**
     * scene constructor
     */
    public Scene(){
        setObjects(new ArrayList<Object3D>());
        setLights(new ArrayList<Light>());
    }

    /**
     * gets camera
     * @return camera
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     *
     * @param camera camera to be placed in scene
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * inserts new object to array
     * @param object object to add
     */
    public void addObject(Object3D object){
        getObjects().add(object);
    }

    /**
     * gets the array of objects
     * @return array of objects
     */
    public ArrayList<Object3D> getObjects() {
        return objects;
    }

    /**
     *
     * @param objects Array of objects to place in Scene
     */
    public void setObjects(ArrayList<Object3D> objects) {
        this.objects = objects;
    }

    /**
     * gets the array of lights
     * @return array of lights
     */
    public ArrayList<Light> getLights() {
        return lights;
    }

    /**
     *
     * @param lights Array of lights to place in Scene
     */
    public void setLights(ArrayList<Light> lights) {
        this.lights = lights;
    }

    /**
     *
     * @param light light to be added in the array
     */
    public void addLight(Light light){
        getLights().add(light);
    }
}
