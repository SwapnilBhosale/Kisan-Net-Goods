package com.kng.app.kngapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kng.app.kngapp.Activitis.MainActivity;
import com.kng.app.kngapp.Config;
import com.kng.app.kngapp.helper.PrefManager;

import org.json.JSONObject;



/**
 * Created by GS-0913 on 15-12-2016.
 */

public class HttpService extends IntentService {

    private static String TAG = HttpService.class.getSimpleName();
    private static String verify_OTP_url = Config.BASE_URL+"/users/verifyOTP";

    public HttpService() {
        super(HttpService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "onHandleIntent: intent is not null");
            String otp = intent.getStringExtra("otp");
            //get customer_id and session_key to verifyOTP function as a url parmas
            verifyOtp(otp);
        }
    }

    private void verifyOtp(final String otp) {
        //adttach customer_id and session_key here
        PrefManager pref = new PrefManager(getApplicationContext());
        String verify_otp_url_with_param = verify_OTP_url+"?otp="+otp+"&session_key="+pref.getSessionKey()+"&customer_id="+pref.getCustomerId();
        Log.d(TAG, "verifyOtp() called with: otp = [" + otp + "] url : "+verify_otp_url_with_param);

        JsonObjectRequest verify_otp_req = new JsonObjectRequest(Request.Method.GET,verify_otp_url_with_param,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: "+response.toString());
                try{
                    final boolean isSuccess = response.getBoolean("status");
                    if(isSuccess){
                        //put in shared preference here
                        PrefManager pref = new PrefManager(getApplicationContext());

                        //Stop spinner and open home activity
                        Log.d(TAG, "Started main activity");
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                }catch (Exception e){
                    Log.e(TAG, "onResponse: "+response.toString(),e);
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(verify_otp_req);

    }
}
