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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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
import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.Fruits;
import com.example.hp.myapplication.HelperProgressDialogue;
import com.example.hp.myapplication.R;
import com.example.hp.myapplication.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Grid extends Fragment {

    GridView grid;
    List<Fruits> myList = new ArrayList<Fruits>();
    ImageLoader imageLoader = Config.getInstance().getImageLoader();
    ProgressDialog pd;
    private String TAG = Fragment_Grid.class.getSimpleName();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = getProgressBar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__grid, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__grid, container, false);


                intioliseId(view);
                setListners();
                CustomGrid adapter = new CustomGrid(getActivity(), myList);

                loadData(adapter);
                grid.setAdapter(adapter);

                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Bundle bundle = new Bundle();
                        Fruits fruits = myList.get(position);
                        bundle.putBoolean("isFruit",true);
                        bundle.putString("fruit_id", fruits.getFruitdId());
                        Fragment_Home_List_Detail_Grid fd=  new Fragment_Home_List_Detail_Grid();
                        fd.setArguments(bundle);
                        //bundle.putString("quantity",view.findViewById());
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.main_activity_fl, fd);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                    }
                });

                //setGrid();


            }
        } catch (Exception e) {
            e.getMessage();
        }
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

    private void loadData(final CustomGrid adapter) {
        //final List<Fruits> list = new ArrayList<>();
        try {
            Log.d("grid", "loadData: ");
            JsonObjectRequest obj = new JsonObjectRequest(Request.Method.GET, Config.FRUITS_URL+""+new PrefManager(Config.getContext()).getAppLangId(), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            pd.dismiss();
                            try {
                                boolean isSuccess = response.getBoolean("status");
                                Log.d("duvvrddd", "isSuccess: " + isSuccess);
                                if (isSuccess) {
                                    JSONArray arr = response.getJSONArray("data");
                                    Log.d("arr", "onResponse: " + arr.length());
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject jsonObject = arr.getJSONObject(i);
                                        Fruits f = new Fruits();
                                        f.setFruitdId(jsonObject.getString("fruit_id"));
                                        f.setFruitImage(jsonObject.getString("fruit_image"));
                                        f.setFruitName(jsonObject.getString("fruit_name"));
                                        myList.add(f);
                                    }
                                    //myList = list;
                                    Log.d("getFruits", "onResponse: " + myList.toString());
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (Exception e) {

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
            Volley.newRequestQueue(getActivity()).add(obj);
            pd.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void setGrid() {


    }

    private void intioliseId(View view) {
        grid = (GridView) view.findViewById(R.id.grid);

    }

    private void setListners() {

    }


    public class CustomGrid extends ArrayAdapter {
        private FragmentActivity activity;
        List<Fruits> list;


        public CustomGrid(FragmentActivity activity, List<Fruits> list) {
            super(activity, R.layout.grid_item);
            this.activity = activity;
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return myList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return myList.get(position);
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
                convertView = inflater.inflate(R.layout.grid_item, null);
                TextView textView = (TextView) convertView.findViewById(R.id.grid_text);
                NetworkImageView imageView = (NetworkImageView) convertView.findViewById(R.id.grid_image);
                Fruits fruit = myList.get(position);
                imageView.setImageUrl(Config.BASE_URL + "" + fruit.getFruitImage(), imageLoader);
                textView.setText(fruit.getFruitName());
            }

            return convertView;
        }
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
