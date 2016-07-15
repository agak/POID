package com.akubis.obraz.zad1.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

//@Getter
//@Setter
public class ChoiceImagePanel extends JPanel implements ActionListener {

    private String directoryImageUrl;
    private JPanel buttonPanel;
    private final java.util.List<JButton> buttons;
    private BufferedImagePanel chosenPanelTab;
    private JButton newPath;
    private final int iconSize = 128;
    private final int maxPanelIconsSizex = 1152;
    private JLabel path;
    private JScrollPane scrollPane;
    private final MainWindow mainWindow;

    public ChoiceImagePanel(MainWindow mainWindow) {
        this.buttons = new ArrayList();
        this.mainWindow = mainWindow;
        createTab1();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
    }

    private void createTab1() {
        directoryImageUrl = "src/main/resources/images";

        chosenPanelTab = new BufferedImagePanel(null, 512, 512);
        chosenPanelTab.setLocation(maxPanelIconsSizex + 60, 10);
        this.add(chosenPanelTab);

        createIconPanel();

        path = new JLabel("Aktualny folder: " + directoryImageUrl);
        path.setLocation(maxPanelIconsSizex + 60, 550);
        path.setSize(512, 20);
        this.add(path);

        newPath = new JButton("Zmień folder obrazów");
        newPath.setSize(200, 50);
        newPath.setLocation(maxPanelIconsSizex + 60, 580);
        newPath.addActionListener(this);
        this.add(newPath);
    }

    private void createIconPanel() {
        int yLocationIcon;
        int xLocationIcon = yLocationIcon = 10;
        File directoryImage = new File(directoryImageUrl);
        for (File fileEntry : directoryImage.listFiles()) {
            BufferedImage imageToChoice = null;
            try {
                imageToChoice = ImageIO.read(fileEntry);
            } catch (IOException e) {
                System.err.println("Blad odczytu obrazka");
            }
            double divider = (double) imageToChoice.getHeight() / (double) iconSize;
            double iconWitdth = (double) (imageToChoice.getWidth() / divider);
            Image scaled = imageToChoice.getScaledInstance((int) iconWitdth, iconSize, Image.SCALE_DEFAULT);

            buttonPanel = new JPanel();
            buttonPanel.setLayout(null);

            JButton buttonWithIcon = new JButton(fileEntry.getName());

            if (xLocationIcon + iconWitdth > maxPanelIconsSizex) {
                yLocationIcon += iconSize + 30; //iconSize - 10
                xLocationIcon = 10;
            }

            buttonWithIcon.setSize((int) (iconWitdth - 10), iconSize - 10);
            buttonWithIcon.setLocation(xLocationIcon, yLocationIcon);
            buttonWithIcon.setIcon(new ImageIcon(scaled));
            buttonWithIcon.addActionListener(this);

            xLocationIcon += iconWitdth - 10;

            buttons.add(buttonWithIcon);
            this.add(buttonWithIcon);
        }

        for (JButton buttonIcon : buttons) {
            buttonPanel.add(buttonIcon);
            JLabel iconName = new JLabel(buttonIcon.getText());
            iconName.setSize(buttonIcon.getWidth() - 10, 20);
            iconName.setLocation(buttonIcon.getX(), buttonIcon.getY() + buttonIcon.getHeight());
            buttonPanel.add(iconName);
        }

        buttonPanel.setPreferredSize(new Dimension(maxPanelIconsSizex, (int) buttons.get(buttons.size() - 1).getLocation().getY() + buttons.get(buttons.size() - 1).getHeight() + 40));
        scrollPane = new JScrollPane(buttonPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setSize(maxPanelIconsSizex, 900);
        scrollPane.setLocation(10, 10);
        this.add(scrollPane);
        buttons.get(0).doClick();
    }

    private void reloadButonPanel() {
        this.remove(scrollPane);
        buttons.clear();
        buttonPanel.removeAll();
        //buttonPanel.getComponentCount();
        createIconPanel();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        //wybór ikony do załadowania
        for (JButton buttonIcon : buttons) {
            if (event.getSource() == buttonIcon) {
                BufferedImage selectedImage = null;
                BufferedImage readImage = null;
                File imageFile = new File(directoryImageUrl + "\\" + buttonIcon.getText());
                try {
                    readImage = ImageIO.read(imageFile);

                } catch (IOException e) {
                    System.err.println("Blad odczytu obrazka" + e);
                }
                selectedImage = readImage;
                if (selectedImage.getType() != BufferedImage.TYPE_BYTE_GRAY && selectedImage.getType() != BufferedImage.TYPE_BYTE_BINARY) {
                    //zmiana typu obrazu na RGB
                    selectedImage = new BufferedImage(readImage.getWidth(), readImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                    selectedImage.getGraphics().drawImage(readImage, 0, 0, null);
                }

                double divider = (double) selectedImage.getWidth() / 512.0;
                chosenPanelTab.setBufferedImage(selectedImage);
                chosenPanelTab.setHeight((int) (selectedImage.getHeight() / divider));
                chosenPanelTab.setWidth(512);
                if (selectedImage.getHeight() > selectedImage.getWidth() && selectedImage.getHeight() > 800) {
                    divider = (double) selectedImage.getHeight() / 512.0;
                    chosenPanelTab.setHeight((int) (selectedImage.getHeight() / divider));
                    chosenPanelTab.setWidth((int) (selectedImage.getWidth() / divider));
                }
                chosenPanelTab.repaint();
                this.repaint();

                mainWindow.updateTabs();
            }
        }
        //zmiana folderu obrazów
        if (event.getSource() == newPath) {
            JFileChooser directoryChooser = new JFileChooser();
            directoryChooser.setCurrentDirectory(new File(directoryImageUrl));
            directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = directoryChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = directoryChooser.getSelectedFile();
                directoryImageUrl = selectedFile.getPath();
                path.setText("Aktualny folder: " + directoryImageUrl);
                reloadButonPanel();
            }
        }
    }

    public BufferedImagePanel getChosenPanelTab() {
        return chosenPanelTab;
    }

    public String getDirectoryImageUrl() {
        return directoryImageUrl;
    }

    public void setDirectoryImageUrl(String directoryImageUrl) {
        this.directoryImageUrl = directoryImageUrl;
    }
}
