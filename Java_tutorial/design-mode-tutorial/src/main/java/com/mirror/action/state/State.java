package com.mirror.action.state;

/**
 * @author mirror
 */
public interface State {
    String reply(String input);
    String init();
}
