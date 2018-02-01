package com.hustler.quote.ui.apiRequestLauncher;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sayi on 07-10-2017.
 */

public class JsonArrayRequestwithAuthHeader extends JsonArrayRequest {
    public JsonArrayRequestwithAuthHeader(int method, String url, JSONArray jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("content-type", "application/json");
        hashMap.put("accept-version", "v1");
        hashMap.put("Authorization", Constants.UNSPLASH_APP_ID);
        return hashMap;
    }
}
