package com.kng.app.kngapp.Activitis;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.kng.app.kngapp.Config;
import com.kng.app.kngapp.Languages;
import com.kng.app.kngapp.R;
import com.kng.app.kngapp.helper.PrefManager;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    Activity mActivity;
    private PopupWindow mPopupWindow;
    private Button login_btn;
    private TextView tv_no_register;
    LinearLayout pop_up_layout;
    RelativeLayout main_layout;
    LayoutInflater inflater;
    private EditText mobileNoView;
    private String TAG = "LoginActivity";
    public static final int MY_PERMISSIONS_REQUEST_SMS_READ = 123;
    ProgressDialog pd;
    private int pos;
    ImageView cancel_button;
    Button popup_button ;
    public EditText otp_text_box ;
    AlertDialog alertDialog;
    private SMSEventReceiver  smsEventReceiver;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        pd = getProgressBar();

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = Login_Activity.this;

        smsEventReceiver = new SMSEventReceiver();

        initiolizeId();
        setListners();

    }

    private boolean checkIfLoggedIn(){

        PrefManager pref = new PrefManager(getApplicationContext());
        boolean isLogged = pref.isLoggedIn();
        return isLogged;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(getString(R.string.exit_confirmation));
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    R.string.option_yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            System.exit(0);
                        }
                    });
            builder1.setNegativeButton(
                    R.string.option_no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
    }

    private void setListners() {

        tv_no_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = checkPermission();
                if(result) {
                    Intent i = new Intent(getApplicationContext(), Register_Activity.class);
                    startActivity(i);
                }
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show spinner here
                boolean result = checkPermission();
                if(result)
                    attemptLogin();
            }
        });
        TextView language = (TextView) findViewById(R.id.change_lang);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* LanguageDailog dialog = new LanguageDailog();
                dialog.show(getFragmentManager(),"TEST");*/

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login_Activity.this);

                // ...Irrelevant code for customizing the buttons and title
                dialogBuilder.setTitle("Change Language");
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.language_layout, null);
                dialogBuilder.setView(dialogView);

               /* TextView tv_hindi = (TextView) dialogView.findViewById(R.id.tv_hindi);
                TextView tv_marathi = (TextView) dialogView.findViewById(R.id.tv_marathi);
                final TextView hindi_image = (TextView) dialogView.findViewById(R.id.hindi_image);
                final TextView marathi_image = (TextView) dialogView.findViewById(R.id.marathi_image);
                LinearLayout hindi = (LinearLayout) dialogView.findViewById(R.id.hindi);
                LinearLayout english = (LinearLayout) dialogView.findViewById(R.id.english);*/


                final List<Languages> lang_list = Config.languageList;
                final ListView language_list = (ListView) dialogView.findViewById(R.id.language_list);
                CustomEventAdapter event_list = new CustomEventAdapter(Login_Activity.this,lang_list);
                language_list.setAdapter(event_list);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(alertDialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                alertDialog.show();
                language_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //adapterView.get
                        TextView tv_image_selected = (TextView) view.findViewById(R.id.tv_image_selected);
                        pos = i;
                        if(tv_image_selected.getVisibility() == View.INVISIBLE)
                            tv_image_selected.setVisibility(View.VISIBLE);
                        for(int j=0;j<Config.languageList.size();j++){
                            if(i!=j){
                                TextView tv_image_sel = (TextView) adapterView.getChildAt(j).findViewById(R.id.tv_image_selected);
                                tv_image_sel.setVisibility(View.INVISIBLE);
                            }
                        }



                    }

                });

                Button btn = (Button) dialogView.findViewById(R.id.lang_select_btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                        Languages lang = lang_list.get(pos);
                        Log.d(TAG, "lang: "+ lang.toString());
                        setLocale(lang);
                        /*Intent intent = new Intent(Login_Activity.this,Login_Activity.class);
                        startActivity(intent);
                        Login_Activity.this.finish();*/
                        Intent intent = getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public Languages getLanguageCode(int i){
        if(Config.languageList.size() > 0)
            return Config.languageList.get(i);
        return null;

    }

    public void setLocale(Languages lang){
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
        getBaseContext().getResources().updateConfiguration(config,Config.getContext().getResources().getDisplayMetrics());


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(Config.getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(Login_Activity.this, Manifest.permission.READ_SMS)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Login_Activity.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Read SMS permission is necessary!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Login_Activity.this, new String[]{Manifest.permission.READ_SMS}, MY_PERMISSIONS_REQUEST_SMS_READ);
                            attemptLogin();
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(Login_Activity.this, new String[]{Manifest.permission.READ_SMS}, MY_PERMISSIONS_REQUEST_SMS_READ);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SMS_READ:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //writeCalendarEvent();
                } else {
                    //code for deny
                    Toast.makeText(this,"We need permission to read message. Please approve",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private ProgressDialog getProgressBar(){
        ProgressDialog pd = new ProgressDialog(Login_Activity.this);
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

    private void attemptLogin() {

        //reset errors;
        mobileNoView.setError(null);
        final String mobileNo = mobileNoView.getText().toString();
        Log.d(TAG, "attemptLogin mobileNo "+mobileNo);

        boolean cancel = false;
        View focusView = null;
        if( TextUtils.isEmpty(mobileNo) || !isMobileValid(mobileNo)) {
            mobileNoView.setError("check mobile no");
            focusView = mobileNoView;
            focusView.requestFocus();
        }else{
            String login_url_with_param = Config.LOGIN_API_URL + "?mobile="+mobileNo+"&language_id="+new PrefManager(Config.getContext()).getAppLanguage();
            try {
                JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.GET,login_url_with_param,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response.toString());
                        try{
                            if(pd.isShowing())
                                pd.dismiss();
                            final boolean isSuccess = response.getBoolean("status");
                            if(isSuccess){

                                //Config.OTP_SCREEN = "login";
                                PrefManager pref = new PrefManager(getApplicationContext());
                                pref.setIsWaitingForSMS(true);

                                JSONArray jsonArray = response.getJSONArray("data");
                                JSONObject data = jsonArray.getJSONObject(0);
                                pref.setCustomerId(data.getString("customer_id"));
                                pref.setName(data.getString("name"));
                                pref.setVillage(data.getString("village"));
                                pref.setSessionKey(data.getString("session_key"));
                                pref.setMobile(mobileNo);
                                pref.setAddress(data.getString("address"));
                                pref.setState(data.getString("state"));
                                pref.setCity(data.getString("city"));
                                pref.setPinCode(data.getString("postal_code"));
                                Log.d(TAG, "Response success. Will be opening enter OTP screen" + jsonArray.toString());



                                // /open verifyOTP screen


                              /*  inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                                // Inflate the custom layout/view
                                View customView = inflater.inflate(R.layout.otp_pop_up, null);

                                mPopupWindow = new PopupWindow(
                                        customView,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                                if (Build.VERSION.SDK_INT >= 21) {
                                    mPopupWindow.setElevation(5.0f);
                                }

                                // Get a reference for the custom view close button
                                Button closeButton = (Button) customView.findViewById(R.id.otp_btn);

                                // Set a click listener for the popup window close button
                                closeButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Dismiss the popup window

                                        Intent in = new Intent(Login_Activity.this, MainActivity.class);
                                        startActivity(in);
                                        mPopupWindow.dismiss();
                                    }
                                });

                                mPopupWindow.showAtLocation(main_layout, Gravity.CENTER, 0, 0);
*/

                                try {
                                    AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(Login_Activity.this);

                                    // ...Irrelevant code for customizing the buttons and title
                                    // dialogBuilder.setTitle("Order History");
                                    LayoutInflater inflater = getLayoutInflater();
                                    View dialogView = inflater.inflate(R.layout.otp_pop_up, null);
                                    dialogBuilder.setView(dialogView);


                                    cancel_button = (ImageView) dialogView.findViewById(R.id.cancel_button);
                                    popup_button = (Button) dialogView.findViewById(R.id.otp_btn);
                                    otp_text_box = (EditText) dialogView.findViewById(R.id.otp_text_box);
                                    TextView text = (TextView) dialogView.findViewById(R.id.otp_text_with_mobile);
                                    text.setText(getString(R.string.verify_mobile_hint)+""+mobileNo);



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
                                                String otp = otp_text_box.getText().toString();
                                                alertDialog.dismiss();
                                                verifyOtp();
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
                                }catch (Exception e){
                                    Log.e("alert", "onClick: ", e);
                                }


                            }else{
                                //check what is the error
                                JSONObject obj = response.getJSONObject("error");
                                String errorCode = obj.getString("errorCode");
                                if(errorCode.equals("10")){
                                    Log.d(TAG, "User is not registered");
                                    Intent intent = new Intent(getApplicationContext(),Register_Activity.class);
                                    intent.putExtra("MOBILE_NO",mobileNo);
                                    Toast.makeText(Config.getContext(),R.string.error_mobile_is_not_registered,Toast.LENGTH_LONG).show();
                                    startActivity(intent);

                                }else if(errorCode.equals("20")){
                                    Log.d(TAG, "Send OTP error");
                                    Toast.makeText(getApplicationContext(),obj.getString("errorMessage"), Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(),obj.getString("errorMessage"), Toast.LENGTH_LONG).show();
                                }
                            }
                        }catch (Exception e){
                            Log.e(TAG, "onResponse: "+response.toString(),e);
                        }

                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "Error: " , volleyError);

                        // hide the progress dialog
                        pd.dismiss();
                        if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof AuthFailureError
                                || volleyError instanceof ParseError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError )
                            Toast.makeText(Login_Activity.this,R.string.error_no_internet_conenction, Toast.LENGTH_LONG).show();
                        Toast.makeText(Login_Activity.this,R.string.error_general_error,Toast.LENGTH_SHORT).show();
                    }
                });
                loginRequest.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT,Config.WEB_RETRY_COUNT,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(getApplicationContext()).add(loginRequest);
                pd.show();
            }
            catch(Exception e){
                Log.e(TAG, "attemptLogin: "+e.getMessage(),e );
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

                            pref.setIsWaitingForSMS(false);
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
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.e(TAG, "Error: " , volleyError);
                    pd.dismiss();
                    if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof AuthFailureError
                            || volleyError instanceof ParseError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError)
                        Toast.makeText(Login_Activity.this,R.string.error_no_internet_conenction, Toast.LENGTH_LONG).show();
                    Toast.makeText(Login_Activity.this,R.string.error_general_error,Toast.LENGTH_SHORT).show();


                }
            });
            verify_otp_req.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT,Config.WEB_RETRY_COUNT,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(verify_otp_req);
            pd.show();
        }

    }

    private void statMainActivity(){
        Log.d(TAG, "Started main activity");
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }




    private void initiolizeId() {

        login_btn = (Button) findViewById(R.id.login_btn);
        tv_no_register = (TextView) findViewById(R.id.tv_no_register);
        main_layout = (RelativeLayout) findViewById(R.id.activity_main);
        mobileNoView = (EditText) findViewById(R.id.editText);
        //pop_up_layout = (LinearLayout) findViewById(R.id.pop_up_layout);
    }

    private boolean isMobileValid(String mobileNo) {
        //TODO: Replace this with your own logic
        return mobileNo.length() == 10;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View view) {

        Intent i;
        switch (view.getId()) {

            case R.id.login_btn:
                /* i= new Intent(this,MainActivity.class);
                startActivity(i);*/
                inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.otp_pop_up, null);

                mPopupWindow = new PopupWindow(
                        customView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );

                if (Build.VERSION.SDK_INT >= 21) {
                    mPopupWindow.setElevation(5.0f);
                }

                // Get a reference for the custom view close button
                Button closeButton = (Button) customView.findViewById(R.id.otp_btn);

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window

                        Intent in = new Intent(Login_Activity.this, MainActivity.class);
                        startActivity(in);
                        mPopupWindow.dismiss();
                    }
                });

                mPopupWindow.showAtLocation(main_layout, Gravity.CENTER, 0, 0);

                break;

            case R.id.tv_no_register:
                i = new Intent(this, Register_Activity.class);
                startActivity(i);
                break;
            default:

                break;

        }

    }

    public  class CustomEventAdapter extends ArrayAdapter {
        private Activity activity;
        List<Languages> languageList;
        private  TextView tv_lang_name;


        public CustomEventAdapter(Activity activity, List<Languages> languageList ) {
            super(activity, R.layout.language_list_item);
            this.languageList = languageList;
            this.activity = activity;
        }


        public int getCount() {
            return languageList.size();
        }

        public Object getItem(int position) {
            return languageList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            try {
                if (view == null) {
                    LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    view = li.inflate(R.layout.language_list_item, null);
                }
                initializeIds(view);
                setItems(languageList.get(position));

            } catch (Exception e) {

            }
            return view;
        }

        private void setItems(Languages lang) {

            tv_lang_name.setText(lang.getLanguageName());


        }


        private void initializeIds(View view) {

            tv_lang_name = (TextView) view.findViewById(R.id.tv_lang_name);


        }


    }


    public class SMSEventReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            if(intent.hasExtra("otp")){
                String otp = intent.getExtras().getString("otp");
                Log.d(TAG, "onReceive: otp received  : "+otp);
                verifyOTP(otp);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(smsEventReceiver,new IntentFilter(Config.SMS_RECIEVED_INTENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsEventReceiver);
    }

    public void verifyOTP(String otp){
        otp_text_box.setText(otp);

        final PrefManager pref = new PrefManager(Config.getContext());
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
                        if(alertDialog.isShowing())
                            alertDialog.dismiss();
                        pref.setIsLoggedIn(true);
                        pref.setIsWaitingForSMS(false);
                        //Stop spinner and open home activitys
                        statMainActivity();
                    }
                }catch (Exception e){
                    Log.e(TAG, "onResponse: "+response.toString(),e);
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "Error: " + volleyError);
                if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof AuthFailureError || volleyError instanceof ParseError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError)
                    Toast.makeText(Config.getContext(),R.string.error_no_internet_conenction, Toast.LENGTH_LONG).show();
                Toast.makeText(Config.getContext(),R.string.error_general_error,Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(Config.getContext()).add(verify_otp_req);
    }

}
