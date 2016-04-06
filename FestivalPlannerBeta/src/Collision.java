
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Collision extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {

	}

	ArrayList<Visitor> visitors = new ArrayList<>();

	public Collision() {
		new Timer(1000 / 60, this).start();
		setPreferredSize(new Dimension(960, 960));
	}

	public boolean isSpawnable(ArrayList<Visitor> visitors) {
		boolean spawnable = true;

		for (Visitor v : visitors) {
			if (v.getLocation().distance(v.getStartLocation()) < 7)
				spawnable = false;
		}

		return spawnable;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for (Visitor v : visitors)
			v.update(visitors);

		repaint();
	}

}
