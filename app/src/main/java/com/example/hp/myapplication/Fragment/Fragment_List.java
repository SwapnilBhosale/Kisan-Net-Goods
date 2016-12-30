package com.example.hp.myapplication.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
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
import com.example.hp.myapplication.Categoris;
import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.HelperProgressDialogue;
import com.example.hp.myapplication.R;
import com.example.hp.myapplication.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Fragment_List extends Fragment {
 ListView home_list;

    private ViewPager pager;
    private TextView[] dots;
    //private LinearLayout dotsLayout;
    private ViewPagerAdapter viewPagerAdapter;
    View view;
    private ArrayList<Integer> items;
    public ImageLoader imageLoader = Config.getInstance().getImageLoader();

    public List<Categoris> list_categories = new ArrayList<Categoris>();
    ProgressDialog pd;
    private String TAG = Fragment_List.class.getSimpleName();
    public boolean initialized = false;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__list, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__list, container, false);


                intialize_Ids(view);

                pager.setOffscreenPageLimit(1);
                items = new ArrayList<>();
                items.add(R.drawable.chicken);
                items.add(R.drawable.chicken1);
                items.add(R.drawable.chicken3);


                viewPagerAdapter = new ViewPagerAdapter(items);
                pager.setAdapter(viewPagerAdapter);
                pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
              //          drawSliderDots(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
                dots = new TextView[5];
               // drawSliderDots(0);




                //lsit view here
                CustomEventAdapter event_list = new CustomEventAdapter(getActivity(),list_categories);
                if(initialized == false)
                    loadData(event_list);
                else
                    home_list.setVisibility(View.VISIBLE);
                home_list.setAdapter(event_list);
               // setListViewHeightBasedOnChildren(home_list);
               // setListener();
                try {


                    home_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            String name = ((TextView) view.findViewById(R.id.home_list_name)).getText().toString();
                            Categoris categoris = (Categoris) adapterView.getItemAtPosition(i);
                            categoris.getCategory_name();

                            if(categoris.getCategory_name().equalsIgnoreCase("fertilizers")) {

                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.main_activity_fl, new Fragment_Grid()).addToBackStack(Config.KEY_FRAGMENT_LIST);
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                ft.commit();
                            }
                            else{
                                Bundle bundle = new Bundle();
                                bundle.putString("category_id", String.valueOf(categoris.getCategory_id()));
                                Fragment_Home_List_Detail_Grid fd=  new Fragment_Home_List_Detail_Grid();
                                fd.setArguments(bundle);
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.main_activity_fl, fd).addToBackStack(Config.KEY_FRAGMENT_LIST);
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                ft.commit();
                            }

                        }
                    });
                }catch (Exception e){
                    e.getMessage();
                }

            }
        } catch (Exception e) {
        }
        return view;
    }
    private void showProgress(){
        pd = new ProgressDialog(getActivity());
        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Set the progress dialog title and message
        pd.setMessage("Loading.........");
        // Set the progress dialog background color
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        // Finally, show the progress dialog
        pd.show();
    }

    private void loadData(final CustomEventAdapter adapter) {

        String url = Config.CATEGORIES_URL+"lang="+new PrefManager(Config.getContext()).getAppLangId();
        try{
            JsonObjectRequest category_request = new JsonObjectRequest(Request.Method.GET,url,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            initialized = true;
                            pd.dismiss();
                            try{
                                final boolean isSuccess = response.getBoolean("status");
                                JSONArray jsonArray = response.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++){

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Categoris categoris = new Categoris();
                                    categoris.setCategory_id(jsonObject.getLong("category_id"));
                                    categoris.setCategory_image_url(jsonObject.getString("category_image"));
                                    categoris.setCategory_name(jsonObject.getString("category_name"));

                                    list_categories.add(categoris);

                                }

                                adapter.notifyDataSetChanged();
                                pd.dismiss();

                            }catch (Exception e){
                                e.getMessage();
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
        }catch (Exception e){
            e.getMessage();
        }



    }


    private void intialize_Ids(View view) {
        pager = (ViewPager) view.findViewById(R.id.pager);
       // dotsLayout = (LinearLayout) view.findViewById(R.id.event_pager_dots);
        //gridView = (GridView) view.findViewById(R.id.home_grid);
        home_list = (ListView) view.findViewById(R.id.home_list);
    }


   /* private void drawSliderDots(int position) {
        position = position % items.size();
      //  dotsLayout.removeAllViews();
        for (int i = 0; i < items.size(); i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            if (i == position) {
                dots[i].setTextColor(getResources().getColor(R.color.colorAccent));
            } else
                dots[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            dotsLayout.addView(dots[i]);
        }
    }*/
   // @Override
   // public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    //}

    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<Integer> items;
        public int pos = 0;
        private Bitmap bitmap;
        private float image_radius = 0.0f;

        public ViewPagerAdapter(ArrayList<Integer> listOfItems) {
            this.items = listOfItems;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.view_pager_item, container, false);
            ImageView starter_imageView = (ImageView) view.findViewById(R.id.slider_image);

            bitmap = BitmapFactory.decodeResource(getResources(), items.get(pos));
            starter_imageView.setImageBitmap(bitmap);
            RoundedBitmapDrawable RBD = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            RBD.setCornerRadius(image_radius);
            RBD.setAntiAlias(true);
            starter_imageView.setImageDrawable(RBD);
            // starter_imageView.setImageResource(items.get(pos));
            container.addView(view);
            if (pos >= items.size() - 1)
                pos = 0;
            else
                ++pos;


//            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            ((ViewPager) container).removeView(view);
        }
    }



    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewPager.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public  class CustomEventAdapter extends ArrayAdapter {
        private FragmentActivity activity;
        List<Categoris> adapter_catagory_list;
        private NetworkImageView home_list_image;
        private  TextView home_list_name;


        public CustomEventAdapter(FragmentActivity activity, List<Categoris> adapter_catagory_list ) {
            super(activity, R.layout.home_list_item);
            this.adapter_catagory_list = adapter_catagory_list;
            this.activity = activity;
        }


        public int getCount() {
            return adapter_catagory_list.size();
        }

        public Object getItem(int position) {
            return adapter_catagory_list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            try {
                if (view == null) {
                    LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        view = li.inflate(R.layout.home_list_item, null);
                    }
                initializeIds(view);
                setItems(list_categories.get(position));

            } catch (Exception e) {

            }
            return view;
        }

        private void setItems(Categoris categoris) {

            home_list_image.setImageUrl(Config.BASE_URL+""+categoris.getCategory_image_url(),imageLoader);
            home_list_name.setText(categoris.getCategory_name());


        }


        private void initializeIds(View view) {

            home_list_image = (NetworkImageView) view.findViewById(R.id.home_list_image);
            home_list_name = (TextView) view.findViewById(R.id.home_list_name);


        }


    }
}
