/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akubis.obraz.zad1.processing;

/**
 *
 * @author gość1
 */
public class Negative extends Procesor{
    
        @Override
    public void enumeratePixelsArray() { 
        for (int i = 0; i < super.pixelsArray.length; i++) {
            super.pixelsArray[i] = 255-i ;
        }
    }
}
