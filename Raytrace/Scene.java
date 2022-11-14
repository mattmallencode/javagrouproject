package Raytrace;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Scene {
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

	public Scene(int width, int height) {
		this.width = width;
		this.height = height;
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		fov = 30; // default horizontal field of view

		eye = new Vector3D(0, 0, 10);
		lookAt = new Vector3D(0, 0, 0);
		up = new Vector3D(0, 1, 0);
		background = new Color(0, 0, 0);

		// Initialize various lists
		objectList = new ArrayList<>(CHUNKSIZE);
		lightList = new ArrayList<>(CHUNKSIZE);
		currentSurface = new Surface(0.8f, 0.2f, 0.9f, 0.2f, 0.4f, 0.4f, 10.0f, 0f, 0f, 1f);

		// Compute viewing matrix that maps a screen coordinate to a ray direction
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
	 * @param v     direction:Vector3D {@link Vector3D}
	 * @param r 	radius of the sphere
	 */
	public final void addSphere(Surface s,Vector3D v,  float r) {
		objectList.add(new Sphere(s, v, r));
	}

	/**
	 * Set the position of the eye.
	 *
	 * @param v     direction:Vector3D {@link Vector3D}
	 */
	public final void setEye(Vector3D v) {
		eye = v;
	}

	// not sure what lookat is :(
	/**
	 * Set the position of lookAt.
	 *
	 * @param v     direction:Vector3D {@link Vector3D}
	 */
	public final void setLookAt(Vector3D v) {
		lookAt = v;
	}

	/**
	 * Set the position of up.
	 *
	 * @param v     direction:Vector3D {@link Vector3D}
	 */
	public final void setUp(Vector3D v) {
		up = v;
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
	 * Add ambient light to the scene.
	 *
	 * @param r         Red
	 * @param g         Green
	 * @param b         Blue
	 */
	public final void addAmbientLight(float r, float g, float b) {
		lightList.add(new Light(Light.AMBIENT, null, r, g, b));
	}
	/**
	 * Add a direction light to the scene.
	 *
	 * @param r         Red
	 * @param g         Green
	 * @param b         Blue
	 * @param v         direction:Vector3D {@link Vector3D}
	 */
	public final void addDirectionalLight(float r, float g, float b, Vector3D v) {
		lightList.add(new Light(Light.DIRECTIONAL, v, r, g, b));
	}
	/**
	 * Add a point light to the scene.
	 *
	 * @param r         Red
	 * @param g         Green
	 * @param b         Blue
	 * @param v         direction:Vector3D {@link Vector3D}
	 */
	public final void addPointLight(float r, float g, float b, Vector3D v) {
		lightList.add(new Light(Light.POINT, v, r, g, b));
	}

	Image getRenderedImage() {
		return canvas;
	}

	/**
	 * TODO
	 * Output rendered image to current working directory in jpeg format
	 */
	public void saveRenderedImage() throws IOException {
		File outfile = new File("out.jpg");
		ImageIO.write(canvas, "jpg", outfile);
	}


	/**
	 * TODO
	 */
	public void renderPixel(int i, int j) {
		Vector3D dir = new Vector3D(
				i * Du.x + j * Dv.x + Vp.x,
				i * Du.y + j * Dv.y + Vp.y,
				i * Du.z + j * Dv.z + Vp.z);
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