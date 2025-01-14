package ru.gr09262.math;

import static java.lang.Math.abs;

/**
 * Класс для конвертации координат
 */
public class Converter {
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private int width;
    private int height;

    /**
     * Конструктор класса Converter.
     * Инициализирует значения для осей и размеров экрана.
     *
     * @param xMin Минимальное значение по оси X.
     * @param xMax Максимальное значение по оси X.
     * @param yMin Минимальное значение по оси Y.
     * @param yMax Максимальное значение по оси Y.
     * @param width Ширина экрана.
     * @param height Высота экрана.
     */
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

    /**
     * Возвращает координаты по нижнему x
     * @return Xmin Координаты по нижнему x
     */
    public double getXMin() {
        return xMin;
    }

    /**
     * Возвращает координаты по верхнему x
     * @return Xmin координаты по верхнему x
     */
    public double getXMax() {
        return xMax;
    }

    /**
     * Устанавливает новые границы для осей X и Y.
     *
     * @param newXMin Новое минимальное значение по оси X.
     * @param newXMax Новое максимальное значение по оси X.
     * @param newYMin Новое минимальное значение по оси Y.
     * @param newYMax Новое максимальное значение по оси Y.
     */
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

    /**
     * Устанавливает новые границы для оси X.
     *
     * @param xMin Новое минимальное значение по оси X.
     * @param xMax Новое максимальное значение по оси X.
     */
    public void setXShape(double xMin, double xMax) {
        this.xMin = Math.min(xMin, xMax);
        this.xMax = Math.max(xMin, xMax);

        double currentAspectRatio = getWidth() / (double) getHeight();
        double newHeight = (xMax - xMin) / currentAspectRatio;
        double centerY = (yMin + yMax) / 2;

        this.yMin = centerY - newHeight / 2;
        this.yMax = centerY + newHeight / 2;
    }

    /**
     * Возвращает координаты по нижнему y
     * @return yMin Координаты по нижнему y
     */
    public double getYMin() {
        return yMin;
    }

    /**
     * Возвращает координаты по верхнему y
     * @return yMax координаты по верхнему y
     */
    public double getYMax() {
        return yMax;
    }

    /**
     * Устанавливает новые границы для оси Y.
     *
     * @param yMin Новое минимальное значение по оси Y.
     * @param yMax Новое максимальное значение по оси Y.
     */
    public void setYShape(double yMin, double yMax) {
        this.yMin = Math.min(yMin, yMax);
        this.yMax = Math.max(yMin, yMax);

        double currentAspectRatio = getWidth() / (double) getHeight();
        double newWidth = (yMax - yMin) * currentAspectRatio;
        double centerX = (xMin + xMax) / 2;

        this.xMin = centerX - newWidth / 2;
        this.xMax = centerX + newWidth / 2;
    }

    /**
     * Возвращает значение ширины
     */
    public int getWidth() {
        return width - 1;
    }

    /**
     * Устанавливает ширину экрана.
     *
     * @param width Новая ширина экрана.
     */
    public void setWidth(int width) {
        this.width = abs(width);
    }

    public int getHeight() {
        return height - 1;
    }

    /**
     * Устанавливает высоту экрана.
     *
     * @param height Новая высота экрана.
     */
    public void setHeight(int height) {
        this.height = abs(height);
    }

    /**
     * Возвращает плотность пикселей по оси X
     */
    public double getXDen(){
        return width / (xMax - xMin);
    }

    /**
     * Возвращает плотность пикселей по оси Y
     */
    public double getYDen(){
        return height / (yMax - yMin);
    }

    /**
     * Метод преобразования координаты из декартовой системы в экранную.
     *
     * @param x Декартовая система координат.
     * @return Экранная система координат, соответствующая указанной декартовой координате.
     */
    public int xCrt2Scr(double x){
        var v = ((x - xMin) * getXDen());
        if (v < -width) v = -width;
        if (v > 2 * width) v = 2 * width;
        return (int)v;
    }

    /**
     * Метод преобразования координаты из декартовой системы в экранную.
     *
     * @param y Декартовая система координат.
     * @return Экранная система координат, соответствующая указанной декартовой координате.
     */
    public int yCrt2Scr(double y){
        var v = ((yMax - y) * getYDen());
        if (v < -height) v = -height;
        if (v > 2 * height) v = 2 * height;
        return (int)v;
    }

    /**
     * Метод преобразования экранной координаты в декартовую.
     *
     * @param x Экранная координата.
     * @return Декартовая координата, соответствующая указанной экранной координате.
     */
    public double xScr2Crt(int x){
        return (double)x / getXDen() + xMin;
    }

    /**
     * Метод преобразования экранной координаты в декартовую.
     *
     * @param y Экранная координата.
     * @return Декартовая координата, соответствующая указанной экранной координате.
     */
    public double yScr2Crt(int y){
        return yMax - (double)y / getYDen();
    }
}
