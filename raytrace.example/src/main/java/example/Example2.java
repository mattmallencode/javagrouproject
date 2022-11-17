package example;

import raytrace.Scene;
import raytrace.Surface;
import raytrace.Vector3D;

import java.io.IOException;

public class Example2 {
    public static void main(String[] args) throws IOException {
        Scene s = new Scene(6000, 6000);
        s.setEye(new Vector3D(1.5F, 10.5F, -1.5F));
        s.setLookAt(new Vector3D(-0.5F, 0F, -0.5F));
        s.setFov(15);
        s.setBackground(0.9F, 0.9F, 0.9F);

        s.addAmbientLight(1.0F, 1.0F, 0.981F);
        s.addAmbientLight(0.9F, 0.9F, 0.9F);
        s.addAmbientLight(0.745F, 0.859F, 0.224F);
        s.addDirectionalLight(0.6F, 0.6F, 0.6F, new Vector3D(-1F, -1F, -1F));
        s.addPointLight(0.6F, 0.6F, 0.3F, new Vector3D(2F, 2F, -1F));


        s.addSphere(new Surface(0.2F, 0.3F, 0.2F, 0.5F, 0.9F, 0.4F, 6.0F, 0F, 0F, 1F),
                new Vector3D(0.6F, -1.05F, -0.6F), 0.43F);
        s.addSphere(new Surface(0.7F, 0.3F, 0.2F, 0.5F, 0.9F, 0.4F, 6.0F, 0F, 0F, 1F),
                new Vector3D(-0.6F, 1.05F, -0.6F), 0.3F);
        s.addSphere(new Surface(0.2F, 0.3F, 0.8F, 0.5F, 0.9F, 0.4F, 10.0F, 0F, 0F, 1F),
                new Vector3D(-0.8F, 1.575F, -0.8F), 0.125F);
        s.addSphere(new Surface(0.5F, 0.5F, 0.8F, 0.5F, 0.9F, 0.4F, 10.0F, 0F, 0F, 1F),
                new Vector3D(-1.2F, 2.575F, -0.8F), 0.1F);


        s.renderImage();
        s.saveRenderedImage("out.png");


    }
}
