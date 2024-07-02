package com.mirror.annotation.validator;

/**
 * @author mirror
 */
public interface Validator {
    void validate(String email, String password, String name);
}