package com.example.hp.myapplication.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.hp.myapplication.Activitis.Checkout_Activity;
import com.example.hp.myapplication.Activitis.MainActivity;
import com.example.hp.myapplication.CartItem;
import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.HelperProgressDialogue;
import com.example.hp.myapplication.R;
import com.example.hp.myapplication.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Fragment_Add_To_Cart extends android.support.v4.app.Fragment {


    //String[] name = {"Puma", "John Deere", "Park Avenue"};
    //String[] dishes = {"Shirt", "T shirt", "Shirt"};
    //int[] images = {R.drawable.shirt_order, R.drawable.shirt_order, R.drawable.shirt_order};
    private ListView list;
    public static List<CartItem> cartList = new ArrayList<CartItem>();
    TextView total_price;
    public static BigDecimal total;
    ProgressDialog pd;
    private String TAG = Fragment_Add_To_Cart.class.getSimpleName();
    Button checkout_btn;
    public Boolean initialized = false;



    //TextView close_tab;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__add__to__cart, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__add__to__cart, container, false);


                initiolizeId(view);
                cartList = new ArrayList<CartItem>();
                getList();
                /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        CartItem item = (CartItem) adapterView.getItemAtPosition(i);
                        Bundle bundle = new Bundle();
                        //bundle.putString("basketId", item.getBasketId());
                        //bundle.putBoolean("isUpdate",true);
                        //bundle.putString("updateQuantity",item.getQuantity());
                        bundle.putString("product_id",item.getProductId());
                        Open_Item fo = new Open_Item();
                        fo.setArguments(bundle);
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.main_activity_fl,fo).addToBackStack(Config.KEY_ADD_TO_CART);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();

                    }
                });*/

                checkout_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(getActivity(), Checkout_Activity.class);
                        startActivity(i);
                    }
                });


            }
        } catch (Exception e) {
        }
        return view;
    }

    private void getList() {
        final CustomEventAdapter adapter = new CustomEventAdapter(getActivity(), cartList);
        loadData(adapter);
        list.setAdapter(adapter);

        //setListViewHeightBasedOnChildren(List);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = getProgressBar();
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


    private void loadData(final CustomEventAdapter adapter) {
        PrefManager pref = new PrefManager(Config.getContext());
        String url = Config.CART_URL+"lang="+pref.getAppLangId()+"&customer_id="+pref.getCustomerId();
        Log.d(TAG, "loadCartData URL: "+url);
        try {
            final JsonObjectRequest category_request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            initialized = true;
                            pd.dismiss();
                            try {
                                final boolean isSuccess = response.getBoolean("status");
                                if (isSuccess) {
                                    checkout_btn.setEnabled(true);
                                    JSONObject obj = response.getJSONObject("data");
                                    JSONArray arr = obj.getJSONArray("productData");
                                    for (int i = 0; i < arr.length(); i++) {

                                        JSONObject jsonObject = arr.getJSONObject(i);
                                        CartItem categoris = new CartItem();
                                        categoris.setBasketId(jsonObject.getString("customers_basket_id"));
                                        categoris.setQuantity(jsonObject.getString("quantity"));
                                        categoris.setName(jsonObject.getString("options_value"));
                                        categoris.setImage(jsonObject.getString("product_image"));
                                        categoris.setPrice(BigDecimal.valueOf(jsonObject.getDouble("product_price")));
                                        categoris.setWeight(jsonObject.getString("product_weight"));
                                        categoris.setTotal(BigDecimal.valueOf(jsonObject.getDouble("final_price")));
                                        categoris.setProductId(jsonObject.getString("product_id"));
                                        cartList.add(categoris);

                                    }

                                    total = BigDecimal.valueOf(obj.getDouble("total"));
                                    Log.d(TAG, "onResponse: total price in Cart : "+total);
                                    total_price.setText(""+total);

                                    adapter.notifyDataSetChanged();
                                }else{
                                    if(response.getJSONObject("error").getInt("errorCode") == 10){

                                        Snackbar.make(getView(),R.string.error_no_item_in_cart,Snackbar.LENGTH_LONG).setAction("Action",null).show();
                                        checkout_btn.setEnabled(false);

                                    }
                                }
                                MainActivity.tv.setText(String.valueOf(getCartSize()));
                            } catch (Exception e) {
                                Log.e("cart", "onResponse: ", e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d(TAG, "Error: " + volleyError.getMessage());

                    // hide the progress dialog
                    pd.dismiss();
                    if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof AuthFailureError
                            || volleyError instanceof ParseError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError )
                        Toast.makeText(getActivity(),R.string.error_no_internet_conenction, Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(),R.string.error_general_error,Toast.LENGTH_SHORT).show();

                }
            });
            category_request.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT,Config.WEB_RETRY_COUNT,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getActivity()).add(category_request);
            pd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void initiolizeId(View view) {
        list = (ListView) view.findViewById(R.id.cart_list);
        checkout_btn = (Button) view.findViewById(R.id.checkout_btn);
        total_price = (TextView) view.findViewById(R.id.total_price);
        //close_tab = (TextView) list.findViewById(R.id.close_tab);


    }

    private class CustomEventAdapter extends ArrayAdapter {
        FragmentActivity activity;
        List<CartItem> list;
        ImageLoader imageLoader = Config.getInstance().getImageLoader();
        //CloseButtonListner customListner;

        private CustomEventAdapter thisInstance;
        private NetworkImageView cart_image;
        private TextView product_name, product_weight, product_quntity, product_price, final_price, close_tab;

        public CustomEventAdapter(FragmentActivity activity, List<CartItem> list) {
            super(activity, R.layout.cart_list_item);
            this.list = list;
            this.activity = activity;
            thisInstance = this;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            try {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = li.inflate(R.layout.cart_list_item, null);


                initializeIds(view);
                setItems(list.get(position));

                TextView increment_button = (TextView) view.findViewById(R.id.increment_button);
                TextView decrement_btn = (TextView) view.findViewById(R.id.decrement_btn);

                increment_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Integer q = Integer.parseInt(cartList.get(position).getQuantity());
                            Log.d(TAG, "onClick: Quantity increment: "+q);
                            q = q + 1;
                            //product_quntity.setText(q.toString());


                            modifyCart(position,q);
                        } catch (Exception e) {
                            Log.e(TAG, "onClick: ",e );
                        }

                    }
                });

                decrement_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Integer q = Integer.parseInt(cartList.get(position).getQuantity());
                            Log.d(TAG, "onClick: Quantity decrement: "+q);
                            if (q != 0) {
                                q = q - 1;
                                modifyCart(position,q);

                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onClick: ",e );
                        }
                    }
                });

                close_tab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closeButtonClicked(position);
                    }
                });
                //setListener(thisInstance);
            } catch (Exception e) {
                Log.e(TAG, "getView: ",e );
            }
            return view;
        }



        private void modifyCart(int pos,int quantity){
            String url = Config.UPDATE_CART_URL+"customer_id=" + new PrefManager(Config.getContext()).getCustomerId() + "&customer_basket_id=" +cartList.get(pos).getBasketId()+"&quantity="+quantity;
            Log.d(TAG, "modifyCart: "+url);
            try{
                JsonObjectRequest updateCartReq = new JsonObjectRequest(Request.Method.GET,url,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "onResponse: "+response.toString());
                                pd.dismiss();
                                try{
                                    final boolean isSuccess = response.getBoolean("status");
                                    Log.d(TAG, "isSuccess: "+isSuccess);
                                    if(isSuccess){
                                        //Toast.makeText(getContext(),R.string.update_cart_success,Toast.LENGTH_LONG).show();
                                        Toast.makeText(getContext(), R.string.update_cart_success, Toast.LENGTH_SHORT).show();
                                        reloadFragment();
                                    }

                                }catch (Exception e){
                                    Log.e(TAG, "onResponse: ",e );
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Log.e(TAG, "onErrorResponse: ", volleyError);
                        if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof AuthFailureError
                                || volleyError instanceof ParseError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError)
                            Toast.makeText(getActivity(),R.string.error_no_internet_conenction, Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),R.string.error_general_error,Toast.LENGTH_SHORT).show();

                    }
                });

                updateCartReq.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT,Config.WEB_RETRY_COUNT,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(getActivity()).add(updateCartReq);
                pd.show();
            }catch (Exception e){
                Log.e(TAG, "modifyCart: ",e );
            }


        }
        private void setItems(CartItem cart) {
            Log.d(TAG, "setItems CartItems: "+cart.toString());

            cart_image.setImageUrl(Config.BASE_URL + "" + cart.getImage(), imageLoader);
            product_name.setText(cart.getName());
            product_price.setText("" + cart.getPrice());
            product_quntity.setText(cart.getQuantity());
            product_weight.setText(cart.getWeight());
            final_price.setText("" + cart.getTotal());
        }

        private void initializeIds(View view) {

            cart_image = (NetworkImageView) view.findViewById(R.id.cart_image);
            product_name = (TextView) view.findViewById(R.id.product_name);
            product_weight = (TextView) view.findViewById(R.id.product_weight);
            product_quntity = (TextView) view.findViewById(R.id.product_quntity);
            product_price = (TextView) view.findViewById(R.id.product_price);
            final_price = (TextView) view.findViewById(R.id.final_price);
            close_tab = (TextView) view.findViewById(R.id.close_tab);

        }

        public void closeButtonClicked(final int pos) {

            //final int pos = (Integer) view.getTag();
            String url = Config.REMOVE_CART_URL + "customer_id=" + new PrefManager(Config.getContext()).getCustomerId()+ "&customer_basket_id=" + cartList.get(pos).getBasketId();
            Log.d("update", "onClick: " + cartList.get(pos).getBasketId());
            try {
                final JsonObjectRequest category_request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    final boolean isSuccess = response.getBoolean("status");
                                    if (isSuccess) {

                                        //cartList.remove(pos);
                                        //thisInstance.notifyDataSetChanged();
                                        reloadFragment();
                                        //initialized = false;
                                        Toast.makeText(getActivity(),"REMOVED SUCCESSFULLY",Toast.LENGTH_LONG).show();

                                    }
                                } catch (Exception e) {
                                    Log.e("cart", "onResponse: ", e);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "onErrorResponse: ",volleyError);
                        if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof AuthFailureError || volleyError instanceof ParseError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError)
                            Toast.makeText(getActivity(),R.string.error_no_internet_conenction, Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),R.string.error_general_error,Toast.LENGTH_SHORT).show();

                    }
                });

                category_request.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT,Config.WEB_RETRY_COUNT,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(getActivity()).add(category_request);
            } catch (Exception e) {
                e.getMessage();
            }


        }

    }



    public void reloadFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        ft.detach(this).attach(this);
        ft.commit();
    }


    public static int getCartSize(){
        return cartList.size();
    }


}