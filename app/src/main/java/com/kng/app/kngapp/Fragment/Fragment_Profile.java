package com.kng.app.kngapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kng.app.kngapp.Config;
import com.kng.app.kngapp.R;
import com.kng.app.kngapp.helper.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Fragment_Profile extends Fragment {

    private TextInputEditText mobile,name,village,address,city,state,pincode;
    private Button updateButton;
    private String TAG = Fragment_Profile.class.getSimpleName();
    ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__profile, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__profile, container, false);
                intioliseId(view);
                pd = getProgressBar();
                populateData();
                setListners();


            }
        }catch (Exception e) {
            e.getMessage();}
        return view;
    }

    private void populateData() {

        PrefManager pref = new PrefManager(Config.getContext());
        mobile.setText(pref.getMobile());
        address.setText(pref.getAddress());
        name.setText(pref.getName());
        village.setText(pref.getVillage());
        pincode.setText(pref.getPincode());
        state.setText(pref.getState());
        city.setText(pref.getCity());
    }

    private JSONObject getRegisterJsonBody() throws JSONException {
        return new JSONObject("{\"customer_id\" : \""+new PrefManager(Config.getContext()).getCustomerId()+"\",\"name\" : \"" + name.getText().toString() + "\",\"village\" : \"" + village.getText().toString() + "\",\"address\" : \"" + address.getText().toString() + "\",\"mobile\" : \"" + mobile.getText().toString() + "\",\"state\" : \"" + state.getText().toString() + "\",\"city\" : \"" + city.getText().toString() + "\",\"postal_code\" : \"" + pincode.getText().toString() + "\"}");
    }

    private ProgressDialog getProgressBar(){
        ProgressDialog pd = new ProgressDialog(getActivity());
        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Set the progress dialog title and message
        pd.setMessage(Config.getContext().getResources().getString(R.string.loading));
        // Set the progress dialog backg    round color
        //pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        // Finally, show the progress dialog
        return pd;
    }

    private void setListners() {
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData()){
                    try{
                        final JSONObject jsonBody = getRegisterJsonBody();
                        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.UPDATE_PROFILE_URL, jsonBody, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    pd.dismiss();
                                    final boolean isSuccess = response.getBoolean("status");
                                    if (isSuccess) {
                                        JSONObject data = response.getJSONObject("data");
                                        PrefManager pref = new PrefManager(Config.getContext());
                                        pref.setName(data.getString("name"));
                                        pref.setVillage(data.getString("village"));
                                        pref.setAddress(data.getString("address"));
                                        pref.setMobile(data.getString("mobile_no"));
                                        pref.setState(data.getString("state"));
                                        pref.setCity(data.getString("city"));
                                        pref.setPinCode(data.getString("postal_code"));
                                        Toast.makeText(getActivity(),"Data updated successfully",Toast.LENGTH_LONG).show();
                                    }
                                }catch (Exception e){
                                    pd.dismiss();
                                    Log.e(TAG, "onResponse: ", e);
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.d(TAG, "Error: " + volleyError.getMessage());

                                // hide the progress dialog
                                pd.dismiss();
                                if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof AuthFailureError || volleyError instanceof ParseError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError )
                                    Toast.makeText(getActivity(),R.string.error_no_internet_conenction, Toast.LENGTH_LONG).show();
                                Toast.makeText(getActivity(),R.string.error_general_error,Toast.LENGTH_SHORT).show();

                            }
                        });
                        req.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT,Config.WEB_RETRY_COUNT,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        Volley.newRequestQueue(getContext()).add(req);
                        pd.show();
                    }catch(Exception e){
                        Log.e(TAG, "onClick: ",e);
                    }

                }
            }
        });
    }

    public boolean validateData(){
        String usr_mobile = mobile.getText().toString();
        String usr_name = name.getText().toString();
        String usr_village = village.getText().toString();
        String usr_add = address.getText().toString();
        String usr_city = city.getText().toString();
        String usr_state = state.getText().toString();
        String usr_pincode = pincode.getText().toString();

        if(isMobileValid(usr_mobile) && isNameValid(usr_name) && islNameValid(usr_village) && isAddressValid(usr_add) && isCityValid(usr_city)
                && isStateValid(usr_state) && isPinCodeValid(usr_pincode))
            return true;
        return false;
    }

    private boolean isPinCodeValid(String pinCode) {
        if(!TextUtils.isEmpty(pinCode) && pinCode.trim().length() == 6)
            return true;
        else
            pincode.setError(getString(R.string.error_pin_code));
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

    private boolean islNameValid(String lName) {
        if(!TextUtils.isEmpty(lName))
            return true;
        else
            village.setError(getString(R.string.error_village));
        return false;
    }

    private boolean isNameValid(String usr_name) {
        if(!TextUtils.isEmpty(usr_name))
            return true;
        else
            name.setError(getString(R.string.error_name));
        return false;
    }
    private boolean isMobileValid(String mob) {
        //TODO: Replace this with your own logic
        if(mob.length() == 10)
            return true;
        else
            mobile.setError(getString(R.string.error_mobile_no));
        return false;
    }
    private void intioliseId(View view) {
        mobile = (TextInputEditText) view.findViewById(R.id.reg_mobile_no);
        city = (TextInputEditText) view.findViewById(R.id.reg_city);
        state = (TextInputEditText) view.findViewById(R.id.reg_state);
        address = (TextInputEditText) view.findViewById(R.id.reg_address);
        name = (TextInputEditText) view.findViewById(R.id.reg_name);
        village = (TextInputEditText) view.findViewById(R.id.reg_village);
        pincode = (TextInputEditText) view.findViewById(R.id.reg_postal_code);
        updateButton = (Button) view.findViewById(R.id.reg_register_btn);
    }
}
