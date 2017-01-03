package com.example.hp.myapplication.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.R;
import com.example.hp.myapplication.helper.PrefManager;

import java.util.List;

import static android.R.id.list;


public class Fragment_Checkout4 extends Fragment {

    TextView c4_address,c4_first_name,c4_last_name,c4_country,c4_city,c4_state,c4_pincode,c4_mobile;
    Button c4_checkout,c4_back;
    CheckBox c4_checkbox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__checkout4, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__checkout4, container, false);


                intioliseId(view);

                PrefManager pref = new PrefManager(Config.getContext());

                c4_address.setText(pref.getAddress());
                c4_first_name.setText(pref.getFname());
                c4_last_name.setText(pref.getLname());
                //c4_country.setText(pref.get());
                c4_city.setText(pref.getCity());
                c4_state.setText(pref.getState());
                c4_mobile.setText(pref.getMobile());
                c4_pincode.setText(pref.getPincode());

                setListners();


            }
        }catch (Exception e) {
            e.getMessage();}
        return view;
    }

    private void setListners() {

        c4_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.checkout_frame, new Fragment_Checkout3());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();*/
                getActivity().onBackPressed();
            }
        });
        c4_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    c4_checkout.setEnabled(true);
                else
                    c4_checkout.setEnabled(false);
            }
        });
        c4_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"WILL OPEN PAYMENT SCREEN",Toast.LENGTH_LONG).show();
            }
        });
        /*c4_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c4_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            getActivity().finish();

                        } else {

                            Snackbar.make(getView(),"Please accept Tearms and Conditions",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                        }
                    }
                });

            }
        });*/



    }

    private void intioliseId(View view) {

        c4_address = (TextView) view.findViewById(R.id.c4_address);
        c4_last_name = (TextView) view.findViewById(R.id.c4_last_name);
        c4_country = (TextView) view.findViewById(R.id.c4_country);
        c4_first_name = (TextView) view.findViewById(R.id.c4_first_name);
        c4_city = (TextView) view.findViewById(R.id.c4_city);
        c4_state = (TextView) view.findViewById(R.id.c4_state);
        c4_pincode = (TextView) view.findViewById(R.id.c4_pincode);
        c4_mobile = (TextView) view.findViewById(R.id.c4_mobile);
        c4_checkout = (Button) view.findViewById(R.id.c4_checkout);
        c4_back = (Button) view.findViewById(R.id.c4_back);
        c4_checkbox = (CheckBox) view.findViewById(R.id.c4_checkbox);

    }
}
