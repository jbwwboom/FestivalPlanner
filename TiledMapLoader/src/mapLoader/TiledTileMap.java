package mapLoader;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

	public static void main(String[] args) {

		JFrame frame = new JFrame("SIM");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new TestPanel();

		frame.getContentPane().add(panel);

		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}

	public TiledTileMap() {

		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(new FileReader("images/disMap1.json"));
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

				while (tileArray.size() < 2000)
					tileArray.add(null);

				for (int y = 0; y < img.getHeight(); y = y + 32) {
					for (int x = 0; x < img.getWidth(); x = x + 32) {
						BufferedImage tile = img.getSubimage(x, y, 32, 32);
						tileArray.set(index, tile);
						index++;
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

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

	public TestPanel() {
		tiled = new TiledTileMap();
		// layer = new TiledLayer((JSONObject) tiled.getLayer().get(4));
		setPreferredSize(new Dimension(1600, 1600));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(0, 0);
		int a = 0;
		for (int i = 0; i < (tiled.getLayer().size() - 1); i++) {
			layer = new TiledLayer((JSONObject) tiled.getLayer().get(i));
			for (int x = 0; x <= (layer.getHeight() * 32 - 32); x = x + 32) {
				for (int y = 0; y <= (layer.getWidth() * 32 - 32); y = y + 32) {
					Long b = layer.getData().get(a);
					int c = b.intValue();
					g2.drawImage(tiled.getTileArray().get(c), y, x, 32, 32, null);
					if (a >= 3999) {
						a = 0;
					} else {
						a++;
					}

				}
			}
		}
	}
}
