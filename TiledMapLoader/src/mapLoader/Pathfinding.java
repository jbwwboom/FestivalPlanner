package mapLoader;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Pathfinding {
	Point positie;
	int[][] distance;

	public Pathfinding() {

	}

	public void route() {
		distance = new int[50][50];
		for (int x = 0; x < 50; x++) {
			for (int y = 0; y < 50; y++) {
				distance[x][y] = 100000;
			}
		}

		Queue<Point> toDo = new LinkedList<>();
		ArrayList<Point> visited = new ArrayList<>();
		distance[positie.x][positie.y] = 0;
		toDo.add(positie);
		visited.add(positie);

		Point neighbours[] = { new Point(1, 0), new Point(-1, 0), new Point(0, -1), new Point(0, 1) };

		while (!toDo.isEmpty()) {

		}

	}

}
