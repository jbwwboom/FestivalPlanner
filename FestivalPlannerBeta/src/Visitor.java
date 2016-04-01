import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Visitor {

	private Point2D location;
	private Pathfinding path;
	private double speed;
	private Image sprite;
	private Point2D direction;
	private Point2D startPosition;
	public double angleSpeed;

	private Point2D destination;

	public Visitor(Point2D location, TiledTileMap tiled) {
		path = tiled.pathFinding;
		this.location = location;
		direction = path.getDirection((int) location.getX(), (int) location.getY());
		startPosition = location;
		speed = 1 + Math.random() * 2;
		destination = path.getDestination();

		int random = (int) (Math.random() * 6);
		switch (random) {
		case 0:
			sprite = new ImageIcon("images/Bezoeker.png").getImage();
			break;
		case 1:
			sprite = new ImageIcon("images/Bezoeker blauw haar.png").getImage();
			break;
		case 2:
			sprite = new ImageIcon("images/Bezoeker blond haar.png").getImage();
			break;
		case 3:
			sprite = new ImageIcon("images/Bezoeker donker haar.png").getImage();
			break;
		case 4:
			sprite = new ImageIcon("images/Bezoeker wit haar.png").getImage();
			break;
		case 5:
			sprite = new ImageIcon("images/Bezoeker zwart haar.png").getImage();
			break;
		}

	}

	public void draw(Graphics2D g2d) {
		AffineTransform transform = new AffineTransform();
		transform.translate(location.getX() - sprite.getWidth(null) / 2, location.getY() - sprite.getHeight(null) / 2);
		transform.rotate(sprite.getWidth(null) / 2, sprite.getHeight(null) / 2);
		g2d.drawImage(sprite, transform, null);

	}

	public void update(ArrayList<Visitor> visitors) {
		// direction += 0.01;

		// if (!atTarget())
		int x = (int) Math.floor(location.getX() / 16);
		int y = (int) Math.floor(location.getY() / 16);

		direction = path.getDirection(x, y);

		Point2D target = new Point2D.Double(16 * (x + direction.getX()) + 8, 16 * (y + direction.getY()) + 8);

		Point2D difference = new Point2D.Double(target.getX() - location.getX(), target.getY() - location.getY());

		difference = new Point2D.Double(difference.getX() / difference.distance(0, 0) * speed,
				difference.getY() / difference.distance(0, 0) * speed);

		angleSpeed = Math.atan2(difference.getX(), difference.getY());

		location = new Point2D.Double(location.getX() + difference.getX(), location.getY() + difference.getY());

		boolean isCollision = false;
		for (Visitor v : visitors) {
			if (v == this)
				continue;
			if (v.location.distance(location) < 7) {
				isCollision = true;
				break;
			}
		}

		// if (!isCollision)

		// else {

		// }

	}

	public void setDestination(Point point) {
		this.destination = point;
	}

	public double getPointX() {
		return location.getX();
	}

	public double getPointY() {
		return location.getY();
	}

	public Image getSprite() {
		return sprite;
	}

	public Point2D getLocation() {
		return location;
	}

	public Point2D getStartLocation() {
		return startPosition;
	}

	public boolean atTarget() {
		int x = (int) Math.floor(location.getX() / 16);
		int y = (int) Math.floor(location.getY() / 16);
		return path.distance[x][y] <= 1;
	}

}
