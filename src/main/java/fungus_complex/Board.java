package fungus_complex;

import fungus_complex.fungi.AbstractFungus;
import fungus_complex.fungi.FastAFungus;

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
//		System.out.println("Supplying food at iteration " + currentIteration);
		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y)
				points[x][y].supplyFood(currentIteration % Point.seasonLength == 0);

//		System.out.println("resolveCompetition at iteration " + currentIteration);
		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y)
				points[x][y].resolveCompetition();

		int totalExploratoryMycelia = 0;
		int totalFungi = 0;
		int maxMycelia = 0;
		int maxFungi = 0;

		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y) {
				totalExploratoryMycelia += points[x][y].presentExploratoryMycelia.size();
				totalFungi += points[x][y].presentFungi.size();
				maxMycelia = Math.max(maxMycelia, points[x][y].presentExploratoryMycelia.size());
				maxFungi = Math.max(maxFungi, points[x][y].presentFungi.size());
			}

//		System.out.println("total mycelia: " + totalExploratoryMycelia);
//		System.out.println("total fungi: " + totalFungi);
//		System.out.println("max mycelia: " + maxMycelia);
//		System.out.println("max fungi: " + maxFungi);
//
//		System.out.println("expandMycelia at iteration " + currentIteration);
		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y)
				points[x][y].expandMycelia();

//		System.out.println("progressToExpandedMycelia at iteration " + currentIteration);
		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y)
				points[x][y].progressToExpandedMycelia();

//		System.out.println("grow at iteration " + currentIteration);
		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y)
				points[x][y].grow();

//		System.out.println("scheduleNextState at iteration " + currentIteration);
		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y)
				points[x][y].scheduleNextState();

//		System.out.println("progressState at iteration " + currentIteration);
		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y)
				points[x][y].progressState();

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
		for (int x = 1; x < points.length-1; ++x) {
			for (int y = 1; y < points[x].length-1; ++y) {
				for(Direction direction : Direction.values()){
					for(int i = 1; i <= N; i++){
						neiX = x + i * direction.getX();
						neiY = y + i * direction.getY();
						if (neiX > 0 && neiX < points.length-1 && neiY > 0 && neiY < points[neiX].length-1) {
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
		// FIXME we could probably have different colors for dormant fungus
		for (x = 0; x < points.length; ++x) {
			for (y = 0; y < points[x].length; ++y) {
				Color cellColor = switch (points[x][y].getState()) {
					case EMPTY -> new Color(220, 220, 220);
					case FAST_A -> new Color(214, 24, 43);
					case FAST_B -> new Color(252, 195, 5);
					case SLOW_A -> new Color(196, 51, 112);
					case SLOW_B -> new Color(102, 147, 12);
					case ALPHA -> new Color(138, 227, 141);
					case BETA -> new Color(27, 80, 143);
				};
				if (!points[x][y].isFungusActive() && points[x][y].getState().representsAFungus()) {
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
			points[x][y].setState(editState);
			AbstractFungus fungus = editState.getCorrelatedFungus(points[x][y]);
			if (fungus != null && points[x][y].presentFungi.stream().noneMatch(f -> f.getClass() == fungus.getClass()))
				points[x][y].placeFungusManually(fungus);
			points[x][y].setNextState(editState);
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
