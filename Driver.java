import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Driver  {
    final static int CHUNKSIZE = 100;
    List<Object> objectList;
    List<Object> lightList;
    Surface currentSurface;
    BufferedImage canvas;

    Vector3D eye, lookAt, up;
    Vector3D dU, dV, vP;
    float fieldOfView;

    Color background;

    int width, height;

    public Driver(int width, int height, String dataFile) {
        this.width = width;
        this.height = height;

        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        fieldOfView = 30;               // default horizonal field of view

        // Initialize various lists
        objectList = new ArrayList<>(CHUNKSIZE);
        lightList = new ArrayList<>(CHUNKSIZE);
        currentSurface = new Surface(0.8f, 0.2f, 0.9f, 0.2f, 0.4f, 0.4f, 10.0f, 0f, 0f, 1f);

        // Parse the scene file
        String filename = dataFile != null ? dataFile : "defaultScene.txt";

        InputStream is = null;
        try {
            is = new FileInputStream(new File(filename));
            ReadInput(is);
            is.close();
        } catch (IOException e) {
            System.err.println("Error reading "+ new File(filename).getAbsolutePath());
            e.printStackTrace();
            System.exit(-1);
        }

        // Initialize more defaults if they weren't specified
        if (eye == null) eye = new Vector3D(0, 0, 10);
        if (lookAt == null) lookAt = new Vector3D(0, 0, 0);
        if (up  == null) up = new Vector3D(0, 1, 0);
        if (background == null) background = new Color(0,0,0);

        // Compute viewing matrix that maps a
        // screen coordinate to a ray direction
        Vector3D look = new Vector3D(lookAt.x - eye.x, lookAt.y - eye.y, lookAt.z - eye.z);
        dU = Vector3D.normalize(look.cross(up));
        dV = Vector3D.normalize(look.cross(dU));
        float fl = (float)(width / (2*Math.tan((0.5* fieldOfView)*Math.PI/180)));
        vP = Vector3D.normalize(look);
        vP.x = vP.x*fl - 0.5f*(width* dU.x + height* dV.x);
        vP.y = vP.y*fl - 0.5f*(width* dU.y + height* dV.y);
        vP.z = vP.z*fl - 0.5f*(width* dU.z + height* dV.z);
    }


    double getNumber(StreamTokenizer st) throws IOException {
        if (st.nextToken() != StreamTokenizer.TT_NUMBER) {
            System.err.println("ERROR: number expected in line "+st.lineno());
            throw new IOException(st.toString());
        }
        return st.nval;
    }

    void ReadInput(InputStream is) throws IOException {
        StreamTokenizer st = new StreamTokenizer(is);
        st.commentChar('#');
        scan: while (true) {
            switch (st.nextToken()) {
                default:
                    break scan;
                case StreamTokenizer.TT_WORD:
                    if (st.sval.equals("sphere")) {
                        Vector3D v = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
                        float r = (float) getNumber(st);
                        objectList.add(new Sphere(currentSurface, v, r));
                    } else
                    if (st.sval.equals("eye")) {
                        eye = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
                    } else
                    if (st.sval.equals("lookat")) {
                        lookAt = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
                    } else
                    if (st.sval.equals("up")) {
                        up = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
                    } else
                    if (st.sval.equals("fov")) {
                        fieldOfView = (float) getNumber(st);
                    } else
                    if (st.sval.equals("background")) {
                        background = new Color((int) getNumber(st), (int) getNumber(st), (int) getNumber(st));
                    } else
                    if (st.sval.equals("light")) {
                        float r = (float) getNumber(st);
                        float g = (float) getNumber(st);
                        float b = (float) getNumber(st);
                        if (st.nextToken() != StreamTokenizer.TT_WORD) {
                            throw new IOException(st.toString());
                        }
                        if (st.sval.equals("ambient")) {
                            lightList.add(new Light(Light.AMBIENT, null, r, g, b));
                        } else
                        if (st.sval.equals("directional")) {
                            Vector3D v = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
                            lightList.add(new Light(Light.DIRECTIONAL, v, r, g, b));
                        } else
                        if (st.sval.equals("point")) {
                            Vector3D v = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
                            lightList.add(new Light(Light.POINT, v, r, g, b));
                        } else {
                            System.err.println("ERROR: in line "+st.lineno()+" at "+st.sval);
                            throw new IOException(st.toString());
                        }
                    } else
                    if (st.sval.equals("surface")) {
                        float r = (float) getNumber(st);
                        float g = (float) getNumber(st);
                        float b = (float) getNumber(st);
                        float ka = (float) getNumber(st);
                        float kd = (float) getNumber(st);
                        float ks = (float) getNumber(st);
                        float ns = (float) getNumber(st);
                        float kr = (float) getNumber(st);
                        float kt = (float) getNumber(st);
                        float index = (float) getNumber(st);
                        currentSurface = new Surface(r, g, b, ka, kd, ks, ns, kr, kt, index);
                    }
                    break;
            }
        }
        is.close();
        if (st.ttype != StreamTokenizer.TT_EOF)
            throw new IOException(st.toString());
    }

    Image getRenderedImage() {
        return canvas;
    }

    public void renderPixel(int i, int j) {
        Vector3D dir = new Vector3D(
                i* dU.x + j* dV.x + vP.x,
                i* dU.y + j* dV.y + vP.y,
                i* dU.z + j* dV.z + vP.z);
        Ray ray = new Ray(eye, dir);
        Color pixelColour;

        if (ray.trace(objectList)) {
            java.awt.Color bg = background;
            pixelColour = ray.Shade(lightList, objectList, bg);
        } else {
            pixelColour = background;
        }
        canvas.setRGB(i, j, pixelColour.getRGB());
    }
}