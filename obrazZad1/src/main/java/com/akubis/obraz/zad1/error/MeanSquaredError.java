package com.akubis.obraz.zad1.error;

import com.akubis.obraz.zad1.gui.BufferedImagePanel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MeanSquaredError {

    private int pixelTableLenght = 3;

    public double[] countError(String originalImageUrl, BufferedImage changedImage) {
        BufferedImage originalImage = loadImage(originalImageUrl);
        double[] error = {0, 0, 0};
        for (int col = 0; col < originalImage.getWidth(); col++) {
            for (int row = 0; row < originalImage.getHeight(); row++) {
                double[] pixelOriginal = new double[pixelTableLenght];
                originalImage.getRaster().getPixel(col, row, pixelOriginal);
                double[] pixelChanged = new double[pixelTableLenght];
                changedImage.getRaster().getPixel(col, row, pixelChanged);
                for (int p = 0; p < pixelTableLenght; p++) {
                    error[p]= error[p]+Math.pow((pixelOriginal[p]-pixelChanged[p]), 2);
                }
            }
        }
        for (int p = 0; p < pixelTableLenght; p++) {
            error[p]=error[p]/(originalImage.getWidth()*originalImage.getHeight());
        }
        return error;
    }

    private BufferedImage loadImage(String originalImageUrl) {
        File directoryImage = new File(originalImageUrl);
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(directoryImage);
        } catch (IOException e) {
            System.err.println("Blad odczytu obrazka");
        }
        return originalImage;
    }

    public int getPixelTableLenght() {
        return pixelTableLenght;
    }

    public void setPixelTableLenght(int pixelTableLenght) {
        this.pixelTableLenght = pixelTableLenght;
    }
}
