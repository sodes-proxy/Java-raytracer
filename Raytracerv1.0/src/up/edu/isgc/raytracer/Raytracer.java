/**
 * [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package up.edu.isgc.raytracer;

import javafx.scene.effect.ColorInput;
import up.edu.isgc.raytracer.lights.DirectionalLight;
import up.edu.isgc.raytracer.lights.Light;
import up.edu.isgc.raytracer.lights.PointLight;
import up.edu.isgc.raytracer.objects.*;
import up.edu.isgc.raytracer.tools.OBJReader;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Jafet Rodríguez,Sebastián Ochoa Osuna
 */
public class Raytracer {

    public static void main(String[] args) {
        System.out.println(new Date());
        Scene scene01 = new Scene();
        scene01.setCamera(new Camera(new Vector3D(0, 0, -10), 160, 160, 1200, 1200, 8.2f, 50f));
        //Refraccion
        /*scene01.addLight(new PointLight(new Vector3D(0, 1f, 1f), Color.white, 3f));
        scene01.addLight(new PointLight(new Vector3D(0, 0f, 0f), Color.green, 2f));
        scene01.addObject(OBJReader.GetPolygon("Plane.obj",new Vector3D(0f,-3.5f,2f),Color.white));
        scene01.addObject(OBJReader.GetPolygon("lagannR.obj",new Vector3D(0f,-4.5f,5.5f),Color.red));
        scene01.addObject(OBJReader.GetPolygon("smallTeapot.obj",new Vector3D(-1.8f,-2.5f,5.5f),Color.blue));
        scene01.addObject(new Sphere(new Vector3D(.5f, -2.5f, 1.5), 1f, new Color(255,255,255)));
        scene01.addObject(new Sphere(new Vector3D(-1.5, -1.8f, 1.5), 1f, Color.red));*/
        //Reflexion
        /*scene01.addLight(new PointLight(new Vector3D(-0, 3f, 3f), Color.white, 3f));
        scene01.addLight(new PointLight(new Vector3D(0f,0f,0f),Color.green,2f));
        scene01.addObject(new Sphere(new Vector3D(2f, -2.5f, 4.5f), 0.5f, Color.red));
        scene01.addObject(new Sphere(new Vector3D(-1f, -2f, 6.6f), 1.0f, Color.yellow));
        scene01.addObject(OBJReader.GetPolygon("cube.obj",new Vector3D(-1f,-3.6f,1f),Color.white));
        scene01.addObject(OBJReader.GetPolygon("lagannR.obj",new Vector3D(1.5,-6f,0.3f),Color.red));
        scene01.addObject(OBJReader.GetPolygon("smallTeapot.obj",new Vector3D(4f,-3.5f,5f),Color.pink));
        scene01.addObject(OBJReader.GetPolygon("Plane.obj",new Vector3D(0f,-3.5f,2f),Color.white));
        scene01.addObject(OBJReader.GetPolygon("smallTeapot.obj", new Vector3D(2f, -3f, 7f), Color.BLUE));*/
        //ambas
        /*scene01.addLight(new PointLight(new Vector3D(-0, 3f, 3f), Color.white, 3f));
        scene01.addLight(new PointLight(new Vector3D(0f,0f,0f),Color.green,2f));
        scene01.addLight(new PointLight(new Vector3D(-1.1f,-2f,0f),Color.red,.4f));
        scene01.addObject(new Sphere(new Vector3D(2f, -2.5f, 4.5f), 0.5f, Color.red));
        scene01.addObject(new Sphere(new Vector3D(-1f, -2f, 6.6f), 1.0f, Color.yellow));
        scene01.addObject(OBJReader.GetPolygon("cube.obj",new Vector3D(-1f,-3.6f,1f),Color.white));
        scene01.addObject(OBJReader.GetPolygon("lagannR.obj",new Vector3D(1.5,-6f,0.3f),Color.red));
        scene01.addObject(OBJReader.GetPolygon("smallTeapot.obj",new Vector3D(4f,-3.5f,5f),Color.pink));
        scene01.addObject(OBJReader.GetPolygon("Plane.obj",new Vector3D(0f,-3.5f,2f),Color.white));
        scene01.addObject(OBJReader.GetPolygon("smallTeapot.obj", new Vector3D(2f, -3f, 7f), Color.BLUE));*/

        BufferedImage image = raytrace(scene01);
        File outputImage = new File("hola1.png");
        try {
            ImageIO.write(image, "png", outputImage);
        } catch (IOException ioe) {
            System.out.println("Something failed");
        }
        System.out.println(new Date());
    }

    /**
     * Simulates light effects over 3D objects in a space to then render in an image
     * @param scene Space where all objects will be placed
     * @return Rendered image
     */
    public static BufferedImage raytrace(Scene scene) {
        ExecutorService es= Executors.newFixedThreadPool(12);
        Camera mainCamera = scene.getCamera();
        ArrayList<Light> lights = scene.getLights();
        float[] nearFarPlanes = mainCamera.getNearFarPlanes();
        BufferedImage image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        ArrayList<Object3D> objects = scene.getObjects();
        Vector3D[][] positionsToRaytrace = mainCamera.calculatePositionsToRay();
        for (int i = 0; i < positionsToRaytrace.length; i++) {
            for (int j = 0; j < positionsToRaytrace[i].length; j++) {
                Runnable runnable=draw(positionsToRaytrace,mainCamera,objects,nearFarPlanes,image,lights,i,j);
                es.execute(runnable);
            }
        }
        es.shutdown();
        try {
            if(es.awaitTermination(10, TimeUnit.MINUTES)){
                es.shutdownNow();
            }
        }catch (InterruptedException e){

        }finally {
            if(!es.isTerminated()){
                System.err.println("Cancel non-finished");
            }
            es.shutdownNow();
        }

        return image;
    }

    /**
     * Thread function, raytraces the scene to calculate the pixel color value to simulate the effect of lights
     * @param positionsToRaytrace All positions in scene
     * @param mainCamera Viewer that casts rays
     * @param objects all 3D objects in scene
     * @param nearFarPlanes clipping planes
     * @param image buffered image to be rendered
     * @param lights all lights in scene
     * @param i horizontal pixel coordinate
     * @param j vertical pixel coordinate
     * @return
     */
    private static Runnable draw(Vector3D[][] positionsToRaytrace, Camera mainCamera, ArrayList<Object3D> objects, float[] nearFarPlanes, BufferedImage image, ArrayList<Light> lights, int i, int j) {
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                double x = positionsToRaytrace[i][j].getX() + mainCamera.getPosition().getX();
                double y = positionsToRaytrace[i][j].getY() + mainCamera.getPosition().getY();
                double z = positionsToRaytrace[i][j].getZ() + mainCamera.getPosition().getZ();

                Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));
                float cameraZ = (float) mainCamera.getPosition().getZ();
                Intersection closestIntersection = raycast(ray, objects, null, new float[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]});
                //Background color
                Color pixelColor = Color.BLACK;
                if (closestIntersection != null) {
                    pixelColor = Color.BLACK;
                    for (Light light : lights) {
                        Vector3D L=Vector3D.normalize(light.getPosition());
                        Ray ray2= new Ray(Vector3D.add(Vector3D.scalarMultiplication(closestIntersection.getNormal(),.0001),closestIntersection.getPosition()),light.getPosition());
                        Intersection shadowIntersection=raycast(ray2,objects,null,new float[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]});
                        //specular
                        double specular=getSpecular(mainCamera,closestIntersection,L);
                        //Reflection
                        double nI=Vector3D.dotProduct(closestIntersection.getNormal(),L);
                        Vector3D R=Vector3D.substract(L,Vector3D.scalarMultiplication(closestIntersection.getNormal(),nI*2));
                        Ray r= new Ray(closestIntersection.getPosition(),R);
                        Intersection reflectIntersection=raycast(r,objects,closestIntersection.getObject(),new float[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]});

                        float nDotL = light.getNDotL(closestIntersection);
                        float intensity = (float) light.getIntensity() * nDotL;
                        float distance= getDistance(light,closestIntersection);
                        Color lightColor = light.getColor();
                        Color objColor = closestIntersection.getObject().getColor();

                        float[] lightColors = new float[]{lightColor.getRed() / 255.0f, lightColor.getGreen() / 255.0f, lightColor.getBlue() / 255.0f};
                        float[] objColors = new float[]{objColor.getRed() / 255.0f, objColor.getGreen() / 255.0f, objColor.getBlue() / 255.0f};
                        float[] ambientColors=objColors.clone();
                        float[] specularColors=objColors.clone();

                        //Blinn-Phong calculations
                        for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                            objColors[colorIndex] *= (intensity/distance) * lightColors[colorIndex];
                            ambientColors[colorIndex]*=.1;
                            specularColors[colorIndex]*=specular*intensity*lightColors[colorIndex]*5;
                        }

                        Color diffuse = new Color(clamp(objColors[0], 0, 1),clamp(objColors[1], 0, 1),clamp(objColors[2], 0, 1));
                        Color ambient= new Color(clamp(ambientColors[0],0,1),clamp(ambientColors[1],0,1),clamp(ambientColors[2],0,1));
                        Color specularColor= new Color(clamp(specularColors[0],0,1),clamp(specularColors[1],0,1),clamp(specularColors[2],0,1));

                        pixelColor=addColor(pixelColor,ambient);
                        if(shadowIntersection==null){
                            pixelColor = addColor(pixelColor, diffuse);
                            pixelColor=addColor(pixelColor,specularColor);
                        }
                        if(reflectIntersection!=null){
                            //Reflection calculations
                            pixelColor=setLayer(reflectIntersection,intensity,distance,lightColors,specular,pixelColor);
                        }
                        //refraction calculations
                        Intersection refractionintersection=null;
                        Vector3D camera=Vector3D.substract(closestIntersection.getPosition(),mainCamera.getPosition());
                        Ray Refraction=Refraction(closestIntersection.getPosition(),camera,closestIntersection.getNormal());
                        if(Refraction!=null){
                            refractionintersection=raycast(Refraction,objects,null,new float[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]});
                        }
                        if(refractionintersection!=null){
                            pixelColor=addColor(pixelColor,refractionintersection.getObject().getColor());
                        }
                    }
                }

            setRGB(image,i, j, pixelColor);
            }
        };
        return runnable;
    }

    /**
     * Synchronizes the threads drawing the image to avoid incorrect coloring
     * @param image buffered image to draw
     * @param x horizontal position
     * @param y vertical position
     * @param pixelColor wanted color for pixel
     */
    public static synchronized void setRGB(BufferedImage image, int x, int y, Color pixelColor){
        image.setRGB(x,y,pixelColor.getRGB());
    }

    /**
     * Restricts that the value given is in range of the min and max
     * @param value number that needs the restriction
     * @param min bottom of range
     * @param max top of range
     * @return clamped value
     */
    public static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * Calculates the distance between light and intersection
     * @param light point light
     * @param closestIntersection intersection area
     * @return distance between the two
     */
    private static float getDistance(Light light,Intersection closestIntersection){
        Vector3D distance= Vector3D.substract(light.getPosition(),closestIntersection.getObject().getPosition());
        return (float) Vector3D.magnitude(distance);
    }

    /**
     * adds two colors
     * @param original color 1
     * @param otherColor color 2
     * @return new color
     */
    public static Color addColor(Color original, Color otherColor){
        float red = clamp((original.getRed() / 255.0f) + (otherColor.getRed() / 255.0f), 0, 1);
        float green = clamp((original.getGreen() / 255.0f) + (otherColor.getGreen() / 255.0f), 0, 1);
        float blue = clamp((original.getBlue() / 255.0f) + (otherColor.getBlue() / 255.0f), 0, 1);
        return new Color(red, green, blue);
    }
    private static Color setLayer(Intersection intersection,float intensity,float distance,float [] lightColors,double specular,Color pixelColor){
        Color ColorR = intersection.getObject().getColor();
        float[] ColorRA = new float[]{ColorR.getRed() / 255.0f, ColorR.getGreen() / 255.0f, ColorR.getBlue() / 255.0f};
        float[] ambientR=ColorRA.clone();
        float[] specularR=ColorRA.clone();
        for (int colorIndex = 0; colorIndex < ColorRA.length; colorIndex++) {
            ColorRA[colorIndex] *= (intensity/distance) * lightColors[colorIndex];
            ambientR[colorIndex]*=.1;
            specularR[colorIndex]*=specular*intensity*lightColors[colorIndex];
        }
        Color diffuseR = new Color(clamp(ColorRA[0], 0, 1),clamp(ColorRA[1], 0, 1),clamp(ColorRA[2], 0, 1));
        Color ambientColorR= new Color(clamp(ambientR[0],0,1),clamp(ambientR[1],0,1),clamp(ambientR[2],0,1));
        Color specularColorR= new Color(clamp(specularR[0],0,1),clamp(specularR[1],0,1),clamp(specularR[2],0,1));
        pixelColor=addColor(pixelColor,ambientColorR);
        pixelColor=addColor(pixelColor,diffuseR);
        pixelColor=addColor(pixelColor,specularColorR);
        return pixelColor;
    }

    /**
     * Calculates specular value
     * @param camera main camera
     * @param intersection
     * @param Light
     * @return
     */
    private static double getSpecular(Camera camera, Intersection intersection, Vector3D Light){
        Vector3D V=Vector3D.normalize(camera.getPosition());
        Vector3D halfVector=Vector3D.normalize(Vector3D.add(Light,V));
        double specular=Math.pow(Vector3D.dotProduct(intersection.getNormal(),halfVector),1000);
        return specular*5;
    }

    /**
     * Cast rays from the camera and calculate the intersections between the objects in the scene and the rays casted
     * @param ray
     * @param objects Array of objects in the scene
     * @param caster Camera
     * @param clippingPlanes Planes that delimit the render space
     * @return The closest intersection between ray and object
     */
    public static Intersection raycast(Ray ray, ArrayList<Object3D> objects, Object3D caster, float[] clippingPlanes) {
        Intersection closestIntersection = null;

        for (int k = 0; k < objects.size(); k++) {
            Object3D currentObj = objects.get(k);
            if (caster == null || !currentObj.equals(caster)) {
                Intersection intersection = currentObj.getIntersection(ray);
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    if (distance >= 0 &&
                            (closestIntersection == null || distance < closestIntersection.getDistance()) &&
                            (clippingPlanes == null || (intersection.getPosition().getZ() >= clippingPlanes[0] &&
                                    intersection.getPosition().getZ() <= clippingPlanes[1]))) {
                        closestIntersection = intersection;
                    }
                }
            }
        }

        return closestIntersection;
    }

    /**
     * Calculates Refraction vector and then creates a ray with position of object intersection position to the
     * direction of the other side of the figure
     * @param position object intersection position
     * @param Camera Viewer vector
     * @param normal object intersection normal
     * @return refraction ray
     */
    public static Ray Refraction(Vector3D position,Vector3D Camera,Vector3D normal){
        float cosi = clamp((float) Vector3D.dotProduct(normal,Vector3D.normalize(Camera)), -1, 1);
        float etai = 1f, etat = .7f;
        if(cosi<0)cosi=-cosi;
        else{
            float tmp=etai;
            etai=etat;
            etat=tmp;
            normal=Vector3D.scalarMultiplication(normal,-1);
        }
        float eta=etai/etat;
        double RefractionConstant=1-eta*eta*(1-cosi*cosi);
        if(RefractionConstant<0)return null;
        Vector3D refraction=Vector3D.add(Vector3D.scalarMultiplication(Camera,eta),Vector3D.scalarMultiplication(normal,(eta*cosi-Math.sqrt(RefractionConstant))));
        position=Vector3D.add(position,Vector3D.scalarMultiplication(refraction,.01));
        return new Ray(position,refraction);
    }

}
