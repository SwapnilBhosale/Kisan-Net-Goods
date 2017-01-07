package com.example.hp.myapplication.Activitis;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hp.myapplication.CartItem;
import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.Fragment.Fragment_Add_To_Cart;
import com.example.hp.myapplication.Fragment.Fragment_Checkout1;
import com.example.hp.myapplication.R;

import java.util.List;

public class Checkout_Activity extends ActionBarActivity {

    private String TAG = Checkout_Activity.class.getSimpleName();
    public static TextView tv;
    public RelativeLayout rl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_);

        Toolbar toolbar = (Toolbar) findViewById(R.id.checkout_toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);


        //View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);



       // mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        transaction();


    }

    private void transaction() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.checkout_frame, new Fragment_Checkout1());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.checkout_menu, menu);

        try {
            RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.action_cart_check).getActionView();
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
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart_check) {


        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if(count == 0){
            //super.onBackPressed();
            super.onBackPressed();
        }else{
            getSupportFragmentManager().popBackStack();
        }
        //}
    }

}
