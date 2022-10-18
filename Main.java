import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    JFrame frame;
    ImagePanel image;
    Driver sceneToRender;

    public static void main(String[] args) throws IOException {
        new Main().startRendering();
    }

    void startRendering() throws IOException {
        Dimension displaySize = new Dimension(600, 600);
        sceneToRender = new Driver(600, 600,"resources/SceneToRender.txt");

        frame = new JFrame("Ray Tracing Demonstration");
        frame.setSize(600, 600);
        frame.setPreferredSize(displaySize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImagePanel image = new ImagePanel("resources/Placeholder-01.png");

        frame.add(image);
        frame.pack();
        frame.setVisible(true);

        long time = System.currentTimeMillis();
        for (int j = 0; j < sceneToRender.height; j += 1) {
            for (int i = 0; i < sceneToRender.width; i += 1) {
                sceneToRender.renderPixel(i, j);
            }
        }
        image.updateImage(sceneToRender.getRenderedImage());

        time = System.currentTimeMillis() - time;
        System.err.println("Rendered in "+(time/60000)+" minutes : "+((time%60000)*0.001) + " seconds");
    }
}
