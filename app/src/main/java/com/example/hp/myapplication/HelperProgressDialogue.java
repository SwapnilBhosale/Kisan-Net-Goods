package com.example.hp.myapplication;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by GS-0913 on 25-12-2016.
 */

public class HelperProgressDialogue {
    String message;
    Context context;
    //ProgressDialog pd;

    public HelperProgressDialogue(String message, Context context) {
        this.message = message;
        this.context = context;
    }

    public static ProgressDialog getInstance(String message, Context context) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage(message);
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return pd;
    }
}
