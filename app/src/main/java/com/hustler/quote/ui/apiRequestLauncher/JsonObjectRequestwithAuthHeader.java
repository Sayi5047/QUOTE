package com.hustler.quote.ui.apiRequestLauncher;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sayi on 07-10-2017.
 */

public class JsonObjectRequestwithAuthHeader extends JsonObjectRequest {
    public JsonObjectRequestwithAuthHeader(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public JsonObjectRequestwithAuthHeader(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String ,String> hashMap=new HashMap<>();
        hashMap.put("Authorization",Constants.API_TOKEN);
        return hashMap;
    }
}
