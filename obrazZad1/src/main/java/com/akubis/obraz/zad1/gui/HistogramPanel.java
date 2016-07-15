package com.akubis.obraz.zad1.gui;

import com.akubis.obraz.zad1.processing.ModifyHistogram;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;

//@Getter
//@Setter
public class HistogramPanel extends JPanel implements ChangeListener {

    private BufferedImagePanel chosenImage;
    private BufferedImagePanel copyPanel;
    private BufferedImage originalImage;
    private BufferedImage copyImage;
    private double[][][] histogramOriginal;
    private ModifyHistogram modifyHistogram;
    private DefaultXYDataset datasetRed;
    private XYPlot plotRed;
    private JFreeChart chartRed;
    private ChartPanel chartPanelRed;
    private DefaultXYDataset datasetGreen;
    private XYPlot plotGreen;
    private JFreeChart chartGreen;
    private ChartPanel chartPanelGreen;
    private DefaultXYDataset datasetBlue;
    private XYPlot plotBlue;
    private JFreeChart chartBlue;
    private ChartPanel chartPanelBlue;
    private DefaultXYDataset datasetBightness;
    private XYPlot plotBightness;
    private JFreeChart chartBightness;
    private ChartPanel chartPanelBightness;
    private JSlider gMinSlider;
    private JSlider gMaxSlider;
    private boolean modification = false;

    public HistogramPanel(BufferedImagePanel chosenPanelTab, boolean modification) throws CloneNotSupportedException {
        this.chosenImage = chosenPanelTab;
        this.modification = modification;
        createTab();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
    }

    private void createTab() throws CloneNotSupportedException {
        chosenImage.setLocation(10, 10);
        this.add(chosenImage);
        originalImage = chosenImage.getBufferedImage();
        BufferedImage imageToConvert = originalImage;

        originalImage = new BufferedImage(imageToConvert.getWidth(), imageToConvert.getHeight(), BufferedImage.TYPE_INT_RGB);
        originalImage.getGraphics().drawImage(imageToConvert, 0, 0, null);

        modifyHistogram = new ModifyHistogram();
        histogramOriginal = modifyHistogram.createHistogramImage(originalImage);
        if (modification) {
            copyPanel = (BufferedImagePanel) chosenImage.clone();
            this.add(copyPanel);
            copyPanel.setLocation(630, 10);

            modifyHistogram.createModificationTable(originalImage);
            copyImage = modifyHistogram.changeImage(originalImage);
            copyPanel.setBufferedImage(copyImage);
            originalImage = copyImage; //??
            copyPanel.repaint();
        }

        histogramOriginal = modifyHistogram.createHistogramImage(originalImage);
        datasetRed = new DefaultXYDataset();
        datasetGreen = new DefaultXYDataset();
        datasetBlue = new DefaultXYDataset();
        datasetBightness = new DefaultXYDataset();

        datasetRed.addSeries("Czerwony", histogramOriginal[0]);
        datasetGreen.addSeries("Zielony", histogramOriginal[1]);
        datasetBlue.addSeries("Niebieski", histogramOriginal[2]);
        datasetBightness.addSeries("Jasność", histogramOriginal[3]);
        chartRed = ChartFactory.createXYLineChart("Kanał czerwony", null, null, datasetRed, PlotOrientation.VERTICAL, false, false, false);
        chartGreen = ChartFactory.createXYLineChart("Kanał zielony", null, null, datasetGreen, PlotOrientation.VERTICAL, false, false, false);
        chartBlue = ChartFactory.createXYLineChart("Kanał niebieski", null, null, datasetBlue, PlotOrientation.VERTICAL, false, false, false);
        chartBightness = ChartFactory.createXYLineChart("Jasność", null, null, datasetBightness, PlotOrientation.VERTICAL, false, false, false);

        plotRed = (XYPlot) chartRed.getPlot();
        plotGreen = (XYPlot) chartGreen.getPlot();
        plotBlue = (XYPlot) chartBlue.getPlot();
        plotBightness = (XYPlot) chartBightness.getPlot();
        plotRed.getRenderer().setSeriesPaint(0, Color.RED);
        plotGreen.getRenderer().setSeriesPaint(0, Color.GREEN);
        plotBlue.getRenderer().setSeriesPaint(0, Color.BLUE);
        plotBightness.getRenderer().setSeriesPaint(0, Color.GRAY);

        chartPanelRed = new ChartPanel(chartRed);
        chartPanelRed.setLocation(10, 550);
        chartPanelRed.setSize(570, 350);
        this.add(chartPanelRed);

        chartPanelGreen = new ChartPanel(chartGreen);
        chartPanelGreen.setLocation(600, 550);
        chartPanelGreen.setSize(570, 350);
        this.add(chartPanelGreen);

        chartPanelBlue = new ChartPanel(chartBlue);
        chartPanelBlue.setLocation(1190, 550);
        chartPanelBlue.setSize(570, 350);
        this.add(chartPanelBlue);

        chartPanelBightness = new ChartPanel(chartBightness);
        chartPanelBightness.setLocation(1190, 170);
        chartPanelBightness.setSize(570, 350);
        this.add(chartPanelBightness);

        if (modification) {
            JLabel gMinLabel = new JLabel("g Min");
            gMinLabel.setLocation(1200, 30);
            gMinLabel.setSize(300, 20);
            this.add(gMinLabel);

            gMinSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
            gMinSlider.setMajorTickSpacing(51);
            gMinSlider.setMinorTickSpacing(10);
            gMinSlider.setPaintTicks(true);
            gMinSlider.setPaintLabels(true);
            gMinSlider.setLocation(1250, 5);
            gMinSlider.setSize(500, 100);
            gMinSlider.addChangeListener(this);
            this.add(gMinSlider);

            JLabel gMaxLabel = new JLabel("g Max");
            gMaxLabel.setLocation(1200, 105);
            gMaxLabel.setSize(300, 20);
            this.add(gMaxLabel);

            gMaxSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 255);
            gMaxSlider.setMajorTickSpacing(51);
            gMaxSlider.setMinorTickSpacing(10);
            gMaxSlider.setPaintTicks(true);
            gMaxSlider.setPaintLabels(true);
            gMaxSlider.setLocation(1250, 80);
            gMaxSlider.setSize(500, 100);
            gMaxSlider.addChangeListener(this);
            this.add(gMaxSlider);
        }

    }

    public void updateTab() {
        originalImage = chosenImage.getBufferedImage();

        if (modification) {
            copyImage = chosenImage.getBufferedImage();
            histogramOriginal = modifyHistogram.createHistogramImage(copyImage);

            modifyHistogram.createModificationTable(originalImage);
            copyImage = modifyHistogram.changeImage(originalImage);
            copyPanel.setBufferedImage(copyImage);
            // originalImage = copyImage; //??
            histogramOriginal = modifyHistogram.createHistogramImage(copyImage);
            copyPanel.repaint();
        } else {

            histogramOriginal = modifyHistogram.createHistogramImage(originalImage);
        }
        datasetRed.addSeries("Czerwony", histogramOriginal[0]);
        datasetGreen.addSeries("Zielony", histogramOriginal[1]);
        datasetBlue.addSeries("Niebieski", histogramOriginal[2]);
        datasetBightness.addSeries("Jasność", histogramOriginal[3]);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == gMinSlider) {
            modifyHistogram.setGmin(gMinSlider.getValue());
            updateTab();
        } else if (event.getSource() == gMaxSlider) {
            modifyHistogram.setGmax(gMaxSlider.getValue());
            updateTab();
        }
    }

    public BufferedImagePanel geChosenImage() {
        return chosenImage;
    }

    public void setChosenImage(BufferedImagePanel chosenImage) {
        this.chosenImage = chosenImage;
    }

    public BufferedImagePanel getCopyPanel() {
        return copyPanel;
    }

    public void setCopyPanel(BufferedImagePanel copyPanel) {
        this.copyPanel = copyPanel;
    }
}
