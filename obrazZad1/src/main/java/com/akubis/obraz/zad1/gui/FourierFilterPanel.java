package com.akubis.obraz.zad1.gui;

import com.akubis.obraz.zad1.processing.FourierProcesor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jtransforms.fft.DoubleFFT_2D;

public class FourierFilterPanel extends JPanel implements ActionListener, ChangeListener {

    private ButtonGroup filterGroup;
    private JRadioButton lowPassRadio;
    private JRadioButton highPassRadio;
    private JRadioButton bandpassBarrageRadio;
    private JRadioButton edgeDetectionRadio;
    private JRadioButton phaseModificationRadio;
    private JButton saveButton;
    private JSlider radiusSlider;
    private JSlider ringSlider;
    private JSlider kSlider;
    private JSlider lSlider;
    private JSlider startPointSlider;
    private JSlider shiftSlider;
    private JLabel radiusLabel;
    private JLabel ringLabel;
    private JLabel kLabel;
    private JLabel lLabel;
    private JLabel startPointLabel;
    private JLabel shiftLabel;

    private BufferedImagePanel chosenImage;
    private BufferedImagePanel maskPanel;
    private BufferedImagePanel resultPanel;
    private BufferedImage originalImage;
    private BufferedImage maskImage;
    private BufferedImage resultImage;
    private FourierProcesor fourierProcesor;
    private double[][] outputRed;
    private double[][] outputGreen;
    private double[][] outputBlue;

    public FourierFilterPanel(BufferedImagePanel chosenPanelTab) throws CloneNotSupportedException {
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
        JLabel maskLabel = new JLabel("Maska widma obrazu");
        JLabel filterLabel = new JLabel("Obraz po zastosowaniu filtru");
        originalLabel.setLocation(10, 5);
        maskLabel.setLocation(560, 5);
        filterLabel.setLocation(1110, 5);
        originalLabel.setSize(300, 20);
        maskLabel.setSize(300, 20);
        filterLabel.setSize(300, 20);
        originalLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        maskLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        filterLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        this.add(originalLabel);
        this.add(maskLabel);
        this.add(filterLabel);

        JLabel lowPassLabel = new JLabel("Filtr dolnoprzepustowy");
        JLabel highPassLabel = new JLabel("Filtr górnoprzepustowy");
        JLabel bandpassBarrageLabel = new JLabel("Filtr pasmozaporowy");
        JLabel edgeDetectionLabel = new JLabel("Filtr z detekcją krawędzi");
        JLabel phaseModificationLabel = new JLabel("Filtr modyfikujący fazę");
        lowPassLabel.setLocation(50, 570);
        highPassLabel.setLocation(50, 600);
        bandpassBarrageLabel.setLocation(50, 630);
        edgeDetectionLabel.setLocation(50, 660);
        phaseModificationLabel.setLocation(50, 710);
        lowPassLabel.setSize(300, 20);
        highPassLabel.setSize(300, 20);
        bandpassBarrageLabel.setSize(300, 20);
        edgeDetectionLabel.setSize(300, 20);
        phaseModificationLabel.setSize(300, 20);
        lowPassLabel.setFont(new Font("Dialog", Font.BOLD, 13));
        highPassLabel.setFont(new Font("Dialog", Font.BOLD, 13));
        bandpassBarrageLabel.setFont(new Font("Dialog", Font.BOLD, 13));
        edgeDetectionLabel.setFont(new Font("Dialog", Font.BOLD, 13));
        phaseModificationLabel.setFont(new Font("Dialog", Font.BOLD, 13));
        this.add(lowPassLabel);
        this.add(highPassLabel);
        this.add(bandpassBarrageLabel);
        this.add(edgeDetectionLabel);
        this.add(phaseModificationLabel);

        lowPassRadio = new JRadioButton();
        lowPassRadio.setLocation(20, 570);
        lowPassRadio.setSize(20, 20);
        lowPassRadio.setSelected(true);
        this.add(lowPassRadio);
        lowPassRadio.addActionListener(this);
        lowPassRadio.setActionCommand("lowPass");

        highPassRadio = new JRadioButton();
        highPassRadio.setLocation(20, 600);
        highPassRadio.setSize(20, 20);
        this.add(highPassRadio);
        highPassRadio.addActionListener(this);
        highPassRadio.setActionCommand("highPass");

        bandpassBarrageRadio = new JRadioButton();
        bandpassBarrageRadio.setLocation(20, 630);
        bandpassBarrageRadio.setSize(20, 20);
        this.add(bandpassBarrageRadio);
        bandpassBarrageRadio.addActionListener(this);
        bandpassBarrageRadio.setActionCommand("bandpassBarrage");

        edgeDetectionRadio = new JRadioButton();
        edgeDetectionRadio.setLocation(20, 660);
        edgeDetectionRadio.setSize(20, 20);
        this.add(edgeDetectionRadio);
        edgeDetectionRadio.addActionListener(this);
        edgeDetectionRadio.setActionCommand("edgeDetection");

        phaseModificationRadio = new JRadioButton();
        phaseModificationRadio.setLocation(20, 710);
        phaseModificationRadio.setSize(20, 20);
        this.add(phaseModificationRadio);
        phaseModificationRadio.addActionListener(this);
        phaseModificationRadio.setActionCommand("phaseModification");

        filterGroup = new ButtonGroup();
        filterGroup.add(lowPassRadio);
        filterGroup.add(highPassRadio);
        filterGroup.add(bandpassBarrageRadio);
        filterGroup.add(edgeDetectionRadio);
        filterGroup.add(phaseModificationRadio);

        saveButton = new JButton("Zapisz obraz");
        saveButton.setLocation(1130, 570);
        saveButton.setSize(200, 50);
        saveButton.addActionListener(this);
        this.add(saveButton);

        radiusSlider = new JSlider(JSlider.HORIZONTAL, 0, 256, 40);
        radiusSlider.setMajorTickSpacing(50);
        radiusSlider.setMinorTickSpacing(10);
        radiusSlider.setPaintTicks(true);
        radiusSlider.setPaintLabels(true);
        radiusSlider.setLocation(660, 565);
        radiusSlider.setSize(400, 100);
        radiusSlider.addChangeListener(this);
        this.add(radiusSlider);

        ringSlider = new JSlider(JSlider.HORIZONTAL, 0, 256, 10);
        ringSlider.setMajorTickSpacing(50);
        ringSlider.setMinorTickSpacing(10);
        ringSlider.setPaintTicks(true);
        ringSlider.setPaintLabels(true);
        ringSlider.setLocation(660, 645);
        ringSlider.setSize(400, 100);
        ringSlider.addChangeListener(this);
        this.add(ringSlider);

        kSlider = new JSlider(JSlider.HORIZONTAL, 0, 512, 20);
        kSlider.setMajorTickSpacing(50);
        kSlider.setMinorTickSpacing(10);
        kSlider.setPaintTicks(true);
        kSlider.setPaintLabels(true);
        kSlider.setLocation(150, 725);
        kSlider.setSize(372, 100);
        kSlider.addChangeListener(this);
        this.add(kSlider);

        lSlider = new JSlider(JSlider.HORIZONTAL, 0, 512, 150);
        lSlider.setMajorTickSpacing(50);
        lSlider.setMinorTickSpacing(10);
        lSlider.setPaintTicks(true);
        lSlider.setPaintLabels(true);
        lSlider.setLocation(150, 805);
        lSlider.setSize(372, 100);
        lSlider.addChangeListener(this);
        this.add(lSlider);

        startPointSlider = new JSlider(JSlider.HORIZONTAL, 0, 1024, 240);
        startPointSlider.setMajorTickSpacing(200);
        startPointSlider.setMinorTickSpacing(20);
        startPointSlider.setPaintTicks(true);
        startPointSlider.setPaintLabels(true);
        startPointSlider.setLocation(660, 725);
        startPointSlider.setSize(400, 100);
        startPointSlider.addChangeListener(this);
        this.add(startPointSlider);

        shiftSlider = new JSlider(JSlider.HORIZONTAL, 0, 1024, 40);
        shiftSlider.setMajorTickSpacing(200);
        shiftSlider.setMinorTickSpacing(20);
        shiftSlider.setPaintTicks(true);
        shiftSlider.setPaintLabels(true);
        shiftSlider.setLocation(660, 805);
        shiftSlider.setSize(400, 100);
        shiftSlider.addChangeListener(this);
        this.add(shiftSlider);

        radiusLabel = new JLabel("Promień: " + fourierProcesor.getRadius());
        radiusLabel.setLocation(560, 590);
        radiusLabel.setSize(512, 20);
        this.add(radiusLabel);

        ringLabel = new JLabel("Pierścień: " + fourierProcesor.getRing());
        ringLabel.setLocation(560, 670);
        ringLabel.setSize(512, 20);
        this.add(ringLabel);

        kLabel = new JLabel("Poziomo: " + fourierProcesor.getK());
        kLabel.setLocation(50, 750);
        kLabel.setSize(512, 20);
        this.add(kLabel);

        lLabel = new JLabel("Pionowo: " + fourierProcesor.getL());
        lLabel.setLocation(50, 830);
        lLabel.setSize(512, 20);
        this.add(lLabel);

        startPointLabel = new JLabel("Kierunek: " + fourierProcesor.getStartPoint());
        startPointLabel.setLocation(560, 750);
        startPointLabel.setSize(512, 20);
        this.add(startPointLabel);

        shiftLabel = new JLabel("Szerokość: " + fourierProcesor.getShift());
        shiftLabel.setLocation(560, 830);
        shiftLabel.setSize(512, 20);
        this.add(shiftLabel);

        chosenImage.setLocation(10, 30);
        this.add(chosenImage);
        originalImage = chosenImage.getBufferedImage();

        maskPanel = new BufferedImagePanel(maskImage, originalImage.getWidth(), originalImage.getHeight());
        maskPanel.setBufferedImage(maskImage);
        maskPanel.setLocation(560, 30);
        this.add(maskPanel);

        resultPanel = new BufferedImagePanel(resultImage, originalImage.getWidth(), originalImage.getHeight());
        resultPanel.setBufferedImage(resultImage);
        resultPanel.setLocation(1110, 30);
        this.add(resultPanel);

        createFourierFilter("lowPass");
    }

    void updateTab() {
        originalImage = chosenImage.getBufferedImage();
        lowPassRadio.setSelected(true);
        createFourierFilter("lowPass");
    }

    private void createFourierFilter(String filterMethod) {
        outputRed = fourierProcesor.preparePixelsTable(originalImage, 0);
        outputGreen = fourierProcesor.preparePixelsTable(originalImage, 1);
        outputBlue = fourierProcesor.preparePixelsTable(originalImage, 2);

        //fourier
        DoubleFFT_2D dfftd = new DoubleFFT_2D(originalImage.getHeight(), originalImage.getWidth());
        dfftd.complexForward(outputRed);
        dfftd.complexForward(outputGreen);
        dfftd.complexForward(outputBlue);

        double[][] maskPowerRed = fourierProcesor.countPowerSpectrum(outputRed, originalImage.getWidth(), originalImage.getHeight());
        double[][] maskPowerGreen = fourierProcesor.countPowerSpectrum(outputGreen, originalImage.getWidth(), originalImage.getHeight());
        double[][] maskPowerBlue = fourierProcesor.countPowerSpectrum(outputBlue, originalImage.getWidth(), originalImage.getHeight());

        maskPowerRed = fourierProcesor.logarithmNormalize(maskPowerRed, originalImage.getWidth(), originalImage.getHeight());
        maskPowerGreen = fourierProcesor.logarithmNormalize(maskPowerGreen, originalImage.getWidth(), originalImage.getHeight());
        maskPowerBlue = fourierProcesor.logarithmNormalize(maskPowerBlue, originalImage.getWidth(), originalImage.getHeight());

        maskPowerRed = fourierProcesor.invertQuadrants(maskPowerRed, originalImage.getWidth(), originalImage.getHeight());
        maskPowerGreen = fourierProcesor.invertQuadrants(maskPowerGreen, originalImage.getWidth(), originalImage.getHeight());
        maskPowerBlue = fourierProcesor.invertQuadrants(maskPowerBlue, originalImage.getWidth(), originalImage.getHeight());

        outputRed = fourierProcesor.invertQuadrants(outputRed, originalImage.getWidth() * 2, originalImage.getHeight());
        outputGreen = fourierProcesor.invertQuadrants(outputGreen, originalImage.getWidth() * 2, originalImage.getHeight());
        outputBlue = fourierProcesor.invertQuadrants(outputBlue, originalImage.getWidth() * 2, originalImage.getHeight());

        double constantRed = outputRed[originalImage.getHeight() / 2][originalImage.getWidth()];
        double constantGreen = outputGreen[originalImage.getHeight() / 2][originalImage.getWidth()];
        double constantBlue = outputBlue[originalImage.getHeight() / 2][originalImage.getWidth()];

        switch (filterMethod) {
            case "lowPass":
                outputRed = fourierProcesor.lowPassFilter(outputRed, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                maskPowerRed = fourierProcesor.lowPassFilter(maskPowerRed, originalImage.getWidth(), originalImage.getHeight(), 1);
                outputGreen = fourierProcesor.lowPassFilter(outputGreen, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                maskPowerGreen = fourierProcesor.lowPassFilter(maskPowerGreen, originalImage.getWidth(), originalImage.getHeight(), 1);
                outputBlue = fourierProcesor.lowPassFilter(outputBlue, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                maskPowerBlue = fourierProcesor.lowPassFilter(maskPowerBlue, originalImage.getWidth(), originalImage.getHeight(), 1);
                break;
            case "highPass":

                outputRed = fourierProcesor.highPassFilter(outputRed, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                maskPowerRed = fourierProcesor.highPassFilter(maskPowerRed, originalImage.getWidth(), originalImage.getHeight(), 1);
                outputGreen = fourierProcesor.highPassFilter(outputGreen, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                maskPowerGreen = fourierProcesor.highPassFilter(maskPowerGreen, originalImage.getWidth(), originalImage.getHeight(), 1);
                outputBlue = fourierProcesor.highPassFilter(outputBlue, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                maskPowerBlue = fourierProcesor.highPassFilter(maskPowerBlue, originalImage.getWidth(), originalImage.getHeight(), 1);

                outputRed[originalImage.getHeight() / 2][originalImage.getWidth()] = constantRed;
                outputGreen[originalImage.getHeight() / 2][originalImage.getWidth()] = constantGreen;
                outputBlue[originalImage.getHeight() / 2][originalImage.getWidth()] = constantBlue;
                break;
            case "bandpassBarrage":
                outputRed = fourierProcesor.bandpassBarrageFilter(outputRed, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                maskPowerRed = fourierProcesor.bandpassBarrageFilter(maskPowerRed, originalImage.getWidth(), originalImage.getHeight(), 1);
                outputGreen = fourierProcesor.bandpassBarrageFilter(outputGreen, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                maskPowerGreen = fourierProcesor.bandpassBarrageFilter(maskPowerGreen, originalImage.getWidth(), originalImage.getHeight(), 1);
                outputBlue = fourierProcesor.bandpassBarrageFilter(outputBlue, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                maskPowerBlue = fourierProcesor.bandpassBarrageFilter(maskPowerBlue, originalImage.getWidth(), originalImage.getHeight(), 1);
                break;
            case "edgeDetection":
                outputRed = fourierProcesor.edgeDetectionFilter(outputRed, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                maskPowerRed = fourierProcesor.edgeDetectionFilter(maskPowerRed, originalImage.getWidth(), originalImage.getHeight(), 1);
                outputGreen = fourierProcesor.edgeDetectionFilter(outputGreen, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                maskPowerGreen = fourierProcesor.edgeDetectionFilter(maskPowerGreen, originalImage.getWidth(), originalImage.getHeight(), 1);
                outputBlue = fourierProcesor.edgeDetectionFilter(outputBlue, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                maskPowerBlue = fourierProcesor.edgeDetectionFilter(maskPowerBlue, originalImage.getWidth(), originalImage.getHeight(), 1);

                outputRed[originalImage.getHeight() / 2][originalImage.getWidth()] = constantRed;
                outputGreen[originalImage.getHeight() / 2][originalImage.getWidth()] = constantGreen;
                outputBlue[originalImage.getHeight() / 2][originalImage.getWidth()] = constantBlue;
                break;
            case "phaseModification":
                outputRed = fourierProcesor.phaseModificationFilter(outputRed, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                //    maskPowerRed = fourierProcesor.phaseModificationFilter(maskPowerRed, originalImage.getWidth(), originalImage.getHeight(), 1);
                outputGreen = fourierProcesor.phaseModificationFilter(outputGreen, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                //    maskPowerGreen = fourierProcesor.phaseModificationFilter(maskPowerGreen, originalImage.getWidth(), originalImage.getHeight(), 1);
                outputBlue = fourierProcesor.phaseModificationFilter(outputBlue, originalImage.getWidth() * 2, originalImage.getHeight(), 2);
                //    maskPowerBlue = fourierProcesor.phaseModificationFilter(maskPowerBlue, originalImage.getWidth(), originalImage.getHeight(), 1);
                break;
        }

        outputRed = fourierProcesor.invertQuadrants(outputRed, originalImage.getWidth() * 2, originalImage.getHeight());
        outputGreen = fourierProcesor.invertQuadrants(outputGreen, originalImage.getWidth() * 2, originalImage.getHeight());
        outputBlue = fourierProcesor.invertQuadrants(outputBlue, originalImage.getWidth() * 2, originalImage.getHeight());

        dfftd.complexInverse(outputRed, true);
        dfftd.complexInverse(outputGreen, true);
        dfftd.complexInverse(outputBlue, true);

        outputRed = fourierProcesor.edgeNormalize(outputRed, originalImage.getWidth() * 2, originalImage.getHeight());
        outputGreen = fourierProcesor.edgeNormalize(outputGreen, originalImage.getWidth() * 2, originalImage.getHeight());
        outputBlue = fourierProcesor.edgeNormalize(outputBlue, originalImage.getWidth() * 2, originalImage.getHeight());

        maskImage = fourierProcesor.createResultImage(maskPowerRed, maskPowerGreen, maskPowerBlue, originalImage.getWidth(), originalImage.getHeight(), originalImage.getType(), 1);
        resultImage = fourierProcesor.createResultImage(outputRed, outputGreen, outputBlue, originalImage.getWidth(), originalImage.getHeight(), originalImage.getType(), 2);

        maskPanel.setBufferedImage(maskImage);
        resultPanel.setBufferedImage(resultImage);
        maskPanel.repaint();
        resultPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == lowPassRadio || event.getSource() == highPassRadio || event.getSource() == bandpassBarrageRadio || event.getSource() == edgeDetectionRadio || event.getSource() == phaseModificationRadio) {
            createFourierFilter(filterGroup.getSelection().getActionCommand());
        } else if (event.getSource() == saveButton) {
            File savedImage = new File("savedImage.png");
            try {
                ImageIO.write(resultImage, "png", savedImage);
            } catch (IOException ex) {
                Logger.getLogger(FourierFilterPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == radiusSlider) {
            fourierProcesor.setRadius(radiusSlider.getValue());
            createFourierFilter(filterGroup.getSelection().getActionCommand());
            radiusLabel.setText("Promień: " + fourierProcesor.getRadius());
        } else if (event.getSource() == ringSlider) {
            bandpassBarrageRadio.setSelected(true);
            fourierProcesor.setRing(ringSlider.getValue());
            createFourierFilter(filterGroup.getSelection().getActionCommand());
            ringLabel.setText("Pierścień: " + fourierProcesor.getRing());
        } else if (event.getSource() == kSlider) {
            phaseModificationRadio.setSelected(true);
            fourierProcesor.setK(kSlider.getValue());
            createFourierFilter(filterGroup.getSelection().getActionCommand());
            kLabel.setText("Poziomo: " + fourierProcesor.getK());
        } else if (event.getSource() == lSlider) {
            phaseModificationRadio.setSelected(true);
            fourierProcesor.setL(lSlider.getValue());
            createFourierFilter(filterGroup.getSelection().getActionCommand());
            lLabel.setText("Pionowo: " + fourierProcesor.getL());
        } else if (event.getSource() == startPointSlider) {
            edgeDetectionRadio.setSelected(true);
            fourierProcesor.setStartPoint(startPointSlider.getValue());
            createFourierFilter(filterGroup.getSelection().getActionCommand());
            startPointLabel.setText("Kierunek: " + fourierProcesor.getStartPoint());

        } else if (event.getSource() == shiftSlider) {
            edgeDetectionRadio.setSelected(true);
            fourierProcesor.setShift(shiftSlider.getValue());
            createFourierFilter(filterGroup.getSelection().getActionCommand());
            shiftLabel.setText("Szerokość: " + fourierProcesor.getShift());
        }
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
