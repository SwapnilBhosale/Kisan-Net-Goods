package com.example.hp.myapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.myapplication.Activitis.MainActivity;
import com.example.hp.myapplication.Activitis.Splash_Activity;
import com.example.hp.myapplication.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by GS-0913 on 15-12-2016.
 */

public class Config extends Application {

    public static final String BASE_URL = "http://kisannetgoods.in";
    public static final String SMS_ORIGIN = "KISANG";
    public static final String OTP_DELIMITER = ":";
    public static final String LOGIN_API_URL = BASE_URL+"/users/login";
    public static final String GET_LANGUAGES_URL = BASE_URL+"/lang";
    public static final String REGISTER_URL = BASE_URL+"/users/register";
    public static List<Languages> languageList = new ArrayList<Languages>();
    public static final String CATEGORIES_URL = BASE_URL+"/product?";
    public static final String FRUITS_URL = BASE_URL+"/fruits?lang=";

    public static final String PRODUCTS_URL = BASE_URL+"/product/catalog?";
    public static final String PRODUCTS_INFO_URL = BASE_URL+"/product/info?";
    public static final String CART_URL = BASE_URL+"/cart/getCart?";
    public static final String REMOVE_CART_URL = BASE_URL+"/cart/updateCart?quantity=0&";

    public static final String UPDATE_CART_URL = BASE_URL+"/cart/updateCart?";
    public static final String ADD_TO_CART_URL = BASE_URL+"/cart/addToCart?";
    public static final String FRUIT_PROD_MAP_URL = BASE_URL+"/product/getFruitProd?";
    public static final String SEARCH_URL = BASE_URL+"/product/search?";

    public static String LOGO_URL = BASE_URL+"/images/logos/logo.jpg";
    public static String BACKGROUND_URL = BASE_URL+"/images/logos/background.jpg";
    public static String UPDATE_PROFILE_URL = BASE_URL+"/users/updateInfo";
    public static String VERIFY_OTP_URL = Config.BASE_URL+"/users/verifyOTP";
    public static String BANNER_URL = Config.BASE_URL+"/getBanner?lang=";

    public static final String TAG = Config.class.getSimpleName();
    public static String OTP_SCREEN="";

    public String langauge_code;

    public static final  String KEY_FRAGMENT_LIST = "Fragment_List";
    public static final  String KEY_ADD_TO_CART = "Fragment_Add_To-Cart";
    public static final  String KEY_FRAGMENT_FEEDBACK = "Fragment_Feedback";
    public static final  String KEY_FRAGMENT_GRID = "Fragment_Grid";
    public static final  String KEY_FRAGMENT_HOME_LIST_DETAIL_GRID = "Fragment_Home_List_Detail_Grid";
    public static final  String KEY_FRAGMENT_ORDER_HISTORY = "Fragment_Order_History";
    public static final  String KEY_FRAGMENT_PROFILE = "Fragment_Profile";
    public static final  String KEY_OPEN_ITEM = "Open_Item";
   /* public static final  String KEY_OPEN_ITEM = "Open_Item";
    public static final  String KEY_OPEN_ITEM = "Open_Item";
    public static final  String KEY_OPEN_ITEM = "Open_Item";
    public static final  String KEY_OPEN_ITEM = "Open_Item";*/


    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Config mInstance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    public static synchronized Config getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,new LruBitmapImgCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static void getLocaleFromServerAndSetLocale(){
        try {
            JsonObjectRequest getLangRequest = new JsonObjectRequest(Request.Method.GET, GET_LANGUAGES_URL,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse: "+response.toString());
                    try{
                        final boolean isSuccess = response.getBoolean("status");
                        languageList = new ArrayList<Languages>();
                        if(isSuccess){

                            // /open verifyOTP screen
                            PrefManager pref = new PrefManager(Config.getContext());

                            JSONArray jsonArray = response.getJSONArray("data");
                            Languages lang = null;
                            //List<Languages> mList = new ArrayList<Languages>();
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject obj = jsonArray.getJSONObject(i);
                                lang = new Languages(obj.getInt("language_id"),obj.getString("code"),obj.getString("name"));
                                /*if(pref.getAppLangId().isEmpty()){
                                    if(lang.getLanguageName().equalsIgnoreCase("मराठी")){
                                        pref.setAppLangId(lang.getLanguageCode());
                                    }
                                }*/
                                Config.languageList.add(lang);
                            }
                            //Config.languageList = mList;
                            Log.d(TAG, "Added languages in arrayList "+languageList.toString());
                        }else{
                            Log.d(TAG, "Error in getting languages");
                        }
                    }catch (Exception e){
                        Log.e(TAG, "onResponse: "+response.toString(),e);
                    }

                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(Config.getContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                    // hide the progress dialog

                }
            });
            Volley.newRequestQueue(Config.getContext()).add(getLangRequest);
        }
        catch(Exception e){
            Log.e(TAG, "getLanguages () : "+e.getMessage(),e );
        }
    }

    private void setLocale(String locale){
        Log.d(TAG, "setLocale: changing locale : "+locale);
        Locale myLocale = new Locale(locale);
        Configuration config = new Configuration();
        Locale.setDefault(myLocale);
        config.locale = myLocale;
        getApplicationContext().getResources().updateConfiguration(config,getApplicationContext().getResources().getDisplayMetrics());
    }

}
