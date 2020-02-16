package com.hustler.quote.ui.apiRequestLauncher.request;


import com.hustler.quote.ui.apiRequestLauncher.Base.BaseRequest;

public class ReqUserLogin extends BaseRequest {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
