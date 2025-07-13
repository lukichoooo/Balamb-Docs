package com.khundadze.Balamb_Docs.exceptions;

public class DuplicatePermissionException extends RuntimeException {
    public DuplicatePermissionException(String message) {
        super(message);
    }
}
