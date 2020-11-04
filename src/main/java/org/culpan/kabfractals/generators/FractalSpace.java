package org.culpan.kabfractals.generators;

public class FractalSpace {
    private double realMin;

    private double realMax;

    private double imMin;

    private double imMax;

    private int height;

    private int width;

    public FractalSpace(double realMin, double realMax, double imMin, int width, int height) {
        this.realMin = realMin;
        this.realMax = realMax;
        this.imMin = imMin;
        this.width = width;
        this.height = height;
        this.imMax = imMin + (realMax - realMin) * height / width;
    }

    public double getRealMin() {
        return realMin;
    }

    public void setRealMin(double realMin) {
        this.realMin = realMin;
    }

    public double getRealMax() {
        return realMax;
    }

    public void setRealMax(double realMax) {
        this.realMax = realMax;
    }

    public double getImMin() {
        return imMin;
    }

    public void setImMin(double imMin) {
        this.imMin = imMin;
    }

    public double getImMax() {
        return imMax;
    }

    public void setImMax(double imMax) {
        this.imMax = imMax;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
