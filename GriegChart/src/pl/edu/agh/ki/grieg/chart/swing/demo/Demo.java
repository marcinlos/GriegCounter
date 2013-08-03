package pl.edu.agh.ki.grieg.chart.swing.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import pl.edu.agh.ki.grieg.chart.Chart;
import pl.edu.agh.ki.grieg.chart.ChartModel;
import pl.edu.agh.ki.grieg.chart.Serie;
import pl.edu.agh.ki.grieg.gfx.Point;

import com.google.common.collect.Lists;

public class Demo extends JFrame {

    private static final int WIDTH = 700;

    private static final int HEIGHT = 400;

    private final ChartModel<List<Point>> chart;

    private final ChannelsChart chartView;

    private final Serie<List<Point>> topSerie;

    private final Serie<List<Point>> bottomSerie;

    private final Executor executor;

    {
        List<Point> topList = Lists.newArrayList();
        List<Point> bottomList = Lists.newArrayList();

        topSerie = Serie.of(topList);
        bottomSerie = Serie.of(bottomList);

        chart = new Chart<List<Point>>();
        chart.add("top", topSerie);
        chart.add("bottom", bottomSerie);

        chartView = new ChannelsChart(chart, 1.0f, 0.5f);

        executor = Executors.newFixedThreadPool(2);
    }

    private Demo() {
        super("Chart demo");
        setupUI();
        runGenerators();
    }

    private void runGenerators() {
        int n = 10000;
        float f = 1.0f / 250;

        RandomWalk topWalk = new RandomWalk(topSerie, n, f, 1);
        RandomWalk bottomWalk = new RandomWalk(bottomSerie, n, f, 1);

        executor.execute(topWalk);
        executor.execute(bottomWalk);
    }

    private void setupUI() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        getContentPane().setBackground(Color.black);
        add(chartView);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Demo demo = new Demo();
                demo.pack();
                demo.setDefaultCloseOperation(EXIT_ON_CLOSE);
                demo.setVisible(true);
            }
        });
    }

}
