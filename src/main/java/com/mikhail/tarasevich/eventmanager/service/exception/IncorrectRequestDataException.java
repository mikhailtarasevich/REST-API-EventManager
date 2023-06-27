package com.mikhail.tarasevich.eventmanager.service.exception;

public class IncorrectRequestDataException extends IllegalArgumentException{

    public IncorrectRequestDataException(String errMessage){
        super(errMessage);
    }

}
