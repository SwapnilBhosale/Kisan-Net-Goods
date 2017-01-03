package com.example.hp.myapplication.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.hp.myapplication.R;


public class Fragment_Checkout2 extends Fragment {

    CheckBox c2_checkbox;
    LinearLayout c2_linear_layout;
    Button c2_back,c2_next;
    private String TAG = Fragment_Checkout2.class.getSimpleName();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__checkout2, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__checkout2, container, false);


                intioliseId(view);

                c2_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean         isChecked)
                    {
                        // TODO Auto-generated method stub
                        if(isChecked)
                        {
                            c2_linear_layout.setVisibility(View.VISIBLE);
                            c2_linear_layout.setVisibility(View.VISIBLE);

                        }
                        else
                        {
                            c2_linear_layout.setVisibility(View.GONE);

                        }
                    }
                });

                setListners();



            }
        }catch (Exception e) {
            e.getMessage();}
        return view;
    }

    private void setListners() {

         CheckBox.OnCheckedChangeListener listener =
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            if (c2_linear_layout.getVisibility() == View.GONE)
                                c2_linear_layout.setVisibility(View.VISIBLE);
                        } else {

                            c2_linear_layout.setVisibility(View.GONE);
                        }
                    }
                };


        c2_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().onBackPressed();
            }
        });

        c2_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.checkout_frame, new Fragment_Checkout3()).addToBackStack(TAG);
                ft.hide(Fragment_Checkout2.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });
    }

    private void intioliseId(View view) {

        c2_checkbox = (CheckBox) view.findViewById(R.id.c2_checkbox);
        c2_linear_layout = (LinearLayout) view.findViewById(R.id.c2_linear_layout);
        c2_next = (Button) view.findViewById(R.id.c2_next);
        c2_back = (Button) view.findViewById(R.id.c2_back);

    }
}
