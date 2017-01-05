package com.example.hp.myapplication.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.myapplication.Activitis.Checkout_Activity;
import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.R;
import com.example.hp.myapplication.helper.PrefManager;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Checkout1 extends Fragment {

    private TextView c1_address,c1_name,c1_village,c1_city,c1_state,c1_postal_code,c1_mobile_no;
    private Button c1_back,c1_next;
    public static List<String> infoList;
    private String TAG = Fragment_Checkout1.class.getSimpleName();

 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__checkout1, container, false); // see it full way

        Log.d(TAG, "onCreateView: ");
        try {
           // if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__checkout1, container, false);

             // getActivity().getActionBar().setTitle("Checkout1");

                intioliseId(view);
                loadData();

                setListners();


          //  }
        }catch (Exception e) {
            Log.e(TAG, "onCreateView: ",e );}
        return view;
    }

    public void setActionBarTitle(String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBarTitle(getString(R.string.billing_address));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle(getString(R.string.billing_address));
    }

    private void loadData() {

        PrefManager pref = new PrefManager(Config.getContext());

        Log.d(TAG, "loadData: "+pref.getAddress());
        c1_address.setText(pref.getAddress());
        c1_name.setText(pref.getName());
        c1_village.setText(pref.getVillage());
        c1_city.setText(pref.getCity());
        c1_state.setText(pref.getState());
        c1_mobile_no.setText(pref.getMobile());
        c1_postal_code.setText(pref.getPincode());
    }

    private void setListners() {

        c1_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.checkout_frame, new Fragment_Checkout2()).addToBackStack(TAG);
                ft.hide(Fragment_Checkout1.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();




            }

        });

        c1_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              getActivity().finish();

            }
        });

    }

    private void intioliseId(View view) {

        c1_address = (TextView) view.findViewById(R.id.c1_address);
        c1_name = (TextView) view.findViewById(R.id.c1_name);
        c1_village = (TextView) view.findViewById(R.id.c1_village);
        c1_city = (TextView) view.findViewById(R.id.c1_city);
        c1_state = (TextView) view.findViewById(R.id.c1_state);
        c1_postal_code = (TextView) view.findViewById(R.id.c1_postal_code);
        c1_mobile_no = (TextView) view.findViewById(R.id.c1_mobile_no);
        c1_back = (Button) view.findViewById(R.id.c1_back);
        c1_next = (Button) view.findViewById(R.id.c1_next);


    }

}
