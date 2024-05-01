package com.onlinelearning.base.exception;

public class OnlienLearningException extends RuntimeException{
    private String errMessage;

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public OnlienLearningException(String errMessage) {
        this.errMessage = errMessage;
    }

    public OnlienLearningException(String message, String errMessage) {
        super(message);
        this.errMessage = errMessage;
    }
    public static void cast(String message){
        throw new OnlienLearningException(message);
    }
    public static void cast(CommonError error){
        throw new OnlienLearningException(error.getErrMessage());
    }
}
