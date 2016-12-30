package com.example.hp.myapplication.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.hp.myapplication.CartItem;
import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.HelperProgressDialogue;
import com.example.hp.myapplication.R;
import com.example.hp.myapplication.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Add_To_Cart extends android.support.v4.app.Fragment {


    //String[] name = {"Puma", "John Deere", "Park Avenue"};
    //String[] dishes = {"Shirt", "T shirt", "Shirt"};
    //int[] images = {R.drawable.shirt_order, R.drawable.shirt_order, R.drawable.shirt_order};
    private ListView list;
    public static List<CartItem> cartList;
    TextView total_price;
    ProgressDialog pd;
    private String TAG = Fragment_Add_To_Cart.class.getSimpleName();

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
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        CartItem item = (CartItem) adapterView.getItemAtPosition(i);
                        Bundle bundle = new Bundle();
                        bundle.putString("basketId", item.getBasketId());
                        bundle.putBoolean("isUpdate",true);
                        bundle.putString("updateQuantity",item.getQuantity());
                        bundle.putString("product_id",item.getProductId());
                        Open_Item fo = new Open_Item();
                        fo.setArguments(bundle);
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.main_activity_fl,fo);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();

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
        try {
            final JsonObjectRequest category_request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            pd.dismiss();
                            try {
                                final boolean isSuccess = response.getBoolean("status");
                                if (isSuccess) {
                                    JSONObject obj = response.getJSONObject("data");
                                    JSONArray arr = obj.getJSONArray("productData");
                                    for (int i = 0; i < arr.length(); i++) {

                                        JSONObject jsonObject = arr.getJSONObject(i);
                                        CartItem categoris = new CartItem();
                                        categoris.setBasketId(jsonObject.getString("customers_basket_id"));
                                        categoris.setQuantity(jsonObject.getString("quantity"));
                                        categoris.setName(jsonObject.getString("options_value"));
                                        categoris.setImage(jsonObject.getString("product_image"));
                                        categoris.setPrice(jsonObject.getLong("product_price"));
                                        categoris.setWeight(jsonObject.getString("product_weight"));
                                        categoris.setTotal(jsonObject.getLong("final_price"));
                                        categoris.setProductId(jsonObject.getString("product_id"));
                                        cartList.add(categoris);

                                    }

                                    total_price.setText("" + obj.getLong("total"));

                                    adapter.notifyDataSetChanged();
                                }else{
                                    if(response.getJSONObject("error").getInt("errorCode") == 10){

                                        Snackbar.make(getView(),R.string.error_no_item_in_cart,Snackbar.LENGTH_LONG).setAction("Action",null).show();
                                    }
                                }
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

            Volley.newRequestQueue(getActivity()).add(category_request);
            pd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void initiolizeId(View view) {
        list = (ListView) view.findViewById(R.id.cart_list);
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
                close_tab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closeButtonClicked(position);
                    }
                });
                //setListener(thisInstance);
            } catch (Exception e) {

            }
            return view;
        }

        private void setItems(CartItem cart) {

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
                                    }
                                } catch (Exception e) {
                                    Log.e("cart", "onResponse: ", e);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                Volley.newRequestQueue(getActivity()).add(category_request);
            } catch (Exception e) {
                e.getMessage();
            }


        }

    }

    public void reloadFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_activity_fl, new Fragment_Add_To_Cart());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.main_activity_fl, new Fragment_List());
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                        return true;
                    }
                    return true;
                }
            });
        } catch (Exception e) {

        }
    }


}