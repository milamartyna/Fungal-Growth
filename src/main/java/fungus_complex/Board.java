package fungus_complex;

import fungus_complex.fungi.Fungus;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	@Serial
	private static final long serialVersionUID = 1L;
	private Point[][] points;
	private final DataPlotter dataPlotter;
	private final int size = 5;
	private int currentIteration = 1;
	private final File csvOutputFile = new File("output.csv");
	public State editState = State.EMPTY;
	private static final int N = 4; // amount of neighbours in each direction
	private final Map<IterationStage, Long> stagesTotalTime = new HashMap<>(Arrays.stream(IterationStage.values()).collect(Collectors.toMap(element -> element, element -> 0L)));

	public Board(int length, int height, DataPlotter dataPlotter) {
		this.dataPlotter = dataPlotter;
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
		setOpaque(true);
		initialize(length / size, height / size);
	}

	public void iteration() {
		for (IterationStage stage : IterationStage.values()) {
			long start = System.nanoTime();
			runIterationStage(stage);
			long finish = System.nanoTime();
			stagesTotalTime.put(stage, stagesTotalTime.get(stage) + (finish - start) / 1000000);
		}

//		if (currentIteration % 5 == 0) {
//			System.out.print("Iteration" + currentIteration + "time elapsed: ");
//			System.out.println(stagesTotalTime.values().stream().mapToLong(Long::longValue).sum());
//			System.out.println(stagesTotalTime);
//		}

		currentIteration++;
		logFungiCounts();

		this.repaint();
	}

	private void runIterationStage(IterationStage stage) {
		for (Point[] pointsRow : points) {
			for (Point point : pointsRow) {
				switch (stage) {
					case SUPPLY_FOOD -> point.supplyFood(currentIteration % Point.seasonLength == 0);
					case GROW -> point.grow();
					case RESOLVE_COMPETITION -> point.resolveCompetition();
					case EXPAND_MYCELIA -> point.expandMycelia();
					case PROGRESS_MYCELIA -> point.progressToExpandedMycelia();
					case UPDATE_VISUAL -> point.updateVisualState();
				}
			}
		}
	}

	private void logFungiCounts() {
		HashMap<State, Integer> fungusCounts = new HashMap<>(Map.of(
				State.FAST_A, 0,
				State.FAST_B, 0,
				State.SLOW_A, 0,
				State.SLOW_B, 0
		));;
		for (Point[] pointsRow : points)
			for (Point point : pointsRow) {
				if (point.getActiveFungus() == null) continue;
				State fungusSpecies = point.getActiveFungus().getCorrelatedState();
				fungusCounts.put(fungusSpecies, fungusCounts.get(fungusSpecies) + 1);
			}

		for (Map.Entry<State, Integer> entry : fungusCounts.entrySet()) {
			dataPlotter.addData(entry.getKey(), currentIteration, entry.getValue());
		}
		try (PrintWriter pw = new PrintWriter(new FileWriter(csvOutputFile, true))) {

			pw.print(fungusCounts.get(State.FAST_A) + ",");
			pw.print(fungusCounts.get(State.FAST_B) + ",");
			pw.print(fungusCounts.get(State.SLOW_A) + ",");
			pw.print(fungusCounts.get(State.SLOW_B) + "\n");
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
		}
	}

	public void clear() {
		for (Point[] pointsRow : points) {
			for (Point point : pointsRow) {
				point.clear();
			}
		}
		this.repaint();
	}

	private void initialize(int length, int height) {
		points = new Point[length][height];
		try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
			pw.println("Fast A,Fast B,Slow A,Slow B");
		}
		catch (Exception ignored) {}

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
						if (neiX >= 0 && neiX < points.length && neiY >= 0 && neiY < points[neiX].length) {
							points[x][y].addNeighbor(direction, points[neiX][neiY]);
						}
					}
				}
			}
		}
		for (Point[] pointsRow : points)
			for (Point point : pointsRow)
				point.updateVisualState();

		repaint();
	}

	protected void paintComponent(Graphics g) {
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		g.setColor(Color.GRAY);
		drawNetting(g);
	}

	private void drawNetting(Graphics g) {
		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[x].length; ++y) {
				Color cellColor = points[x][y].getVisualState().getColor();
				if (points[x][y].getActiveFungus() == null && points[x][y].getVisualState().representsAFungus()) {
					cellColor = cellColor.darker();
				}
				g.setColor(cellColor);
				g.fillRect((x * size) + 1, (y * size) + 1, (size), (size));
			}
		}

	}

	private void mouseClickedOrDragged(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			Class<? extends Fungus> fungusClass = editState.getCorrelatedFungusClass();
			if (fungusClass != null && points[x][y].presentFungi.stream().noneMatch(f -> f.getClass() == fungusClass)) {
				editState.createCorrelatedFungus(points[x][y]);
			}
			points[x][y].setVisualState(editState);
			this.repaint();
		}
	}

	public void mouseClicked(MouseEvent e) {
		mouseClickedOrDragged(e);
	}

	public void componentResized(ComponentEvent e) {

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
