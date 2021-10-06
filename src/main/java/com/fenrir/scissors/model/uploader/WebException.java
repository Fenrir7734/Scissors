package com.fenrir.scissors.model.uploader;

public class WebException extends Exception {

    private StatusCode code;

    public WebException(StatusCode code) {
        super(code.getDescription());
        this.code = code;
    }

    public StatusCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return "Status code " + code.getCode() + ": " + code.getDescription();
    }
}
