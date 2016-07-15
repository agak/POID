package com.akubis.obraz.zad1.gui;

import com.akubis.obraz.zad1.error.MeanSquaredError;
import com.akubis.obraz.zad1.processing.Brightness;
import com.akubis.obraz.zad1.processing.Contrast;
import com.akubis.obraz.zad1.processing.FilterArithmeticMean;
import com.akubis.obraz.zad1.processing.FilterIdentifyLines;
import com.akubis.obraz.zad1.processing.FilterMedian;
import com.akubis.obraz.zad1.processing.FilterUolisOperator;
import com.akubis.obraz.zad1.processing.Negative;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//@Getter
//@Setter
public class BaseOperationPanel extends JPanel implements ChangeListener, ActionListener {

    private BufferedImagePanel chosenImage;
    private BufferedImagePanel copyPanel;
    private JSlider brightnessSlider;
    private JSlider contrastSlider;
    private JButton negativeButton;
    private Brightness brightness;
    private Negative negative;
    private Contrast contrast;
    private BufferedImage originalImage;
    private BufferedImage copyImage;
    private JButton filterArithmeticMeanButton;
    private JButton filterMedianButton;
    private JButton filterIdentifyLinesButton;
    private JComboBox maskDimesionList;
    private FilterArithmeticMean filterArithmeticMean;
    private FilterMedian filterMedian;
    private FilterIdentifyLines filterIdentifyLines;
    private FilterUolisOperator filterUolisOperator;
    private ButtonGroup matrixGroup;
    private JButton loadOriginal;
    private JButton meanSquaredErrorButton;
    private final MainWindow mainWindow;
    private JButton filterUolisOperatorButton;
    private String directoryImageUrl;
    private MeanSquaredError meanSquaredError;
    private JLabel imageNotNoisy;
    private final List<JLabel> channels;
    private JPanel meanSquaredErrorPanel;

    public BaseOperationPanel(BufferedImagePanel chosenPanelTab, MainWindow mainWindow) {
        this.chosenImage = chosenPanelTab;
        this.mainWindow = mainWindow;
        channels = new ArrayList();
        createTab();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
    }

    private void createTab() {
        chosenImage.setLocation(10, 10);
        this.add(chosenImage);

        brightness = new Brightness();
        negative = new Negative();
        contrast = new Contrast();
        filterIdentifyLines = new FilterIdentifyLines();
        filterArithmeticMean = new FilterArithmeticMean();
        filterMedian = new FilterMedian();
        filterUolisOperator = new FilterUolisOperator();
        meanSquaredError = new MeanSquaredError();

        try {
            copyPanel = (BufferedImagePanel) chosenImage.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(BaseOperationPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.add(copyPanel);
        copyPanel.setLocation(560, 10);

        JLabel basicOparationLabel = new JLabel("Podstawowe operacje:");
        basicOparationLabel.setLocation(20, 550);
        basicOparationLabel.setSize(300, 20);
        this.add(basicOparationLabel);

        JLabel brightnessLabel = new JLabel("Zmiana jasności");
        brightnessLabel.setLocation(50, 590);
        brightnessLabel.setSize(300, 20);
        this.add(brightnessLabel);

        brightnessSlider = new JSlider(JSlider.HORIZONTAL, -255, 255, 0);

        brightnessSlider.setMajorTickSpacing(51);
        brightnessSlider.setMinorTickSpacing(10);
        brightnessSlider.setPaintTicks(true);
        brightnessSlider.setPaintLabels(true);
        brightnessSlider.setLocation(160, 565);
        brightnessSlider.setSize(500, 100);
        brightnessSlider.addChangeListener(this);
        this.add(brightnessSlider);

        JLabel contrastLabel = new JLabel("Zmiana kontrastu");
        contrastLabel.setLocation(50, 670);
        contrastLabel.setSize(300, 20);
        this.add(contrastLabel);

        contrastSlider = new JSlider(JSlider.HORIZONTAL, 0, 1500, 100);

        Hashtable<Integer, JLabel> contrastValues = new Hashtable<>();
        contrastValues.put(0, new JLabel("O"));
        for (int i = 0; i < 1501; i = i + 100) {
            contrastValues.put(i, new JLabel(Integer.toString(i / 100)));
        }

        contrastSlider.setLabelTable(contrastValues);

        contrastSlider.setPaintTicks(true);
        contrastSlider.setPaintLabels(true);
        contrastSlider.setLocation(160, 645);
        contrastSlider.setSize(500, 100);
        contrastSlider.addChangeListener(this);
        this.add(contrastSlider);

        negativeButton = new JButton("Negatyw");
        negativeButton.setLocation(50, 750);
        negativeButton.setSize(100, 50);
        negativeButton.addActionListener(this);
        this.add(negativeButton);

        //Filtracja liniowa
        JLabel linearFiltrationLabel = new JLabel("Filtracja liniowa:");
        linearFiltrationLabel.setLocation(1100, 10);
        linearFiltrationLabel.setSize(300, 20);
        this.add(linearFiltrationLabel);

        JLabel textMaskDimesion = new JLabel("Rozmiar maski filtru:");
        textMaskDimesion.setLocation(1130, 60);
        textMaskDimesion.setSize(300, 20);
        add(textMaskDimesion);

        String[] maskDimesionStrings = {"3x3", "5x5", "7x7"};
        maskDimesionList = new JComboBox(maskDimesionStrings);
        maskDimesionList.setSelectedIndex(0);
        maskDimesionList.setLocation(1260, 40);
        maskDimesionList.setSize(70, 50);
        maskDimesionList.addActionListener(this);
        add(maskDimesionList);

        JLabel textMaskDimesionPixel = new JLabel("pixeli");
        textMaskDimesionPixel.setLocation(1340, 60);
        textMaskDimesionPixel.setSize(40, 20);
        add(textMaskDimesionPixel);

        filterArithmeticMeanButton = new JButton("Filtr ze średnią artmetyczną");
        filterArithmeticMeanButton.setLocation(1130, 110);
        filterArithmeticMeanButton.setSize(200, 50);
        filterArithmeticMeanButton.addActionListener(this);
        add(filterArithmeticMeanButton);

        filterMedianButton = new JButton("Filtr medianowy");
        filterMedianButton.setLocation(1350, 110);
        filterMedianButton.setSize(200, 50);
        filterMedianButton.addActionListener(this);
        add(filterMedianButton);

        JSeparator jSeparator1 = new JSeparator(JSeparator.HORIZONTAL);
        jSeparator1.setLocation(1100, 200);
        jSeparator1.setSize(650, 10);
        add(jSeparator1);

        JLabel textIdentifyLines = new JLabel("Filtracja liniowa oparta o splot: identyfikowanie linii");
        textIdentifyLines.setLocation(1100, 230);
        textIdentifyLines.setSize(300, 20);
        add(textIdentifyLines);

        MatrixPanel matrixPanel1 = new MatrixPanel(filterIdentifyLines, 0);
        matrixPanel1.setLocation(1160, 280);
        add(matrixPanel1);

        MatrixPanel matrixPanel2 = new MatrixPanel(filterIdentifyLines, 1);
        matrixPanel2.setLocation(1310, 280);
        add(matrixPanel2);

        MatrixPanel matrixPanel3 = new MatrixPanel(filterIdentifyLines, 2);
        matrixPanel3.setLocation(1460, 280);
        add(matrixPanel3);

        MatrixPanel matrixPanel4 = new MatrixPanel(filterIdentifyLines, 3);
        matrixPanel4.setLocation(1610, 280);
        add(matrixPanel4);

        JRadioButton matrix1Radio = new JRadioButton();
        matrix1Radio.setLocation(1130, 280);
        matrix1Radio.setSize(20, 20);
        matrix1Radio.setSelected(true);
        add(matrix1Radio);
        matrix1Radio.addActionListener(this);
        matrix1Radio.setActionCommand("0");

        JRadioButton matrix2Radio = new JRadioButton();
        matrix2Radio.setLocation(1280, 280);
        matrix2Radio.setSize(20, 20);
        matrix2Radio.setSelected(true);
        add(matrix2Radio);
        matrix2Radio.addActionListener(this);
        matrix2Radio.setActionCommand("1");

        JRadioButton matrix3Radio = new JRadioButton();
        matrix3Radio.setLocation(1430, 280);
        matrix3Radio.setSize(20, 20);
        add(matrix3Radio);
        matrix3Radio.addActionListener(this);
        matrix3Radio.setActionCommand("2");

        JRadioButton matrix4Radio = new JRadioButton();
        matrix4Radio.setLocation(1580, 280);
        matrix4Radio.setSize(20, 20);
        add(matrix4Radio);
        matrix4Radio.addActionListener(this);
        matrix4Radio.setActionCommand("3");

        matrixGroup = new ButtonGroup();
        matrixGroup.add(matrix1Radio);
        matrixGroup.add(matrix2Radio);
        matrixGroup.add(matrix3Radio);
        matrixGroup.add(matrix4Radio);

        filterIdentifyLinesButton = new JButton("Przekształć obraz");
        filterIdentifyLinesButton.setLocation(1130, 355);
        filterIdentifyLinesButton.setSize(200, 50);
        filterIdentifyLinesButton.addActionListener(this);
        add(filterIdentifyLinesButton);

        JSeparator jSeparator2 = new JSeparator(JSeparator.HORIZONTAL);
        jSeparator2.setLocation(1100, 440);
        jSeparator2.setSize(650, 10);
        add(jSeparator2);

        loadOriginal = new JButton("Przywróć oryginał");
        loadOriginal.setLocation(870, 550);
        loadOriginal.setSize(200, 50);
        loadOriginal.addActionListener(this);
        this.add(loadOriginal);

        meanSquaredErrorButton = new JButton("Błąd średniokwadratowy");
        meanSquaredErrorButton.setLocation(870, 620);
        meanSquaredErrorButton.setSize(200, 50);
        meanSquaredErrorButton.addActionListener(this);
        this.add(meanSquaredErrorButton);

        meanSquaredErrorPanel=new JPanel();
        meanSquaredErrorPanel.setLayout(null);
        meanSquaredErrorPanel.setSize(800,300 );
        meanSquaredErrorPanel.setLocation(870,680 );
        createChanelLabel();

        //Filtracja liniowal
        JLabel filterUolisOperatornLabel = new JLabel("Filtracja nieliniowa: ");
        filterUolisOperatornLabel.setLocation(1100, 470);
        filterUolisOperatornLabel.setSize(300, 20);
        this.add(filterUolisOperatornLabel);

        filterUolisOperatorButton = new JButton("Operator Uolisa");
        filterUolisOperatorButton.setLocation(1130, 520);
        filterUolisOperatorButton.setSize(200, 50);
        filterUolisOperatorButton.addActionListener(this);
        add(filterUolisOperatorButton);

    }

    public void createChanelLabel() {
        channels.clear();
        meanSquaredErrorPanel.removeAll();
        
        imageNotNoisy = new JLabel("Obraz niezaszumiony:");
        imageNotNoisy.setLocation(0, 0);
        imageNotNoisy.setSize(800, 20);
        meanSquaredErrorPanel.add(imageNotNoisy);
        
        for (int i = 0; i < meanSquaredError.getPixelTableLenght(); i++) {
            JLabel channel = new JLabel("Kanał " + i + ":");
            channel.setLocation(0, i * 20+20);
            channel.setSize(800, 20);
            channels.add(channel);
        }
        for (JLabel channel : channels) {
            meanSquaredErrorPanel.add(channel);
        }
        this.add(meanSquaredErrorPanel);
    }

    private void resetSliders() {
        brightnessSlider.setValue(0);
        brightnessSlider.repaint();
        contrastSlider.setValue(100);
        contrastSlider.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == negativeButton) {
            resetSliders();
            originalImage = chosenImage.getBufferedImage();
            originalImage = negative.changeImage(originalImage);
            copyPanel.setBufferedImage(originalImage);
            copyPanel.repaint();
        } else if (event.getSource() == filterArithmeticMeanButton) {
            resetSliders();
            originalImage = chosenImage.getBufferedImage();
            copyImage = copyPanel.getBufferedImage();
            copyImage = filterArithmeticMean.filter(originalImage, "ArithmeticMean");
            copyPanel.setBufferedImage(copyImage);
            originalImage = copyImage; //??
            copyPanel.repaint();
        } else if (event.getSource() == filterMedianButton) {
            resetSliders();
            originalImage = chosenImage.getBufferedImage();
            copyImage = copyPanel.getBufferedImage();
            copyImage = filterMedian.filter(originalImage, "Median");
            copyPanel.setBufferedImage(copyImage);
            originalImage = copyImage; //??
            copyPanel.repaint();
        } else if (event.getSource() == maskDimesionList) {
            filterArithmeticMean.setMaskDimesion(Integer.parseInt(((String) maskDimesionList.getSelectedItem()).substring(0, 1)));
            filterMedian.setMaskDimesion(Integer.parseInt(((String) maskDimesionList.getSelectedItem()).substring(0, 1)));
        } else if (event.getSource() == filterIdentifyLinesButton) {
            resetSliders();
            originalImage = chosenImage.getBufferedImage();
            copyImage = copyPanel.getBufferedImage();
            filterIdentifyLines.setSelectedMask(Integer.parseInt(matrixGroup.getSelection().getActionCommand()));
            copyImage = filterIdentifyLines.filter(originalImage, "Lines");
            copyPanel.setBufferedImage(copyImage);
            originalImage = copyImage; //??
            copyPanel.repaint();
        } else if (event.getSource() == filterUolisOperatorButton) {
            resetSliders();
            originalImage = chosenImage.getBufferedImage();
            copyImage = copyPanel.getBufferedImage();
            copyImage = filterUolisOperator.filterUolisOperator(originalImage);
            copyPanel.setBufferedImage(copyImage);
            originalImage = copyImage; //??
            copyPanel.repaint();
        } else if (event.getSource() == loadOriginal) {
            mainWindow.updateTabs();
            brightnessSlider.setValue(0);
            brightnessSlider.repaint();
            contrastSlider.setValue(100);
            contrastSlider.repaint();
        } else if (event.getSource() == meanSquaredErrorButton) {
            JFileChooser directoryChooser = new JFileChooser();
            directoryChooser.setCurrentDirectory(new File(directoryImageUrl));
            int returnValue = directoryChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = directoryChooser.getSelectedFile();
                String originalImageUrl = selectedFile.getPath();
                imageNotNoisy.setText("Obraz niezaszumiony: " + originalImageUrl);
                double[] error = meanSquaredError.countError(originalImageUrl, copyPanel.getBufferedImage());
                for (int i = 0; i < channels.size(); i++) {
                    channels.get(i).setText("Kanał " + i + ":     "+error[i]);
                }
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == brightnessSlider) {
            contrastSlider.setValue(100);
            contrastSlider.repaint();
            originalImage = chosenImage.getBufferedImage();
            brightness.setOfffset(brightnessSlider.getValue());
            originalImage = brightness.changeImage(originalImage);
            copyPanel.setBufferedImage(originalImage);
            copyPanel.repaint();
        } else if (event.getSource() == contrastSlider) {
            brightnessSlider.setValue(0);
            brightnessSlider.repaint();
            originalImage = chosenImage.getBufferedImage();
            contrast.setWspolczynniki(contrastSlider.getValue());
            originalImage = contrast.changeImage(originalImage);
            copyPanel.setBufferedImage(originalImage);
            copyPanel.repaint();
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

    public Brightness getBrightness() {
        return brightness;
    }

    public void setBrightness(Brightness brightness) {
        this.brightness = brightness;
    }

    public Negative getNegative() {
        return negative;
    }

    public void setNegative(Negative negative) {
        this.negative = negative;
    }

    public Contrast getContrast() {
        return contrast;
    }

    public void setContrast(Contrast contrast) {
        this.contrast = contrast;
    }

    public FilterArithmeticMean getFilterArithmeticMean() {
        return filterArithmeticMean;
    }

    public void setFilterArithmeticMean(FilterArithmeticMean filterArithmeticMean) {
        this.filterArithmeticMean = filterArithmeticMean;
    }

    public FilterMedian getFilterMedian() {
        return filterMedian;
    }

    public void setFilterMedian(FilterMedian filterMedian) {
        this.filterMedian = filterMedian;
    }

    public FilterIdentifyLines getFilterIdentifyLines() {
        return filterIdentifyLines;
    }

    public void setFilterIdentifyLines(FilterIdentifyLines filterIdentifyLines) {
        this.filterIdentifyLines = filterIdentifyLines;
    }

    public FilterUolisOperator getFilterUolisOperator() {
        return filterUolisOperator;
    }

    public void setFilterUolisOperator(FilterUolisOperator filterUolisOperator) {
        this.filterUolisOperator = filterUolisOperator;
    }

    public String getDirectoryImageUrl() {
        return directoryImageUrl;
    }

    public void setDirectoryImageUrl(String directoryImageUrl) {
        this.directoryImageUrl = directoryImageUrl;
    }

    public MeanSquaredError getMeanSquaredError() {
        return meanSquaredError;
    }

    public void setMeanSquaredError(MeanSquaredError meanSquaredError) {
        this.meanSquaredError = meanSquaredError;
    }
}
