package mapLoader;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.json.simple.JSONObject;

public class Pathfinding {
	Point position;
	int[][] distance;
	TiledLayer layer;
	TiledTileMap tiled;
	ArrayList<Point> podiums = new ArrayList<Point>();

	public static void main(String args[]) {
		new Pathfinding();
	}

	public Pathfinding() {
		tiled = new TiledTileMap();
		podiums.add(new Point(16, 18));
		podiums.add(new Point(43, 24));
		podiums.add(new Point(15, 48));
		layer = new TiledLayer((JSONObject) tiled.getLayer().get(tiled.getLayer().size() - 1));
		route(layer);
	}

	public void route(TiledLayer layer) {
		distance = new int[layer.getHeight()][layer.getWidth()];
		for (int x = 0; x < layer.getHeight(); x++) {
			for (int y = 0; y < layer.getWidth(); y++) {
				distance[x][y] = 60 * 60 * 60;
			}
		}

		int podium = (int) Math.random() * 2;
		Point podiumPoint = null;

		switch (podium) {
		case 0:
			podiumPoint = podiums.get(podium);
			break;
		case 1:
			podiumPoint = podiums.get(podium);
			break;
		case 2:
			podiumPoint = podiums.get(podium);
			break;
		}

		Queue<Point> toDo = new LinkedList<>();
		ArrayList<Point> visited = new ArrayList<>();
		distance[(int) podiumPoint.getX()][(int) podiumPoint.getY()] = 0;
		Point position = new Point(16, 18);
		toDo.add(position);
		visited.add(position);

		Point neighbours[] = { new Point(1, 0), new Point(-1, 0), new Point(0, -1), new Point(0, 1) };

		while (!toDo.isEmpty()) {
			Point current = toDo.remove();
			for (int i = 0; i < neighbours.length; i++) {
				Point point = new Point((int) (current.getX() + neighbours[i].getX()),
						(int) (current.getY() + neighbours[i].getY()));
				System.out.println(point);
				if (point.x < 0 || point.y < 0 || point.x >= layer.getWidth() || point.y >= layer.getHeight()) {
					continue;
				}
				if (visited.contains(point)) {
					continue;
				}

				distance[point.x][point.y] = distance[current.x][current.y] + 1;
				visited.add(point);
				toDo.add(point);

			}
		}
	}
}
// 16,18
// 43,24
// 15,48