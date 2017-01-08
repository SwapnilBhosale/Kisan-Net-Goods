package com.example.hp.myapplication.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
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
import com.example.hp.myapplication.Activitis.Login_Activity;
import com.example.hp.myapplication.Activitis.MainActivity;
import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.Fruits;
import com.example.hp.myapplication.HelperProgressDialogue;
import com.example.hp.myapplication.Products;
import com.example.hp.myapplication.R;
import com.example.hp.myapplication.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Fragment_Home_List_Detail_Grid extends Fragment {

    List<Products> myList = new ArrayList<Products>();
    ImageLoader imageLoader = Config.getInstance().getImageLoader();

    public boolean initialized = false;
    GridView home_grid;
    static String category_id;
    private String category_name;
    String search_item;
    ProgressDialog pd;
    CustomGrid adapter;
    private String fruitId;
    private boolean isFruit;
    private String TAG = Fragment_Home_List_Detail_Grid.class.getSimpleName();
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = getProgressBar();
        if (getArguments() != null) {
            category_id = getArguments().getString("category_id");
            category_name = getArguments().getString("category_name");
            isFruit = getArguments().getBoolean("isFruit");
            if (isFruit) {
                fruitId = getArguments().getString("fruit_id");
            }

        }
        setActionBarTitle(category_name);

    }

    public void setActionBarTitle(String title) {
        getActivity().setTitle(title);
    }

    public void onResume() {
        super.onResume();

        // Set title bar
        setActionBarTitle(category_name);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment__home__list__detail__grid, container, false); // see it full way
            initiolizeId(view);

            search_item = getArguments().getString("search_item");
            Log.d(TAG, "Search item: " + search_item);

            adapter = new CustomGrid(getActivity(), myList);
            if (initialized == false)
                loadData(adapter);
            else
                home_grid.setVisibility(View.VISIBLE);
            home_grid.setAdapter(adapter);


            home_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //  Products prod = new Products();
                    Bundle bundle = new Bundle();
                    Products prod = myList.get(position);
                    bundle.putString("product_id", prod.getProductId());
                    bundle.putString("product_name", prod.getProductName());
                    //bundle.putString("quantity",view.findViewById());
                    Open_Item fd = new Open_Item();
                    fd.setArguments(bundle);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.main_activity_fl, fd).addToBackStack(Config.KEY_FRAGMENT_HOME_LIST_DETAIL_GRID);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();

                }
            });

        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        }
        return view;
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

    private void loadData(final CustomGrid adapter) {
        //final List<Fruits> list = new ArrayList<>();
        String url = "";
        if (isFruit)
            url = Config.FRUIT_PROD_MAP_URL + "lang=" + new PrefManager(Config.getContext()).getAppLangId() + "&fruit_id=" + fruitId;
        else if (search_item != null)
            url = Config.SEARCH_URL + "lang=" + new PrefManager(Config.getContext()).getAppLangId() + "&q=" + search_item;
        else
            url = Config.PRODUCTS_URL + "lang=" + new PrefManager(Config.getContext()).getAppLangId() + "&category_id=" + category_id;

        Log.d(TAG, "URL in loadData : " + url);
        try {
            Log.d("grid", "loadData: ");
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
                                        Products f = new Products();
                                        f.setProductId(jsonObject.getString("product_id"));
                                        f.setProductImage(jsonObject.getString("product_image"));
                                        f.setProductName(jsonObject.getString("options_value"));
                                        f.setProductWeight(jsonObject.getString("product_weight"));
                                        f.setProductPrice(BigDecimal.valueOf(jsonObject.getDouble("product_price")));
                                        myList.add(f);
                                    }
                                    //myList = list;
                                    Log.d("getProduct", "onResponse: " + myList.toString());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    if (response.getJSONObject("error").getInt("errorCode") == 10) {
                                        home_grid.setEmptyView(view.findViewById(R.id.emptyView1));
                                        adapter.notifyDataSetChanged();
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
            Volley.newRequestQueue(getActivity()).add(obj);
            pd.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void initiolizeId(View view) {

        home_grid = (GridView) view.findViewById(R.id.home_grid);
    }

    public class CustomGrid extends ArrayAdapter {
        private FragmentActivity activity;
        List<Products> prod_list;


        public CustomGrid(FragmentActivity activity, List<Products> prod_list) {
            super(activity, R.layout.home_list_details_grid_item);

            this.prod_list = prod_list;
            this.activity = activity;

        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return prod_list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return prod_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //View grid;


            if (convertView == null) {

                //grid = new View(activity);
                LayoutInflater inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.home_list_details_grid_item, null);

                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView price = (TextView) convertView.findViewById(R.id.price);
                TextView weight = (TextView) convertView.findViewById(R.id.weight);
                NetworkImageView image = (NetworkImageView) convertView.findViewById(R.id.grid_image);
                Products prod = prod_list.get(position);

                name.setText(prod.getProductName());
                price.setText(Config.formatCurrency(prod.getProductPrice()));
                weight.setText(prod.getProductWeight());
                image.setImageUrl(Config.BASE_URL + "" + prod.getProductImage(), imageLoader);
            }

            return convertView;
        }
    }


}
