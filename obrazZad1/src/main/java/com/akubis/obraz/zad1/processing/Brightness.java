package com.akubis.obraz.zad1.processing;

public class Brightness extends Procesor {

    private int offfset = 0;

    public void setOfffset(int offfset) {
        this.offfset = offfset;
    }

    public int getOfffset() {
        return offfset;
    }

    @Override
    public void enumeratePixelsArray() { 
        for (int i = 0; i < super.pixelsArray.length; i++) {
            super.pixelsArray[i] = i + offfset;
            if (super.pixelsArray[i] > 255) {
                super.pixelsArray[i] = 255;
            } else if (super.pixelsArray[i] < 0) {
                super.pixelsArray[i] = 0;
            }
        }
    }
}
