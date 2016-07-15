package com.akubis.obraz.zad1.processing;

import java.awt.image.BufferedImage;
import java.util.Arrays;

//mediana
public class FilterMedian extends Procesor {

    public void setMaskDimesion(int maskDimesion) {
        super.maskDimesion = maskDimesion;
    }

    @Override
    public void changePixel(double[] newPixelValue, int col, int row, BufferedImage image) {
        double[][] valueToMedian = new double[super.pixelTableLenght][(maskDimesion * maskDimesion)];
        int iterator = 0;
        int maskFloor = (int) Math.floor(maskDimesion / 2);
        for (int i = row - maskFloor; i < (row + maskFloor + 1); i++) {
            for (int j = col - maskFloor; j < (col + maskFloor + 1); j++) {
                double[] pixel = new double[super.pixelTableLenght];
                image.getRaster().getPixel(j, i, pixel);
                for (int p = 0; p < super.pixelTableLenght; p++) {
                    valueToMedian[p][iterator] = pixel[p];
                }
                iterator++;
            }
        }
        int position = (int) Math.ceil(maskDimesion * maskDimesion / 2);
        for (int p = 0; p < super.pixelTableLenght; p++) {
            Arrays.sort(valueToMedian[p]);
            newPixelValue[p] = valueToMedian[p][position];
        }
    }
}
