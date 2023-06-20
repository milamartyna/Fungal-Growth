package fungus_complex;

import fungus_complex.fungi.AbstractFungus;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	private Point[][] points;
	private int size = 5;
	private int currentIteration = 0;
	public State editState = State.EMPTY;
	private static final int N = 4; // amount of neighbours in each direction

	public Board(int length, int height) {
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
		setOpaque(true);
		initialize(length, height);
	}

	public void iteration() {
		for (Point[] pointsRow : points)
			for (Point point : pointsRow)
				point.supplyFood(currentIteration % Point.seasonLength == 0);

		for (Point[] pointsRow : points)
			for (Point point : pointsRow)
				point.resolveCompetition();

		for (Point[] pointsRow : points)
			for (Point point : pointsRow)
				point.expandMycelia();

		for (Point[] pointsRow : points)
			for (Point point : pointsRow)
				point.progressToExpandedMycelia();

		for (Point[] pointsRow : points)
			for (Point point : pointsRow)
				point.grow();

		for (Point[] pointsRow : points)
			for (Point point : pointsRow)
				point.updateVisualState();

		currentIteration++;
		this.repaint();
	}

	public void clear() {
		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y) {
				points[x][y].clear();
			}
		this.repaint();
	}

	private void initialize(int length, int height) {
		points = new Point[length][height];

		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[x].length; ++y) {
				points[x][y] = new Point(x, y);
			}
		}

		// right now we're adding 4 neighbours in each direction
		int neiX, neiY;
		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[x].length; ++y) {
				for(Direction direction : Direction.values()){
					for(int i = 1; i <= N; i++){
						neiX = x + i * direction.getX();
						neiY = y + i * direction.getY();
						if (neiX >= 0 && neiX < points.length-1 && neiY >= 0 && neiY < points[neiX].length-1) {
							points[x][y].addNeighbor(direction, points[neiX][neiY]);
						}
					}
				}
			}
		}
	}

	protected void paintComponent(Graphics g) {
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		g.setColor(Color.GRAY);
		drawNetting(g, size);
	}

	private void drawNetting(Graphics g, int gridSpace) {
		Insets insets = getInsets();
		int firstX = insets.left;
		int firstY = insets.top;
		int lastX = this.getWidth() - insets.right;
		int lastY = this.getHeight() - insets.bottom;

		int x = firstX;
		while (x < lastX) {
			g.drawLine(x, firstY, x, lastY);
			x += gridSpace;
		}

		int y = firstY;
		while (y < lastY) {
			g.drawLine(firstX, y, lastX, y);
			y += gridSpace;
		}
		for (x = 0; x < points.length; ++x) {
			for (y = 0; y < points[x].length; ++y) {
				Color cellColor = switch (points[x][y].getVisualState()) {
					case EMPTY -> new Color(220, 220, 220);
					case FAST_A -> new Color(214, 24, 43);
					case FAST_B -> new Color(252, 195, 5);
					case SLOW_A -> new Color(196, 51, 112);
					case SLOW_B -> new Color(102, 147, 12);
					case ALPHA -> new Color(138, 227, 141);
					case BETA -> new Color(27, 80, 143);
				};
				if (!points[x][y].isFungusActive() && points[x][y].getVisualState().representsAFungus()) {
					cellColor = cellColor.darker();
				}
				g.setColor(cellColor);
				g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
			}
		}

	}

	private void mouseClickedOrDragged(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			Class<? extends AbstractFungus> fungusClass = editState.getCorrelatedFungusClass();
			if (fungusClass != null && points[x][y].presentFungi.stream().noneMatch(f -> f.getClass() == fungusClass)) {
				AbstractFungus fungus = editState.getCorrelatedFungus(points[x][y]);
				points[x][y].placeFungus(fungus);
			}
			points[x][y].setVisualState(editState);
			this.repaint();
		}
	}

	public void mouseClicked(MouseEvent e) {
		mouseClickedOrDragged(e);
	}

	public void componentResized(ComponentEvent e) {
		int dlugosc = (this.getWidth() / size) + 1;
		int wysokosc = (this.getHeight() / size) + 1;
		initialize(dlugosc, wysokosc);
	}

	public void mouseDragged(MouseEvent e) {
		mouseClickedOrDragged(e);
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}
	
}
