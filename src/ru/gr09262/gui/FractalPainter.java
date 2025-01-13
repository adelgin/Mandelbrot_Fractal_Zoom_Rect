package ru.gr09262.gui;

import ru.gr09262.fractals.Mondelbrot;
import ru.gr09262.math.Complex;
import ru.gr09262.math.Converter;

import java.awt.*;
import java.util.ArrayList;

public class FractalPainter implements Painter{

    private final Mondelbrot mondelbrot = new Mondelbrot();
    private final Converter converter;
    ArrayList<Thread> threads = new ArrayList<Thread>();
    public int colorId;

    public FractalPainter(double xMin, double xMax, double yMin, double yMax){
        converter = new Converter(xMin,xMax,yMin,yMax,0,0);
    }

    public void updateCoordinates(double xMin, double xMax,
                                  double yMin, double yMax){
        converter.setShape(xMin, xMax, yMin, yMax);
    }

    private Color setColor(int iterations, double x, double y) {
        Color color = new Color(0,0,0);
        if (colorId == 1) {
            if (iterations != 200) {
                float hue = (float) iterations / 200;
                color = Color.getHSBColor(hue, 1.0f, 1.0f);
            }
        } else if (colorId == 2) {
            color = mondelbrot.isInSet(new Complex(x, y)) == 200 ? Color.BLACK : Color.WHITE;
        } else if (colorId == 3) {
            color = (iterations % 2 == 0) ? Color.RED : Color.BLUE;
        } else {
            if (iterations != 200) {
                int redValue = Math.min(255, (iterations * 255) / 200);
                color = new Color(redValue, 0, 0);
            } else {
                color = Color.BLACK;
            }
        }
        return color;
    }

    @Override
    public void paint(Graphics g) {
        try {
            for (Thread thread : threads) {
                thread.interrupt();
            }
            threads.clear();

            int numThreads = 8;

            int width = converter.getWidth() + 1;
            int height = converter.getHeight() + 1;

            int segmentWidth = width / numThreads;
            for (int t = 0; t < numThreads; t++) {
                final int startX = t * segmentWidth;
                final int endX = (t == numThreads - 1) ? width : startX + segmentWidth;

                Runnable task = () -> {
                    for (int i = startX; i < endX; i++) {
                        for (int j = 0; j <= height; j++) {
                            var x = converter.xScr2Crt(i);
                            var y = converter.yScr2Crt(j);

                            var iterations = mondelbrot.isInSet(new Complex(x, y));

                            Color color = setColor(iterations, x, y);

                            synchronized (g) {
                                g.setColor(color);
                                g.fillRect(i, j, 1, 1);
                            }
                        }
                    }
                };
                threads.add(new Thread(task));
                threads.getLast().start();
            }

            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getWidth() {
        return converter.getWidth();
    }

    @Override
    public void setWidth(int width) {
        converter.setWidth(width);
    }

    @Override
    public int getHeight() {
        return converter.getHeight();
    }

    @Override
    public void setHeight(int height) {
        converter.setHeight(height);
    }

    public Converter getConverter() {
        return this.converter;
    }
}
