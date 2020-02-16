package com.hustler.quote.ui.apiRequestLauncher.response;

import com.hustler.quote.ui.apiRequestLauncher.Base.BaseResponseUser;

public class ResLoginUser extends BaseResponseUser {
    private String fbAuthToken;
    private String sysAuthToken;

    public String getFbAuthToken() {
        return fbAuthToken;
    }

    public void setFbAuthToken(String fbAuthToken) {
        this.fbAuthToken = fbAuthToken;
    }

    public String getSysAuthToken() {
        return sysAuthToken;
    }

    public void setSysAuthToken(String sysAuthToken) {
        this.sysAuthToken = sysAuthToken;
    }
}
