package com.hustler.quote.ui.apiRequestLauncher.request;

import com.hustler.quote.ui.apiRequestLauncher.Base.BaseRequest;

public class ReqUserGoogleSignup extends BaseRequest {
    private String gId;
    private String email;
    private String password;
    private String personId;

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getPersonId() {
        return personId;
    }

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
