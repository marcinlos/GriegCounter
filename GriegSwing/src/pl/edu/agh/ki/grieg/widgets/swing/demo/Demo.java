package pl.edu.agh.ki.grieg.widgets.swing.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import pl.edu.agh.ki.grieg.model.CompositeModel;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.model.Models;
import pl.edu.agh.ki.grieg.util.math.Point;
import pl.edu.agh.ki.grieg.widgets.swing.ChannelsChart;

import com.google.common.collect.Lists;

public class Demo extends JFrame {

    private static final int WIDTH = 700;

    private static final int HEIGHT = 400;

    private final CompositeModel<?> chart;

    private final ChannelsChart chartView;

    private final Model<List<Point>> leftSerie;

    private final Model<List<Point>> rightSerie;

    private final Executor executor;

    {
        List<Point> leftList = Lists.newArrayList();
        List<Point> rightList = Lists.newArrayList();

        leftSerie = Models.simple(rightList);
        rightSerie = Models.simple(leftList);

        chart = Models.container();
        chart.addModel("left", leftSerie);
        chart.addModel("right", rightSerie);

        chartView = new ChannelsChart("Random walk", chart, 1.0f, 0.0f, 0.5f);

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

        RandomWalk topWalk = new RandomWalk(leftSerie, n, f, 1);
        RandomWalk bottomWalk = new RandomWalk(rightSerie, n, f, 1);

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
