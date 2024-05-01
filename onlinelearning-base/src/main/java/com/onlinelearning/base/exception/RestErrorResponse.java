package com.onlinelearning.base.exception;

import java.io.Serializable;

public class RestErrorResponse implements Serializable {
    private String errMessage;

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public RestErrorResponse(String errMessage) {
        this.errMessage = errMessage;
    }

    public RestErrorResponse() {
    }

    public String getErrMessage() {
        return errMessage;
    }
}
