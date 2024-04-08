package com.example.SpringBootResearch.exceptions;

public class FileEmptyException extends RuntimeException{
    public FileEmptyException(String message) {
        super(message);
    }
}
