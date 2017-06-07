package com.ooad.exception;

/**
 * Created by Qizixi on 2017/6/7.
 */
public class DuplicateAttributeException extends RiskCheckException{

    public DuplicateAttributeException(){
        super();
    }

    public DuplicateAttributeException(String message){
        super(message);
    }
}
