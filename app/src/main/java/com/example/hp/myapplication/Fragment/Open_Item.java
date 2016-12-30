package com.example.hp.myapplication.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.hp.myapplication.Categoris;
import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.HelperProgressDialogue;
import com.example.hp.myapplication.ProductData;
import com.example.hp.myapplication.ProductInfo;
import com.example.hp.myapplication.R;
import com.example.hp.myapplication.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Open_Item extends Fragment {
    ListView feture_list;

    TextView prod_name, prod_price, prod_manufacture_name,prod_weight;
    NetworkImageView prod_image;
    EditText prod_quantity;
    LinearLayout btn_add_to_kart;
    Button btn_cart_button;

    String product_id;
    List<ProductInfo> prodList = new ArrayList<>();
    ImageLoader imageLoader = Config.getInstance().getImageLoader();
    ProductData f = new ProductData();
    ProgressDialog pd ;
    private String TAG = Open_Item.class.getSimpleName();
    private boolean isUpdateCart = false;
    private String updateQuantity;
    private String basket_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = getProgressBar();
        CartItem item = new CartItem();
        if (getArguments() != null) {
            product_id = getArguments().getString("product_id");
            isUpdateCart = getArguments().getBoolean("isUpdate");
            if(isUpdateCart) {
                updateQuantity = getArguments().getString("updateQuantity");
                basket_id = getArguments().getString("basketId");
            } else if ((item = isProductInCart(product_id))!= null) {
                isUpdateCart = true;
                updateQuantity = item.getQuantity();
                basket_id = item.getQuantity();
            }

            Log.d("oncreate", "onCreate: "+product_id);
        }
    }

    public CartItem isProductInCart(String product_id){
        Iterator<CartItem> iterator = Fragment_Add_To_Cart.cartList.iterator();
        while(iterator.hasNext()){
            CartItem item = null;
            item = iterator.next();
            if(item.getProductId().equalsIgnoreCase(product_id))
                return  item;
        }
        return null;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open__item, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment_open__item, container, false);

                //  loadData();
                initiolizeId(view);
                if(isUpdateCart) {
                    btn_cart_button.setText(R.string.update_cart);
                    prod_quantity.setText(updateQuantity);
                }

                btn_add_to_kart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String quantity =prod_quantity.getText().toString();
                        String url;
                        if(isUpdateCart){
                            url = Config.UPDATE_CART_URL+"customer_id=" + new PrefManager(Config.getContext()).getCustomerId() + "&customer_basket_id=" +basket_id+"&quantity="+quantity;
                        }else{
                            url = Config.ADD_TO_CART_URL+"customer_id="+new PrefManager(Config.getContext()).getCustomerId()+"&price="+f.getProduct_Price()+"&quantity="+quantity+"&product_id="+product_id;
                        }

                        Log.d("addtopcart", "URL : "+url);
                        try{
                            JsonObjectRequest category_request = new JsonObjectRequest(Request.Method.GET,url,null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d(TAG, "onResponse: "+response.toString());
                                            pd.dismiss();
                                            try{
                                                final boolean isSuccess = response.getBoolean("status");
                                                if(isSuccess){
                                                    if(isUpdateCart)
                                                        Toast.makeText(getContext(),R.string.update_cart_success,Toast.LENGTH_LONG).show();
                                                    else {
                                                        Toast.makeText(getContext(), R.string.add_to_cart_success, Toast.LENGTH_LONG).show();
                                                        loadCartData();
                                                    }

                                                }

                                            }catch (Exception e){
                                                Log.e(TAG, "onResponse: ",e );
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    pd.dismiss();
                                    Log.e(TAG, "onErrorResponse: ", error);
                                }
                            });

                            Volley.newRequestQueue(getActivity()).add(category_request);
                            pd.show();
                        }catch (Exception e){
                            e.getMessage();
                        }

                    }
                });

                try {

                    String url = Config.PRODUCTS_INFO_URL+"lang="+new PrefManager(Config.getContext()).getAppLangId()+"&product_id="+product_id;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    pd.dismiss();
                                    try{
                                        boolean isSuccess = response.getBoolean("status");
                                        Log.d("duvvrddd", "isSuccess: "+isSuccess);
                                        if(isSuccess){
                                            JSONObject obj = response.getJSONObject("data");
                                            JSONArray prod_data = obj.getJSONArray("product_data");
                                            Log.d("arr", "onResponse: "+prod_data.length());
                                            for (int i = 0; i<prod_data.length(); i++) {
                                                JSONObject jsonObject = prod_data.getJSONObject(i);

                                                f.setProductId(jsonObject.getString("product_id"));
                                                f.setProductImage(jsonObject.getString("product_image"));
                                                f.setProduct_Price(jsonObject.getLong("product_price"));
                                                f.setProduct_weight(jsonObject.getString("product_weight"));
                                                f.setManufacturer_name(jsonObject.getString("manufacturer_name"));
                                                prod_image.setImageUrl(Config.BASE_URL+""+f.getProductImage(),imageLoader);
                                                prod_price.setText(""+f.getProduct_Price());
                                                prod_weight.setText(f.getProduct_weight());
                                                prod_manufacture_name.setText(f.getManufacturer_name());



                                            }

                                            JSONArray array = obj.getJSONArray("product_details");
                                            for (int i = 0; i<array.length(); i++){

                                                JSONObject jsonObject = array.getJSONObject(i);
                                                ProductInfo f = new ProductInfo();
                                                f.setOptions_key(jsonObject.getString("options_key"));
                                                f.setOptions_value(jsonObject.getString("options_value"));
                                                prodList.add(f);

                                            }

                                            f.setProductInfo(prodList);
                                            CustomEventAdapter event_list = new CustomEventAdapter(getActivity(),prodList);
                                            feture_list.setAdapter(event_list);
                                        }

                                    }catch (Exception e){
                                        e.getMessage();
                                        Log.e("hgj", "onResponse: ",e );
                                    }

                                }
                            },
                            new Response.ErrorListener() {
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


                    Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);
                    pd.show();
                } catch (Exception e) {
                    e.getMessage();
                }

                //list view implementation




            }

        } catch (Exception e) {
            e.getMessage();
        }
        return view;
    }

    public void reloadFragment() {
        Bundle bundle =  new Bundle();
        bundle.putString("product_id",product_id);
        Open_Item fo = new Open_Item();
        fo.setArguments(bundle);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_activity_fl, fo);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void loadCartData() {

        PrefManager pref = new PrefManager(Config.getContext());
        String url = Config.CART_URL+"lang="+pref.getAppLangId()+"&customer_id="+pref.getCustomerId();
        try {
            final JsonObjectRequest category_request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                final boolean isSuccess = response.getBoolean("status");
                                Fragment_Add_To_Cart.cartList = new ArrayList<CartItem>();
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
                                        Fragment_Add_To_Cart.cartList.add(categoris);

                                    }
                                    reloadFragment();
                                }else{
                                    //if(response.getJSONObject("error").getInt("errorCode") == 10){

                                    //Snackbar.make(getView(),"Sorry! no items in the cart",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                                    Log.e(TAG, "OnLoadCartDataError: "+response.getJSONObject("error").getString("errorMessage"));
                                    // }
                                }
                            } catch (Exception e) {
                                Log.e("cart", "onResponse: ", e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    //pd.dismiss();
                    //Log.e(TAG, "LoadCartData: ", error);
                    Log.d(TAG, "Error: " + volleyError.getMessage());

                    // hide the progress dialog
                    //pd.dismiss();
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
            //   pd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }



    private void initiolizeId(View view) {

        prod_name = (TextView) view.findViewById(R.id.prod_name);
        prod_price = (TextView) view.findViewById(R.id.prod_price);
        prod_manufacture_name = (TextView) view.findViewById(R.id.prod_manufacture_name);
        prod_image = (NetworkImageView) view.findViewById(R.id.prod_image);
        prod_weight = (TextView) view.findViewById(R.id.prod_weight);
        feture_list = (ListView) view.findViewById(R.id.feture_list);
        prod_quantity = (EditText) view.findViewById(R.id.prod_quantity);
        btn_add_to_kart = (LinearLayout) view.findViewById(R.id.btn_add_to_kart);
        btn_cart_button = (Button) view.findViewById(R.id.btn_cart_button);
        Log.d("btn_add_to_kart", "btn_add_to_kart: "+btn_add_to_kart.toString());


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

    public class CustomEventAdapter extends ArrayAdapter {
        private FragmentActivity activity;
        List<ProductInfo> adapter_product_list;

        private  TextView key,value;


        public CustomEventAdapter(FragmentActivity activity, List<ProductInfo> adapter_product_list ) {
            super(activity, R.layout.detail_page_list_item);
            this.adapter_product_list = adapter_product_list;
            Log.d("prodlist", "CustomEventAdapter: "+adapter_product_list);
            this.activity = activity;
        }


        public int getCount() {
            return adapter_product_list.size();
        }

        public Object getItem(int position) {
            return adapter_product_list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            try {
                if (view == null) {
                    LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    view = li.inflate(R.layout.detail_page_list_item, null);
                }
                initializeIds(view);
                setItems(adapter_product_list.get(position));

            } catch (Exception e) {

            }
            return view;
        }

        private void setItems(ProductInfo categoris) {


            key.setText(categoris.getOptions_key());
            value.setText(categoris.getOptions_value());


        }


        private void initializeIds(View view) {


            key = (TextView) view.findViewById(R.id.key);
            value = (TextView) view.findViewById(R.id.value);


        }


    }
}
