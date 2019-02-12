package io.hustler.qtzy.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Locale;

import static java.lang.Boolean.FALSE;

public class PreferenceUtils {
    PreferenceManager preferenceManager;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String DEFAULT_STRING = "NA";
    private static final Short DEFAULT_INT = 0;
    /*KEYS*/
    public static final String KEY_USER_NAME = "USER_NAME";
    public static final String KEY_EMAIL = "EMAIL";
    public static final String KEY_AGE = "AGE";
    public static final String KEY_GENDER = "GENDER";

    public static final String KEY_DOB = "DOB";
    public static final String KEY_LOCALE = "LOCALE";
    public static final String KEY_FB_AUTH_TOKEN = "FB_AUTH_TOKEN";
    public static final String KEY_SYS_AUTH_TOKEN = "SYS_AUTH_TOKEN";
    public static final String KEY_IS_GOOGLE_LOGIN = "IS_GOOGLE_LOGIN";
    public static final String KEY_PROFILE_IMAGE = "PROFILE_IMAGE";
    public static final String KEY_IS_USER_LOGGED_IN = "KEY_IS_USER_LOGGED_IN";
    /*KEYS*/
    /*VALUES*/
    public String USER_NAME = "USER_NAME";
    public String EMAIL = "EMAIL";
    public int AGE = 0;
    public String GENDER = "GENDER";
    public String DOB = "DOB";
    public String LOCALE = "LOCALE";
    public String FB_AUTH_TOKEN = "FB_AUTH_TOKEN";
    public String SYS_AUTH_TOKEN = "SYS_AUTH_TOKEN";
    public boolean IS_GOOGLE_LOGIN = false;
    public String PROFILE_IMAGE = "PROFILE_IMAGE";
    public boolean IS_USER_LOGGED_IN = false;


    /*VALUES*/
    /*CONSTRUCTOR*/
    public PreferenceUtils(Context context) {
        this.preferenceManager = preferenceManager;
        this.context = context;
        sharedPreferences = context.getSharedPreferences("App", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /*UTILS METHODS*/
    private void setStringPreference(String key, String values) {
        editor.putString(key, values);
        editor.apply();
    }

    private void setBooleanPreference(String key, boolean values) {
        editor.putBoolean(key, values);
        editor.apply();
    }

    private void setIntPreference(String key, int values) {
        editor.putInt(key, values);
        editor.apply();
    }

    private String getStringPreferences(String key, String dv) {
        return sharedPreferences.getString(key, dv);
    }

    private int getIntPreferences(String key, int dv) {
        return sharedPreferences.getInt(key, dv);
    }

    private Boolean getBooleanPreferences(String key, boolean dv) {
        return sharedPreferences.getBoolean(key, dv);
    }

    /*UTILS METHODS*/

    /*SETTERS*/
    public void setUSER_NAME(String USER_NAME) {
        setStringPreference(KEY_USER_NAME, USER_NAME);
    }

    public void setEMAIL(String EMAIL) {
        setStringPreference(KEY_EMAIL, EMAIL);

    }

    public void setAGE(int AGE) {
        setIntPreference(KEY_AGE, AGE);

    }

    public void setGENDER(String GENDER) {
        setStringPreference(KEY_GENDER, GENDER);

    }

    public void setDOB(String DOB) {
        setStringPreference(KEY_DOB, DOB);
    }

    public void setLOCALE(String LOCALE) {
        setStringPreference(KEY_LOCALE, LOCALE);
    }

    public void setFB_AUTH_TOKEN(String FB_AUTH_TOKEN) {
        setStringPreference(KEY_FB_AUTH_TOKEN, FB_AUTH_TOKEN);
    }

    public void setSYS_AUTH_TOKEN(String SYS_AUTH_TOKEN) {
        setStringPreference(KEY_SYS_AUTH_TOKEN, SYS_AUTH_TOKEN);
    }

    public void setIS_GOOGLE_LOGIN(boolean IS_GOOGLE_LOGIN) {
        editor.putBoolean(KEY_IS_GOOGLE_LOGIN, IS_GOOGLE_LOGIN);
        editor.apply();
    }

    public void setPROFILE_IMAGE(String PROFILE_IMAGE) {
        setStringPreference(KEY_PROFILE_IMAGE, PROFILE_IMAGE);
    }

    public void setIS_USER_LOGGED_IN(boolean is_user_logged_in) {
        setBooleanPreference(KEY_IS_USER_LOGGED_IN, is_user_logged_in);
    }

    /*GETTERS*/

    public String getUSER_NAME() {
        return getStringPreferences(KEY_USER_NAME, DEFAULT_STRING);
    }

    public String getEMAIL() {
        return getStringPreferences(KEY_EMAIL, DEFAULT_STRING);
    }

    public int getAGE() {
        return getIntPreferences(KEY_EMAIL, DEFAULT_INT);
    }

    public String getGENDER() {
        return getStringPreferences(KEY_GENDER, DEFAULT_STRING);
    }

    public String getDOB() {
        return getStringPreferences(KEY_DOB, DEFAULT_STRING);
    }

    public String getLOCALE() {
        return getStringPreferences(KEY_LOCALE, DEFAULT_STRING);
    }

    public String getFB_AUTH_TOKEN() {
        return getStringPreferences(KEY_FB_AUTH_TOKEN, DEFAULT_STRING);
    }

    public String getSYS_AUTH_TOKEN() {
        return getStringPreferences(KEY_SYS_AUTH_TOKEN, DEFAULT_STRING);
    }

    public boolean isIS_GOOGLE_LOGIN() {
        return getBooleanPreferences(KEY_IS_GOOGLE_LOGIN, FALSE);
    }

    public String getPROFILE_IMAGE() {
        return getStringPreferences(KEY_PROFILE_IMAGE, DEFAULT_STRING);
    }

    public boolean isIS_USER_LOGGED_IN() {
        return getBooleanPreferences(KEY_IS_USER_LOGGED_IN, FALSE);
    }
}
