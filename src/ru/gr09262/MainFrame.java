package ru.gr09262;

import ru.gr09262.gui.*;
import ru.gr09262.math.Converter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {
    private final FractalPainter fPainter = new FractalPainter(-2.0, 1.0, -1.0, 1.0);
    private final JPanel mainPanel = new JPanel() {
        @Override
        public void paint(Graphics g) {
            fPainter.paint(g);
        }
    };
    private final AreaSelector selector = new AreaSelector();
    private final UndoStack undoStack = new UndoStack(); // fPainter.getConverter());

    public MainFrame() {
        mainPanel.setBackground(Color.WHITE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 500));
        add(mainPanel);

        selector.setColor(Color.BLUE);

        JMenuBar menuBar = new JMenuBar();

        FileClass fileSaving = new FileClass(fPainter);

        // код для меню опции файл

        JMenu fileMenu = new JMenu("Файл");

        JMenuItem openFileItem = new JMenuItem("Открыть");
        fileMenu.add(openFileItem);

        openFileItem.addActionListener(e -> fileSaving.openFileDialog(mainPanel, fPainter));

        JMenuItem pngFileItem = new JMenuItem("Сохранить как PNG");
        fileMenu.add(pngFileItem);

        pngFileItem.addActionListener(e -> fileSaving.showSaveDialogPNG(mainPanel, fPainter));

        JMenuItem mndlFileItem = new JMenuItem("Сохранить как MNDL");
        fileMenu.add(mndlFileItem);

        mndlFileItem.addActionListener(e -> fileSaving.showSaveDialogMNDL(mainPanel, fPainter));

        // код для меню опции Правка

        JMenu editMenu = new JMenu("Изменить");

        JMenuItem undoItem = new JMenuItem("Отменить");
        editMenu.add(undoItem);

        undoItem.addActionListener(e -> undoStack.undo(mainPanel, fPainter));

        JMenuItem redoItem = new JMenuItem("Отменить изменения");
        editMenu.add(redoItem);

        redoItem.addActionListener(e -> undoStack.redo(mainPanel, fPainter));

        // для цвета

        JMenu viewMenu = new JMenu("Вид");

        JMenuItem redColorItem = new JMenuItem("Радужный");
        viewMenu.add(redColorItem);

        redColorItem.addActionListener(e -> {fPainter.colorId = 1; mainPanel.repaint();});

        JMenuItem blackWhiteColorItem = new JMenuItem("Чёрно-белый");
        viewMenu.add(blackWhiteColorItem);

        blackWhiteColorItem.addActionListener(e -> {fPainter.colorId = 2; mainPanel.repaint();});

        JMenuItem pinkFloydColorItem = new JMenuItem("Pink-Floyd тема");
        viewMenu.add(pinkFloydColorItem);

        pinkFloydColorItem.addActionListener(e -> {fPainter.colorId = 3; mainPanel.repaint();});

        JMenuItem standartColorItem = new JMenuItem("По умолчанию");
        viewMenu.add(standartColorItem);

        standartColorItem.addActionListener(e -> {fPainter.colorId = -1; mainPanel.repaint();});

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        setJMenuBar(menuBar);

        mainPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown()) {
                    if(e.getKeyCode() == KeyEvent.VK_Z){
                        if(e.isShiftDown()){
                            undoStack.redo(mainPanel, fPainter);
                            mainPanel.repaint();
                        } else {
                            undoStack.undo(mainPanel, fPainter);
                            mainPanel.repaint();
                        }
                    } else if(e.getKeyCode() == KeyEvent.VK_Y){
                        undoStack.redo(mainPanel, fPainter);
                        mainPanel.repaint();
                    }
                }
            }
        });

        mainPanel.setFocusable(true);
        mainPanel.requestFocusInWindow();

        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                selector.setGraphics(mainPanel.getGraphics());
                fPainter.setWidth(mainPanel.getWidth());
                fPainter.setHeight(mainPanel.getHeight());

                var conv = fPainter.getConverter();

                fPainter.updateCoordinates(conv.getXMin(), conv.getXMax(), conv.getYMin(), conv.getYMax());
                mainPanel.repaint();
            }
        });


        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selector.addPoint(e.getPoint());
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    undoStack.addOperation(fPainter.getConverter());
                    double x = fPainter.getConverter().xScr2Crt(e.getX());
                    double y = fPainter.getConverter().yScr2Crt(e.getY());

                    double centerX = (fPainter.getConverter().getXMax() + fPainter.getConverter().getXMin()) / 2;
                    double centerY = (fPainter.getConverter().getYMax() + fPainter.getConverter().getYMin()) / 2;

                    x = x - centerX;
                    y = y - centerY;


                    fPainter.updateCoordinates(fPainter.getConverter().getXMin() + x, fPainter.getConverter().getXMax() + x, fPainter.getConverter().getYMin() + y, fPainter.getConverter().getYMax() + y);
                    mainPanel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                selector.paint();
                Rect rect = selector.getRect();
                if (rect != null) {
                    undoStack.addOperation(fPainter.getConverter());
                    Converter converter = fPainter.getConverter();
                    double newXMin = converter.xScr2Crt(rect.getStartPoint().x);
                    double newYMin = converter.yScr2Crt(rect.getStartPoint().y);
                    double newXMax = converter.xScr2Crt(rect.getStartPoint().x + rect.getWidth());
                    double newYMax = converter.yScr2Crt(rect.getStartPoint().y + rect.getHeigth());

                    fPainter.updateCoordinates(newXMin, newXMax, newYMin, newYMax);
                    mainPanel.repaint();
                }
                selector.clearSelection();
            }
        });

        mainPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                selector.paint();
                selector.addPoint(e.getPoint());
                selector.paint();
            }
        });
    }
}
