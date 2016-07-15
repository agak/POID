package com.akubis.obraz.zad1.processing;

import java.awt.image.BufferedImage;

public class Procesor {

    protected int maskDimesion = 3;
    protected int pixelTableLenght = 3;
    protected double[] pixelsArray = new double[256];

    public BufferedImage changeImage(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int width = image.getWidth();
        int height = image.getHeight();
        enumeratePixelsArray();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                double[] pixel = new double[pixelTableLenght];
                image.getRaster().getPixel(col, row, pixel);
                changePixel(pixel);
                newImage.getRaster().setPixel(col, row, pixel);
            }
        }
        return newImage;
    }

    public BufferedImage filter(BufferedImage image, String filterName) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int width = image.getWidth();
        int height = image.getHeight();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int maskFloor = (int) Math.floor(maskDimesion / 2);
                if (row >= (maskFloor) & col >= (maskFloor) & row < (height - (maskFloor)) & col < (width - (maskFloor))) {
                    double[] valueTable = {0, 0, 0};
                    changePixel(valueTable, col, row, image);
                    newImage.getRaster().setPixel(col, row, valueTable);
                } else if (!"Lines".equals(filterName)) {
                    fillFrame(image, col, row, newImage);
                }
            }
        }
        return newImage;
    }

    public BufferedImage filterUolisOperator(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int width = image.getWidth();
        int height = image.getHeight();

        //pobranie nieznormalizowanej tablicy wartości
        double[][][] allPixel = new double[width][height][pixelTableLenght];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int maskFloor = (int) Math.floor(maskDimesion / 2);
                if (row >= (maskFloor) & col >= (maskFloor) & row < (height - (maskFloor)) & col < (width - (maskFloor))) {
                    double[] valueTable = {0, 0, 0};
                    changePixel(valueTable, col, row, image);
                    for (int p = 0; p < pixelTableLenght; p++) {
                        allPixel[row][col][p] = valueTable[p];
                    }
                }
            }
        }
        for (int p = 0; p < pixelTableLenght; p++) {
            int maskFloor = (int) Math.floor(maskDimesion / 2);
            double min = allPixel[maskFloor][maskFloor][p];
            double max = allPixel[height - maskFloor][width - maskFloor][p];

            //poszukiwanie minium i maximum na potrzeby normalizacji
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (row >= (maskFloor) & col >= (maskFloor) & row < (height - (maskFloor)) & col < (width - (maskFloor))) {
                        if (allPixel[col][row][p] < min) {
                            min = allPixel[col][row][p];
                        } else if (allPixel[col][row][p] > max) {
                            max = allPixel[col][row][p];
                        }
                    }
                }
            }
            //wyliczenie wartości po normalizacji
            double factorA = 255.0 / Math.abs(min - max);
            double factorB = 255.0 - factorA * max;
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (row >= (maskFloor) & col >= (maskFloor) & row < (height - (maskFloor)) & col < (width - (maskFloor))) {
                        allPixel[col][row][p] = factorA * allPixel[col][row][p] + factorB;
                    } else {
                        allPixel[col][row][p] = factorB; //x=0
                    }
                }
            }
        }
        //utworzenie obrazu
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int maskFloor = (int) Math.floor(maskDimesion / 2);
                newImage.getRaster().setPixel(row, col, allPixel[col][row]);
            }
        }
        return newImage;
    }

    private void fillFrame(BufferedImage image, int col, int row, BufferedImage newImage) {
        double[] pixel = new double[pixelTableLenght];
        image.getRaster().getPixel(col, row, pixel);
        newImage.getRaster().setPixel(col, row, pixel);
    }

    public void enumeratePixelsArray() {
    }

    public void changePixel(double[] pixel) {
        for (int i = 0; i < pixelTableLenght; i++) {
            pixel[i] = pixelsArray[(int) pixel[i]];
        }
    }

    public void changePixel(double[] valueTable, int col, int row, BufferedImage image) {
    }

    public int getMaskDimesion() {
        return maskDimesion;
    }

    public int getPixelTableLenght() {
        return pixelTableLenght;
    }

    public void setPixelTableLenght(int pixelTableLenght) {
        this.pixelTableLenght = pixelTableLenght;
    }
}
