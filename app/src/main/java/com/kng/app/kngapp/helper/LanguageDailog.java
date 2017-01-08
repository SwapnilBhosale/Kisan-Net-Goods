package com.kng.app.kngapp.helper;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.kng.app.kngapp.Activitis.Login_Activity;
import com.kng.app.kngapp.Config;
import com.kng.app.kngapp.Languages;

import java.util.Locale;

/**
 * Created by GS-0913 on 06-12-2016.
 */

public class LanguageDailog extends DialogFragment {

    private Login_Activity activity;
    private String TAG = LanguageDailog.class.getSimpleName();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String [] arr = new String[Config.languageList.size()];
        for(int i = 0 ; i < Config.languageList.size();i++){
            arr[i] = Config.languageList.get(i).getLanguageName().toUpperCase();
        }
        builder.setTitle("Choose Language")
                .setItems(arr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        setLocale(getLanguageCode(which));

                        Intent intent = new Intent(getActivity(),getActivity().getClass());
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
        return builder.create();
    }

    public Languages getLanguageCode(int i){
        if(Config.languageList.size() > 0)
            return Config.languageList.get(i);
        return null;

    }

    public static void setLocale(Languages lang){
        Locale myLocale = null;
        PrefManager pref = new PrefManager(Config.getContext());
        pref.storeAppLanguage(lang.getLanguageCode());
        pref.setAppLangId(""+lang.getLanguageId());
        Log.d("LanguageDialogue", "setLocale in pref : "+lang.getLanguageCode());
        Configuration config = new Configuration();
        myLocale = new Locale(lang.getLanguageCode());
        Locale.setDefault(myLocale);
        config.locale = myLocale;
        Log.d("Change Local", "setLocale: "+myLocale);
        Config.getContext().getResources().updateConfiguration(config,Config.getContext().getResources().getDisplayMetrics());


    }
}

