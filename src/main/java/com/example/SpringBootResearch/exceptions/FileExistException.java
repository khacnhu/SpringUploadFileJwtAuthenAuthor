package com.example.SpringBootResearch.exceptions;

public class FileExistException extends RuntimeException{

    public FileExistException(String message) {
        super(message);
    }

}
