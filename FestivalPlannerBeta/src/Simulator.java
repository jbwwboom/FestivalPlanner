
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Simulator implements ActionListener {

	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	private JSONObject jsonObject;
	private JSONArray layers;
	private ArrayList<BufferedImage> tileArray;
	private JSONObject layer;
	private int visitorAmount;
	private Timer timer = new Timer(1000 * 30, this);
	private int indexTimer;

	private ArrayList<Point> spawnPoints = new ArrayList<Point>();
	private ArrayList<Visitor> visitors = new ArrayList<Visitor>();
	ArrayList<Pathfinding> pathFinding = new ArrayList<Pathfinding>();

	@SuppressWarnings("unchecked")

	public Simulator(int visitorAmount) {
		this.visitorAmount = visitorAmount;
		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(new FileReader("src/podiums.json"));
			jsonObject = (JSONObject) obj;

			layers = new JSONArray();
			layers = (JSONArray) jsonObject.get("layers");

			for (int i = 0; i < layers.size(); i++) {
				layer = (JSONObject) layers.get(i);
			}

			tileArray = new ArrayList<>();
			JSONArray jsonTilesets = (JSONArray) jsonObject.get("tilesets");
			for (int i = 0; i < jsonTilesets.size(); i++) {
				JSONObject tileset = (JSONObject) jsonTilesets.get(i);

				String imageFile = (String) tileset.get("image");
				BufferedImage img = ImageIO.read(new File(imageFile));

				int index = ((Long) tileset.get("firstgid")).intValue();

				while (tileArray.size() < 22000)
					tileArray.add(null);

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

			pathFinding.add(new Pathfinding(this, new Point(16, 18)));
			pathFinding.add(new Pathfinding(this, new Point(43, 24)));
			pathFinding.add(new Pathfinding(this, new Point(15, 48)));
			pathFinding.add(new Pathfinding(this, new Point(42, 2)));
			pathFinding.add(new Pathfinding(this, new Point(57, 13)));
			pathFinding.add(new Pathfinding(this, new Point(57, 48)));

			timer.start();

			// nieuwe bezoekers aanmaken
			for (int i = 0; i < visitorAmount; i++) {
				int spawn = (int) Math.floor(Math.random() * 3);
				Pathfinding path = pathFinding.get((int) Math.floor(Math.random() * 3));
				Point2D spawnPoint = null;
				switch (spawn) {
				case 0:
					spawnPoint = spawnPoints.get(spawn);
					spawnPoint = new Point((int) spawnPoint.getX() * 16, (int) spawnPoint.getY() * 16);
					break;
				case 1:
					spawnPoint = spawnPoints.get(spawn);
					spawnPoint = new Point((int) spawnPoint.getX() * 16, (int) spawnPoint.getY() * 16);
					break;

				case 2:
					spawnPoint = spawnPoints.get(spawn);
					spawnPoint = new Point((int) spawnPoint.getX() * 16, (int) spawnPoint.getY() * 16);
					break;
				}

				Point2D location = new Point2D.Double(spawnPoint.getX(), spawnPoint.getY());
				Visitor v = new Visitor(location, this, path);
				visitors.add(v);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	// public boolean isSpawnable(ArrayList<Visitor> visitors) {
	// boolean spawnable = true;
	//
	// for (Visitor v : visitors) {
	// if (v.getLocation().distance(v.getStartLocation()) < 7)
	// spawnable = false;
	// }
	//
	// return spawnable;
	// }

	public void makeGUI(int visitorAmount) {
		JFrame frame = new JFrame("SIM");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.visitorAmount = visitorAmount;
		JPanel panel = new TestPanel(visitorAmount);

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

	public ArrayList<Visitor> getVisitors() {
		return visitors;
	}

	public ArrayList<Pathfinding> getPathfinding() {
		return pathFinding;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		indexTimer++;
		if (indexTimer % 3 == 0 && indexTimer != 0) {
			for (Visitor v : visitors) {
				v.setPath(pathFinding.get((int) Math.floor(Math.random() * 3) + 3));
			}
			timer.restart();
			System.out.println(visitorAmount + " " + visitors.size() + " " + indexTimer);
		} else {
			makeVisitors();
			for (Visitor v : visitors) {
				v.setPath(pathFinding.get((int) Math.floor(Math.random() * 3)));

			}
			timer.restart();
			System.out.println(visitorAmount + " " + visitors.size() + " " + indexTimer);
		}
	}

	private void makeVisitors() {
		for (int i = 0; visitors.size() <= visitorAmount; i++) {
			int spawn = (int) Math.floor(Math.random() * 3);
			Pathfinding path = pathFinding.get((int) Math.floor(Math.random() * 3));
			Point2D spawnPoint = null;
			switch (spawn) {
			case 0:
				spawnPoint = spawnPoints.get(spawn);
				spawnPoint = new Point((int) spawnPoint.getX() * 16, (int) spawnPoint.getY() * 16);
				break;
			case 1:
				spawnPoint = spawnPoints.get(spawn);
				spawnPoint = new Point((int) spawnPoint.getX() * 16, (int) spawnPoint.getY() * 16);
				break;

			case 2:
				spawnPoint = spawnPoints.get(spawn);
				spawnPoint = new Point((int) spawnPoint.getX() * 16, (int) spawnPoint.getY() * 16);
				break;
			}

			Point2D location = new Point2D.Double(spawnPoint.getX(), spawnPoint.getY());
			Visitor v = new Visitor(location, this, path);
			visitors.add(v);
		}

	}

}

class TestPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Simulator tiled;
	private TiledLayer layer;

	private Point mouseP;
	private int newCX;
	private int visitorAmount;
	private int newCY;
	private float cameraZoom = 1;
	float rotation = 0;
	Timer timer;
	GUI gui;

	public TestPanel(int visitorAmount) {
		timer = new Timer((1000 / 60), this);
		timer.start();
		this.visitorAmount = visitorAmount;
		tiled = new Simulator(visitorAmount);
		setPreferredSize(new Dimension((60 * 16), (60 * 16)));
		camera();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

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

		for (Visitor v : tiled.getVisitors()) {
			v.draw(g2);
		}

		Iterator<Visitor> iterator = tiled.getVisitors().iterator();
		while (iterator.hasNext()) {// for (Visitor v : tiled.getVisitors()) {
			Visitor v = iterator.next();
			v.update(tiled.getVisitors());

			if (v.atTarget() && (v.getPath() == tiled.getPathfinding().get(3)
					|| v.getPath() == tiled.getPathfinding().get(4) || v.getPath() == tiled.getPathfinding().get(5))) {
				iterator.remove();
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
				if (me.isControlDown())
					rotation += me.getWheelRotation() / 10.0f;
				else {
					try {
						Point2D mousePD = getCamera().inverseTransform(me.getPoint(), null);

						cameraZoom *= 1 - me.getWheelRotation() * 0.05f;

						Point2D mousePD2 = getCamera().inverseTransform(me.getPoint(), null);

						newCX -= (mousePD.getX() - mousePD2.getX()) * cameraZoom;
						newCY -= (mousePD.getY() - mousePD2.getY()) * cameraZoom;

					} catch (NoninvertibleTransformException e) {
						e.printStackTrace();
					}
				}

				repaint();
			}
		});

	}

	public AffineTransform getCamera() {
		AffineTransform camera = new AffineTransform();
		camera.translate(getWidth() / 2, getHeight() / 2);
		camera.rotate(rotation);
		camera.translate(newCX, newCY);
		camera.scale(cameraZoom, cameraZoom);
		return camera;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();

	}

}
