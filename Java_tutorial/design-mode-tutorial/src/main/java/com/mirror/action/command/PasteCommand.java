package com.mirror.action.command;

/**
 * @author mirror
 */
public class PasteCommand implements Command {
    private final TextEditor receiver;

    public PasteCommand(TextEditor receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.paste();
    }
}
