package fungus_complex;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;

public class GUI extends JPanel implements ActionListener, ChangeListener {
	@Serial
	private static final long serialVersionUID = 1L;
	private final Timer timer;
	private DataPlotter dataPlotter;
	private Board board;
	private JButton start;
	private JButton clear;
	private JButton plot;
	private JComboBox<State> drawType;
	private JSlider pred;
	private final JFrame frame;
	private int iterNum = 0;
	private final int maxDelay = 500;
	private final int initDelay = 100;
	private boolean running = false;

	public GUI(JFrame jf) {
		frame = jf;
		timer = new Timer(initDelay, this);
		timer.stop();
	}

	public void initialize(Container container) {
		container.setLayout(new BorderLayout());
		container.setSize(new Dimension(1024, 768));

		JPanel buttonPanel = new JPanel();

		dataPlotter = new DataPlotter();

		start = new JButton("Start");
		start.setActionCommand("Start");
		start.addActionListener(this);

		clear = new JButton("Clear");
		clear.setActionCommand("clear");
		clear.addActionListener(this);
		
		pred = new JSlider();
		pred.setMinimum(0);
		pred.setMaximum(maxDelay);
		pred.addChangeListener(this);
		pred.setValue(maxDelay - timer.getDelay());

		plot = new JButton("Plot");
		plot.setActionCommand("plot");
		plot.addActionListener(this);

		drawType = new JComboBox<>(State.values());
		drawType.addActionListener(this);
		drawType.setActionCommand("drawType");

		buttonPanel.add(start);
		buttonPanel.add(clear);
		buttonPanel.add(drawType);
		buttonPanel.add(pred);
		buttonPanel.add(plot);

		board = new Board(1024, 768 - 70, dataPlotter);
		container.add(buttonPanel, BorderLayout.SOUTH);
		container.add(board, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(timer)) {
			iterNum++;
			frame.setTitle("Fungal Growth (" + iterNum + " iteration)");
			board.iteration();
		} else {
			String command = e.getActionCommand();
			switch (command) {
				case "Start" -> {
					if (!running) {
						timer.start();
						start.setText("Pause");
					} else {
						timer.stop();
						start.setText("Start");
					}
					running = !running;
					clear.setEnabled(true);
				}
				case "clear" -> {
					iterNum = 0;
					timer.stop();
					start.setEnabled(true);
					board.clear();
				}
				case "drawType" -> board.editState = (State) drawType.getSelectedItem();
				case "plot" -> dataPlotter.loadUI();
			}

		}
	}

	public void stateChanged(ChangeEvent e) {
		timer.setDelay(maxDelay - pred.getValue());
	}
}
