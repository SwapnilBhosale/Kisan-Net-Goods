package com.example.hp.myapplication.Activitis;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.HelperProgressDialogue;
import com.example.hp.myapplication.R;
import com.example.hp.myapplication.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Register_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button register_btn;
    private EditText mobileNo;
    private EditText fName;
    private EditText lName;
    private EditText address;
    private EditText city;
    private EditText state;
    private EditText postalCode;
    private Context mContext;
    LinearLayout main_layout, pop_up_layout;

    private static String TAG = Register_Activity.class.getSimpleName();
    private PopupWindow mPopupWindow = null;

    ProgressDialog pd;

    ImageView cancel_button;
    Button popup_button ;
    public static EditText otp_text_box ;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        mContext = getApplicationContext();
        pd = getProgressBar();
        initiolizeId();
        setListners();
        populateMobileNo();
    }

    private ProgressDialog getProgressBar(){
        ProgressDialog pd = new ProgressDialog(Register_Activity.this);
        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Set the progress dialog title and message
        pd.setMessage(Config.getContext().getResources().getString(R.string.loading));
        // Set the progress dialog background color
        //pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        // Finally, show the progress dialog
        return pd;
    }


    private void populateMobileNo() {
        Intent intent = getIntent();
        if (intent.hasExtra("MOBILE_NO")) {
            String mobile = intent.getStringExtra("MOBILE_NO");
            mobileNo.setText(mobile);
        }
    }

    private void setListners() {

        register_btn.setOnClickListener(this);
    }

    private JSONObject getRegisterJsonBody() throws JSONException {
        return new JSONObject("{\"language_id\" : "+new PrefManager(Config.getContext()).getAppLanguage()+",\"firstName\" : \"" + fName.getText().toString() + "\",\"lastName\" : \"" + lName.getText().toString() + "\",\"address\" : \"" + address.getText().toString() + "\",\"mobile\" : \"" + mobileNo.getText().toString() + "\",\"state\" : \"" + state.getText().toString() + "\",\"city\" : \"" + city.getText().toString() + "\",\"postal_code\" : \"" + postalCode.getText().toString() + "\"}");
    }

    private void initiolizeId() {

        register_btn = (Button) findViewById(R.id.register_btn);
        mobileNo = (EditText) findViewById(R.id.mobile_no);
        fName = (EditText) findViewById(R.id.first_name);
        lName = (EditText) findViewById(R.id.last_name);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        postalCode = (EditText) findViewById(R.id.postal_code);
        main_layout = (LinearLayout) findViewById(R.id.activity_register_);
    }

    public boolean validateData(){
        String mobile = mobileNo.getText().toString();
        String fname = fName.getText().toString();
        String lname = lName.getText().toString();
        String add = address.getText().toString();
        String clientCity = city.getText().toString();
        String clientState = state.getText().toString();
        String pinCode = postalCode.getText().toString();

        if(isMobileValid(mobile) && isfNameValid(fname) && islNameValid(lname) && isAddressValid(add) && isCityValid(clientCity)
                && isStateValid(clientState) && isPinCodeValid(pinCode))
            return true;
        return false;
    }

    private boolean isPinCodeValid(String pinCode) {
        if(!TextUtils.isEmpty(pinCode) && pinCode.length() == 6)
            return true;
        else
            postalCode.setError(getString(R.string.error_pin_code));
        return false;
    }

    private boolean isStateValid(String clientState) {
        if(!TextUtils.isEmpty(clientState))
            return true;
        else
            state.setError(getString(R.string.error_state));
        return false;
    }

    private boolean isCityValid(String clientCity) {
        if(!TextUtils.isEmpty(clientCity))
            return true;
        else
            city.setError(getString(R.string.error_city));
        return false;
    }

    private boolean isAddressValid(String add) {
        if(!TextUtils.isEmpty(add))
            return true;
        else
            address.setError(getString(R.string.error_address));
        return false;
    }

    private boolean islNameValid(String lname) {
        if(!TextUtils.isEmpty(lname))
            return true;
        else
            lName.setError(getString(R.string.error_lname));
        return false;
    }

    private boolean isfNameValid(String fname) {
        if(!TextUtils.isEmpty(fname))
            return true;
        else
            fName.setError(getString(R.string.error_fname));
        return false;
    }
    private boolean isMobileValid(String mobile) {
        //TODO: Replace this with your own logic
        if(mobile.length() == 10)
            return true;
        else
            mobileNo.setError(getString(R.string.error_mobile_no));
        return false;
    }

    @Override
    public void onClick(View view) {
        if(validateData()) {
            try {
                final JSONObject jsonBody = getRegisterJsonBody();
                JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, Config.REGISTER_URL, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse Register_Activity: " + response.toString());
                        pd.dismiss();
                        try {
                            final boolean isSuccess = response.getBoolean("status");
                            if (isSuccess) {
                                //open verifyOTP screen

                                Config.OTP_SCREEN = "register";
                                Log.d(TAG, "opening OTP from register activiy");
                                PrefManager pref = new PrefManager(getApplicationContext());

                                //JSONArray jsonArray = response.getJSON("data");
                                JSONObject data = response.getJSONObject("data");
                                pref.setCustomerId(data.getString("customer_id"));
                                pref.setFname(data.getString("firstname"));
                                pref.setLname(data.getString("lastname"));
                                pref.setSessionKey(data.getString("session_key"));
                                pref.setMobile(data.getString("mobile_no"));
                                pref.setAddress(data.getString("address"));
                                pref.setState(data.getString("state"));
                                pref.setCity(data.getString("city"));
                                pref.setPinCode(data.getString("postal_code"));

                                //Log.d(TAG, "Response success. Will be opening enter OTP screen" + jsonArray.toString());


                                Log.d(TAG, "Response success. Will be opening enter OTP screen");
                                AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(Register_Activity.this);

                                // ...Irrelevant code for customizing the buttons and title
                                // dialogBuilder.setTitle("Order History");
                                LayoutInflater inflater = getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.otp_pop_up, null);
                                dialogBuilder.setView(dialogView);

                                cancel_button = (ImageView) dialogView.findViewById(R.id.cancel_button);
                                popup_button = (Button) dialogView.findViewById(R.id.otp_btn);
                                otp_text_box = (EditText) dialogView.findViewById(R.id.otp_text_box);



                                alertDialog = dialogBuilder.create();
                                alertDialog.setCanceledOnTouchOutside(false);
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(alertDialog.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                alertDialog.show();

                                try {
                                    popup_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                                /*Intent in = new Intent(Login_Activity.this, MainActivity.class);
                                                startActivity(in);*/
                                            //String otp = otp_text_box.getText().toString();

                                            verifyOtp();
                                            //alertDialog.dismiss();
                                        }
                                    });

                                } catch (Exception e) {
                                    e.getMessage();
                                    Log.e("submit", "onClick: ",e );
                                }
                                try {
                                    cancel_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            alertDialog.dismiss();
                                        }
                                    });

                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            } else {
                                //error in registartion
                                //do error handling
                                pd.dismiss();
                                if (response.getJSONObject("error").getInt("errorCode") == 5)
                                    Toast.makeText(getApplicationContext(), R.string.err_mobile_already_exist, Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getApplicationContext(), "User registartion error" + response.getJSONObject("error").getString("errorMessage"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: " + response.toString(), e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Error: " + volleyError.getMessage());

                        // hide the progress dialog
                        pd.dismiss();
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();

                    }
                });
                Volley.newRequestQueue(getApplicationContext()).add(loginRequest);
                pd.show();
            } catch (Exception e) {
                Log.e(TAG, "attemptLogin: " + e.getMessage(), e);
            }
        }
    }

    private void verifyOtp() {
        String otp = (otp_text_box.getText().toString()).trim();
        if(TextUtils.isEmpty(otp) || (otp.length() != 6)){
            otp_text_box.setError(getString(R.string.error_empty_otp));
        }else{
            PrefManager pref = new PrefManager(Config.getContext());
            String verify_otp_url_with_param = Config.VERIFY_OTP_URL+"?otp="+otp+"&session_key="+pref.getSessionKey()+"&customer_id="+pref.getCustomerId();
            Log.d(TAG, "verifyOtp() called with: otp = [" + otp + "] url : "+verify_otp_url_with_param);

            JsonObjectRequest verify_otp_req = new JsonObjectRequest(Request.Method.GET,verify_otp_url_with_param,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse: verify OTP "+response.toString());
                    pd.dismiss();
                    try{
                        final boolean isSuccess = response.getBoolean("status");
                        if(isSuccess){
                            //put in shared preference here
                            PrefManager pref = new PrefManager(Config.getContext());

                            pref.setIsLoggedIn(true);
                            //Stop spinner and open home activitys
                            statMainActivity();
                        }else{
                            int errorCode = response.getJSONObject("error").getInt("errorCode");
                            if(errorCode == 15)
                                otp_text_box.setError(getString(R.string.error_wrong_otp));
                        }
                    }catch (Exception e){
                        Log.e(TAG, "onResponse: "+response.toString(),e);
                    }

                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(Register_Activity.this,
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                    pd.dismiss();

                }
            });
            Volley.newRequestQueue(this).add(verify_otp_req);
            pd.show();
        }

    }

    private void statMainActivity(){
        Log.d(TAG, "Started main activity");
        Intent i = new Intent(Config.getContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Config.getContext().startActivity(i);
    }

   /* private void showProgress(){
        pd = new ProgressDialog(Register_Activity.this);
        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Set the progress dialog title and message
        pd.setMessage("Loading.........");
        // Set the progress dialog background color
        //pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        // Finally, show the progress dialog
        pd.show();
    }*/
}
