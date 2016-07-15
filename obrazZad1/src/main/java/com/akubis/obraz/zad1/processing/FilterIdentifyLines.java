package com.akubis.obraz.zad1.processing;

import java.awt.image.BufferedImage;

//filtracja liniowa oparta o splot- identyfikowanie linii
public class FilterIdentifyLines extends Procesor {

    private final Integer[][][] mask = {
        {
            {-1, 2, -1},
            {-1, 2, -1},
            {-1, 2, -1},},
        {
            {-1, -1, -1},
            {2, 2, 2},
            {-1, -1, -1}
        },
        {
            {-1, -1, 2},
            {-1, 2, -1},
            {2, -1, -1}
        },
        {
            {2, -1, -1},
            {-1, 2, -1},
            {-1, -1, 2}
        }
    };

    int selectedMask = 0;

    public void setSelectedMask(int selectedMask) {
        this.selectedMask = selectedMask;
    }

    public Integer[][][] getMask() {
        return mask;
    }

    @Override
    public void changePixel(double[] newPixelValue, int col, int row, BufferedImage image) {
        int maskFloor = (int) Math.floor(maskDimesion / 2);
        for (int i = row - maskFloor, n = 0; i < (row + maskFloor + 1); i++, n++) {
            for (int j = col - maskFloor, m = 0; j < (col + maskFloor + 1); j++, m++) {
                double[] pixel = new double[super.pixelTableLenght];
                image.getRaster().getPixel(j, i, pixel);
                for (int p = 0; p < super.pixelTableLenght; p++) {
                    newPixelValue[p] = newPixelValue[p] + pixel[p] * mask[selectedMask][m][n];
                }
            }
        }
        for (int p = 0; p < super.pixelTableLenght; p++) {
            if (newPixelValue[p] > 255) {
                newPixelValue[p] = 255;
            } else if (newPixelValue[p] < 0) {
                newPixelValue[p] = 0;
            }
        }
    }

}
