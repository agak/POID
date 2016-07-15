package com.akubis.obraz.zad1.processing;

import java.awt.image.BufferedImage;
import java.util.Arrays;

//opertor Uolisa
public class FilterUolisOperator extends Procesor {

    public void setMaskDimesion(int maskDimesion) {
        super.maskDimesion = maskDimesion;
    }

    @Override
    public void changePixel(double[] newPixelValue, int col, int row, BufferedImage image) {
        double[][] pixel = new double[5][super.pixelTableLenght];
        double[] f = image.getRaster().getPixel(col, row, pixel[0]);
        double[] a1 = image.getRaster().getPixel(col, row - 1, pixel[1]);
        double[] a3 = image.getRaster().getPixel(col + 1, row, pixel[2]);
        double[] a5 = image.getRaster().getPixel(col, row + 1, pixel[3]);
        double[] a7 = image.getRaster().getPixel(col - 1, row, pixel[4]);
        for (int p = 0; p < super.pixelTableLenght; p++) {
            double p1 = a1[p] * a3[p] * a5[p] * a7[p];
            double p2 = (Math.pow(f[p], 4)) / p1;
            double p3 = Math.log(p2);
            double p4 = p3 / 4.0;
            newPixelValue[p] = (Math.log(Math.pow(f[p], 4) / (a1[p] * a3[p] * a5[p] * a7[p]))) / 4.0;
            if (a1[p] == 0 || a3[p] == 0 || a5[p] == 0 || a7[p] == 0 || f[p] == 0) {
                newPixelValue[p] = 0;
            }
        }
    }
}
