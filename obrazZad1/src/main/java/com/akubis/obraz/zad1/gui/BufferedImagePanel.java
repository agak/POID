package com.akubis.obraz.zad1.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;


//@Getter
//@Setter
public class BufferedImagePanel extends JPanel implements Cloneable {

    private BufferedImage bufferedImage;
    public int width;
    private int height;

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public BufferedImagePanel(BufferedImage bufferedImage, int width, int height) {
        this.bufferedImage = bufferedImage;
        this.width = width;
        this.height = height;
        Dimension dimension = new Dimension(width, height);
        setSize(dimension);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bufferedImage, 0, 0, width, height, this);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        BufferedImagePanel copy = null;
        try {
            copy = (BufferedImagePanel) super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("Blad klonowania obiektu" + e);
        }
        return copy;
    }
}
