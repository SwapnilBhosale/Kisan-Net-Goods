package com.kng.app.kngapp.Fragment;

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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kng.app.kngapp.Activitis.MainActivity;
import com.kng.app.kngapp.Bill;
import com.kng.app.kngapp.Config;
import com.kng.app.kngapp.Customer;
import com.kng.app.kngapp.Fruits;
import com.kng.app.kngapp.PaymentType;
import com.kng.app.kngapp.R;
import com.kng.app.kngapp.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class Fragment_Checkout4 extends Fragment {

    TextView adress_billing_text, shipping_Address_text;
    TextView cart_total,cart_discount,cart_shipping_charge,cart_discounted_total,tearms_condn;
    Button c4_checkout,c4_back;
    private int paymentListIndex;
    private String TAG = Fragment_Checkout4.class.getSimpleName();
    ProgressDialog pd;
    Bill bill;
    CheckBox c4_checkbox;


    public JSONObject getOrderJsonBody(String payment_type_id,Bill bill) throws JSONException {
        JSONObject jsonObject;
        HashMap<String,String> map = new HashMap<>();
        PrefManager pref = new PrefManager(Config.getContext());
        map.put("customer_id",pref.getCustomerId());
        map.put("payment_type_id",payment_type_id);
        map.put("bill", String.valueOf(bill.getTotal()));
        map.put("discount", String.valueOf(bill.getDiscount()));
        map.put("total_bill", String.valueOf(bill.getDiscountedBill()));
        if(Fragment_Checkout2.customer  == null) {
            map.put("is_same_delivery_address","Y");
        }else{
            Customer cust = Fragment_Checkout2.customer;
            map.put("delivery_name",cust.getName());
            map.put("delivery_village",cust.getVillage());
            map.put("delivery_mobile_no",cust.getMobileNo());
            map.put("delivery_address",cust.getAddress());
            map.put("delivery_city",cust.getCity());
            map.put("delivery_state",cust.getState());
            map.put("delivery_postal_code",cust.getPincode());
        }

        jsonObject = new JSONObject(map);
        Log.d(TAG, "getOrderJsonBody: "+jsonObject.toString());
        return jsonObject;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = getProgressBar();
        if (getArguments() != null) {
            paymentListIndex = getArguments().getInt("payment_type_id");

        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__checkout4, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__checkout4, container, false);


                intioliseId(view);

                bill = Fragment_Checkout3.getBill();
                cart_discounted_total.setText(Config.formatCurrency(bill.getDiscountedBill()));
                cart_discount.setText(Config.formatCurrency(bill.getDiscount()));
                cart_total.setText(Config.formatCurrency(bill.getTotal()));
                cart_shipping_charge.setText(Config.formatCurrency(bill.getShippingCharge()));

                PrefManager pref = new PrefManager(Config.getContext());

                adress_billing_text.setText(pref.getMobile()+",\n"+pref.getName()+",\n"+pref.getAddress()+", "+pref.getVillage()+", "+pref.getCity()+",\n"+pref.getState()+", "+pref.getPincode());

                if(Fragment_Checkout2.customer != null){
                    Customer cust = Fragment_Checkout2.customer;

                    shipping_Address_text.setText(cust.getMobileNo()+",\n"+cust.getName()+",\n"+cust.getAddress()+", "+cust.getVillage()+", "+cust.getCity()+",\n"+cust.getState()+", "+cust.getPincode());
                 /*   c4_ship_address.setText(cust.getAddress());
                    c4_ship_name.setText(cust.getName());
                    c4_ship_village.setText(cust.getVillage());
                    c4_ship_city.setText(cust.getCity());
                    c4_ship_state.setText(cust.getState());
                    c4_ship_mobile.setText(cust.getMobileNo());
                    c4_ship_pincode.setText(cust.getPincode());*/
                }else{
                        shipping_Address_text.setText(pref.getMobile()+",\n"+pref.getName()+",\n"+pref.getAddress()+", "+pref.getVillage()+", "+pref.getCity()+",\n"+pref.getState()+", "+pref.getPincode());

                    /*c4_ship_address.setText(pref.getAddress());
                    c4_ship_name.setText(pref.getName());
                    c4_ship_village.setText(pref.getVillage());
                    c4_ship_city.setText(pref.getCity());
                    c4_ship_state.setText(pref.getState());
                    c4_ship_mobile.setText(pref.getMobile());
                    c4_ship_pincode.setText(pref.getPincode());*/

                }

                setListners();


            }
        }catch (Exception e) {
            e.getMessage();}
        return view;
    }

    private void setListners() {

        c4_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.checkout_frame, new Fragment_Checkout3());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();*/
                Fragment_Checkout3.setZeroDiscount();
                getActivity().onBackPressed();
            }
        });
        c4_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    c4_checkout.setEnabled(true);
                else
                    c4_checkout.setEnabled(false);
            }
        });
        c4_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PaymentType paymentType = Fragment_Checkout3.paymentTypeList.get(paymentListIndex);
                if(paymentType.getPayment_type_name().equalsIgnoreCase("online pay")){
                    Log.d(TAG, "onClick: open cashless screen");
                    Toast.makeText(getActivity(),"WILL OPEN PAYMENT SCREEN",Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG, "onClick: Confirm order on server");
                    Log.d(TAG, "onClick: payment type : "+Fragment_Checkout3.paymentTypeList.get(paymentListIndex).toString());
                    Log.d(TAG, "onClick: Bill "+bill.toString());

                    try {
                        JSONObject obj = getOrderJsonBody(paymentType.getPaymentTypeId(), bill);
                        doOrder(obj);
                        Log.d(TAG, "onClick: "+obj.toString());
                    }catch(Exception e){
                        Log.e(TAG, "onClick: ", e);
                    }
                }
            }
        });

        tearms_condn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* String url = Config.TERMS_AND_CONDITION_URL;
                WebView webView = (WebView) view.findViewById(R.id.wv);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(url);*/
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.checkout_frame,new Fragment_tearms_and_condn()).addToBackStack(TAG).commit();
            }
        });



    }

    @Override
    public void onPause() {
        super.onPause();
        if (!getActivity().isFinishing() && pd != null) {
            pd.dismiss();
        }
    }

    private ProgressDialog getProgressBar() {
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

    private void intioliseId(View view) {

        adress_billing_text = (TextView) view.findViewById(R.id.adress_billing_text);
        shipping_Address_text = (TextView) view.findViewById(R.id.shipping_Address_text);

        tearms_condn = (TextView) view.findViewById(R.id.tearms_condn);

        cart_discount = (TextView) view.findViewById(R.id.cart_discount);
        cart_discounted_total = (TextView) view.findViewById(R.id.cart_discounted_total);
        cart_shipping_charge = (TextView) view.findViewById(R.id.cart_shipping_charge);
        cart_total = (TextView) view.findViewById(R.id.cart_total);
        c4_checkout = (Button) view.findViewById(R.id.c4_checkout);
        c4_back = (Button) view.findViewById(R.id.c4_back);
        c4_checkbox = (CheckBox) view.findViewById(R.id.c4_checkbox);

    }


    private void doOrder(JSONObject jsonObject) {
        JsonObjectRequest doOrderRequest = new JsonObjectRequest(Request.Method.POST, Config.CHECKOUT_URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                paraseJson(response);
                Fragment_Checkout3.setBill(new Bill());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Error: " + volleyError.getMessage());

                // hide the progress dialog
                pd.dismiss();

                if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof AuthFailureError || volleyError instanceof ParseError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError)
                    Toast.makeText(getActivity(), R.string.error_no_internet_conenction, Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), R.string.error_general_error, Toast.LENGTH_SHORT).show();

            }
        });
        doOrderRequest.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT, Config.WEB_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getContext()).add(doOrderRequest);
        pd.show();
    }

    private void paraseJson(JSONObject response) {
        pd.dismiss();
        Log.d(TAG, "paraseJson: "+response.toString());
        try{
            boolean isSuccess = response.getBoolean("status");
            Log.d(TAG, "isSuccess: " + isSuccess);
            if (isSuccess) {
                Toast.makeText(getContext(),getString(R.string.order_placed_successful),Toast.LENGTH_SHORT).show();
                Fragment_Add_To_Cart.cartList.removeAll(Fragment_Add_To_Cart.cartList);
                getActivity().finish();
                return;
            }
            Toast.makeText(getContext(),getString(R.string.error_general_error),Toast.LENGTH_SHORT).show();
        }catch(Exception e){
                Log.e(TAG, "paraseJson: ",e );
        }finally{
            pd.dismiss();
        }


    }
}
