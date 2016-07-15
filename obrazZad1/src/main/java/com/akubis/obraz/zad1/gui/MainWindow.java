package com.akubis.obraz.zad1.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindow extends JFrame implements ChangeListener, ActionListener {

    private JTabbedPane tabbedPane;
    private ChoiceImagePanel tab1;
    private BaseOperationPanel tab2;
    private HistogramPanel tab3;
    private HistogramPanel tab4;
    private FourierPanel tab5;
    private FourierSpectrumPanel tab6;
    private FourierFilterPanel tab7;
    private SegmentationPanel tab8;
    private JPanel mainPanel;
    private JLabel tab1Label;
    private JLabel tab2Label;
    private JLabel tab3Label;
    private JLabel tab4Label;
    private JLabel tab5Label;
    private JLabel tab6Label;
    private JLabel tab7Label;
    private JLabel tab8Label;

    public MainWindow() {
        init();
    }

    private void init() {
        setTitle("Przetwarzanie obrazu");
        setSize(1800, 1000);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);
        addTabbedPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void addTabbedPane() {
        tab1 = new ChoiceImagePanel(this);
        tab1.setLayout(null);
        try {
            BufferedImagePanel toCloneTab1 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
            tab2 = new BaseOperationPanel(toCloneTab1, this);
            tab2.setLayout(null);
            tab2.setDirectoryImageUrl(tab1.getDirectoryImageUrl());
            BufferedImagePanel toCloneTab2 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
            tab3 = new HistogramPanel(toCloneTab2, false);
            tab3.setLayout(null);
            BufferedImagePanel toCloneTab3 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
            tab4 = new HistogramPanel(toCloneTab3, true);
            tab4.setLayout(null);
            BufferedImagePanel toCloneTab4 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
            tab5 = new FourierPanel(toCloneTab4);
            tab5.setLayout(null);
            BufferedImagePanel toCloneTab5 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
            tab6 = new FourierSpectrumPanel(toCloneTab5);
            tab6.setLayout(null);
            BufferedImagePanel toCloneTab6 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
            tab7 = new FourierFilterPanel(toCloneTab6);
            tab7.setLayout(null);
            BufferedImagePanel toCloneTab7 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
            tab8 = new SegmentationPanel(toCloneTab7);
            tab8.setLayout(null);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab(null, tab1);
        tabbedPane.addTab(null, tab2);
        tabbedPane.addTab(null, tab3);
        tabbedPane.addTab(null, tab4);
        tabbedPane.addTab(null, tab5);
        tabbedPane.addTab(null, tab6);
        tabbedPane.addTab(null, tab7);
        tabbedPane.addTab(null, tab8);
        tab1Label = new JLabel("Wybór obrazu");
        tab1Label.setFont(new Font("Dialog", Font.BOLD, 18));
        tab2Label = new JLabel("Podstawowe operacje i filtracja");
        tab2Label.setFont(new Font("Dialog", Font.BOLD, 18));
        tab3Label = new JLabel("Histogram obrazu");
        tab3Label.setFont(new Font("Dialog", Font.BOLD, 18));
        tab4Label = new JLabel("Modyfikacja oparta o histogram obrazu");
        tab4Label.setFont(new Font("Dialog", Font.BOLD, 18));
        tab5Label = new JLabel("Fourier");
        tab5Label.setFont(new Font("Dialog", Font.BOLD, 18));
        tab6Label = new JLabel("Widma");
        tab6Label.setFont(new Font("Dialog", Font.BOLD, 18));
        tab7Label = new JLabel("Filtry");
        tab7Label.setFont(new Font("Dialog", Font.BOLD, 18));
        tab8Label = new JLabel("Segmentacja");
        tab8Label.setFont(new Font("Dialog", Font.BOLD, 18));
        tabbedPane.setTabComponentAt(0, tab1Label);
        tabbedPane.setTabComponentAt(1, tab2Label);
        tabbedPane.setTabComponentAt(2, tab3Label);
        tabbedPane.setTabComponentAt(3, tab4Label);
        tabbedPane.setTabComponentAt(4, tab5Label);
        tabbedPane.setTabComponentAt(5, tab6Label);
        tabbedPane.setTabComponentAt(6, tab7Label);
        tabbedPane.setTabComponentAt(7, tab8Label);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    public void updateTabs() {
        if (tab2 != null && tab1 != null && tab3 != null && tab4 != null) {
            BufferedImagePanel cloneBufferedImagePanelTab2 = null;
            try {
                tab2.setDirectoryImageUrl(tab1.getDirectoryImageUrl());
                tab2.createChanelLabel();
                cloneBufferedImagePanelTab2 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
                tab2.geChosenImage().setBufferedImage(cloneBufferedImagePanelTab2.getBufferedImage());
                tab2.getCopyPanel().setBufferedImage(cloneBufferedImagePanelTab2.getBufferedImage());

                BufferedImagePanel cloneBufferedImagePanelTab3 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
                BufferedImage bufferedImageForTab3 = cloneBufferedImagePanelTab3.getBufferedImage();
                bufferedImageForTab3 = new BufferedImage(cloneBufferedImagePanelTab3.getBufferedImage().getWidth(), cloneBufferedImagePanelTab3.getBufferedImage().getHeight(), BufferedImage.TYPE_INT_RGB);
                bufferedImageForTab3.getGraphics().drawImage(cloneBufferedImagePanelTab3.getBufferedImage(), 0, 0, null);
                tab3.geChosenImage().setBufferedImage(bufferedImageForTab3);

                BufferedImagePanel cloneBufferedImagePanelTab4 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
                BufferedImage bufferedImageForTab4 = cloneBufferedImagePanelTab4.getBufferedImage();
                bufferedImageForTab4 = new BufferedImage(cloneBufferedImagePanelTab4.getBufferedImage().getWidth(), cloneBufferedImagePanelTab4.getBufferedImage().getHeight(), BufferedImage.TYPE_INT_RGB);
                bufferedImageForTab4.getGraphics().drawImage(cloneBufferedImagePanelTab4.getBufferedImage(), 0, 0, null);
                tab4.geChosenImage().setBufferedImage(bufferedImageForTab4);
                tab4.getCopyPanel().setBufferedImage(bufferedImageForTab4);

                BufferedImagePanel cloneBufferedImagePanelTab5 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
                BufferedImage bufferedImageForTab5 = cloneBufferedImagePanelTab5.getBufferedImage();
                bufferedImageForTab5 = new BufferedImage(cloneBufferedImagePanelTab5.getBufferedImage().getWidth(), cloneBufferedImagePanelTab4.getBufferedImage().getHeight(), BufferedImage.TYPE_INT_RGB);
                bufferedImageForTab5.getGraphics().drawImage(cloneBufferedImagePanelTab5.getBufferedImage(), 0, 0, null);
                tab5.geChosenImage().setBufferedImage(bufferedImageForTab5);

                BufferedImagePanel cloneBufferedImagePanelTab6 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
                BufferedImage bufferedImageForTab6 = cloneBufferedImagePanelTab6.getBufferedImage();
                bufferedImageForTab6 = new BufferedImage(cloneBufferedImagePanelTab6.getBufferedImage().getWidth(), cloneBufferedImagePanelTab4.getBufferedImage().getHeight(), BufferedImage.TYPE_INT_RGB);
                bufferedImageForTab6.getGraphics().drawImage(cloneBufferedImagePanelTab6.getBufferedImage(), 0, 0, null);
                tab6.geChosenImage().setBufferedImage(bufferedImageForTab6);

                BufferedImagePanel cloneBufferedImagePanelTab7 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
                BufferedImage bufferedImageForTab7 = cloneBufferedImagePanelTab7.getBufferedImage();
                bufferedImageForTab7 = new BufferedImage(cloneBufferedImagePanelTab7.getBufferedImage().getWidth(), cloneBufferedImagePanelTab4.getBufferedImage().getHeight(), BufferedImage.TYPE_INT_RGB);
                bufferedImageForTab7.getGraphics().drawImage(cloneBufferedImagePanelTab7.getBufferedImage(), 0, 0, null);
                tab7.geChosenImage().setBufferedImage(bufferedImageForTab5);

                BufferedImagePanel cloneBufferedImagePanelTab8 = (BufferedImagePanel) tab1.getChosenPanelTab().clone();
                BufferedImage bufferedImageForTab8 = cloneBufferedImagePanelTab8.getBufferedImage();
                bufferedImageForTab8 = new BufferedImage(cloneBufferedImagePanelTab8.getBufferedImage().getWidth(), cloneBufferedImagePanelTab4.getBufferedImage().getHeight(), BufferedImage.TYPE_INT_RGB);
                bufferedImageForTab8.getGraphics().drawImage(cloneBufferedImagePanelTab8.getBufferedImage(), 0, 0, null);
                tab8.geChosenImage().setBufferedImage(bufferedImageForTab8);
                // tab8.getMaskPanel().setBufferedImage(bufferedImageForTab8);

            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }

            //zjeśli obraz szary lub monochromatyczny to pracujemy na tablicy jednoelementowej - nie musimy zmieniać 3 kolorów RGB- optymalizacja
            if (cloneBufferedImagePanelTab2.getBufferedImage().getType() != BufferedImage.TYPE_INT_RGB) {
                tab2.getNegative().setPixelTableLenght(1);
                tab2.getBrightness().setPixelTableLenght(1);
                tab2.getContrast().setPixelTableLenght(1);
                tab2.getFilterArithmeticMean().setPixelTableLenght(1);
                tab2.getFilterMedian().setPixelTableLenght(1);
                tab2.getFilterIdentifyLines().setPixelTableLenght(1);
                tab2.getFilterUolisOperator().setPixelTableLenght(1);
                tab2.getMeanSquaredError().setPixelTableLenght(1);
                //tab5.getFourierProcesor().setPixelTableLenght(1);
                //tab6.getFourierProcesor().setPixelTableLenght(1);
                //  tab7.getFourierProcesor().setPixelTableLenght(1);
            } else {
                tab2.getNegative().setPixelTableLenght(3);
                tab2.getBrightness().setPixelTableLenght(3);
                tab2.getContrast().setPixelTableLenght(3);
                tab2.getFilterArithmeticMean().setPixelTableLenght(3);
                tab2.getFilterMedian().setPixelTableLenght(3);
                tab2.getFilterIdentifyLines().setPixelTableLenght(3);
                tab2.getFilterUolisOperator().setPixelTableLenght(3);
                tab2.getMeanSquaredError().setPixelTableLenght(3);
                // tab5.getFourierProcesor().setPixelTableLenght(3);
                //  tab6.getFourierProcesor().setPixelTableLenght(3);
                //  tab7.getFourierProcesor().setPixelTableLenght(3);
            }

            tab2.geChosenImage().repaint();
            tab2.getCopyPanel().repaint();
            tab3.updateTab();
            tab3.geChosenImage().repaint();
            tab4.updateTab();
            tab4.geChosenImage().repaint();
            tab5.updateTab();
            tab5.geChosenImage().repaint();
            tab6.updateTab();
            tab6.geChosenImage().repaint();
            tab7.updateTab();
            tab7.geChosenImage().repaint();
            tab8.updateTab();
            tab8.geChosenImage().repaint();
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
