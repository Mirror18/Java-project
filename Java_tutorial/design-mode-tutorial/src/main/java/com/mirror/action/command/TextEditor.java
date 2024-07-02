package com.mirror.action.command;

public class TextEditor {
    private StringBuilder buffer = new StringBuilder();

    public void copy() {

    }

    public void paste() {
        String text = getFromClipBoard();
        add(text);
    }

    public void add(String s) {
        buffer.append(s);
    }

    public void delete() {
        if (buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
    }

    public String getState() {
        return buffer.toString();
    }

    public String getFromClipBoard() {
        return getState();
    }
}
