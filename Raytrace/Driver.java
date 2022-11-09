package Raytrace;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

class Driver {
	final static int CHUNKSIZE = 100;
	static List<Object> objectList;
	static List<Object> lightList;
	static Surface currentSurface;
	static BufferedImage canvas;
	static Vector3D eye, lookAt, up;
	static Vector3D Du, Dv, Vp;
	static float fov;
	static Color background;
	int width, height;

	public Driver(int width, int height, String dataFile) {
		this.width = width;
		this.height = height;
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		fov = 30; // default horizontal field of view
		// Initialize various lists
		objectList = new ArrayList<>(CHUNKSIZE);
		lightList = new ArrayList<>(CHUNKSIZE);
		currentSurface = new Surface(0.8f, 0.2f, 0.9f, 0.2f, 0.4f, 0.4f, 10.0f, 0f, 0f, 1f);
		// Parse the scene file
		String filename = dataFile != null ? dataFile : "defaultScene.txt";
		// Initialize more defaults if they weren't specified
		if (eye == null)
			eye = new Vector3D(0, 0, 10);
		if (lookAt == null)
			lookAt = new Vector3D(0, 0, 0);
		if (up == null)
			up = new Vector3D(0, 1, 0);
		if (background == null)
			background = new Color(0, 0, 0);
		// Compute viewing matrix that maps a
		// screen coordinate to a ray direction
		Vector3D look = new Vector3D(lookAt.x - eye.x, lookAt.y - eye.y, lookAt.z - eye.z);
		Du = Vector3D.normalize(look.cross(up));
		Dv = Vector3D.normalize(look.cross(Du));
		float fl = (float) (width / (2 * Math.tan((0.5 * fov) * Math.PI / 180)));
		Vp = Vector3D.normalize(look);
		Vp.x = Vp.x * fl - 0.5f * (width * Du.x + height * Dv.x);
		Vp.y = Vp.y * fl - 0.5f * (width * Du.y + height * Dv.y);
		Vp.z = Vp.z * fl - 0.5f * (width * Du.z + height * Dv.z);
	}

	double getNumber(StreamTokenizer st) throws IOException {
		if (st.nextToken() != StreamTokenizer.TT_NUMBER) {
			System.err.println("ERROR: number expected in line " + st.lineno());
			throw new IOException(st.toString());
		}
		return st.nval;
	}

	/**
	 * add a new sphere to the scene
	 *
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
	 * Set the position of the eye.
	 *
	 * @param x X-position of the eye
	 * @param y Y-position of the eye
	 * @param z Z-position of the eye
	 */
	public final void setEye(float x, float y, float z) {
		eye = new Vector3D(x, y, z);
	}

	// not sure what lookat is :(
	/**
	 * Set the position of lookAt.
	 *
	 * @param x X-position
	 * @param y Y-position
	 * @param z Z-position
	 */
	public final void setLookAt(float x, float y, float z) {
		lookAt = new Vector3D(x, y, z);
	}

	/**
	 * Set the position of up.
	 *
	 * @param x X-position
	 * @param y Y-position
	 * @param z Z-position
	 */
	public final void setUp(float x, float y, float z) {
		up = new Vector3D(x, y, z);
	}

	/**
	 * Set the field of view.
	 *
	 * @param f fov
	 */
	public final void setFov(float f) {
		fov = f;
	}

	/**
	 * Set the colour of the background.
	 *
	 * @param r Red
	 * @param g Greed
	 * @param b Blue
	 */
	public final void setBackground(float r, float g, float b) {
		background = new Color(r, g, b);
	}

	/**
	 * Add a light to the scene.
	 *
	 * @param r         Red
	 * @param g         Green
	 * @param b         Blue
	 * @param x         X-position
	 * @param y         Y-position
	 * @param z         Z-position
	 * @param lightType "ambient", "directional", or "point"
	 */
	public final void addLight(float r, float g, float b, float x, float y, float z, String lightType) {
		Vector3D v = new Vector3D(x, y, z);
		if (lightType == "ambient") {
			lightList.add(new Light(Light.AMBIENT, null, r, g, b));
		} else if (lightType == "directional") {
			lightList.add(new Light(Light.DIRECTIONAL, v, r, g, b));
		} else if (lightType == "point") {
			lightList.add(new Light(Light.POINT, v, r, g, b));
		} else {
			throw new IllegalArgumentException("lightType can only be: ambient, directional, or point.");
		}
	}

	/**
	 * Changes the current surface of the scene.
	 *
	 * @param r     Red
	 * @param g     Green
	 * @param b     Blue
	 * @param ka    Ambient reflection constant
	 * @param kd    Diffuse reflection constant
	 * @param ks    Specular reflection constant
	 * @param ns    TODO
	 * @param kr    TODO
	 * @param kt    TODO
	 * @param index TODO
	 */
	public final void changeCurrentSurface(float r, float g, float b, float ka, float kd, float ks, float ns, float kr,
										   float kt, float index) {
		currentSurface = new Surface(r, g, b, ka, kd, ks, ns, kr, kt, index);
	}

	Image getRenderedImage() {
		return canvas;
	}

	public void renderPixel(int i, int j) {
		Vector3D dir = new Vector3D(
				i * Du.x + j * Dv.x + Vp.x,
				i * Du.y + j * Dv.y + Vp.y,
				i * Du.z + j * Dv.z + Vp.z);
		Ray ray = new Ray(eye, dir);
		Color pixelColour;

		if (ray.trace(objectList)) {
			Color bg = background;
			pixelColour = ray.Shade(lightList, objectList, bg);
		} else {
			pixelColour = background;
		}
		canvas.setRGB(i, j, pixelColour.getRGB());
	}
}