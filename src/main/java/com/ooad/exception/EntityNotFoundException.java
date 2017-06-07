package com.ooad.exception;

/**
 * Created by Qizixi on 2017/6/7.
 */
public class EntityNotFoundException extends RiskCheckException {

    public EntityNotFoundException(){
        super();
    }

    public EntityNotFoundException(String message){
        super(message);
    }
}
