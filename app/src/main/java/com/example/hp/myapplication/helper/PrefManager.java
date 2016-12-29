package com.example.hp.myapplication.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.Languages;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by GS-0913 on 15-12-2016.
 */

public class PrefManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "kisaan_anand";

    // All Shared Preferences Keys
    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_LNAME = "lname";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_CUSTOMER_ID = "customerId";
    private static final String KEY_SESSION_KEY = "sessionKey";
    private static final String KEY_APP_LANG = "appLang";

    private static final String KEY_APP_LANG_ID = "appLangId";

    private static final String KEY_LANGUAGE_LIST = "languages";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }

    public boolean isWaitingForSms() {
        return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }

    public void setMobile(String mobileNumber) {
        editor.putString(KEY_MOBILE, mobileNumber);
        editor.commit();
    }

    public String getMobile() {
        return pref.getString(KEY_MOBILE, null);
    }


    public void setCustomerId(String customerId) {
        editor.putString(KEY_CUSTOMER_ID, customerId);
        editor.commit();
    }

    public String getCustomerId() {
        return pref.getString(KEY_CUSTOMER_ID, null);
    }


    public void setSessionKey(String sessionKey) {
        editor.putString(KEY_SESSION_KEY, sessionKey);
        editor.commit();
    }

    public String getSessionKey() {
        return pref.getString(KEY_SESSION_KEY, null);
    }

    public String getFname() {
        return pref.getString(KEY_FNAME,null);
    }

    public void setFname(String fName){
        editor.putString(KEY_FNAME,fName);
        editor.commit();
    }

    public String getLname() {
        return pref.getString(KEY_LNAME,null);
    }

    public void setLname(String lName){
        editor.putString(KEY_LNAME,lName);
        editor.commit();
    }



    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    public void setIsLoggedIn(boolean val) {
        editor.putBoolean(KEY_IS_LOGGED_IN,val);
        editor.commit();
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put(KEY_FNAME, pref.getString(KEY_FNAME, null));
        profile.put(KEY_LNAME, pref.getString(KEY_LNAME, null));
        profile.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
        profile.put(KEY_APP_LANG, pref.getString(KEY_APP_LANG, null));
        profile.put(KEY_CUSTOMER_ID, pref.getString(KEY_CUSTOMER_ID, null));
        profile.put(KEY_SESSION_KEY, pref.getString(KEY_SESSION_KEY, null));
        return profile;
    }


    public void restoreAppLanguage(){
        /**
         *Use this method to store the app language with other preferences.
         *This makes it possible to use the language set before, at any time, whenever
         *the app will started.
         */

        String lang = pref.getString(KEY_APP_LANG, "");
        if(!lang.isEmpty() && lang.length() == 2){
            Locale myLocale;
            myLocale = new Locale(lang);
            Locale.setDefault(myLocale);
            Configuration config = new Configuration();
            config.locale = myLocale;
            Config.getContext().getResources().updateConfiguration( config, Config.getContext().getResources().getDisplayMetrics());
        }
    }


    public void storeAppLanguage(String lang) {
        /**
         *Store app language on demand
         */
        Locale myLocale;
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        Config.getContext().getResources().updateConfiguration( config, Config.getContext().getResources().getDisplayMetrics() );
        editor.putString(KEY_APP_LANG, lang);
        editor.commit();
    }

    public void setAppLanguage(String lang){
        /**
         *Use this method together with getAppLanguage() to set and then restore
         *language, whereever you need, for example, the specifically localized
         *resources.
         */
        Locale myLocale;
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        Config.getContext().getResources().updateConfiguration( config, Config.getContext().getResources().getDisplayMetrics() );
    }

    public String getAppLanguage(){
        /**
         *Use this method to obtain the current app language name
         */
        String lang = pref.getString(KEY_APP_LANG, "");
        return lang;
    }

    public String getAppLangId(){
        return pref.getString(KEY_APP_LANG_ID,"");
    }

    public void setAppLangId(String langId){
        editor.putString(KEY_APP_LANG_ID, langId);
        editor.commit();
    }
}
