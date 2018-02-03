package com.hustler.quote.ui.apiRequestLauncher;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.hustler.quote.R;
import com.hustler.quote.ui.networkhandler.MySingleton;
import com.hustler.quote.ui.pojo.ImagesResponse;
import com.hustler.quote.ui.pojo.UnsplashImages_Collection_Response;
import com.hustler.quote.ui.pojo.Unsplash_Image_collection_response_listener;
import com.hustler.quote.ui.pojo.unspalsh.ImagesFromUnsplashResponse;
import com.hustler.quote.ui.pojo.unspalsh.UnsplashImageResponse;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;

import org.json.JSONArray;
import org.json.JSONException;
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

//
//    public void getRandomQuotes(final Context context, final QuotesApiResponceListener listener) {
//        logtheRequest(Constants.API_FAVQ_RANDOM_QUOTES);
//        JsonObjectRequest jsonObjectRequest = new JsonArrayRequestwithAuthHeader(Request.Method.GET, Constants.API_FAVQ_RANDOM_QUOTES, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        logtheResponse(response);
//                        RandomQuotes randomQuotes = new Gson().fromJson(response.toString(), RandomQuotes.class);
//                        if (randomQuotes.getQuotes().size() <= 0) {
//                            listener.onError(activity.getResources().getString(R.string.no_quotes_available));
//                        } else {
//                            listener.onSuccess(randomQuotes.getQuotes());
//                        }
//
//                    }
//                }
//                ,
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        listener.onError(activity.getResources().getString(R.string.no_quotes_available));
//                        Toast_Snack_Dialog_Utils.show_ShortToast(activity, error.getMessage());
//                    }
//                });
//        MySingleton.addJsonObjRequest(activity, jsonObjectRequest);
//    }

    public void getRandomImages(final Context context, final ImagesApiResponceListner listner, final String request) {
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
                listner.onError(getRelevantVolleyErrorMessage(context, error));
            }
        });
        MySingleton.addJsonObjRequest(context, jsonObject);
    }

    public void getUnsplashRandomImages(final Context context, final ImagesFromUnsplashResponse listener, final String request) {
        logtheRequest(request);
        JsonArrayRequest request1 = new JsonArrayRequestwithAuthHeader(Request.Method.GET, request, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                UnsplashImageResponse imagesFromUnsplashResponse = new UnsplashImageResponse();
                imagesFromUnsplashResponse.Value = new Unsplash_Image[response.length()];
                Log.d("RESPONSE LENGTGH", String.valueOf(response.length()));
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Unsplash_Image unspalshImage = new Gson().fromJson(response.get(i).toString(), Unsplash_Image.class);
                        imagesFromUnsplashResponse.Value[i] = unspalshImage;
                        Log.d("JSON ARRAY NAME", unspalshImage.getWidth());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onSuccess(imagesFromUnsplashResponse.Value);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR LENGTGH", String.valueOf(error));

                listener.onError(getRelevantVolleyErrorMessage(context, error));

            }
        });
        MySingleton.addJsonArrayObjRequest(context, request1);
    }

    public void getUnsplashUSERImages(final Context context, final ImagesFromUnsplashResponse listener, final String request) {
        logtheRequest(request);
        JsonArrayRequest request1 = new JsonArrayRequestwithAuthHeader(Request.Method.GET, request, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                UnsplashImageResponse imagesFromUnsplashResponse = new UnsplashImageResponse();
                imagesFromUnsplashResponse.Value = new Unsplash_Image[response.length()];
                Log.d("RESPONSE LENGTGH", String.valueOf(response.length()));
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Unsplash_Image unspalshImage = new Gson().fromJson(response.get(i).toString(), Unsplash_Image.class);
                        imagesFromUnsplashResponse.Value[i] = unspalshImage;
                        Log.d("JSON ARRAY NAME", unspalshImage.getWidth());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onSuccess(imagesFromUnsplashResponse.Value);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                listener.onError(getRelevantVolleyErrorMessage(context, error));

            }
        });
        MySingleton.addJsonArrayObjRequest(context, request1);
    }

    public void getUnsplash_Collections_Images(final Context context, final Unsplash_Image_collection_response_listener listener, final String request) {
        logtheRequest(request);
        JsonObjectRequest request1 = new JsonObjectRequestwithAuthHeader(Request.Method.GET, request, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE LENGTGH", String.valueOf(response.length()));
                        UnsplashImages_Collection_Response unspalshImage = new Gson().fromJson(response.toString(), UnsplashImages_Collection_Response.class);
                        listener.onSuccess(unspalshImage);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                listener.onError(getRelevantVolleyErrorMessage(context, error));

            }
        });
        MySingleton.addJsonObjRequest(context, request1);
    }

    public String getRelevantVolleyErrorMessage(Context context, VolleyError volleyError) {
        try {
            volleyError.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        try {
            if (volleyError instanceof NoConnectionError) {
                return context.getString(R.string.NO_CONNECTION_ERROR);
            } else if (volleyError instanceof TimeoutError) {
                return context.getString(R.string.TIMEOUT_ERROR);
            } else if (volleyError instanceof AuthFailureError) {
                return context.getString(R.string.AUTH_FAILURE_ERROR);
            } else if (volleyError instanceof ServerError) {
                return context.getString(R.string.SERVER_ERROR);
            } else if (volleyError instanceof NetworkError) {
                return context.getString(R.string.NETWORK_ERROR);
            } else if (volleyError instanceof ParseError) {
                return context.getString(R.string.PARSE_ERROR);
            }
            return null;
        } catch (Exception e) {
//            FirebaseCrash.log(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


}
