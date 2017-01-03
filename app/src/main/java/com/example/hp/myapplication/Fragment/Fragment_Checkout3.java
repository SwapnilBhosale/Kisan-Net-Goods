package com.example.hp.myapplication.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.PaymentType;
import com.example.hp.myapplication.R;
import com.example.hp.myapplication.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Fragment_Checkout3 extends Fragment {

    Button c3_next,c3_back,coupon_submit_btn;
    EditText coupoun_code_text;
    private String TAG = Fragment_Checkout3.class.getSimpleName();
    private String user_coupon_code;
    public BigDecimal discount = BigDecimal.ZERO,discountedBill = BigDecimal.ZERO;
    public String couponDiscPercentage = "";
    ProgressDialog pd;
    private List<PaymentType> paymentTypeList = new ArrayList<PaymentType>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = getProgressBar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__checkout3, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__checkout3, container, false);


                intioliseId(view);
                setListners();
                loadPaymentTypeData();

            }
        }catch (Exception e) {
            e.getMessage();}
        return view;
    }

    private ProgressDialog getProgressBar(){
        ProgressDialog pd = new ProgressDialog(getActivity());
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

    public void loadPaymentTypeData(){
        try{
            String url = Config.PAYMENT_TYPE_URL+""+new PrefManager(Config.getContext()).getAppLangId();
            Log.d(TAG, "loadPaymentTypeData URL : "+url);
            JsonObjectRequest getPaymentTypeRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.dismiss();
                    try {
                        final boolean isSuccess = response.getBoolean("status");

                        if (isSuccess) {
                            JSONArray obj = response.getJSONArray("data");
                            if(obj.length() > 0){
                                PaymentType paymentType;
                                JSONObject jsonObject;
                                for(int i=0;i<obj.length();i++){
                                    paymentType = new PaymentType();
                                    jsonObject = obj.getJSONObject(i);
                                    paymentType.setPaymentTypeId(jsonObject.getString("payment_type_id"));
                                    paymentType.setPayment_type_name(jsonObject.getString("payment_type_name"));
                                    paymentType.setPayment_details(jsonObject.getString("payment_details"));
                                    paymentTypeList.add(paymentType);
                                }
                            }else{
                                Toast.makeText(getActivity(),"No Payment Types available",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity(),"Get Payment Type error",Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        Log.e(TAG, "loadPaymentTypeData onResponse: ", e);
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
                    Toast.makeText(Config.getContext(),message, Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(getActivity()).add(getPaymentTypeRequest);
            pd.show();
        }catch(Exception e){
            Log.e(TAG, "loadPaymentTypeData: ",e );
        }

    }

    private void setListners() {

        c3_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.checkout_frame, new Fragment_Checkout2());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();*/
                getActivity().onBackPressed();
            }
        });

        c3_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.checkout_frame, new Fragment_Checkout4()).addToBackStack(TAG);
                ft.hide(Fragment_Checkout3.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });

        //validate coupon on coupon code
        coupon_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_coupon_code = coupoun_code_text.getText().toString().toUpperCase();

                //validate coupon code if empty requrest focus

                if(user_coupon_code.trim().length() == 0){
                    coupoun_code_text.setError("Please enter coupon code");
                    View focusView = coupoun_code_text;
                    focusView.requestFocus();
                    return;
                }
                validateCoupon(user_coupon_code);
            }
        });

    }

    private void validateCoupon(String couponCode) {
        try{
            final BigDecimal total = Fragment_Add_To_Cart.total;
            String url = Config.APPLY_COUPON_URL+""+couponCode+"&bill="+total;

            Log.d(TAG, "validateCoupon url: "+url);
            JsonObjectRequest applyCouponReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.dismiss();
                    try {
                        final boolean isSuccess = response.getBoolean("status");
                        Log.d(TAG, "onResponse isSuccess: "+response.toString());
                        if (isSuccess) {
                            JSONObject jsonObject = response.getJSONObject("data");
                            setDiscount(BigDecimal.valueOf(jsonObject.getDouble("totalDiscount")));
                            setDiscountedBill(BigDecimal.valueOf(jsonObject.getDouble("newTotalBill")));
                            couponDiscPercentage = jsonObject.getString("percentDiscount");
                            Log.d(TAG, "Original Bill: "+total);
                            Log.d(TAG, "Discount : "+discount);
                            Log.d(TAG, "DiscountPercentage "+couponDiscPercentage);
                            Log.d(TAG, "DiscountedBill: "+discountedBill);
                            Toast.makeText(getActivity(),"Coupon Code applied Successfully .",Toast.LENGTH_SHORT).show();
                        }else{
                            if(response.getJSONObject("error").getInt("errorCode") == 10){
                                Toast.makeText(getActivity(),"Please check Coupon Code .",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }catch (Exception e){
                        Log.e(TAG, "onResponse validateCoupon: ", e);
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
                    Toast.makeText(Config.getContext(),message, Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(getActivity()).add(applyCouponReq);
            pd.show();
        }catch(Exception e){
            Log.e(TAG, "validateCoupon: ",e );
        }
    }

    private void intioliseId(View view) {
        c3_next = (Button) view.findViewById(R.id.c3_next);
        c3_back = (Button) view.findViewById(R.id.c3_back);
        coupoun_code_text = (EditText) view.findViewById(R.id.coupoun_code_text);
        coupon_submit_btn = (Button) view.findViewById(R.id.coupon_submit_btn);

    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal getDiscountedBill() {
        return discountedBill;
    }

    public void setDiscountedBill(BigDecimal discountedBill) {
        this.discountedBill = discountedBill.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
}
