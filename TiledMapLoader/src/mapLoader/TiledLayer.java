package mapLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class TiledLayer {
	private ArrayList<Long> dataArray;
	private JSONArray dataJSONArray;
	private JSONObject layer;
	private int width;
	private int height;
	private boolean visible;
	private double opacity;
	private int x;
	private int y;
	private String name;
	private String type;
	private TiledTileMap tiled;

	public TiledLayer(JSONObject layer) {
		tiled = new TiledTileMap();
		try {
			tiledLayerData(layer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void tiledLayerData(JSONObject layer) throws FileNotFoundException, IOException, ParseException {
		this.layer = layer;
		dataJSONArray = (JSONArray) layer.get("data");
		width = ((Long) layer.get("width")).intValue();
		height = ((Long) layer.get("height")).intValue();
		name = (String) layer.get("name");
		type = (String) layer.get("type");
		opacity = ((Long) layer.get("opacity")).doubleValue();
		visible = (boolean) layer.get("visible");
		x = (((Long) layer.get("x")).intValue());
		y = ((Long) layer.get("y")).intValue();
		dataArray = ((ArrayList<Long>) dataJSONArray);
		// System.out.println(width + " " + height + " " + name + " " + type + "
		// " + opacity + " " + visible + " " + x
		// + " " + y + " " + dataArray + " ");
	}

	public ArrayList<Long> getData() {
		return dataArray;
	}

	public JSONObject getLayer() {
		return layer;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean getVisibility() {
		return visible;
	}

	public double getOpacity() {
		return opacity;
	}

}
