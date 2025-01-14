package ru.gr09262.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Класс для работы с файлами, используется для сохранения и открытия файлов
 */
public class FileClass extends JFileChooser {
    private final FileFilter pngFilter;
    private final FileFilter mndlFilter;
    public final FractalPainter fractalPainter;

    /**
     * Конструктор класса. Здесь задаются параметры для сохранения в формат .png и в произвольный файловый формат .mndl
     * @param Painter сюда передаётся FractalPainter, чтобы получить с него все данные о текущей ситуации на картинке
     */
    public FileClass(FractalPainter Painter) {
        fractalPainter = Painter;
        pngFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().toLowerCase().endsWith(".png");
            }
            @Override
            public String getDescription() {
                return "*.png";
            }
        };
        mndlFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().toLowerCase().endsWith(".mndl");
            }
            @Override
            public String getDescription() {
                return "*.mndl";
            }
        };
        this.setAcceptAllFileFilterUsed(false);
    }

    /**
     * Метод для сохранения файла в формат PNG
     * @param panel текущая панель с картинкой фрактала
     * @param fractalPainter сюда передаётся FractalPainter, чтобы получить с него все данные о текущей ситуации на картинке
     */
    public void showSaveDialogPNG(JPanel panel, FractalPainter fractalPainter){
        this.setDialogTitle("Сохранение файла");
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.resetChoosableFileFilters();
        this.addChoosableFileFilter(pngFilter);
        int result = this.showSaveDialog(panel);

        if (result == JFileChooser.APPROVE_OPTION ){
            String extension = "";
            if (this.getFileFilter() == pngFilter) {
                extension = ".png";
            }

            File fileToSave = this.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(extension)) {
                fileToSave = new File(fileToSave.getAbsolutePath() + extension);
            }

            BufferedImage image = new BufferedImage(panel.getWidth(),
                    panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
            panel.paint(image.createGraphics());
            try {
                ImageIO.write(image, extension.substring(1), fileToSave);
                JOptionPane.showMessageDialog(panel,
                        "Изображение успешно сохранено: " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(panel,
                        "Ошибка при сохранении изображения: " + e.getMessage());
            }
            }
        }

    /**
     * Метод для сохранения файла в формат MNDL
     * @param panel текущая панель с картинкой фрактала
     * @param fractalPainter сюда передаётся FractalPainter, чтобы получить с него все данные о текущей ситуации на картинке
     */
    public void showSaveDialogMNDL(JPanel panel, FractalPainter fractalPainter){
        this.setDialogTitle("Сохранение файла");
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.resetChoosableFileFilters();
        this.addChoosableFileFilter(mndlFilter);
        int result = this.showSaveDialog(panel);

        if (result == JFileChooser.APPROVE_OPTION ){
            String extension = "";
            if (this.getFileFilter() == mndlFilter) {
                extension = ".mndl";
            }

            File fileToSave = this.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(extension)) {
                fileToSave = new File(fileToSave.getAbsolutePath() + extension);
            }

            BufferedImage image = new BufferedImage(panel.getWidth(),
                    panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
            panel.paint(image.createGraphics());
            try {
                FileWriter fileWriter = new FileWriter(fileToSave, false);
                var saved_options = fractalPainter.getConverter();
                fileWriter.write(String.format("%s\n%s\n%s\n%s\n", saved_options.getXMin(), saved_options.getXMax(), saved_options.getYMin(), saved_options.getYMax()));
                fileWriter.close();
                JOptionPane.showMessageDialog(panel,
                        "Файл: " + fileToSave.getAbsolutePath() + " успешно сохранён.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(panel,
                        "Ошибка при сохранении изображения: " + e.getMessage());
            }
        }
    }

    /**
     * Метод для открытия файла
     * @param panel текущая панель с картинкой фрактала
     * @param fractalPainter сюда передаётся FractalPainter, чтобы получить с него все данные о текущей ситуации на картинке
     */
    public void openFileDialog(JPanel panel, FractalPainter fractalPainter) {
        this.setDialogTitle("Сохранение файла");
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.resetChoosableFileFilters();
        this.addChoosableFileFilter(mndlFilter);
        int result = this.showOpenDialog(panel);

        if (result == JFileChooser.APPROVE_OPTION ){
            File fileToOpen = this.getSelectedFile();
            double Xmin = fractalPainter.getConverter().getXMin(), Xmax = fractalPainter.getConverter().getXMax(), Ymin = fractalPainter.getConverter().getYMin(), Ymax = fractalPainter.getConverter().getYMax();

            try (Scanner scanner = new Scanner(fileToOpen)) {
                if (scanner.hasNextLine()) {
                    Xmin = Double.parseDouble(scanner.nextLine());
                }
                if (scanner.hasNextLine()) {
                    Xmax = Double.parseDouble(scanner.nextLine());
                }
                if (scanner.hasNextLine()) {
                    Ymin = Double.parseDouble(scanner.nextLine());
                }
                if (scanner.hasNextLine()) {
                    Ymax = Double.parseDouble(scanner.nextLine());
                }

                fractalPainter.updateCoordinates(Xmin, Xmax, Ymin, Ymax);
                panel.repaint();
            } catch (FileNotFoundException e) {
                System.err.println("Файл не найден: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Ошибка преобразования строки в число: " + e.getMessage());
            }
        }
    }
}
