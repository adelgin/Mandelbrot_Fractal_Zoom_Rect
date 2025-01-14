package ru.gr09262.gui;

import java.awt.*;

/**
 * Этот интерфейс реализует основные методы которые будут в программе
 */
public interface Painter {
    void paint(Graphics g);
    int getWidth();
    void setWidth(int width);
    int getHeight();
    void setHeight(int height);
}

