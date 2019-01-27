package io.hustler.qtzy.ui.pojo;

public class QuotzyBaseResponse {
    private boolean apiSuccess;
    private String message;
    private int statuscode;

    public boolean isApiSuccess() {
        return apiSuccess;
    }

    public void setApiSuccess(boolean apiSuccess) {
        apiSuccess = apiSuccess;
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
