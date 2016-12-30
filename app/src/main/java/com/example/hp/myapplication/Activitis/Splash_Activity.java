package com.example.hp.myapplication.Activitis;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.Languages;
import com.example.hp.myapplication.R;
import com.example.hp.myapplication.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Splash_Activity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private String TAG = Splash_Activity.class.getSimpleName();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_);
        final Activity act = this;

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent intent = null;
                Log.d(TAG, "run: dsad");
                Locale current = Config.getContext().getResources().getConfiguration().locale;
                Log.d(TAG, "current locale in device: "+current.getLanguage());
                PrefManager pref = new PrefManager(getApplicationContext());
                Log.d(TAG, "Pref current Language :  "+pref.getAppLanguage());
                Config.getLocaleFromServerAndSetLocale();

                if(pref.getAppLanguage().isEmpty()){
                    //pref.storeAppLanguage("mr");
                    pref.storeAppLanguage("mr");

                    /*Intent i = new Intent(Splash_Activity.this,Splash_Activity.class);
                    startActivity(i);
                    Splash_Activity.this.finish();*/
                    Intent i = getIntent();
                    pref.setAppLangId("1");
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(i);

                } else if(!pref.getAppLanguage().isEmpty() && !pref.getAppLanguage().equals(current.getLanguage())){
                    Log.d(TAG, "restoreAppLanguage: "+pref.getAppLanguage());
                    pref.restoreAppLanguage();
                    /*Intent i = new Intent(Splash_Activity.this,Splash_Activity.class);
                    startActivity(i);
                    Splash_Activity.this.finish();*/
                    Intent i = getIntent();
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(i);
                }else{
                    if(!current.getLanguage().equals(pref.getAppLanguage())){
                        pref.storeAppLanguage(pref.getAppLanguage());
                        Log.d(TAG, "Restarting splash activity ");
                       /* Intent i = new Intent(Splash_Activity.this,Splash_Activity.class);
                        startActivity(i);
                        Splash_Activity.this.finish();*/
                        Intent i = getIntent();
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        startActivity(i);
                    }
                }
                finish();
                if(checkIfLoggedIn()){
                    intent = new Intent(Splash_Activity.this,MainActivity.class);
                }else{
                    intent = new Intent(Splash_Activity.this,Login_Activity.class);
                }
                startActivity(intent);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private boolean checkIfLoggedIn(){

        PrefManager pref = new PrefManager(getApplicationContext());
        boolean isLogged = pref.isLoggedIn();
        Log.d(TAG, "checkIfLoggedIn: "+isLogged);
        if(isLogged)
            return true;
        return false;
    }



}
