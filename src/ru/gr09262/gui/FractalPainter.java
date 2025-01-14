package ru.gr09262.gui;

import ru.gr09262.fractals.Mondelbrot;
import ru.gr09262.math.Complex;
import ru.gr09262.math.Converter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Класс FractalPainter реализует интерфейс Painter и отвечает за рисование фрактала Мандельброта.
 */
public class FractalPainter implements Painter {
    private final Mondelbrot mondelbrot = new Mondelbrot();
    private final Converter converter;
    public int colorId;

    /**
     * Конструктор класса FractalPainter.
     * Инициализирует конвертер с заданными границами координат.
     *
     * @param xMin Минимальное значение по оси X.
     * @param xMax Максимальное значение по оси X.
     * @param yMin Минимальное значение по оси Y.
     * @param yMax Максимальное значение по оси Y.
     */
    public FractalPainter(double xMin, double xMax, double yMin, double yMax){
        converter = new Converter(xMin,xMax,yMin,yMax,0,0);
    }

    /**
     * Метод для обновления границ координат.
     *
     * @param xMin Новое минимальное значение по оси X.
     * @param xMax Новое максимальное значение по оси X.
     * @param yMin Новое минимальное значение по оси Y.
     * @param yMax Новое максимальное значение по оси Y.
     */
    public void updateCoordinates(double xMin, double xMax,
                                  double yMin, double yMax){
        converter.setShape(xMin, xMax, yMin, yMax);
    }

    /**
     * Метод для установки цвета в зависимости от количества итераций.
     *
     * @param iterations Количество итераций.
     * @param x Координата X.
     * @param y Координата Y.
     * @return Цвет, соответствующий количеству итераций.
     */
    private Color setColor(int iterations, double x, double y) {
        Color color = new Color(0,0,0);
        if (colorId == 1) {
            if (iterations != mondelbrot.getMaxIter()) {
                float hue = (float) iterations / mondelbrot.getMaxIter();
                color = Color.getHSBColor(hue, 1.0f, 1.0f);
            }
        } else if (colorId == 2) {
            color = mondelbrot.isInSet(new Complex(x, y)) == mondelbrot.getMaxIter() ? Color.BLACK : Color.WHITE;
        } else if (colorId == 3) {
            color = (iterations % 2 == 0) ? Color.RED : Color.BLUE;
        } else if (colorId == 4) {
            if (iterations != mondelbrot.getMaxIter()) {
                float ratio = (float) iterations / mondelbrot.getMaxIter();
                Color startColor = new Color(135, 206, 250);

                Color endColor = Color.WHITE;

                int red = (int) (startColor.getRed() + ratio * (endColor.getRed() - startColor.getRed()));
                int green = (int) (startColor.getGreen() + ratio * (endColor.getGreen() - startColor.getGreen()));
                int blue = (int) (startColor.getBlue() + ratio * (endColor.getBlue() - startColor.getBlue()));

                color = new Color(red, green, blue);

            } else {
                color = Color.WHITE;
            }
        } else {
            if (iterations != mondelbrot.getMaxIter()) {
                int redValue = Math.min(255, (iterations * 255) / mondelbrot.getMaxIter());
                color = new Color(redValue, 0, 0);
            } else {
                color = Color.BLACK;
            }
        }
        return color;
    }

    /**
     * Метод для отрисовки множества. Перегруженный метод из Painter.
     * @param g картинка
     */
    @Override
    public void paint(Graphics g) {
        int numThreads = 8;
        int width = converter.getWidth() + 1;
        int height = converter.getHeight() + 1;
        int segmentWidth = width / numThreads;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        try {
            for (int t = 0; t < numThreads; t++) {
                final int startX = t * segmentWidth;
                final int endX = (t == numThreads - 1) ? width : startX + segmentWidth;

                Runnable task = () -> {
                    for (int i = startX; i < endX; i++) {
                        for (int j = 0; j <= height; j++) {
                            var x = converter.xScr2Crt(i);
                            var y = converter.yScr2Crt(j);

                            int maxIters = ((int) ((100 / Math.sqrt(Math.sqrt(converter.getXMax() - converter.getXMin())))));

                            mondelbrot.setMaxIter(Math.min(maxIters, 1500));
                            var iterations = mondelbrot.isInSet(new Complex(x, y));

                            Color color = setColor(iterations, x, y);

                            synchronized (g) {
                                g.setColor(color);
                                g.fillRect(i, j, 1, 1);
                            }
                        }
                    }
                };
                executor.submit(task);
            }
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
    }


    /**
     * Возвращает ширину
     */
    @Override
    public int getWidth() {
        return converter.getWidth();
    }

    /**
     * Задаёт ширину
     */
    @Override
    public void setWidth(int width) {
        converter.setWidth(width);
    }

    /**
     * Возвращает высоту
     */
    @Override
    public int getHeight() {
        return converter.getHeight();
    }

    /**
     * Задаёт высоту
     */
    @Override
    public void setHeight(int height) {
        converter.setHeight(height);
    }

    /**
     * Возвращает объект конвертер
     */
    public Converter getConverter() {
        return this.converter;
    }

}
