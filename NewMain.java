import Raytrace.*;
import Raytrace.Vector3D;

public class NewMain {
    public static void main(String[] args) {
        Scene s = new Scene(600, 600);
        s.setEye(1.5F, 10.5F, -1.5F);
        s.setLookAt(-0.5F, 0F, -0.5F);

        s.addAmbientLight(1.0F, 1.0F, 0.981F);
        s.addAmbientLight(0.9F, 0.9F, 0.9F);
        s.addAmbientLight(0.74F, 0.859F, 0.224F);
        s.addDirectionalLight(0.6F, 0.6F, 0.6F, new Vector3D(-1F, -1F, -1F));


//        # Objects
//        surface 0.2 0.8 0.2 0.5 0.9 0.4 10.0 0 0 1
//        sphere -0.4 0.375 -0.4 0.375
//
//        surface 0.7 0.3 0.2 0.5 0.9 0.4 6.0 0 0 1
//        sphere -0.6 1.05 -0.6 0.3
//
//        surface 0.2 0.3 0.8 0.5 0.9 0.4 10.0 0 0 1
//        sphere -0.8 1.575 -0.8 0.125
//
//        surface 0.5 0.5 0.8 0.5 0.9 0.4 10.0 0 0 1
//        sphere -1.2 2.575 -0.8 0.1

    }
}
