package com.ooad.exception;

/**
 * Created by Qizixi on 2017/6/7.
 */
public class InvalidAttributeException extends RiskCheckException {

    public InvalidAttributeException(){
        super();
    }

    public InvalidAttributeException(String message){
        super(message);
    }
}
