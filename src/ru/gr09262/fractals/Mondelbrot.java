package ru.gr09262.fractals;

import ru.gr09262.math.Complex;

/**
 * Класс Mondelbrot представляет фрактал Мандельброта и содержит методы для его вычисления.
 */
public class Mondelbrot {
    private final double r2 = 4.0; // Квадрат радиуса
    private int maxIter = 200; // Максимальное количество итераций

    /**
     * Метод для получения максимального количества итераций.
     *
     * @return Максимальное количество итераций.
     */
    public int getMaxIter() {
        return this.maxIter;
    }

    /**
     * Метод для установки нового максимального количества итераций.
     *
     * @param newMaxIter Новое максимальное количество итераций.
     */
    public void setMaxIter(int newMaxIter) {
        if (newMaxIter < 200) {
            newMaxIter = 200;
        }
        this.maxIter = newMaxIter;
    }

    /**
     * Метод для проверки, принадлежит ли комплексное число множеству Мандельброта.
     *
     * @param c Комплексное число для проверки.
     * @return Количество итераций, пройденных до выхода за пределы.
     */
    public int isInSet(Complex c) {
        Complex z = new Complex();
        int i = 0;
        while(z.abs2() < r2 && i < maxIter) {
            z = z.times(z).plus(c);
            i++;
        }
        return i;
    }
}
