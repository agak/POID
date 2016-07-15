package com.akubis.obraz.zad1.gui;

import com.akubis.obraz.zad1.processing.FourierProcesor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jtransforms.fft.DoubleFFT_2D;

public class FourierPanel extends JPanel {

    private BufferedImagePanel chosenImage;
    private BufferedImagePanel copyPanel;
    private BufferedImage originalImage;
    private BufferedImage copyImage;
    private FourierProcesor fourierProcesor;
    private double[][] outputRed;
    private double[][] outputGreen;
    private double[][] outputBlue;

    public FourierPanel(BufferedImagePanel chosenPanelTab) throws CloneNotSupportedException {
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
        JLabel fourierLabel = new JLabel("Obraz po transformacie");
        originalLabel.setLocation(10, 5);
        fourierLabel.setLocation(560, 5);
        originalLabel.setSize(300, 20);
        fourierLabel.setSize(300, 20);
        originalLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        fourierLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        this.add(originalLabel);
        this.add(fourierLabel);

        chosenImage.setLocation(10, 30);
        this.add(chosenImage);
        originalImage = chosenImage.getBufferedImage();

        fourierOperations();

        copyPanel = new BufferedImagePanel(copyImage, originalImage.getWidth(), originalImage.getHeight());
        copyPanel.setBufferedImage(copyImage);
        copyPanel.repaint();
        copyPanel.setLocation(560, 30);
        this.add(copyPanel);
    }

    public void updateTab() {
        originalImage = chosenImage.getBufferedImage();

        fourierOperations();
        copyPanel.setBufferedImage(copyImage);
        copyPanel.repaint();
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

        dfftd.complexInverse(outputRed, true);
        dfftd.complexInverse(outputGreen, true);
        dfftd.complexInverse(outputBlue, true);

        copyImage = fourierProcesor.createResultImage(outputRed, outputGreen, outputBlue, originalImage.getWidth(), originalImage.getHeight(), originalImage.getType(), 2);
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

    public BufferedImagePanel getCopyPanel() {
        return copyPanel;
    }

    public void setCopyPanel(BufferedImagePanel copyPanel) {
        this.copyPanel = copyPanel;
    }
}
