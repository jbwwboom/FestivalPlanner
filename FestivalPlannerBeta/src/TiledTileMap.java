
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TiledTileMap {

	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	private JSONObject jsonObject;
	private JSONArray layers;
	private ArrayList<BufferedImage> tileArray;
	private JSONObject layer;

	private ArrayList<Point> spawnPoints = new ArrayList<Point>();
	private ArrayList<Visitor> visitors = new ArrayList<Visitor>();

	@SuppressWarnings("unchecked")

	public TiledTileMap() {

		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(new FileReader("src/podiums.json"));
			jsonObject = (JSONObject) obj;

			layers = new JSONArray();
			layers = (JSONArray) jsonObject.get("layers");

			for (int i = 0; i < layers.size(); i++) {
				layer = (JSONObject) layers.get(i);
			}

			// System.out.println(layers.size());

			tileArray = new ArrayList<>();
			JSONArray jsonTilesets = (JSONArray) jsonObject.get("tilesets");
			for (int i = 0; i < jsonTilesets.size(); i++) {
				JSONObject tileset = (JSONObject) jsonTilesets.get(i);

				String imageFile = (String) tileset.get("image");
				BufferedImage img = ImageIO.read(new File(imageFile));
				images.add(img);

				int index = ((Long) tileset.get("firstgid")).intValue();

				while (tileArray.size() < 22000)
					tileArray.add(null);

				// System.out.println(img.getHeight() + " " + img.getWidth());

				for (int y = 0; y < img.getHeight(); y = y + 16) {
					for (int x = 0; x < img.getWidth(); x = x + 16) {
						BufferedImage tile = img.getSubimage(x, y, 16, 16);
						tileArray.set(index, tile);
						index++;
					}
				}
			}

			spawnPoints.add(new Point(42, 5));
			spawnPoints.add(new Point(54, 13));
			spawnPoints.add(new Point(54, 48));

			for (int i = 0; i < 50; i++) {
				int spawn = (int) Math.random() * 2;
				double direction = 0;
				Point2D spawnPoint = null;
				switch (spawn) {
				case 0:
					spawnPoint = spawnPoints.get(spawn);
					spawnPoint = new Point((int) spawnPoint.getX() * 16, (int) spawnPoint.getY() * 16);
					direction = Math.PI * 0.25;
					break;
				case 1:
					spawnPoint = spawnPoints.get(spawn);
					spawnPoint = new Point((int) spawnPoint.getX() * 16, (int) spawnPoint.getY() * 16);
					direction = Math.PI * 1.75;
					break;

				case 2:
					spawnPoint = spawnPoints.get(spawn);
					spawnPoint = new Point((int) spawnPoint.getX() * 16, (int) spawnPoint.getY() * 16);
					direction = Math.PI * 0.75;
					break;
				}

				Point2D location = new Point2D.Double(16 * spawnPoint.getX(), 16 * spawnPoint.getY());

				// visitors.add(new Visitor(location, ,direction));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void makeGUI() {
		JFrame frame = new JFrame("SIM");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new TestPanel();

		frame.getContentPane().add(panel);

		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}

	public ArrayList<BufferedImage> getTileArray() {
		return tileArray;
	}

	public JSONArray getLayer() {
		return layers;
	}
}

class TestPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private TiledTileMap tiled;
	private TiledLayer layer;
	private Pathfinding path;

	private Point mouseP;
	private int newCX;
	private int newCY;
	private float cameraZoom = 1;

	public TestPanel() {
		tiled = new TiledTileMap();
		path = new Pathfinding();
		path.route(new TiledLayer((JSONObject) tiled.getLayer().get(tiled.getLayer().size() - 1)));
		setPreferredSize(new Dimension((60 * 16), (60 * 16)));
		camera();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		// g2.translate(-100, -150);

		AffineTransform camera = getCamera();
		g2.setTransform(camera);

		int a = 0;
		for (int i = 0; i < (tiled.getLayer().size() - 1); i++) {
			layer = new TiledLayer((JSONObject) tiled.getLayer().get(i));
			for (int x = 0; x <= (layer.getHeight() * 16 - 16); x = x + 16) {
				for (int y = 0; y <= (layer.getWidth() * 16 - 16); y = y + 16) {
					Long b = layer.getData().get(a);
					int c = b.intValue();
					g2.drawImage(tiled.getTileArray().get(c), y, x, 16, 16, null);
					if (a >= (60 * 60 - 1)) {
						a = 0;
					} else {
						a++;
					}
				}

			}
		}
	}

	public void camera() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				mouseP = me.getPoint();
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent me) {

				if (SwingUtilities.isLeftMouseButton(me)) {
					newCX -= (int) (mouseP.getX() - me.getX());
					newCY -= (int) (mouseP.getY() - me.getY());
				}

				mouseP = me.getPoint();
				repaint();

			}
		});

		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent me) {

				try {
					Point2D mousePD = getCamera().inverseTransform(me.getPoint(), null);

					cameraZoom *= 1 - me.getWheelRotation() * 0.05f;

					Point2D mousePD2 = getCamera().inverseTransform(me.getPoint(), null);

					newCX -= (mousePD.getX() - mousePD2.getX()) * cameraZoom;
					newCY -= (mousePD.getY() - mousePD2.getY()) * cameraZoom;

				} catch (NoninvertibleTransformException e) {
					e.printStackTrace();
				}

				repaint();
			}
		});
	}

	public AffineTransform getCamera() {
		AffineTransform camera = new AffineTransform();
		camera.translate(newCX, newCY);
		camera.scale(cameraZoom, cameraZoom);
		return camera;
	}
}
