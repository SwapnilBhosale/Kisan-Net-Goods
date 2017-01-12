package com.kng.app.kngapp.Activitis;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kng.app.kngapp.CartItem;
import com.kng.app.kngapp.CircleTransform;
import com.kng.app.kngapp.Config;
import com.kng.app.kngapp.Fragment.Fragment_Add_To_Cart;
import com.kng.app.kngapp.Fragment.Fragment_Contact_us;
import com.kng.app.kngapp.Fragment.Fragment_Feedback;
import com.kng.app.kngapp.Fragment.Fragment_Home_List_Detail_Grid;
import com.kng.app.kngapp.Fragment.Fragment_List;

import com.kng.app.kngapp.Fragment.Fragment_Oredr_History;
import com.kng.app.kngapp.Fragment.Fragment_Profile;
import com.kng.app.kngapp.Languages;
import com.kng.app.kngapp.R;
import com.kng.app.kngapp.helper.PrefManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    PopupWindow mpopup;
    static TextView hindi_image;



    static TextView marathi_image;
    ImageLoader imageLoader = Config.getInstance().getImageLoader();
    private String TAG = MainActivity.class.getSimpleName();
    private int pos;
    NavigationView navigationView;
    public static TextView tv;
    TextView cart_count;
    public RelativeLayout rl;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initiolizeId();

        transaction();
        //    popUp();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View hView = navigationView.getHeaderView(0);
        ImageView nav_image = (ImageView) hView.findViewById(R.id.header_image);
        ImageView background = (ImageView) hView.findViewById(R.id.img_header_bg);


        //TextView name = (TextView) hView.findViewById(R.id.name);
        //TextView website = (TextView) hView.findViewById(R.id.website);

        Glide.with(this).load(Config.BACKGROUND_URL)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(background);
        Glide.with(this).load(Config.BACKGROUND_URL)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(background);
        Glide.with(this).load(Config.LOGO_URL)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(nav_image);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        loadCartData();
    }

    private void initiolizeId() {

    }

    public void doThis(MenuItem item) {

    }


    /*private void popUp() {

        final Handler abc = new Handler();
        abc.postDelayed(new Runnable() {
            @Override
            public void run() {


                View popUpView = getLayoutInflater().inflate(R.layout.activity_new_,
                        null); // inflating popup layout
                mpopup = new PopupWindow(popUpView, LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT, true); // Creation of popup
                mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
                ImageButton cancel_btn = (ImageButton) popUpView.findViewById(R.id.cancel_btn);
                try {


                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mpopup.dismiss();
                        }
                    });
                }catch (Exception e){}
            }
        }, 5000);


    }*/


    private void transaction() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_activity_fl, new Fragment_List());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        // } else {

        Log.d(TAG, "onBackPressed: " + getFragmentManager().getBackStackEntryCount());
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            //super.onBackPressed();
            //super.onBackPressed();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(getString(R.string.exit_confirmation));
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    R.string.option_yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(Integer.valueOf(android.os.Build.VERSION.SDK) >= 21)
                                finishAndRemoveTask();
                            //add else
                        }
                    });
            builder1.setNegativeButton(
                    R.string.option_no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            getSupportFragmentManager().popBackStack();
        }
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        final SearchView searchView =
                (SearchView) item.getActionView();
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo info =
                searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String Item = searchView.getQuery().toString();

                Bundle bundle = new Bundle();
                bundle.putString("search_item", Item);
                Fragment_Home_List_Detail_Grid fd = new Fragment_Home_List_Detail_Grid();
                fd.setArguments(bundle);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_activity_fl, fd).addToBackStack(Config.KEY_FRAGMENT_LIST);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (TextUtils.isEmpty(newText.trim()))
                    searchView.setIconified(true);
                return true;
            }


        });

        try {
            RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.action_cart).getActionView();
            rl = (RelativeLayout) badgeLayout.findViewById(R.id.option_item_layout);

            tv = (TextView) badgeLayout.findViewById(R.id.cart_count);

            List<CartItem> list = Fragment_Add_To_Cart.cartList;
            //Log.d(TAG, "onCreateOptionsMenu: " + list);

            String j = String.valueOf(list.size());
            Log.d(TAG, "onCreateOptionsMenu: TV " + tv.toString());
            if (list.size() == 0) {
                tv.setText("0");
            } else
                tv.setText(j);


        } catch (Exception e) {
            Log.e(TAG, "onCreateOptionsMenu: ", e);
        }

        //Log.d(TAG, "onCreateOptionsMenu cart : "+cart.toString());
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onMenuItemClick: ");
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_activity_fl, new Fragment_Add_To_Cart()).addToBackStack(Config.KEY_FRAGMENT_LIST);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });
        super.onCreateOptionsMenu(menu);
        return true;
    }


    private void loadCartData() {

        PrefManager pref = new PrefManager(Config.getContext());
        String url = Config.CART_URL + "lang=" + pref.getAppLangId() + "&customer_id=" + pref.getCustomerId();
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
                                        categoris.setPrice(BigDecimal.valueOf(jsonObject.getDouble("product_price")));
                                        categoris.setWeight(jsonObject.getString("product_weight"));
                                        categoris.setTotal(BigDecimal.valueOf(jsonObject.getDouble("final_price")));
                                        categoris.setProductId(jsonObject.getString("product_id"));
                                        Fragment_Add_To_Cart.cartList.add(categoris);

                                    }
                                    tv.setText(String.valueOf(Fragment_Add_To_Cart.getCartSize()));

                                } else {
                                    //if(response.getJSONObject("error").getInt("errorCode") == 10){

                                    //Snackbar.make(getView(),"Sorry! no items in the cart",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                                    Log.e(TAG, "OnLoadCartDataError: " + response.getJSONObject("error").getString("errorMessage"));
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
                    if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof AuthFailureError || volleyError instanceof ParseError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError)
                        Toast.makeText(MainActivity.this, R.string.error_no_internet_conenction, Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, R.string.error_general_error, Toast.LENGTH_SHORT).show();
                }
            });

            category_request.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT, Config.WEB_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(category_request);
            //   pd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        Log.d(TAG, "onOptionsItemSelected: ");
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {

            Log.d(TAG, "onOptionsItemSelected: " + id);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_activity_fl, new Fragment_Add_To_Cart()).addToBackStack(Config.KEY_FRAGMENT_LIST);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();

        }


        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_activity_fl, new Fragment_List()).addToBackStack(Config.KEY_FRAGMENT_LIST);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            //onBackPressed();
        } else if (id == R.id.go_to_cart) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_activity_fl, new Fragment_Add_To_Cart()).addToBackStack(Config.KEY_FRAGMENT_LIST);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            //onBackPressed();
        } else if (id == R.id.feedback) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_activity_fl, new Fragment_Feedback()).addToBackStack(Config.KEY_FRAGMENT_LIST);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            //onBackPressed();
        } else if (id == R.id.profile) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_activity_fl, new Fragment_Profile()).addToBackStack(Config.KEY_FRAGMENT_LIST);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            // onBackPressed();
        } else if (id == R.id.change_language) {


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

            // ...Irrelevant code for customizing the buttons and title
            dialogBuilder.setTitle("Change Language");
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.language_layout, null);
            dialogBuilder.setView(dialogView);

               /* TextView tv_hindi = (TextView) dialogView.findViewById(R.id.tv_hindi);
                TextView tv_marathi = (TextView) dialogView.findViewById(R.id.tv_marathi);
                final TextView hindi_image = (TextView) dialogView.findViewById(R.id.hindi_image);
                final TextView marathi_image = (TextView) dialogView.findViewById(R.id.marathi_image);
                LinearLayout hindi = (LinearLayout) dialogView.findViewById(R.id.hindi);
                LinearLayout english = (LinearLayout) dialogView.findViewById(R.id.english);*/


            List<Languages> lang_list = Config.languageList;
            final ListView language_list = (ListView) dialogView.findViewById(R.id.language_list);
            CustomEventAdapter event_list = new CustomEventAdapter(MainActivity.this, lang_list);
            language_list.setAdapter(event_list);

            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alertDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            alertDialog.show();
            language_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //adapterView.get
                    TextView tv_image_selected = (TextView) view.findViewById(R.id.tv_image_selected);
                    pos = i;
                    if (tv_image_selected.getVisibility() == View.INVISIBLE)
                        tv_image_selected.setVisibility(View.VISIBLE);
                    for (int j = 0; j < Config.languageList.size(); j++) {
                        if (i != j) {
                            TextView tv_image_sel = (TextView) adapterView.getChildAt(j).findViewById(R.id.tv_image_selected);
                            tv_image_sel.setVisibility(View.INVISIBLE);
                        }
                    }


                }

            });

            Button btn = (Button) dialogView.findViewById(R.id.lang_select_btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialog.dismiss();
                    Languages lang = Config.languageList.get(pos);
                    Log.d(TAG, "lang: " + lang.toString());
                    setLocale(lang);
                            /*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                            MainActivity.this.finish();*/
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
            });
        } else if (id == R.id.logout) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(getString(R.string.logout_confirmation));
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    R.string.option_yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            openLoginScreenAndSetPref();
                        }
                    });
            builder1.setNegativeButton(
                    R.string.option_no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else if (id == R.id.order_history) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_activity_fl, new Fragment_Oredr_History()).addToBackStack(Config.KEY_FRAGMENT_LIST);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            //onBackPressed();
        } else if(id == R.id.contact_us) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_activity_fl, new Fragment_Contact_us()).addToBackStack(Config.KEY_FRAGMENT_LIST);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




public void setLocale(Languages lang){
        Locale myLocale=null;
        PrefManager pref=new PrefManager(Config.getContext());
        pref.storeAppLanguage(lang.getLanguageCode());
        pref.setAppLangId(""+lang.getLanguageId());
        Log.d("LanguageDialogue","setLocale in pref : "+lang.getLanguageCode());
        Configuration config=new Configuration();
        myLocale=new Locale(lang.getLanguageCode());
        Locale.setDefault(myLocale);
        config.locale=myLocale;
        Log.d("Change Local","setLocale: "+myLocale);
        getBaseContext().getResources().updateConfiguration(config,Config.getContext().getResources().getDisplayMetrics());


        }

/**
 * ATTENTION: This was auto-generated to implement the App Indexing API.
 * See https://g.co/AppIndexing/AndroidStudio for more information.
 */
public Action getIndexApiAction(){
        Thing object=new Thing.Builder()
        .setName("Main Page") // TODO: Define a title for the content shown.
        // TODO: Make sure this auto-generated URL is correct.
        .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
        .build();
        return new Action.Builder(Action.TYPE_VIEW)
        .setObject(object)
        .setActionStatus(Action.STATUS_TYPE_COMPLETED)
        .build();
        }

@Override
public void onConfigurationChanged(Configuration newConfig){
        // refresh your views here
        super.onConfigurationChanged(newConfig);
        }

@Override
public void onStart(){
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client,getIndexApiAction());
        }

@Override
public void onStop(){
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client,getIndexApiAction());
        client.disconnect();
        }

public class CustomEventAdapter extends ArrayAdapter {
    private Activity activity;
    List<Languages> languageList;
    private TextView tv_lang_name;


    public CustomEventAdapter(Activity activity, List<Languages> languageList) {
        super(activity, R.layout.language_list_item);
        this.languageList = languageList;
        this.activity = activity;
    }


    public int getCount() {
        return languageList.size();
    }

    public Object getItem(int position) {
        return languageList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        try {
            if (view == null) {
                LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = li.inflate(R.layout.language_list_item, null);
            }
            initializeIds(view);
            setItems(languageList.get(position));

        } catch (Exception e) {

        }
        return view;
    }

    private void setItems(Languages lang) {

        tv_lang_name.setText(lang.getLanguageName());


    }


    private void initializeIds(View view) {

        tv_lang_name = (TextView) view.findViewById(R.id.tv_lang_name);


    }


}

    private void openLoginScreenAndSetPref() {
        PrefManager pref = new PrefManager(getApplicationContext());
        pref.setCustomerId(null);
        pref.setName(null);
        pref.setVillage(null);
        pref.setSessionKey(null);
        pref.setMobile(null);
        pref.setIsLoggedIn(false);
        pref.setAddress(null);
        pref.setPinCode(null);
        pref.setCity(null);
        pref.setState(null);
        Intent i = new Intent(this, Login_Activity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();  //Kill the activity from which you will go to next activity
        startActivity(i);
    }


    public void openFragmentList(View v){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_activity_fl, new Fragment_List());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(Config.KEY_FRAGMENT_LIST);
        ft.commit();
    }
}
