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
import android.support.v4.app.ServiceCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.kng.app.kngapp.Products;
import com.kng.app.kngapp.R;
import com.kng.app.kngapp.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.kng.app.kngapp.R.id.home_grid;


public class Fragment_Oredr_History extends Fragment {


    public static List<Order> orderList = new ArrayList<>();
    public  boolean initialized = false;

    //String[] order_id={"8QW895EW45","QE8654QD54","QEW4579W65"};
    //String[] total_item={"3","8","2"};
    //String[] date={"22-05-2016","17/12/2016","22/12/2016"};
    //String[] price={"1025","2035","5010"};
    private ListView order_list;
    String TAG = Fragment_Oredr_History.class.getSimpleName();
    ProgressDialog pd;
    CustomEventAdapter adapter;


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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = getProgressBar();
    }

    private void loadData() {
        PrefManager pref = new PrefManager(Config.getContext());
        String url = Config.ORDER_URL+"c_id="+pref.getCustomerId()+"&language_id="+pref.getAppLangId();
        Log.d(TAG, "URL in loadData : " + url);
        try {
            JsonObjectRequest obj = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            initialized = true;
                            pd.dismiss();
                            try {
                                boolean isSuccess = response.getBoolean("status");
                                Log.d("duvvrddd", "isSuccess: " + isSuccess);
                                if (isSuccess) {
                                    JSONArray arr = response.getJSONArray("data");
                                    Log.d("arr", "onResponse: " + arr.length());
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject jsonObject = arr.getJSONObject(i);
                                        Order order = new Order();
                                        order.setDiscount(BigDecimal.valueOf(jsonObject.getDouble("discount")));
                                        order.setBill(BigDecimal.valueOf(jsonObject.getDouble("bill")));
                                        order.setTotal_bill(BigDecimal.valueOf(jsonObject.getDouble("total_bill")));
                                        order.setVat_total(BigDecimal.valueOf(jsonObject.getDouble("vat_total")));

                                        order.setPayment_type_name(jsonObject.getString("payment_type_name"));
                                        order.setStatus(jsonObject.getString("status"));

                                        String orderId = jsonObject.getString("orders_id");
                                        orderId = orderId.length() < 4 ? String.format("%6s", orderId).replace(' ', '0') : orderId;
                                        order.setOrders_id("KNG"+orderId);

                                        Calendar cal = Calendar.getInstance();
                                        cal.setTimeInMillis(jsonObject.getLong("date_purchased"));
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                        String strDate = sdf.format(cal.getTime());
                                        Log.d(TAG, " Date : "+strDate);

                                        order.setDate_purchased(strDate);

                                        List<OrderItem> orderItemList = new ArrayList<>();

                                        JSONArray orderItemArr = jsonObject.getJSONArray("productData");
                                        for(int j=0;j<orderItemArr.length();j++){
                                            OrderItem item = new OrderItem();
                                            JSONObject obj = orderItemArr.getJSONObject(j);
                                            item.setFinal_price(BigDecimal.valueOf(obj.getDouble("final_price")));
                                            item.setProductName(obj.getString("options_value"));
                                            item.setQuantity(obj.getString("product_quantity"));
                                            orderItemList.add(item);
                                        }
                                        order.setOrderItems(orderItemList);
                                        orderList.add(order);

                                    }
                                    //myList = list;
                                    Log.d(TAG, "onResponse: " + orderList.toString());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    if (response.getJSONObject("error").getInt("errorCode") == 10) {
                                        /*home_grid.setEmptyView(view.findViewById(R.id.emptyView1));
                                        adapter.notifyDataSetChanged();*/
                                        Toast.makeText(getActivity(), "No Product available in this category", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "onResponse: ",e );
                            }

                        }
                    },
                    new Response.ErrorListener() {
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
            obj.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT, Config.WEB_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            orderList.removeAll(orderList);
            Volley.newRequestQueue(getActivity()).add(obj);
            pd.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__oredr__history, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__oredr__history, container, false);


                intioliseId(view);
                setListners();
                setAdapter();
                Log.d(TAG, "onCreateView: inititalized : "+initialized);
                if(!initialized)
                    loadData();
                else
                    order_list.setVisibility(View.VISIBLE);




            }
        }catch (Exception e) {
            e.getMessage();}
        return view;
    }



    private void setAdapter() {
        adapter =  new CustomEventAdapter(getActivity(), orderList);
        order_list.setAdapter(adapter);
    }

    private void setListners() {

        try {
            order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putInt("orderNumber", i);
                    Order_History_Detail fg=  new Order_History_Detail();
                    fg.setArguments(bundle);
                    ft.replace(R.id.main_activity_fl, fg).addToBackStack(Config.KEY_FRAGMENT_LIST);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }
            });

        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "setListners: ", e);
        }
    }

    private void intioliseId(View view) {
        order_list = (ListView) view.findViewById(R.id.order_list);

    }

    public  class CustomEventAdapter extends ArrayAdapter implements ListAdapter {
        private FragmentActivity activity;
        List<Order> list;
        private  TextView order_id,total_item,date,price;


        public CustomEventAdapter(FragmentActivity activity, List<Order> list) {
            super(activity, R.layout.order_list_item);
            this.list = list;
            this.activity = activity;

        }

/*
        @Override
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

        /*@Override
        public boolean hasStableIds() {
            return false;
        }*/

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            try {
                if (view == null) {
                    LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    view = li.inflate(R.layout.order_list_item, null);
                }
                initializeIds(view);
                setItems(position);

            } catch (Exception e) {
                Log.e(TAG, "getView: ",e );
            }
            return view;
        }


        private void setItems(int position) {

            Order order = list.get(position);

            order_id.setText(order.getOrders_id());
            total_item.setText(String.valueOf(order.getOrderItems().size()));
            date.setText(order.getDate_purchased());
            price.setText(Config.formatCurrency(order.getTotal_bill()));
        }

        private void initializeIds(View view) {

            order_id = (TextView) view.findViewById(R.id.order_id);
            total_item = (TextView) view.findViewById(R.id.total_item);
            date = (TextView) view.findViewById(R.id.date);
            price = (TextView) view.findViewById(R.id.price);

        }
    }
}
