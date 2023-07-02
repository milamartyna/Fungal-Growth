package fungus_complex;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.Series;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataPlotter extends JFrame {

    private final LinkedHashMap<State, XYSeries> fungusPopulationSeries = new LinkedHashMap<>();

    private final XYSeriesCollection fungusPopulationDataset = new XYSeriesCollection();

    public DataPlotter() {
        fungusPopulationSeries.put(State.FAST_A, new XYSeries("Fast A"));
        fungusPopulationSeries.put(State.FAST_B, new XYSeries("Fast B"));
        fungusPopulationSeries.put(State.SLOW_A, new XYSeries("Slow A"));
        fungusPopulationSeries.put(State.SLOW_B, new XYSeries("Slow B"));
        for (XYSeries series : fungusPopulationSeries.values()) fungusPopulationDataset.addSeries(series);

        JFreeChart chart = createChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);

        XYPlot plot = chart.getXYPlot();

        plot.setBackgroundPaint(Color.white);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        int seriesIndex = 0;
        for (State fungusSpecies : fungusPopulationSeries.keySet()) {
            renderer.setSeriesPaint(seriesIndex, fungusSpecies.getColor());
            renderer.setSeriesStroke(seriesIndex, new BasicStroke(2.0f));
            seriesIndex++;
        }
        add(chartPanel);
        plot.setRenderer(renderer);

        pack();
        setTitle("Line chart");
        setLocationRelativeTo(null);
        setSize(800, 600);
    }

    public void addData(State fungusSpecies, int iteration, int population) {
        fungusPopulationSeries.get(fungusSpecies).add(iteration, population);

    }

    public void loadUI() {
        setVisible(true);
    }

    private JFreeChart createChart() {
        return ChartFactory.createXYLineChart(
                "Active fungus populations by iteration",
                "iteration",
                "population",
                fungusPopulationDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

}
