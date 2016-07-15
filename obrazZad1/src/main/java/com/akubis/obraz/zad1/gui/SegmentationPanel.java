package com.akubis.obraz.zad1.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import com.akubis.obraz.zad1.processing.Node;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SegmentationPanel extends JPanel implements ActionListener, ChangeListener {

    private BufferedImagePanel chosenImage;
    private BufferedImage originalImage;
    private BufferedImagePanel maskPanel;
    private BufferedImage maskImage;
    private int globalSegment = 1;
    private int choiceSegment = 1;
    private Node root;
    private int euclideanDistanceGlobal = 15;
    private JButton saveButton;
    private JLabel euclideanDistanceLabel;
    private JLabel segmentLabel;
    private JTextField euclideanDistanceField;
    private JButton euclideanDistanceButton;
    private JSpinner spinner;
    private List<Node> listNodeWithSegment;
    private SpinnerModel spinnerModel;

    public SegmentationPanel(BufferedImagePanel chosenPanelTab) throws CloneNotSupportedException {
        this.chosenImage = chosenPanelTab;
        createTab();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
    }

    private void createTab() throws CloneNotSupportedException {
        JLabel originalLabel = new JLabel("Obraz oryginalny");
        JLabel maskLabel = new JLabel("Obraz z wybraną maską");
        originalLabel.setLocation(10, 5);
        maskLabel.setLocation(560, 5);
        originalLabel.setSize(300, 20);
        maskLabel.setSize(300, 20);
        originalLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        maskLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        this.add(originalLabel);
        this.add(maskLabel);

        listNodeWithSegment = new ArrayList<>();

        chosenImage.setLocation(10, 30);
        this.add(chosenImage);
        originalImage = chosenImage.getBufferedImage();
        BufferedImage imageToConvert = originalImage;

        originalImage = new BufferedImage(imageToConvert.getWidth(), imageToConvert.getHeight(), BufferedImage.TYPE_INT_RGB);
        originalImage.getGraphics().drawImage(imageToConvert, 0, 0, null);

        maskPanel = (BufferedImagePanel) chosenImage.clone();
        maskPanel.setLocation(560, 30);
        this.add(maskPanel);

        saveButton = new JButton("Zapisz obraz");
        saveButton.setLocation(580, 570);
        saveButton.setSize(200, 50);
        saveButton.addActionListener(this);
        this.add(saveButton);

        euclideanDistanceLabel = new JLabel("Próg- odległość euklidesowa: ");
        euclideanDistanceLabel.setLocation(1100, 50);
        euclideanDistanceLabel.setSize(512, 20);
        this.add(euclideanDistanceLabel);

        euclideanDistanceField = new JTextField();
        euclideanDistanceField.setSize(40, 30);
        euclideanDistanceField.setLocation(1280, 40);
        euclideanDistanceField.setText(Integer.toString(euclideanDistanceGlobal));
        this.add(euclideanDistanceField);

        euclideanDistanceButton = new JButton("Dokonaj segmentacji");
        euclideanDistanceButton.setLocation(1350, 30);
        euclideanDistanceButton.setSize(200, 50);
        euclideanDistanceButton.addActionListener(this);
        this.add(euclideanDistanceButton);

        segmentLabel = new JLabel("Segmnent: ");
        segmentLabel.setLocation(1100, 100);
        segmentLabel.setSize(512, 20);
        this.add(segmentLabel);

        spinnerModel
                = new SpinnerNumberModel(1, 
                        1, 
                        100, 
                        1);
        spinner = new JSpinner(spinnerModel);
        spinner.setLocation(1180, 100);
        spinner.setSize(50, 30);
        spinner.addChangeListener(this);
        this.add(spinner);

        updateTab();

    }

    public void updateTab() {
        originalImage = chosenImage.getBufferedImage();
        choiceSegment = 1;
        globalSegment = 1;

        int start = 0;
        int divider = originalImage.getHeight() / 2;

        root = new Node(null, start, originalImage.getWidth(), start, originalImage.getHeight());
        root.setUniform(false);
        regionSplitting(start, start, divider, root);
        regionSplitting(start + divider, start, divider, root);
        regionSplitting(start, start + divider, divider, root);
        regionSplitting(start + divider, start + divider, divider, root);

        regionMerging(root);

       // seeTree(root);

        spinnerModel
                = new SpinnerNumberModel(1, 
                        1,
                        globalSegment, 
                        1);

        spinner.setModel(spinnerModel);

        listNodeWithSegment.removeAll(listNodeWithSegment);
        listNodeWithSegment = getBySegment(root, choiceSegment, listNodeWithSegment);

        System.out.println("lista" + listNodeWithSegment.size());

        maskImage = createMaskOnImage(originalImage, listNodeWithSegment);
        maskPanel.setBufferedImage(maskImage);
        maskPanel.repaint();
    }

    private void regionSplitting(int startCol, int startRow, int divider, Node parent) {
        Node node = new Node(parent, startCol, (startCol + divider), startRow, (startRow + divider));
        parent.addChild(node);

        double[] averagePixels = countSumToAvarage(startRow, divider, startCol);
        for (int i = 0; i < 3; i++) {
            averagePixels[i] = averagePixels[i] / (Math.pow((divider), 2));
        }

        outerloop:
        for (int row = startRow; row < startRow + divider; row++) {
            for (int col = startCol; col < startCol + divider; col++) {
                double[] pixel = new double[3];
                originalImage.getRaster().getPixel(col, row, pixel);

                double euclideanDistance = Math.sqrt(Math.pow((pixel[0] - averagePixels[0]), 2) + Math.pow((pixel[1] - averagePixels[1]), 2) + Math.pow((pixel[2] - averagePixels[2]), 2));

                if (euclideanDistance > euclideanDistanceGlobal && divider > 4) {
                    regionSplitting(startCol, startRow, divider / 2, node);
                    regionSplitting(startCol, startRow + divider / 2, divider / 2, node);
                    regionSplitting(startCol + divider / 2, startRow, divider / 2, node);
                    regionSplitting(startCol + divider / 2, startRow + divider / 2, divider / 2, node);

  node.setUniform(false);
                    break outerloop;
                } else if (euclideanDistance > euclideanDistanceGlobal && divider == 4) {
                    node.setUniform(false);
                }
            }
        }
    }

    private double[] countSumToAvarage(int startRow, int divider, int startCol) {
        double[] averagePixels = new double[3];
        for (int row = startRow; row < startRow + divider; row++) {
            for (int col = startCol; col < startCol + divider; col++) {
                double[] pixel = new double[3];
                originalImage.getRaster().getPixel(col, row, pixel);

                for (int i = 0; i < 3; i++) {
                    if (col == startCol && row == startRow) {
                        averagePixels[i] = 0;
                    } else {
                        averagePixels[i] = averagePixels[i] + pixel[i];
                    }
                }
            }
        }
        return averagePixels;
    }

    private void regionMerging(Node node) {
     if (!node.getChildren().isEmpty()) {
            for (Node child : node.getChildren()) {
                if (child.isUniform()) {
                   if (child.getSegment() == 0) {
                        child.setSegment(globalSegment);
                        globalSegment++;
                    }
                  if (child.getEndCol() < originalImage.getWidth()) {
                        Node brotherRight = getPossibleBrother(child.getEndCol(), child.getStartRow(), root, 0);
                        if (brotherRight != null && brotherRight.isUniform()) {
                 boolean isBrotherRightUniform = checkBrotherUniform(child, brotherRight);
                            if (isBrotherRightUniform) {
                                brotherRight.setSegment(child.getSegment());
                            }
                        }
                    }
                    if (child.getEndRow() < originalImage.getHeight()) {
                        Node brotherBottom = getPossibleBrother(child.getStartCol(), child.getEndRow(), root, 1);
                        if (brotherBottom != null && brotherBottom.isUniform()) {
                                       boolean isBrotherBottomUniform = checkBrotherUniform(child, brotherBottom);
                            if (isBrotherBottomUniform) {
                                brotherBottom.setSegment(child.getSegment());
                            }
                        }
                    }
                } else {
                     regionMerging(child);
                }

            }
        }
    }

    private Node getPossibleBrother(int startCol, int startRow, Node parent, int typeBrother) {
       if (!parent.getChildren().isEmpty()) {
            for (Node child : parent.getChildren()) {
                 if (((typeBrother == 0 && child.getStartCol() == startCol && child.getStartRow() <= startRow && child.getEndRow() > startRow) || (typeBrother == 1 && child.getStartCol() <= startCol && child.getEndCol() > startCol && child.getStartRow() == startRow)) && child.isUniform()) {
                    return child;
                } else {
                    Node returnChild = getPossibleBrother(startCol, startRow, child, typeBrother);
                    if (returnChild != null) {
                        return returnChild;
                    }
                }
            }
        }
        return null;
    }

    private List<Node> getBySegment(Node node, int segment, List<Node> listNode) {
 if (!node.getChildren().isEmpty()) {
            for (Node child : node.getChildren()) {
                if (child.getSegment() == segment) {
                    listNode.add(child);
                }
                listNode = getBySegment(child, segment, listNode);
            }
        }
        return listNode;
    }

    private boolean checkBrotherUniform(Node original, Node possibleBrother) {
        boolean isBrotherUniform = true;
        double[] sumPixelsOriginal = countSumToAvarage(original.getStartRow(), (original.getEndCol() - original.getStartCol()), original.getStartCol());
        double[] sumPixelsPossibleBrother = countSumToAvarage(possibleBrother.getStartRow(), (possibleBrother.getEndCol() - possibleBrother.getStartCol()), possibleBrother.getStartCol());
        double[] averagePixels = new double[3];
        for (int i = 0; i < 3; i++) {
            averagePixels[i] = (sumPixelsOriginal[i] + sumPixelsPossibleBrother[i]) / ((Math.pow((original.getEndCol() - original.getStartCol()), 2)) + (Math.pow((possibleBrother.getEndCol() - possibleBrother.getStartCol()), 2)));
        }

        for (int row = original.getStartRow(); row < (original.getEndRow() - original.getStartRow()); row++) {
            for (int col = original.getStartCol(); col < (original.getEndCol() - original.getStartCol()); col++) {
                double[] pixel = new double[3];
                originalImage.getRaster().getPixel(col, row, pixel);
                double euclideanDistance = Math.sqrt(Math.pow((pixel[0] - averagePixels[0]), 2) + Math.pow((pixel[1] - averagePixels[1]), 2) + Math.pow((pixel[2] - averagePixels[2]), 2));
                if (euclideanDistance > euclideanDistanceGlobal) {
                    isBrotherUniform = false;
                }
            }
        }

        for (int row = possibleBrother.getStartRow(); row < (possibleBrother.getEndRow() - possibleBrother.getStartRow()); row++) {
            for (int col = possibleBrother.getStartCol(); col < (possibleBrother.getEndCol() - possibleBrother.getStartCol()); col++) {
                double[] pixel = new double[3];
                originalImage.getRaster().getPixel(col, row, pixel);
                double euclideanDistance = Math.sqrt(Math.pow((pixel[0] - averagePixels[0]), 2) + Math.pow((pixel[1] - averagePixels[1]), 2) + Math.pow((pixel[2] - averagePixels[2]), 2));
                if (euclideanDistance > euclideanDistanceGlobal) {
                    isBrotherUniform = false;
                }
            }
        }
        return isBrotherUniform;
    }

    private void seeTree(Node node) {
        if (!node.getChildren().isEmpty()) {
            for (Node child : node.getChildren()) {
                seeTree(child);
            }
        }
    }

    private BufferedImage createMaskOnImage(BufferedImage image, List<Node> listNodeWithSegment) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int row = 0; row < image.getHeight(); row++) {
            for (int col = 0; col < image.getWidth(); col++) {
                double[] pixel = new double[3];
                image.getRaster().getPixel(col, row, pixel);
                resultImage.getRaster().setPixel(col, row, pixel);
            }
        }

        for (Node node : listNodeWithSegment) {
            for (int row = node.getStartRow(); row < node.getEndRow(); row++) {
                for (int col = node.getStartCol(); col < node.getEndCol(); col++) {
                    double[] pixel = new double[3];
                    pixel[0] = 255;
                    pixel[1] = 255;
                    pixel[2] = 255;
                    resultImage.getRaster().setPixel(col, row, pixel);
                }
            }
        }
        return resultImage;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == saveButton) {
            File savedImage = new File("segImage.png");
            try {
                ImageIO.write(maskImage, "png", savedImage);
            } catch (IOException ex) {
                Logger.getLogger(FourierFilterPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (event.getSource() == euclideanDistanceButton) {
            euclideanDistanceGlobal = Integer.parseInt(euclideanDistanceField.getText());
            updateTab();
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == spinner) {
            choiceSegment = (Integer) spinner.getValue();
            listNodeWithSegment.removeAll(listNodeWithSegment);
            listNodeWithSegment = getBySegment(root, choiceSegment, listNodeWithSegment);

            maskImage = createMaskOnImage(originalImage, listNodeWithSegment);
            maskPanel.setBufferedImage(maskImage);
            maskPanel.repaint();;
        }
    }

    public BufferedImagePanel geChosenImage() {
        return chosenImage;
    }

    public void setChosenImage(BufferedImagePanel chosenImage) {
        this.chosenImage = chosenImage;
    }

    public BufferedImagePanel getMaskPanel() {
        return maskPanel;
    }

    public void setMaskPanel(BufferedImagePanel maskPanel) {
        this.maskPanel = maskPanel;
    }

}
