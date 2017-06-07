package com.ooad.exception;

/**
 * Created by Qizixi on 2017/6/7.
 */
public class NullAttributeException extends RiskCheckException {

    public NullAttributeException(){
        super();
    }

    public NullAttributeException(String message){
        super(message);
    }
}
