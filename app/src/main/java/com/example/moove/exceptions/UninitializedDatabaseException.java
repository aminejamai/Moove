package com.example.moove.exceptions;

public class UninitializedDatabaseException extends Throwable {
    private static final long serialVersionUID = -8078059893066017473L;

    public UninitializedDatabaseException() { super("Firestore database is uninitialised!"); }
}
