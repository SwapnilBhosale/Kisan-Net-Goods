package com.example.hp.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.myapplication.Activitis.Login_Activity;
import com.example.hp.myapplication.Activitis.MainActivity;
import com.example.hp.myapplication.Activitis.Register_Activity;
import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.helper.PrefManager;
import com.example.hp.myapplication.service.HttpService;

import org.json.JSONObject;


/**
 * Created by GS-0913 on 15-12-2016.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = SmsReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.d(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);

                    // if the SMS is not from our gateway, ignore the message
                    if (!senderAddress.toLowerCase().contains(Config.SMS_ORIGIN.toLowerCase())) {
                        Log.d(TAG, "onReceive: ");
                        return;
                    }

                    // verification code from sms
                    String verificationCode = getVerificationCode(message);

                    Log.e(TAG, "OTP received: " + verificationCode);
                    verifyOtp(verificationCode,context);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void verifyOtp(String otp, final Context context){
        if(Config.OTP_SCREEN.equalsIgnoreCase("register"))
            Register_Activity.otp_text_box.setText(otp);
        else if(Config.OTP_SCREEN.equalsIgnoreCase("login"))
            Login_Activity.otp_text_box.setText(otp);
        PrefManager pref = new PrefManager(context);
        String verify_otp_url_with_param = Config.VERIFY_OTP_URL+"?otp="+otp+"&session_key="+pref.getSessionKey()+"&customer_id="+pref.getCustomerId();
        Log.d(TAG, "verifyOtp() called with: otp = [" + otp + "] url : "+verify_otp_url_with_param);

        JsonObjectRequest verify_otp_req = new JsonObjectRequest(Request.Method.GET,verify_otp_url_with_param,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: verify OTP "+response.toString());
                try{
                    final boolean isSuccess = response.getBoolean("status");
                    if(isSuccess){
                        //put in shared preference here
                        //PrefManager pref = new PrefManager(context);

                        new PrefManager(Config.getContext()).setIsLoggedIn(true);
                        //Stop spinner and open home activitys
                        statMainActivity();
                    }
                }catch (Exception e){
                    Log.e(TAG, "onResponse: "+response.toString(),e);
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }
        });
        Volley.newRequestQueue(context).add(verify_otp_req);

    }

    private void statMainActivity(){
        Log.d(TAG, "Started main activity");
        Intent i = new Intent(Config.getContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Config.getContext().startActivity(i);
    }
    private String getVerificationCode(String message) {
        String code = null;
        int index = message.indexOf(Config.OTP_DELIMITER);

        if (index != -1) {
            int start = index + 3;
            int length = 6;
            code = message.substring(start, start + length);
            return code;
        }

        return code;
    }
}
