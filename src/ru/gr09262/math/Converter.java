package ru.gr09262.math;

import static java.lang.Math.abs;

public class Converter {
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private int width;
    private int height;

    public Converter(
            double xMin,
            double xMax,
            double yMin,
            double yMax,
            int width,
            int height
    ){
        setXShape(xMin, xMax);
        setYShape(yMin, yMax);
        setShape(xMin, xMax, yMin, yMax);
        setWidth(width);
        setHeight(height);
    }

    public double getXMin() {
        return xMin;
    }

    public double getXMax() {
        return xMax;
    }

    public void setShape(double newXMin, double newXMax, double newYMin, double newYMax) {
        this.xMin = Math.min(newXMin, newXMax);
        this.xMax = Math.max(newXMin, newXMax);

        double currentAspectRatio = getWidth() / (double) getHeight();

        double newHeight = (xMax - xMin) / currentAspectRatio;
        double centerY = (this.yMin + this.yMax) / 2;

        this.yMin = centerY - newHeight / 2;
        this.yMax = centerY + newHeight / 2;

        this.yMin = Math.min(newYMin, newYMax);
        this.yMax = Math.max(newYMin, newYMax);

        double newWidth = (yMax - yMin) * currentAspectRatio;
        double centerX = (this.xMin + this.xMax) / 2;

        this.xMin = centerX - newWidth / 2;
        this.xMax = centerX + newWidth / 2;
    }

    public void setXShape(double xMin, double xMax) {
        this.xMin = Math.min(xMin, xMax);
        this.xMax = Math.max(xMin, xMax);

        double currentAspectRatio = getWidth() / (double) getHeight();
        double newHeight = (xMax - xMin) / currentAspectRatio;
        double centerY = (yMin + yMax) / 2;

        this.yMin = centerY - newHeight / 2;
        this.yMax = centerY + newHeight / 2;
    }

    public double getYMin() {
        return yMin;
    }

    public double getYMax() {
        return yMax;
    }

    public void setYShape(double yMin, double yMax) {
        this.yMin = Math.min(yMin, yMax);
        this.yMax = Math.max(yMin, yMax);

        double currentAspectRatio = getWidth() / (double) getHeight();
        double newWidth = (yMax - yMin) * currentAspectRatio;
        double centerX = (xMin + xMax) / 2;

        this.xMin = centerX - newWidth / 2;
        this.xMax = centerX + newWidth / 2;
    }

    public int getWidth() {
        return width - 1;
    }

    public void setWidth(int width) {
        this.width = abs(width);
    }

    public int getHeight() {
        return height - 1;
    }

    public void setHeight(int height) {
        this.height = abs(height);
    }

    public double getXDen(){
        return width / (xMax - xMin);
    }

    public double getYDen(){
        return height / (yMax - yMin);
    }

    /**
     * Метод преобразования координаты из декартовой системы в экранную
     * @param x декартовая система координат
     * @return экранная система координат, соответствующая указанной декартовой координате
     */
    public int xCrt2Scr(double x){
        var v = ((x - xMin) * getXDen());
        if (v < -width) v = -width;
        if (v > 2 * width) v = 2 * width;
        return (int)v;
    }

    public int yCrt2Scr(double y){
        var v = ((yMax - y) * getYDen());
        if (v < -height) v = -height;
        if (v > 2 * height) v = 2 * height;
        return (int)v;
    }

    public double xScr2Crt(int x){
        return (double)x / getXDen() + xMin;
    }

    public double yScr2Crt(int y){
        return yMax - (double)y / getYDen();
    }
}
