package com.akubis.obraz.zad1.processing;

import java.awt.image.BufferedImage;
import org.apache.commons.math3.complex.Complex;

public class FourierProcesor {

    private int pixelTableLenght = 3;
    private int radius = 40;
    private int ring = 10;
    private int startPoint = 240;
    private int shift = 40;
    private int k = 20;
    private int l = 150;

    public double[][] preparePixelsTable(BufferedImage image, int whichPixel) {
        double[][] output = new double[image.getHeight()][image.getWidth() * 2];

        for (int row = 0; row < image.getHeight(); row++) {
            for (int col = 0; col < image.getWidth(); col++) {
                double[] pixel = new double[pixelTableLenght];
                image.getRaster().getPixel(row, col, pixel);
                output[row][2 * col] = pixel[whichPixel];
                output[row][2 * col + 1] = 0;
            }
        }
        return output;
    }

    public double[][] countPhaseSpectrum(double[][] output, int width, int height) {
        double[][] mask = new double[height][width];

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                double real = output[row][2 * col];
                double imaginary = output[row][2 * col + 1];
                mask[row][col] = Math.atan((real / imaginary));
            }
        }
        return mask;
    }

    public double[][] countPowerSpectrum(double[][] output, int width, int height) {
        double[][] mask = new double[height][width];

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                double real = output[row][2 * col];
                double imaginary = output[row][2 * col + 1];
                mask[row][col] = Math.sqrt(real * real + imaginary * imaginary);
            }
        }
        return mask;
    }

    public double[][] linearNormalize(double[][] mask, int width, int height) {
        double min, max;
        min = max = Math.abs(mask[0][0]);
        //poszukiwanie minium i maximum na potrzeby normalizacji
        for (int row = 0; row < height; row++) {
            for (int col = 2; col < width; col++) {
                if (Math.abs(mask[row][col]) < 10000000) {
                    if (Math.abs(mask[row][col]) < min) {
                        min = Math.abs(mask[row][col]);
                    } else if (Math.abs(mask[row][col]) > max) {
                        max = Math.abs(mask[row][col]);
                    }
                }
            }
        }
        double factorA = 255.0 / Math.abs(min - max);
        double factorB = 255.0 - factorA * max;

        for (int row = 0; row < height; row++) {
            for (int col = 2; col < width; col++) {
                mask[row][col] = factorA * Math.abs(mask[row][col]) + factorB;
            }
        }
        return mask;
    }

    public double[][] logarithmNormalize(double[][] mask, int width, int height) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                double val = Math.abs(mask[row][col]);
                val = (Math.log(val + 0.001) + 1) / 20;
                if (val < 0) {
                    val = 0;
                }
                if (val > 1) {
                    val = 1;
                }
                mask[row][col] = val * 255;
            }
        }
        return mask;
    }

    public double[][] edgeNormalize(double[][] mask, int width, int height) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (mask[row][col] > 255) {
                    mask[row][col] = 255;
                }
                if (mask[row][col] < 0) {
                    mask[row][col] = 0;
                }

            }
        }
        return mask;
    }

    public double[][] lowPassFilter(double[][] output, int width, int height, int divider) {
        int halfW = width / (2 * divider);
        int halfH = height / 2;
        //   int r = 40;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if ((col % divider) == 0) {
                    if ((Math.sqrt(Math.pow(Math.abs(halfH - row), 2) + Math.pow(Math.abs(halfW - col / divider), 2)) > radius)) {
                        output[row][col] = 0;
                        if (divider == 2) {
                            output[row][col + 1] = 0;
                        }
                    }
                }
            }
        }
        return output;
    }

    public double[][] highPassFilter(double[][] output, int width, int height, int divider) {
        int halfW = width / (2 * divider);
        int halfH = height / 2;
        // int r = 10;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if ((col % divider) == 0) {
                    if (!(Math.sqrt(Math.pow(Math.abs(halfH - row), 2) + Math.pow(Math.abs(halfW - col / divider), 2)) > radius)) {
                        output[row][col] = 0;
                        if (divider == 2) {
                            output[row][col + 1] = 0;
                        }
                    }
                }
            }
        }
        return output;
    }

    public double[][] bandpassBarrageFilter(double[][] output, int width, int height, int divider) {
        double[][] outputFromMethod = new double[height][width];
        int halfW = width / (2 * divider);
        int halfH = height / 2;
        // int n = 50;
        //int r = 20;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if ((col % divider) == 0) {
                    if (!(Math.sqrt(Math.pow(Math.abs(halfH - row), 2) + Math.pow(Math.abs(halfW - col / divider), 2)) > ring + radius)) {
                        outputFromMethod[row][col] = 0;
                        if (divider == 2) {
                            outputFromMethod[row][col + 1] = 0;
                        }
                    } else {
                        outputFromMethod[row][col] = output[row][col];
                        if (divider == 2) {
                            outputFromMethod[row][col + 1] = output[row][col + 1];
                        }
                    }
                }
            }
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if ((col % divider) == 0) {
                    if ((Math.sqrt(Math.pow(Math.abs(halfH - row), 2) + Math.pow(Math.abs(halfW - col / divider), 2)) < radius)) {
                        outputFromMethod[row][col] = output[row][col];
                        if (divider == 2) {
                            outputFromMethod[row][col + 1] = output[row][col + 1];
                        }
                    }
                }
            }
        }
        return outputFromMethod;
    }

    public double[][] edgeDetectionFilter(double[][] output, int width, int height, int divider) {
        double[][] outputFromMethod = new double[height][width];
        int halfW = width / (2 * divider);
        int halfH = height / 2;
        // int r = 40;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                outputFromMethod[row][col] = 0;
            }
        }

        int ax, ay, bx, by, cx, cy, px, py, d1, d2, d3;
        ax = 0;
        ay = startPoint - shift / 2;
        bx = 0;
        by = startPoint + shift / 2;
        if (startPoint > 512) {
            ay = 512;
            ax = startPoint - shift / 2 - 512;
            by = 512;
            bx = startPoint + shift / 2 - 512;
        }
        cx = 256;
        cy = 256;
        outputFromMethod = drawTriangle(height, width, divider, ay, by, bx, ax, cy, cx, outputFromMethod, output);

        ax = 512;
        ay = 512 - (startPoint - shift / 2);
        bx = 512;
        by = 512 - (startPoint + shift / 2);

        if (startPoint > 512) {
            ay = 0;
            ax = 512 - ((startPoint - shift / 2) - 512);
            by = 0;
            bx = 512 - ((startPoint + shift / 2) - 512);
        }

        cx = 256;
        cy = 256;
        outputFromMethod = drawTriangle(height, width, divider, ay, by, bx, ax, cy, cx, outputFromMethod, output);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if ((col % divider) == 0) {
                    if ((Math.sqrt(Math.pow(Math.abs(halfH - row), 2) + Math.pow(Math.abs(halfW - col / divider), 2)) < radius)) {
                        outputFromMethod[row][col] = 0;
                        if (divider == 2) {
                            outputFromMethod[row][col + 1] = 0;
                        }
                    }
                }
            }
        }
        return outputFromMethod;
    }

    private double[][] drawTriangle(int height, int width, int divider, double ay, double by, double bx, double ax, double cy, double cx, double[][] outputFromMethod, double[][] output) {
        double d1;
        double d2;
        double d3;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if ((col % divider) == 0) {

                    d1 = row * (ay - by) + (col / divider) * (bx - ax) + (ax * by - ay * bx);
                    d2 = row * (by - cy) + (col / divider) * (cx - bx) + (bx * cy - by * cx);
                    d3 = row * (cy - ay) + (col / divider) * (ax - cx) + (cx * ay - cy * ax);

                    if (((d1 <= 0) && (d2 <= 0) && (d3 <= 0) || (d1 >= 0) && (d2 >= 0) && (d3 >= 0))) {
                        outputFromMethod[row][col] = output[row][col];
                        if (divider == 2) {
                            outputFromMethod[row][col + 1] = output[row][col + 1];
                        }
                    }
                }
            }
        }
        return outputFromMethod;
    }

    public double[][] phaseModificationFilter(double[][] output, int width, int height, int divider) {
        // double k = 20;
        //double l = 150;
        Complex imaginaryUnit = new Complex(0.0, 1.0);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if ((col % divider) == 0) {
                    Complex data = new Complex(output[row][col], output[row][col + 1]);
                    double mask = ((-col / divider * l * 2 * Math.PI) / (width / divider) + (-row * k * 2 * Math.PI) / height + (k + l) * Math.PI);
                    data = data.multiply((imaginaryUnit.multiply(mask)).exp());
                    output[row][col] = data.getReal();
                    output[row][col + 1] = data.getImaginary();
                }
            }
        }
        return output;
    }

    public BufferedImage createResultImage(double[][] outputImageRed, double[][] outputImageGreen, double[][] outputImageBlue, int width, int height, int type, int divider) {
        BufferedImage resultImage = new BufferedImage(width, height, type);
        //widmo
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width * divider; col++) {
                if ((col % divider) == 0) {
                    double[] pixel = new double[pixelTableLenght];
                    pixel[0] = outputImageRed[row][col];
                    pixel[1] = outputImageGreen[row][col];
                    pixel[2] = outputImageBlue[row][col];
                    resultImage.getRaster().setPixel(row, col / divider, pixel);
                }
            }
        }
        return resultImage;
    }

    public double[][] invertQuadrants(double[][] mask, int width, int height) {
        double[][] invertMmask = new double[height][width];
        int halfW = width / 2;
        int halfH = height / 2;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (col >= 0 && col < halfW && row >= 0 && row < halfH) {
                    invertMmask[row + halfH][col + halfW] = mask[row][col];
                } else if (col >= halfW && col < width && row >= halfH && row < height) {
                    invertMmask[row - halfH][col - halfW] = mask[row][col];
                } else if (col >= halfW && col < width && row >= 0 && row < halfH) {
                    invertMmask[row + halfH][col - halfW] = mask[row][col];
                } else {
                    invertMmask[row - halfH][col + halfW] = mask[row][col];
                }
            }
        }
        return invertMmask;
    }

    public int getPixelTableLenght() {
        return pixelTableLenght;
    }

    public void setPixelTableLenght(int pixelTableLenght) {
        this.pixelTableLenght = pixelTableLenght;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRing() {
        return ring;
    }

    public void setRing(int ring) {
        this.ring = ring;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }
}
