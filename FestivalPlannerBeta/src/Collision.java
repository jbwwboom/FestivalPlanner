
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Collision extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		JFrame frame = new JFrame("Bezoeker demo");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Collision());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	ArrayList<Visitor> visitors = new ArrayList<>();

	public Collision() {

		new Timer(1000 / 60, this).start();

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				for (Visitor v : visitors)
					v.setDestination(e.getPoint());
			}
		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		for (Visitor p : visitors)
			p.draw(g2d);

		if (isSpawnable(visitors))
			addVisitor();

	}

	public boolean isSpawnable(ArrayList<Visitor> visitors) {
		boolean spawnable = true;

		for (Visitor v : visitors) {
			if (v.getLocation().distance(v.getStartLocation()) < 8)
				spawnable = false;
		}

		return spawnable;
	}

	public void addVisitor() {
		if (visitors.size() <= 100) {
			int random = (int) (Math.random() * 4);
			Point2D startPosition = null;
			double direction = 0;
			switch (random) {
			case 0:
				startPosition = new Point2D.Double(32, 32);
				direction = Math.PI * 0.25;
				break;

			case 1:
				startPosition = new Point2D.Double(32, 536);
				direction = Math.PI * 1.75;
				break;

			case 2:
				startPosition = new Point2D.Double(736, 32);
				direction = Math.PI * 0.75;
				break;

			case 3:
				startPosition = new Point2D.Double(736, 536);
				direction = Math.PI * 1.25;
				break;
			}
			// visitors.add(new Visitor(startPosition, path, direction));
		}

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for (Visitor v : visitors)
			v.update(visitors);

		repaint();
	}

}
