package io.hustler.qtzy.ui.pojo;

public class QuotzyBaseResponse {
    private boolean isApiSuccess;
    private String message;
    private int statuscode;

    public boolean isApiSuccess() {
        return isApiSuccess;
    }

    public void setApiSuccess(boolean apiSuccess) {
        isApiSuccess = apiSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }
}
