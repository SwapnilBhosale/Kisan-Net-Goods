package com.kng.app.kngapp.Fragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kng.app.kngapp.R;

import java.util.List;

public class Order_History_Detail extends Fragment {

    String TAG = "";
    ListView detail_order_list;
    TextView detail_order_id,detail_ord_date,detail_ord_total_item,detail_ord_price;
    String[] name = {"ABC","PQR","XYZ"};
    String[] quantity = {"12","14","52"};
    String[] price = {"120","500","200"};
    static String id;
    static String date;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order__history__detail, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment_order__history__detail, container, false);

                intioliseId(view);
                setListners();

                    if (getArguments() != null) {
                        id = getArguments().getString("ord_id");
                        date = getArguments().getString("ord_date");
                    }

                Log.d(TAG, "order_id: "+id);
                Log.d(TAG, "order_id: "+date);
                detail_order_id.setText(id);
                detail_ord_date.setText(date);
                getList();
            }


        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        }
        return view;
    }

    private void getList() {
        CustomEventAdapter adapter = new CustomEventAdapter(getActivity(),name,quantity,price);
        detail_order_list.setAdapter(adapter);
    }

    private void setListners() {

    }

    private void intioliseId(View view) {
        detail_order_id = (TextView) view.findViewById(R.id.detail_order_id);
        detail_ord_total_item = (TextView) view.findViewById(R.id.detail_ord_total_item);
        detail_ord_date = (TextView) view.findViewById(R.id.detail_ord_date);
        detail_ord_price = (TextView) view.findViewById(R.id.detail_ord_price);
        detail_order_list = (ListView) view.findViewById(R.id.detail_order_list);

    }

    public static class CustomEventAdapter extends ArrayAdapter implements ListAdapter {
        private FragmentActivity activity;
        String[] name;
        String[] quantity;
        String[] price;

        private  TextView detail_ord_item_name,detail_ord_item_quantity,detail_ord_item_price;

    public CustomEventAdapter(FragmentActivity activity, String[] name, String[] quantity, String[] price) {
        super(activity, R.layout.order_details_list_item);
        this.name = name;
        this.quantity = quantity;
        this.price = price;

        this.activity = activity;

    }


    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    public int getCount() {
        return name.length;
    }

    public Object getItem(int position) {
        return name[position];
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        try {
            if (view == null) {
                LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = li.inflate(R.layout.order_details_list_item, null);
            }
            initializeIds(view);
            setItems(position);

        } catch (Exception e) {

        }
        return view;
    }


    private void setItems(int position) {

            detail_ord_item_name.setText(name[position]);
            detail_ord_item_quantity.setText(quantity[position]);
            detail_ord_item_price.setText(price[position]);
    }

    private void initializeIds(View view) {

        detail_ord_item_name = (TextView) view.findViewById(R.id.detail_ord_item_name);
        detail_ord_item_quantity = (TextView) view.findViewById(R.id.detail_ord_item_quantity);
        detail_ord_item_price = (TextView) view.findViewById(R.id.detail_ord_item_price);

    }
}
}