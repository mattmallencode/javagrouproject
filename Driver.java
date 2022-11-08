import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Driver  {
    final static int CHUNKSIZE = 100;
	static List<Object> objectList;
	static List<Object> lightList;
	static Surface currentSurface;
	static BufferedImage canvas;
	static Vector3D eye, lookat, up;
	static Vector3D Du, Dv, Vp;
	static float fov;
	static Color background;
	int width, height;

    public Driver(int width, int height, String dataFile) {
        this.width = width;
        this.height = height;
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        fov = 30;               // default horizonal field of view
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
        if (lookat == null) lookat = new Vector3D(0, 0, 0);
        if (up  == null) up = new Vector3D(0, 1, 0);
        if (background == null) background = new Color(0,0,0);

        // Compute viewing matrix that maps a
        // screen coordinate to a ray direction
        Vector3D look = new Vector3D(lookat.x - eye.x, lookat.y - eye.y, lookat.z - eye.z);
        Du = Vector3D.normalize(look.cross(up));
        Dv = Vector3D.normalize(look.cross(Du));
        float fl = (float)(width / (2*Math.tan((0.5*fov)*Math.PI/180)));
        Vp = Vector3D.normalize(look);
        Vp.x = Vp.x*fl - 0.5f*(width*Du.x + height*Dv.x);
        Vp.y = Vp.y*fl - 0.5f*(width*Du.y + height*Dv.y);
        Vp.z = Vp.z*fl - 0.5f*(width*Du.z + height*Dv.z);
    }


    double getNumber(StreamTokenizer st) throws IOException {
        if (st.nextToken() != StreamTokenizer.TT_NUMBER) {
            System.err.println("ERROR: number expected in line "+st.lineno());
            throw new IOException(st.toString());
        }
        return st.nval;
    }

	/**
	 * add a new sphere to the driver
	 * @param x X-position of the sphere
	 * @param y Y-position of the sphere
	 * @param z Z-position of the sphere
	 * @param r radius of the sphere
	 */
	public final void addSphere(float x, float y, float z, float r) {
		Vector3D v = new Vector3D(x, y, z);
		objectList.add(new Sphere(currentSurface, v, r));
	}

	/**
	 * set the position of the eye
	 * @param x X-position of the eye
	 * @param y Y-position of the eye
	 * @param z Z-position of the eye
	 */
	public final void setEye(float x, float y, float z) {
		eye = new Vector3D(x, y, z);
	}
	// not sure what lookat is :(
	/**
	 * set the position of lookat
	 * @param x X-position
	 * @param y Y-position
	 * @param z Z-position
	 */
	public final void setLookat(float x, float y, float z) {
		lookat = new Vector3D(x, y, z);
	}
	/**
	 * set the position of up
	 * @param x X-position
	 * @param y Y-position
	 * @param z Z-position
	 */
	public final void setUp(float x, float y, float z) {
        up = new Vector3D(x, y, z);
    }
	/**
	 * set the field of view
	 * @param f fov
	 */
	public final void setFov(float f) {
            fov = f;
    }
	/**
	 * set the colour of the background
	 * @param r red
	 * @param g greed
	 * @param b blue
	 */
	public final void setBackground(int r, int g, int b) {
        background = new Color(r, g, b);
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
		            lookat = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
			    } else
			    if (st.sval.equals("up")) {
		            up = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
			    } else
			    if (st.sval.equals("fov")) {
                    fov = (float) getNumber(st);
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
				i*Du.x + j*Dv.x + Vp.x,
				i*Du.y + j*Dv.y + Vp.y,
				i*Du.z + j*Dv.z + Vp.z);
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