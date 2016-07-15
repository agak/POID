package com.akubis.obraz.zad1.processing;

import java.awt.image.BufferedImage;

//jednostajna wyjściowa gęstość prawdopodobieństwa
public class ModifyHistogram extends Procesor {

    private double gmin = 0;
    private double gmax = 255;
    private double[][][] histogramOriginal;
    private double[][] histogramSum;
    private double[][] modificatioTable;

    @Override
    public void changePixel(double[] newPixelValue) {
        for (int i = 0; i < 3; i++) {
            newPixelValue[i] = modificatioTable[i][(int) newPixelValue[i]];
        }
    }

    public void createModificationTable(BufferedImage image) {
        histogramSum = new double[4][256];
        modificatioTable = new double[4][256];
        for (int p = 0; p < 4; p++) {
            for (int i = 1; i < 256; i++) {
                if (i == 0) {
                    histogramSum[p][i] = histogramOriginal[p][1][i];
                } else {
                    histogramSum[p][i] += histogramSum[p][i - 1] + histogramOriginal[p][1][i];
                }
                modificatioTable[p][i] = gmin + (gmax - gmin) * histogramSum[p][i] / (image.getWidth() * image.getHeight());
            }
        }
    }

    public double[][][] createHistogramImage(BufferedImage image) {
        histogramOriginal = new double[4][2][256];
        for (int p = 0; p < 4; p++) {
            for (int i = 0; i < 256; i++) {
                histogramOriginal[p][0][i] = i;
                histogramOriginal[p][1][i] = 0;
            }
        }

        for (int row = 0; row < image.getHeight(); row++) {
            for (int col = 0; col < image.getWidth(); col++) {
                double[] pixel = new double[3];
                image.getRaster().getPixel(col, row, pixel);

                histogramOriginal[0][1][(int) pixel[0]]++;
                histogramOriginal[1][1][(int) pixel[1]]++;
                histogramOriginal[2][1][(int) pixel[2]]++;

                int luminance = (int) (0.299 * pixel[0] + 0.587 * pixel[1] + 0.114 * pixel[2]);
                histogramOriginal[3][1][luminance]++;
            }
        }
        return histogramOriginal;
    }

    public double getGmin() {
        return gmin;
    }

    public void setGmin(double gmin) {
        this.gmin = gmin;
    }

    public double getGmax() {
        return gmax;
    }

    public void setGmax(double gmax) {
        this.gmax = gmax;
    }
}
