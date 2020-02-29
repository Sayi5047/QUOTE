
package com.hustler.quote.ui.apiRequestLauncher;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.hustler.quote.ui.apiRequestLauncher.Base.BaseResponse;
import com.hustler.quote.ui.apiRequestLauncher.ListnereInterfaces.ImagesApiResponceListner;
import com.hustler.quote.ui.apiRequestLauncher.ListnereInterfaces.QuotzyApiResponseListener;
import com.hustler.quote.ui.apiRequestLauncher.ListnereInterfaces.ResponseListener;
import com.hustler.quote.ui.apiRequestLauncher.ListnereInterfaces.StickerResponseListener;
import com.hustler.quote.ui.apiRequestLauncher.request.ReqUserGoogleSignup;
import com.hustler.quote.ui.apiRequestLauncher.request.ReqUserLogin;
import com.hustler.quote.ui.apiRequestLauncher.request.ReqUserSignup;
import com.hustler.quote.ui.apiRequestLauncher.response.ResLoginUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.hustler.quote.BuildConfig;
import com.hustler.quote.R;

import com.hustler.quote.ui.networkhandler.MySingleton;
import com.hustler.quote.ui.pojo.ImagesResponse;
import com.hustler.quote.ui.pojo.ResGetSearchResultsDto;
import com.hustler.quote.ui.pojo.listeners.GetCollectionsResponseListener;
import com.hustler.quote.ui.pojo.listeners.SearchImagesResponseListener;
import com.hustler.quote.ui.pojo.unspalsh.FeaturedImagesRespoonseListener;
import com.hustler.quote.ui.pojo.unspalsh.ResGetCollectionsDto;
import com.hustler.quote.ui.pojo.unspalsh.UnsplashImageResponse;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;


/**
 * Created by Sayi on 07-10-2017.
 */
/*   Copyright [2018] [Sayi Manoj Sugavasi]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
public class Restutility {
    Activity activity;

    public Restutility(Activity activity) {
        this.activity = activity;
    }

    public Restutility() {
    }

    public Restutility(Application application) {
    }

    public void logtheResponse(@NonNull JSONObject response) {
        Log.d("API RESPONSE", response.toString());
    }

    public void logtheRequest(String val) {
        Log.d("API REQUEST --> ", val);
    }

    //
//    public void getRandomQuotes(final Context context, final QuotesApiResponceListener listener) {
//        logtheRequest(Shared_prefs_constants.API_FAVQ_RANDOM_QUOTES);
//        JsonObjectRequest jsonObjectRequest = new JsonArrayRequestwithAuthHeader(Request.Method.GET, Shared_prefs_constants.API_FAVQ_RANDOM_QUOTES, null,
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
    /*========*/
    /*GIPHY METHODS*/
    public void getStickersByQuery(@NonNull final Context context, @NonNull final StickerResponseListener listner, final String request) {
        String REQUEST = Constants.API_GET_Stickers_FROM_GIPHY.replace("@QUERY", request.trim().replace(" ", "+")).replace("@LIMIT", "20");
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, REQUEST, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {

                        listner.onSuccess(response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                listner.onError(getRelevantVolleyErrorMessage(context, error));
            }
        });
        MySingleton.addJsonObjRequest(context, jsonObject);
    }

    /*=================*/
    /*UNSPLASH*/
    public void getRandomImages(@NonNull final Context context, @NonNull final ImagesApiResponceListner listner, final String request) {
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, request, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        ImagesResponse imagesResponse = new Gson().fromJson(response.toString(), ImagesResponse.class);
                        if (imagesResponse.hits.size() > 0) {
                            listner.onSuccess(imagesResponse.hits);
                        } else {
                            listner.onError(context.getString(R.string.no_images));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                listner.onError(getRelevantVolleyErrorMessage(context, error));
            }
        });
        MySingleton.addJsonObjRequest(context, jsonObject);
    }

    public void getUnsplashUserImages(@NonNull final Context context, @NonNull final FeaturedImagesRespoonseListener listener, final String request) {
        if (BuildConfig.DEBUG) {
            logtheRequest(request);
        }
        JsonArrayRequest request1 = new JsonArrayRequestwithAuthHeader(Request.Method.GET, request, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(@NonNull JSONArray response) {
                UnsplashImageResponse imagesFromUnsplashResponse = new UnsplashImageResponse();
                imagesFromUnsplashResponse.Value = new Unsplash_Image[response.length()];
                if (BuildConfig.DEBUG) {
                    Log.d("RESPONSE LENGTGH", String.valueOf(response.length()));
                }
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Unsplash_Image unspalshImage = new Gson().fromJson(response.get(i).toString(), Unsplash_Image.class);
                        imagesFromUnsplashResponse.Value[i] = unspalshImage;
                        if (BuildConfig.DEBUG) {
                            Log.d("JSON ARRAY NAME", unspalshImage.getWidth());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onSuccess(imagesFromUnsplashResponse.Value);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                listener.onError(getRelevantVolleyErrorMessage(context, error));

            }
        });
        MySingleton.addJsonArrayObjRequest(context, request1);
    }

    public void getUnsplashFeaturedImages(@NonNull final Context context, @NonNull final FeaturedImagesRespoonseListener listener, final String request) {
        if (BuildConfig.DEBUG) {
            logtheRequest(request);
        }
        JsonArrayRequest request1 = new JsonArrayRequestwithAuthHeader(Request.Method.GET, request, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(@NonNull JSONArray response) {
                UnsplashImageResponse imagesFromUnsplashResponse = new UnsplashImageResponse();
                imagesFromUnsplashResponse.Value = new Unsplash_Image[response.length()];
                if (BuildConfig.DEBUG) {
                    Log.d("RESPONSE LENGTGH", String.valueOf(response.length()));
                }
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Unsplash_Image unspalshImage = new Gson().fromJson(response.get(i).toString(), Unsplash_Image.class);
                        imagesFromUnsplashResponse.Value[i] = unspalshImage;
                        if (BuildConfig.DEBUG) {
                            Log.d("JSON ARRAY NAME", unspalshImage.getWidth());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onSuccess(imagesFromUnsplashResponse.Value);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.e("ERROR LENGTGH", String.valueOf(error));

                listener.onError(getRelevantVolleyErrorMessage(context, error));

            }
        });
        MySingleton.addJsonArrayObjRequest(context, request1);
    }

    public void getUnsplashImagesForSearchQuery(@NonNull final Context context, @NonNull final SearchImagesResponseListener listener,
                                                final String request) {
        if (BuildConfig.DEBUG) {
            logtheRequest(request);
        }
        JsonObjectRequest request1 = new JsonObjectRequestwithAuthHeader(Request.Method.GET, request, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        if (BuildConfig.DEBUG) {
                            Log.d("RESPONSE LENGTGH", String.valueOf(response.length()));
                        }
                        ResGetSearchResultsDto unspalshImage = new Gson().fromJson(response.toString(), ResGetSearchResultsDto.class);
                        listener.onSuccess(unspalshImage);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                listener.onError(getRelevantVolleyErrorMessage(context, error));

            }
        });
        MySingleton.addJsonObjRequest(context, request1);
    }

    public void getUnsplashImageCollections(@NonNull final Context context, @NonNull final GetCollectionsResponseListener listener, final String request) {
        if (BuildConfig.DEBUG) {
            if (BuildConfig.DEBUG) {
                logtheRequest(request);
            }
        }
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequestwithAuthHeader(Request.Method.GET, request, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (BuildConfig.DEBUG) {
                    logtheRequest(response.toString());
                }
                ArrayList<ResGetCollectionsDto> resGetCollectionsDtoArrayList = new ArrayList<>();
                for (int t = 0; t < response.length(); t++) {
                    try {
                        ResGetCollectionsDto resGetCollectionsDto = new Gson().fromJson(response.get(t).toString(), ResGetCollectionsDto.class);
                        resGetCollectionsDtoArrayList.add(resGetCollectionsDto);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                listener.onSuccess(resGetCollectionsDtoArrayList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(getRelevantVolleyErrorMessage(context, error));
            }
        });
        MySingleton.addJsonArrayObjRequest(context, jsonObjectRequest);
    }



    /*UNSPLASH*/
    /*=====================*/
    /*QUOTZY*/

    /**
     * QOUTE METHODS
     */
    public void uploadQuotes(@NonNull final QuotzyApiResponseListener listener, @NonNull final Context context, String request) {
//        CategoriesFragment.Data quotes = new CategoriesFragment.Data();
//        ArrayList<CategoriesFragment.Quotes> quotes1 = new ArrayList<>();
//        quotes1.add(quotess);
//        quotes.setData(quotes1);
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, request, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                listener.onError(getRelevantVolleyErrorMessage(context, error));
            }
        });
        MySingleton.addJsonObjRequest(context, jsonObject);
    }

    public void getQuotes(@NonNull final QuotzyApiResponseListener listener, @NonNull final Context context, final String request) {

        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, request, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        ResponseQuotesService responseQuotesService = new Gson().fromJson(response.toString(), ResponseQuotesService.class);
                        if (responseQuotesService.data.size() > 0) {
                            listener.onDataGet(responseQuotesService);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                listener.onError(getRelevantVolleyErrorMessage(context, error));
            }
        });
        MySingleton.addJsonObjRequest(context, jsonObject);
    }

    /**
     * AUTH METHODS
     */
    public void signUp(@NonNull final ResponseListener listener, @NonNull final Context context, final ReqUserSignup reqUserSignup) {
        JSONObject requestPbject = convertToJson(reqUserSignup);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.QUOTZY_API_EMAIL_SIGNUP_USER,
                requestPbject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
//                BaseResponse baseResponse = getBaseResponseFromResponseJsonObject(response);
                ResLoginUser baseResponse = new Gson().fromJson(response.toString(), ResLoginUser.class);
                if (baseResponse.isApiSuccess()) {
                    listener.onSuccess(baseResponse);
                } else {
                    listener.onError(baseResponse.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                if (null != listener) {
                    listener.onError(getRelevantVolleyErrorMessage(context, error));
                }
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void gSignup(@NonNull final ResponseListener listener, @NonNull final Context context, ReqUserGoogleSignup reqUserSignup) {
        JSONObject jsonObject = convertToJson(reqUserSignup);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.QUOTZY_API_GOOGLE_SIGNUP_USER,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        ResLoginUser baseResponse = new Gson().fromJson(response.toString(), ResLoginUser.class);

                        if (baseResponse.isApiSuccess()) {
                            listener.onSuccess(baseResponse);
                        } else {
                            listener.onError(baseResponse.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                if (null != listener) {
                    listener.onError(getRelevantVolleyErrorMessage(context, error));
                }
            }
        });
        MySingleton.addJsonObjRequest(context, jsonObjectRequest);


    }

    public void gLogin(@Nullable final ResponseListener listener, @NonNull final Context context, ReqUserLogin reqUserSignup) {
        JSONObject jsonObject = convertToJson(reqUserSignup);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                Constants.QUOTZY_API_GOOGLE_LOGIN_USER, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        BaseResponse baseResponse = getBaseResponseFromResponseJsonObject(response);
                        if (null != listener) {
                            listener.onSuccess(baseResponse);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                if (null != listener) {
                    listener.onError(getRelevantVolleyErrorMessage(context, error));
                }
            }
        });
        MySingleton.addJsonObjRequest(context, request);

    }
    /*QUOTZY*/
    /*=======================*/

    /**
     * Util Methods
     */
    private BaseResponse getBaseResponseFromResponseJsonObject(@NonNull JSONObject response) {
        logtheResponse(response);
        return new Gson().fromJson(response.toString(), BaseResponse.class);
    }

    @Nullable
    public String getRelevantVolleyErrorMessage(@NonNull Context context, @NonNull VolleyError volleyError) {
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

    @Nullable
    public JSONObject convertToJson(Object o) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new Gson().toJson(o));
        } catch (JSONException js) {
            js.printStackTrace();
        }
        Log.e("JSON REQUEST", new Gson().toJson(o));
        return jsonObject;
    }
}
