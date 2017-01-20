package com.kng.app.kngapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.kng.app.kngapp.Config;
import com.kng.app.kngapp.Order;
import com.kng.app.kngapp.PaymentType;
import com.kng.app.kngapp.R;
import com.kng.app.kngapp.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order_History_Detail extends Fragment {

    String TAG = Order_History_Detail.class.getSimpleName();
    ListView detail_order_list;
    TextView detail_order_id, detail_ord_date, detail_ord_total_item, detail_ord_price;
    Button cancel_order,download_bill;
    private int orderId;
    private String orderStatus;
    private Order order;
    ProgressDialog pd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderId = getArguments().getInt("orderNumber");
            orderStatus = getArguments().getString("orderStatus");
            order = Fragment_Oredr_History.orderList.get(orderId);
        }
        pd = getProgressBar();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order__history__detail, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment_order__history__detail, container, false);

                intioliseId(view);
                setListners();
                detail_order_id.setText(order.getOrders_id());
                detail_ord_date.setText(order.getDate_purchased());
                detail_ord_total_item.setText(String.valueOf(order.getOrderItems().size()));
                detail_ord_price.setText(Config.formatCurrency(order.getTotal_bill()));
                getList();
                if(orderStatus.equals("Order Canceled")){
                    cancel_order.setEnabled(false);
                }
            }


        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        }
        return view;
    }

    private void getList() {
        CustomEventAdapter adapter = new CustomEventAdapter(getActivity(), order.getOrderItems());
        detail_order_list.setAdapter(adapter);
    }

    private void setListners() {

        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Will do cancel order ",Toast.LENGTH_SHORT).show();
                cancelOrder();
            }
        });

        download_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Will download bill",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelOrder() {
        try {

            PrefManager pref = new PrefManager(Config.getContext());
            Map<String,String> map = new HashMap<>();
            map.put("customer_id",pref.getCustomerId());
            map.put("name",pref.getName());
            map.put("mobile_no",pref.getMobile());
            map.put("lang",pref.getAppLanguage());
            String orderId = Integer.valueOf(order.getOrders_id().split("KNG")[1]).toString();
            Log.d(TAG, "cancelOrder orderID : "+orderId);
            map.put("orders_id",orderId);
            String url = Config.CANCEL_ORDER_URL;
            Log.d(TAG, "loadPaymentTypeData URL : " + url);
            Log.d(TAG, "cancelOrder: inside cancel order : "+url);
            JsonObjectRequest cancelOrderReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(map), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.dismiss();
                    try {
                        Log.d(TAG, "onResponse: "+response.toString());
                        final boolean isSuccess = response.getBoolean("status");

                        if (isSuccess) {
                            Toast.makeText(getActivity(),"Order canceled Successfully",Toast.LENGTH_SHORT).show();
                            Fragment_Oredr_History.isReload = true;
                            getActivity().onBackPressed();
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

            cancelOrderReq.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT, Config.WEB_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getActivity()).add(cancelOrderReq);
            pd.show();
        } catch (Exception e) {
            Log.e(TAG, "cancelOrderReq: ", e);
        }

    }

    private void intioliseId(View view) {
        detail_order_id = (TextView) view.findViewById(R.id.detail_order_id);
        detail_ord_total_item = (TextView) view.findViewById(R.id.detail_ord_total_item);
        detail_ord_date = (TextView) view.findViewById(R.id.detail_ord_date);
        detail_ord_price = (TextView) view.findViewById(R.id.detail_ord_price);
        detail_order_list = (ListView) view.findViewById(R.id.detail_order_list);
        cancel_order = (Button) view.findViewById(R.id.cancel_order);
        download_bill = (Button) view.findViewById(R.id.download_bill);

    }

    public static class CustomEventAdapter extends ArrayAdapter implements ListAdapter {
        private FragmentActivity activity;
        private List<OrderItem> list;

        private TextView detail_ord_item_name, detail_ord_item_quantity, detail_ord_item_price;

        public CustomEventAdapter(FragmentActivity activity, List<OrderItem> list) {
            super(activity, R.layout.order_details_list_item);
            this.list = list;
            this.activity = activity;

        }


    /*@Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }*/

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

   /* @Override
    public boolean hasStableIds() {
        return false;
    }*/

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            try {
                if (view == null) {
                    LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    view = li.inflate(R.layout.order_details_list_item, null);
                }
                initializeIds(view);
                setItems(position);

            } catch (Exception e) {

            }
            return view;
        }


        private void setItems(int position) {

            OrderItem item = list.get(position);
            detail_ord_item_name.setText(item.getProductName());
            detail_ord_item_quantity.setText(item.getQuantity());
            detail_ord_item_price.setText(Config.formatCurrency(item.getFinal_price()));
        }

        private void initializeIds(View view) {

            detail_ord_item_name = (TextView) view.findViewById(R.id.detail_ord_item_name);
            detail_ord_item_quantity = (TextView) view.findViewById(R.id.detail_ord_item_quantity);
            detail_ord_item_price = (TextView) view.findViewById(R.id.detail_ord_item_price);

        }
    }
}