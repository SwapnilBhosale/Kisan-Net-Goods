package com.kng.app.kngapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
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
import com.kng.app.kngapp.Bill;
import com.kng.app.kngapp.Config;
import com.kng.app.kngapp.PaymentType;
import com.kng.app.kngapp.R;
import com.kng.app.kngapp.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Fragment_Checkout3 extends Fragment {

    Button c3_next, c3_back, coupon_submit_btn;
    EditText coupoun_code_text;
    private String TAG = Fragment_Checkout3.class.getSimpleName();
    private static Bill bill= null;
    private View view;
    private String user_coupon_code;
    public String couponDiscPercentage = "";
    private boolean isPaymentSelected = false;
    ProgressDialog pd;
    public static List<PaymentType> paymentTypeList = new ArrayList<PaymentType>();
    private ListView payment_list;
    private int paymentTypeSelectd;
    private RadioButton listRadioButton = null;
    private static boolean isCouponAdded = false;
    int listIndex = -1;
    public PaymentListAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = getProgressBar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__checkout3, container, false); // see it full way
        Log.d(TAG, "onCreateView: " + view.toString());
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__checkout3, container, false);


                intioliseId(view);
                setListners();
                getLst();

            }
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        }
        return view;
    }

    private void getLst() {

            loadPaymentTypeData();

    }

    /*public static void setListViewHeightBasedOnChildren(ListView home_list) {
        ListAdapter listAdapter = home_list.getAdapter();
        if (listAdapter == null)
            return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(home_list.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int desiredHeight = View.MeasureSpec.makeMeasureSpec(home_list.getHeight(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, home_list);
            //if (i == 0)

                //View.MeasureSpec.makeMeasureSpec(home_list.getWidth(), desiredHeight);
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewPager.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, desiredHeight);
            totalHeight += view.getMeasuredHeight();

        }
        ViewGroup.LayoutParams params = home_list.getLayoutParams();
        params.height = totalHeight + (home_list.getDividerHeight() * (listAdapter.getCount() - 1));
        home_list.setLayoutParams(params);
    }*/


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int listWidth = listView.getMeasuredWidth();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(View.MeasureSpec.makeMeasureSpec(listWidth, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()-1));
        listView.setLayoutParams(params);
        listView.requestLayout();
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

    @Override
    public void onPause() {
        super.onPause();
        if (!getActivity().isFinishing() && pd != null) {
            pd.dismiss();
        }
    }

    public static void setBill(Bill newBill){
        bill = newBill;
    }

    public static void setZeroDiscount(){
        bill.setTotal(bill.getTotal());
        bill.setDiscountedBill(bill.getTotal());
        bill.setDiscount(BigDecimal.ZERO);
    }

    public void loadPaymentTypeData() {
        try {
            String url = Config.PAYMENT_TYPE_URL + "" + new PrefManager(Config.getContext()).getAppLangId();
            Log.d(TAG, "loadPaymentTypeData URL : " + url);
            JsonObjectRequest getPaymentTypeRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.dismiss();
                    try {
                        final boolean isSuccess = response.getBoolean("status");

                        if (isSuccess) {

                            JSONArray obj = response.getJSONArray("data");
                            if (obj.length() > 0) {
                                PaymentType paymentType;
                                JSONObject jsonObject;

                                for (int i = 0; i < obj.length(); i++) {
                                    paymentType = new PaymentType();
                                    jsonObject = obj.getJSONObject(i);
                                    paymentType.setPaymentTypeId(jsonObject.getString("payment_type_id"));
                                    paymentType.setPayment_type_name(jsonObject.getString("payment_type_name"));
                                    paymentType.setPayment_details(jsonObject.getString("payment_details"));
                                    paymentTypeList.add(paymentType);
                                }
                                Log.d(TAG, "onResponse: " + paymentTypeList.toString());
                                adapter = new PaymentListAdapter(getActivity(), paymentTypeList);
                                payment_list.setAdapter(adapter);
                                setListViewHeightBasedOnChildren(payment_list);

                            } else {
                                Toast.makeText(getActivity(), "No Payment Types available", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Get Payment Type error", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "loadPaymentTypeData onResponse: ", e);
                    }
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

            getPaymentTypeRequest.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT, Config.WEB_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            paymentTypeList.removeAll(paymentTypeList);
            Volley.newRequestQueue(getActivity()).add(getPaymentTypeRequest);
            pd.show();
        } catch (Exception e) {
            Log.e(TAG, "loadPaymentTypeData: ", e);
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

                if(isPaymentSelected) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putInt("payment_type_id", paymentTypeSelectd);
                    Fragment_Checkout4 fc4 = new Fragment_Checkout4();
                    fc4.setArguments(bundle);
                    ft.add(R.id.checkout_frame, fc4).addToBackStack(TAG);
                    ft.hide(Fragment_Checkout3.this);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }else{
                    Snackbar.make(view,"Please Select Payment Ztype : ",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //validate coupon on coupon code
        coupon_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_coupon_code = coupoun_code_text.getText().toString().toUpperCase();

                //validate coupon code if empty requrest focus

                if (user_coupon_code.trim().length() == 0) {
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
        try {
            final BigDecimal total = Fragment_Add_To_Cart.total;
            String url = Config.APPLY_COUPON_URL + "" + couponCode + "&bill=" + total;

            Log.d(TAG, "validateCoupon url: " + url);
            JsonObjectRequest applyCouponReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.dismiss();
                    try {
                        isCouponAdded = true;
                        final boolean isSuccess = response.getBoolean("status");
                        Log.d(TAG, "onResponse isSuccess: " + response.toString());
                        if (isSuccess) {
                            bill = new Bill(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);
                            JSONObject jsonObject = response.getJSONObject("data");
                            bill.setDiscount(BigDecimal.valueOf(jsonObject.getDouble("totalDiscount")));
                            bill.setDiscountedBill(BigDecimal.valueOf(jsonObject.getDouble("newTotalBill")));
                            bill.setDiscountPer(jsonObject.getString("percentDiscount")+"%");
                            bill.setTotal(Fragment_Add_To_Cart.total);
                            Log.d(TAG, "Original Bill: " + Fragment_Add_To_Cart.total);
                            Log.d(TAG, "Discount : " + bill.getDiscount());
                            Log.d(TAG, "DiscountPercentage " + bill.getDiscountPer());
                            Log.d(TAG, "DiscountedBill: " + bill.getDiscountedBill());
                            Toast.makeText(getActivity(), "Coupon Code applied Successfully .", Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.getJSONObject("error").getInt("errorCode") == 10) {
                                Toast.makeText(getActivity(), "Please check Coupon Code .", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse validateCoupon: ", e);
                    }
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
            applyCouponReq.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT, Config.WEB_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getActivity()).add(applyCouponReq);
            pd.show();
        } catch (Exception e) {
            Log.e(TAG, "validateCoupon: ", e);
        }
    }

    private void intioliseId(View view) {
        c3_next = (Button) view.findViewById(R.id.c3_next);
        c3_back = (Button) view.findViewById(R.id.c3_back);
        coupoun_code_text = (EditText) view.findViewById(R.id.coupoun_code_text);
        coupon_submit_btn = (Button) view.findViewById(R.id.coupon_submit_btn);
        payment_list = (ListView) view.findViewById(R.id.payment_list);
        //listRadioButton = (RadioButton) view.findViewById(R.id.listRadioButton);

    }


    private class PaymentListAdapter extends ArrayAdapter {
        List<PaymentType> adapterpaymentTypeList = new ArrayList<PaymentType>();
        FragmentActivity activity;
        private TextView pay_method_name, pay_method_detail;
        private RadioButton listRadioButton;
        // int listIndex = -1;


        public PaymentListAdapter(FragmentActivity activity, List<PaymentType> adapterpaymentTypeList) {
            super(activity, R.layout.payment_list_item);
            this.adapterpaymentTypeList = adapterpaymentTypeList;
            this.activity = activity;

        }

        public int getCount() {
            return adapterpaymentTypeList.size();
        }

        public Object getItem(int position) {
            return adapterpaymentTypeList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            try {
                if (convertView == null) {
                    LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    convertView = li.inflate(R.layout.payment_list_item, null);
                }
                initializeIds(convertView);

                setItems(paymentTypeList.get(position));
                listRadioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onClick: radio button clicked");
                        paymentTypeSelectd = position;
                        Log.d(TAG, "onClick: "+paymentTypeList.get(position).toString());
                        View vMain = ((View) view.getParent());
                        isPaymentSelected = true;
                        if (listRadioButton != null) listRadioButton.setChecked(false);
                        // assign to the variable the new one
                        listRadioButton = (RadioButton) view;
                        // find if the new one is checked or not, and set "listIndex"
                        if (listRadioButton.isChecked()) {
                            listIndex = ((ViewGroup) vMain.getParent()).indexOfChild(vMain);
                        } else {
                            listRadioButton = null;
                            listIndex = -1;
                        }

                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "getView: ", e);
            }
            return convertView;
        }

        private void setItems(PaymentType paymenttype) {

            pay_method_name.setText(paymenttype.getPayment_type_name());
            if (!paymenttype.getPayment_details().equalsIgnoreCase("null"))
                pay_method_detail.setText(paymenttype.getPayment_details());
        }


        private void initializeIds(View convertView) {
            listRadioButton = (RadioButton) convertView.findViewById(R.id.listRadioButton);
            pay_method_name = (TextView) convertView.findViewById(R.id.pay_method_name);
            pay_method_detail = (TextView) convertView.findViewById(R.id.pay_method_detail);


        }

    }

    public static Bill getBill(){
        if(bill == null){
            bill = new Bill(Fragment_Add_To_Cart.total,Fragment_Add_To_Cart.total,BigDecimal.ZERO,BigDecimal.ZERO);
            return bill;
        }
        return bill;
    }
}
