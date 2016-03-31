
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Pathfinding {
	Point position;
	int[][] distance;
	TiledLayer layer;
	TiledTileMap tiled;

	public static void main(String args[]) {
		new Pathfinding();
	}

	public Pathfinding() {
		tiled = new TiledTileMap();
	}

	public void route(TiledLayer layer) {
		distance = new int[layer.getHeight()][layer.getWidth()];
		for (int x = 0; x < layer.getHeight(); x++) {
			for (int y = 0; y < layer.getWidth(); y++) {
				distance[x][y] = 60 * 60 * 60;
			}
		}

		Queue<Point> toDo = new LinkedList<>();
		ArrayList<Point> visited = new ArrayList<>();
		distance[position.x][position.y] = 0;
		Point position = new Point(16, 18);
		toDo.add(position);
		visited.add(position);

		Point neighbours[] = { new Point(1, 0), new Point(-1, 0), new Point(0, -1), new Point(0, 1) };

		while (!toDo.isEmpty()) {
			// Point current = new Point(16, 18);
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

	public Point getDirection(int x, int y) {
		Point neighbours[] = { new Point(1, 0), new Point(-1, 0), new Point(0, -1), new Point(0, 1) };

		Point direction = new Point(0, 0);
		for (Point p : neighbours) {
			Point newPoint = new Point(x + p.x, y + p.y);
			if (distance[newPoint.x][newPoint.y] < distance[x][y]) {
				direction = p;
			}
		}
		return direction;
	}
}
// 16,18
// 43,24
// 15,48