package com.hustler.quote.ui.apiRequestLauncher;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.hustler.quote.R;
import com.hustler.quote.ui.networkhandler.MySingleton;
import com.hustler.quote.ui.pojo.ImagesResponse;
import com.hustler.quote.ui.pojo.RandomQuotes;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import org.json.JSONObject;

/**
 * Created by Sayi on 07-10-2017.
 */

public class Restutility {
    Activity activity;

    public Restutility(Activity activity) {
        this.activity = activity;
    }

    public void logtheResponse(JSONObject response) {
        Log.d("API RESPONSE", response.toString());
    }

    public void logtheRequest(String val) {
        Log.d("API REQUEST --> ", val);
    }


    public void getRandomQuotes(final Context context, final QuotesApiResponceListener listener) {
        logtheRequest(Constants.API_FAVQ_RANDOM_QUOTES);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequestwithAuthHeader(Request.Method.GET, Constants.API_FAVQ_RANDOM_QUOTES, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        logtheResponse(response);
                        RandomQuotes randomQuotes = new Gson().fromJson(response.toString(), RandomQuotes.class);
                        if (randomQuotes.getQuotes().size() <= 0) {
                            listener.onError(activity.getResources().getString(R.string.no_quotes_available));
                        } else {
                            listener.onSuccess(randomQuotes.getQuotes());
                        }

                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(activity.getResources().getString(R.string.no_quotes_available));
                        Toast_Snack_Dialog_Utils.show_ShortToast(activity, error.getMessage());
                    }
                });
        MySingleton.addJsonObjRequest(activity, jsonObjectRequest);
    }

    public void getRandomImages(final Context context, final ImagesApiResponceListner listner,final String request) {
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, request, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ImagesResponse imagesResponse = new Gson().fromJson(response.toString(), ImagesResponse.class);
                        if (imagesResponse.hits.size() > 0) {
                            listner.onSuccess(imagesResponse.hits);
                        } else {
                            listner.onError(context.getString(R.string.no_images));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listner.onError(error.getMessage());
            }
        });
        MySingleton.addJsonObjRequest(context, jsonObject);
    }
}
