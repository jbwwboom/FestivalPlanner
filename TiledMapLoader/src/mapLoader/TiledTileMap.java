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
	// private BufferedImage img = new
	// BufferedImage(1024,1024,BufferedImage.TYPE_INT_ARGB);
	// private BufferedImage img2 = new
	// BufferedImage(1024,1024,BufferedImage.TYPE_INT_ARGB);

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
			System.out.println(layers.size());

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
}

class TestPanel extends JPanel {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	private TiledTileMap tiled;
	// member variables

	// Constructor
	public TestPanel() {
		tiled = new TiledTileMap();
		setPreferredSize(new Dimension(1600, 2560));

	}

	// Super important, override paintComponents
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(0, 0);
		int a = 0;

		for (int x = 0; x < 1280; x = x + 32) {
			for (int y = 0; y < 1280; y = y + 32) {
				g2.drawImage(tiled.getTileArray().get(a), y, x, 32, 32, null);
				a++;
			}
		}
	}
}
