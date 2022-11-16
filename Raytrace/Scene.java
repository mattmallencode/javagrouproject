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
	static BufferedImage canvas;
	static Vector3D eye, lookAt, up;
	static Vector3D Du, Dv, Vp;
	static float fieldOfView;
	static Color background;
	int width, height;

	/**
	 * Initialize a scene with default configuration.
	 *
	 * @param width			Horizontal resolution of the scene.
	 * @param height		Vertical resolution of the scene.
	 */
	public Scene(int width, int height) {
		this.width = width;
		this.height = height;
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		fieldOfView = 30; // default horizontal field of view

		eye = new Vector3D(0, 0, 10);
		lookAt = new Vector3D(0, 0, 0);
		up = new Vector3D(0, 1, 0);
		background = new Color(0, 0, 0);

		// Initialize various lists
		objectList = new ArrayList<>(CHUNKSIZE);
		lightList = new ArrayList<>(CHUNKSIZE);

	}

	/**
	 * Add a new sphere to the scene.
	 *
	 * @param v			Position of the sphere in the scene {@link Vector3D}
	 * @param r			Radius of the sphere
	 */
	public final void addSphere(Surface s,Vector3D v,  float r) {
		objectList.add(new Sphere(s, v, r));
	}

	/**
	 * Set the position of the eye.
	 *
	 * @param v			Position of the eye in the scene {@link Vector3D}
	 */
	public final void setEye(Vector3D v) {
		eye = v;
	}

	/**
	 * Set the position that the eye is looking at.
	 *
	 * @param v			Position of the eye looking at. {@link Vector3D}
	 */
	public final void setLookAt(Vector3D v) {
		lookAt = v;
	}

	/**
	 * Set the position of up.
	 *
	 * @param v			Position of the up. {@link Vector3D}
	 */
	public final void setUp(Vector3D v) {
		up = v;
	}

	/**
	 * Set the field of view.
	 *
	 * @param f			Value of the field of view.
	 */
	public final void setFov(float f) {
		fieldOfView = f;
	}

	/**
	 * Set the background colour of the scene.
	 *
	 * @param r			Red
	 * @param g			Greed
	 * @param b			Blue
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
	 * @param v         position {@link Vector3D}
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
	 * @param v         Position {@link Vector3D}
	 */
	public final void addPointLight(float r, float g, float b, Vector3D v) {
		lightList.add(new Light(Light.POINT, v, r, g, b));
	}

	/**
	 * Output rendered image to current working directory in PNG format.
	 *
	 * @param filePath			File path to store the output image file.
	 * @throws IOException		If unable to write file to specified location in the file system.
	 */
	public final void saveRenderedImage(String filePath) throws IOException {
		BufferedImage img = canvas;
		File f = new File(filePath);
		ImageIO.write(img, "PNG", f);
	}

	/**
	 * Overload method.
	 * Output rendered image to ./out.png in PNG format, if the filePath not provided.
	 *
	 * @throws IOException		If unable to write file to specified location in the file system.
	 */
	public final void saveRenderedImage() throws IOException {
		BufferedImage img = canvas;
		File f = new File("./out.png");
		ImageIO.write(img, "PNG", f);
	}


	/**
	 * Take all the configurations and render the scene to the image.
	 */
	public final void renderImage() {
		// Compute viewing matrix that maps a screen coordinate to a ray direction
		Vector3D look = new Vector3D(lookAt.x - eye.x, lookAt.y - eye.y, lookAt.z - eye.z);
		Du = Vector3D.normalize(look.cross(up));
		Dv = Vector3D.normalize(look.cross(Du));
		float fl = (float) (width / (2 * Math.tan((0.5 * fieldOfView) * Math.PI / 180)));
		Vp = Vector3D.normalize(look);
		Vp.x = Vp.x * fl - 0.5f * (width * Du.x + height * Dv.x);
		Vp.y = Vp.y * fl - 0.5f * (width * Du.y + height * Dv.y);
		Vp.z = Vp.z * fl - 0.5f * (width * Du.z + height * Dv.z);

		for (int j = 0; j < this.height; j += 1) {
			for (int i = 0; i < this.width; i += 1) {
				renderPixel(i, j);
			}
		}
	}

	/**
	 * Render a particular pixel.
	 * @param i		x-axis position of the pixel to be rendered
	 * @param j		x-axis position of the pixel to be rendered
	 */
	private void renderPixel(int i, int j) {
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