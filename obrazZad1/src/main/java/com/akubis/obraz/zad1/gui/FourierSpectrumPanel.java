package com.akubis.obraz.zad1.gui;

import com.akubis.obraz.zad1.processing.FourierProcesor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jtransforms.fft.DoubleFFT_2D;

public class FourierSpectrumPanel extends JPanel {

    private BufferedImagePanel chosenImage;
    private BufferedImagePanel powerPanel;
    private BufferedImagePanel phasePanel;
    private BufferedImage originalImage;
    private BufferedImage powerImage;
    private BufferedImage phaseImage;
    private FourierProcesor fourierProcesor;
    private double[][] outputRed;
    private double[][] outputGreen;
    private double[][] outputBlue;

    public FourierSpectrumPanel(BufferedImagePanel chosenPanelTab) throws CloneNotSupportedException {
        this.chosenImage = chosenPanelTab;
        fourierProcesor = new FourierProcesor();
        createTab();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
    }

    private void createTab() throws CloneNotSupportedException {
        JLabel originalLabel = new JLabel("Obraz oryginalny");
        JLabel powerLabel = new JLabel("Widmo mocy");
        JLabel phaseLabel = new JLabel("Widmo fazy");
        originalLabel.setLocation(10, 5);
        powerLabel.setLocation(560, 5);
        phaseLabel.setLocation(1110, 5);
        originalLabel.setSize(300, 20);
        powerLabel.setSize(300, 20);
        phaseLabel.setSize(300, 20);
        originalLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        powerLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        phaseLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        this.add(originalLabel);
        this.add(powerLabel);
        this.add(phaseLabel);

        chosenImage.setLocation(10, 30);
        this.add(chosenImage);
        originalImage = chosenImage.getBufferedImage();

        fourierOperations();

        powerPanel = new BufferedImagePanel(powerImage, originalImage.getWidth(), originalImage.getHeight());
        powerPanel.setBufferedImage(powerImage);
        powerPanel.repaint();
        powerPanel.setLocation(560, 30);
        this.add(powerPanel);

        phasePanel = new BufferedImagePanel(phaseImage, originalImage.getWidth(), originalImage.getHeight());
        phasePanel.setBufferedImage(phaseImage);
        phasePanel.repaint();
        phasePanel.setLocation(1110, 30);
        this.add(phasePanel);
    }

        public void updateTab() {
        originalImage = chosenImage.getBufferedImage();

        fourierOperations();
        powerPanel.setBufferedImage(powerImage);
        powerPanel.repaint();
                phasePanel.setBufferedImage(phaseImage);
        phasePanel.repaint();
    }
    
    private void fourierOperations() {
        outputRed = fourierProcesor.preparePixelsTable(originalImage, 0);
        outputGreen = fourierProcesor.preparePixelsTable(originalImage, 1);
        outputBlue = fourierProcesor.preparePixelsTable(originalImage, 2);

        //fourier
        DoubleFFT_2D dfftd = new DoubleFFT_2D(originalImage.getHeight(), originalImage.getWidth());
        dfftd.complexForward(outputRed);
        dfftd.complexForward(outputGreen);
        dfftd.complexForward(outputBlue);

        double[][] maskPhaseRed = fourierProcesor.countPhaseSpectrum(outputRed, originalImage.getWidth(), originalImage.getHeight());
        double[][] maskPowerRed = fourierProcesor.countPowerSpectrum(outputRed, originalImage.getWidth(), originalImage.getHeight());
        double[][] maskPhaseGreen = fourierProcesor.countPhaseSpectrum(outputGreen, originalImage.getWidth(), originalImage.getHeight());
        double[][] maskPowerGreen = fourierProcesor.countPowerSpectrum(outputGreen, originalImage.getWidth(), originalImage.getHeight());
        double[][] maskPhaseBlue = fourierProcesor.countPhaseSpectrum(outputBlue, originalImage.getWidth(), originalImage.getHeight());
        double[][] maskPowerBlue = fourierProcesor.countPowerSpectrum(outputBlue, originalImage.getWidth(), originalImage.getHeight());

        maskPowerRed = fourierProcesor.logarithmNormalize(maskPowerRed, originalImage.getWidth(), originalImage.getHeight());
        maskPhaseRed = fourierProcesor.linearNormalize(maskPhaseRed, originalImage.getWidth(), originalImage.getHeight());
        maskPowerGreen = fourierProcesor.logarithmNormalize(maskPowerGreen, originalImage.getWidth(), originalImage.getHeight());
        maskPhaseGreen = fourierProcesor.linearNormalize(maskPhaseGreen, originalImage.getWidth(), originalImage.getHeight());
        maskPowerBlue = fourierProcesor.logarithmNormalize(maskPowerBlue, originalImage.getWidth(), originalImage.getHeight());
        maskPhaseBlue = fourierProcesor.linearNormalize(maskPhaseBlue, originalImage.getWidth(), originalImage.getHeight());

        maskPowerRed = fourierProcesor.invertQuadrants(maskPowerRed, originalImage.getWidth(), originalImage.getHeight());
        maskPhaseRed = fourierProcesor.invertQuadrants(maskPhaseRed, originalImage.getWidth(), originalImage.getHeight());
        maskPowerGreen = fourierProcesor.invertQuadrants(maskPowerGreen, originalImage.getWidth(), originalImage.getHeight());
        maskPhaseGreen = fourierProcesor.invertQuadrants(maskPhaseGreen, originalImage.getWidth(), originalImage.getHeight());
        maskPowerBlue = fourierProcesor.invertQuadrants(maskPowerBlue, originalImage.getWidth(), originalImage.getHeight());
        maskPhaseBlue = fourierProcesor.invertQuadrants(maskPhaseBlue, originalImage.getWidth(), originalImage.getHeight());

        powerImage = fourierProcesor.createResultImage(maskPowerRed, maskPowerGreen, maskPowerBlue, originalImage.getWidth(), originalImage.getHeight(), originalImage.getType(), 1);
        phaseImage = fourierProcesor.createResultImage(maskPhaseRed, maskPhaseGreen, maskPhaseBlue, originalImage.getWidth(), originalImage.getHeight(), originalImage.getType(), 1);
    }

    public FourierProcesor getFourierProcesor() {
        return fourierProcesor;
    }

    public void setFourierProcesor(FourierProcesor fourierProcesor) {
        this.fourierProcesor = fourierProcesor;
    }
    
        public BufferedImagePanel geChosenImage() {
        return chosenImage;
    }

    public void setChosenImage(BufferedImagePanel chosenImage) {
        this.chosenImage = chosenImage;
    }
}
