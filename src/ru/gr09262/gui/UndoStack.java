package ru.gr09262.gui;

import java.util.Stack;
import ru.gr09262.math.Converter;

import javax.swing.*;

public class UndoStack {
    private final Stack<Converter> undoStack = new Stack<>();
    private final Stack<Converter> redoStack = new Stack<>();
    private Converter currentConverter;

    public void addOperation(Converter converter) {
        Converter newOperation = new Converter(converter.getXMin(), converter.getXMax(), converter.getYMin(), converter.getYMax(), converter.getWidth(), converter.getHeight());
        undoStack.push(newOperation);
        redoStack.clear();
        currentConverter = converter;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void undo(JPanel panel, FractalPainter fPainter) {
        if (canUndo()) {
            if (currentConverter != null) {
                redoStack.push(new Converter(currentConverter.getXMin(), currentConverter.getXMax(), currentConverter.getYMin(), currentConverter.getYMax(), currentConverter.getWidth(), currentConverter.getHeight())); // Сохраняем текущее состояние в redo
            }

            Converter operation = undoStack.pop();

            fPainter.updateCoordinates(operation.getXMin(), operation.getXMax(), operation.getYMin(), operation.getYMax());
            panel.repaint();
            currentConverter = operation;
        }
    }

    public void redo(JPanel panel, FractalPainter fPainter) {
        if (canRedo()) {
            Converter operation = redoStack.pop();
            if (currentConverter != null) {
                undoStack.push(new Converter(currentConverter.getXMin(), currentConverter.getXMax(), currentConverter.getYMin(), currentConverter.getYMax(), currentConverter.getWidth(), currentConverter.getHeight()));
            }

            fPainter.updateCoordinates(operation.getXMin(), operation.getXMax(), operation.getYMin(), operation.getYMax());
            panel.repaint();
            currentConverter = operation;
        }
    }
}

