package ru.gr09262.math;

/**
 * Класс Complex представляет комплексное число с действительной и мнимой частью.
 */
public class Complex {
    private double re; // Действительная часть
    private double im; // Мнимая часть

    /**
     * Конструктор класса Complex.
     * Инициализирует комплексное число с заданными значениями действительной и мнимой частей.
     *
     * @param re Действительная часть.
     * @param im Мнимая часть.
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * Конструктор класса Complex.
     * Инициализирует комплексное число с нулевыми значениями для действительной и мнимой частей.
     */
    public Complex() {
        re = 0;
        im = 0;
    }

    /**
     * Метод для сложения двух комплексных чисел.
     *
     * @param other Другой комплексный номер для сложения.
     * @return Новый объект Complex, представляющий сумму двух комплексных чисел.
     */
    public Complex plus(Complex other){
        return new Complex(re + other.re, im + other.im);
    }

    /**
     * Метод для умножения двух комплексных чисел.
     *
     * @param other Другой комплексный номер для умножения.
     * @return Новый объект Complex, представляющий произведение двух комплексных чисел.
     */
    public Complex times(Complex other){
        return new Complex(
                re * other.re - im * other.im,
                re * other.im + im * other.re
        );
    }

    /**
     * Метод для вычисления квадрата модуля комплексного числа.
     *
     * @return Квадрат модуля комплексного числа.
     */
    public double abs2(){
        return re * re + im * im;
    }
}
