package com.akubis.obraz.zad1.processing;

import java.awt.image.BufferedImage;

//średnia artmetyczna
public class FilterArithmeticMean extends Procesor {

    public void setMaskDimesion(int maskDimesion) {

        super.maskDimesion = maskDimesion;
    }

    @Override
    public void changePixel(double[] newPixelValue, int col, int row, BufferedImage image) {
        int maskFloor = (int) Math.floor(maskDimesion / 2);
        for (int i = row - maskFloor; i < (row + maskFloor + 1); i++) {
            for (int j = col - maskFloor; j < (col + maskFloor + 1); j++) {
                double[] pixel = new double[super.pixelTableLenght];
                image.getRaster().getPixel(j, i, pixel);
                for (int p = 0; p < super.pixelTableLenght; p++) {
                    newPixelValue[p] = newPixelValue[p] + pixel[p];
                }
            }
        }
        for (int p = 0; p < super.pixelTableLenght; p++) {
            newPixelValue[p] = newPixelValue[p] / (maskDimesion * maskDimesion);
        }
    }
}
