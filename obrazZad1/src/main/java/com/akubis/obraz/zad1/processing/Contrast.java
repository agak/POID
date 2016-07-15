package com.akubis.obraz.zad1.processing;

public class Contrast extends Procesor {

    private double wspolczynnikA = 1;
    private double wspolczynnikB;

    public void setWspolczynnikA(double wspolczynnikA) {
        this.wspolczynnikA = wspolczynnikA;
    }

    public void setWspolczynniki(double wspolczynnikA) {

        this.wspolczynnikA = wspolczynnikA / 100;
        this.wspolczynnikB = 177 * (1 - this.wspolczynnikA);

    }

    public double getWspolczynnikA() {
        return wspolczynnikA;
    }

    @Override
    public void enumeratePixelsArray() {
        for (int i = 0; i < super.pixelsArray.length; i++) {
            super.pixelsArray[i] = (int) (wspolczynnikA * i + wspolczynnikB);
            if (super.pixelsArray[i] > 255) {
                super.pixelsArray[i] = 255;
            } else if (super.pixelsArray[i] < 0) {
                super.pixelsArray[i] = 0;
            }
        }
    }
}
