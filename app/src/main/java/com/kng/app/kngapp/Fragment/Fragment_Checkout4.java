package com.kng.app.kngapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kng.app.kngapp.Bill;
import com.kng.app.kngapp.Config;
import com.kng.app.kngapp.Customer;
import com.kng.app.kngapp.R;
import com.kng.app.kngapp.helper.PrefManager;


public class Fragment_Checkout4 extends Fragment {

    TextView adress_billing_text,c4_ship_address,c4_ship_name,c4_ship_village,c4_ship_city,c4_ship_state,c4_ship_pincode,c4_ship_mobile;
    TextView cart_total,cart_discount,cart_shipping_charge,cart_discounted_total;
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

                Bill bill = Fragment_Checkout3.getBill();
                cart_discounted_total.setText(String.valueOf(bill.getDiscountedBill()));
                cart_discount.setText(String.valueOf(bill.getDiscount()));
                cart_total.setText(String.valueOf(bill.getTotal()));
                cart_shipping_charge.setText(String.valueOf(bill.getShippingCharge()));

                PrefManager pref = new PrefManager(Config.getContext());

                adress_billing_text.setText(pref.getAddress());

                if(Fragment_Checkout2.customer != null){
                    Customer cust = Fragment_Checkout2.customer;
                    c4_ship_address.setText(cust.getAddress());
                    c4_ship_name.setText(cust.getName());
                    c4_ship_village.setText(cust.getVillage());
                    c4_ship_city.setText(cust.getCity());
                    c4_ship_state.setText(cust.getState());
                    c4_ship_mobile.setText(cust.getMobileNo());
                    c4_ship_pincode.setText(cust.getPincode());
                }else{
                    c4_ship_address.setText(pref.getAddress());
                    c4_ship_name.setText(pref.getName());
                    c4_ship_village.setText(pref.getVillage());
                    c4_ship_city.setText(pref.getCity());
                    c4_ship_state.setText(pref.getState());
                    c4_ship_mobile.setText(pref.getMobile());
                    c4_ship_pincode.setText(pref.getPincode());
                }

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



    }

    private void intioliseId(View view) {

        adress_billing_text = (TextView) view.findViewById(R.id.adress_billing_text);


        c4_ship_address = (TextView) view.findViewById(R.id.c4_ship_address);
        c4_ship_village = (TextView) view.findViewById(R.id.c4_ship_address);
        c4_ship_name = (TextView) view.findViewById(R.id.c4_ship_name);
        c4_ship_city = (TextView) view.findViewById(R.id.c4_ship_city);
        c4_ship_state = (TextView) view.findViewById(R.id.c4_ship_state);
        c4_ship_pincode = (TextView) view.findViewById(R.id.c4_ship_pincode);
        c4_ship_mobile = (TextView) view.findViewById(R.id.c4_ship_mobile);

        cart_discount = (TextView) view.findViewById(R.id.cart_discount);
        cart_discounted_total = (TextView) view.findViewById(R.id.cart_discounted_total);
        cart_shipping_charge = (TextView) view.findViewById(R.id.cart_shipping_charge);
        cart_total = (TextView) view.findViewById(R.id.cart_total);
        c4_checkout = (Button) view.findViewById(R.id.c4_checkout);
        c4_back = (Button) view.findViewById(R.id.c4_back);
        c4_checkbox = (CheckBox) view.findViewById(R.id.c4_checkbox);

    }
}
