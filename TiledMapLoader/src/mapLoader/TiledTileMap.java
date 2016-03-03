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
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("SIM");
		JPanel panel = new TestPanel();
		frame.setContentPane(panel);
		
		frame.setVisible(true);
		frame.setSize(600, 800);
		
		new TiledTileMap();
	}
	
	public TiledTileMap(){
		
		JSONParser parser = new JSONParser();
		
		
		
		try{
			Object obj = parser.parse(new FileReader("images/disMap1.json"));
			JSONObject jsonObject = (JSONObject)obj;
			JSONArray layers = new JSONArray();
			
			
			long height =  (long) jsonObject.get("height");
			long width =  (long) jsonObject.get("width");
			layers = (JSONArray) jsonObject.get("layers");
			
			JSONArray tilesets = (JSONArray)jsonObject.get("tilesets");
			for(int i = 0; i < tilesets.size(); i++)
			{
				JSONObject tileset = (JSONObject)tilesets.get(i);
				
				String imageFile = (String) tileset.get("image");
				
				images.add(ImageIO.read(new File(imageFile)));
				
				
			}
			
			System.out.println(images.size());
			for(int i = 0; i < layers.size(); i++)
			{
				JSONObject layer = (JSONObject) layers.get(i);
				
				layer.get("data");
				
			}
			
			
//			Component json = (Component) jsonObject.get("layers");
			
//			Iterator<JSONArray> iterator = layers.iterator();
//			while (iterator.hasNext()) {
//				System.out.println(iterator.next());
//			}
//			jsonObject.
//			content.add(json);
		
			
			System.out.println("hoogte: " + height + " breedte: " + width);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		}
}
	class TestPanel extends JPanel
	{
	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	// member variables

	// Constructor
	public TestPanel()
	{
		
	setPreferredSize( new Dimension(640,480) );
	    
	}

	// Super important, override paintComponents
	public void paintComponent(Graphics g)
	{
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D)g;
	g2.translate(0,0);
		
//	BufferedImage bi = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);
//	Graphics2D big = bi.createGraphics();
//	Rectangle r = new Rectangle(0, 0, 0, 5);
//	TexturePaint tp = new TexturePaint(bi, r);
//	
//	g2.setPaint(tp);
//	g2.drawOval(10, 10, 100, 200);
	

	    }
	}


	
	
	
	



