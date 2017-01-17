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

import com.kng.app.kngapp.Config;
import com.kng.app.kngapp.Order;
import com.kng.app.kngapp.R;

import java.util.List;

public class Order_History_Detail extends Fragment {

    String TAG = "";
    ListView detail_order_list;
    TextView detail_order_id, detail_ord_date, detail_ord_total_item, detail_ord_price;
    String[] name = {"ABC", "PQR", "XYZ"};
    String[] quantity = {"12", "14", "52"};
    String[] price = {"120", "500", "200"};
    private int orderId;
    private Order order;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderId = getArguments().getInt("orderNumber");
            order = Fragment_Oredr_History.orderList.get(orderId);
        }

    }

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
                detail_order_id.setText(order.getOrders_id());
                detail_ord_date.setText(order.getDate_purchased());
                detail_ord_total_item.setText(String.valueOf(order.getOrderItems().size()));
                detail_ord_price.setText(Config.formatCurrency(order.getTotal_bill()));
                getList();
            }


        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        }
        return view;
    }

    private void getList() {
        CustomEventAdapter adapter = new CustomEventAdapter(getActivity(), order.getOrderItems());
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
        private List<OrderItem> list;

        private TextView detail_ord_item_name, detail_ord_item_quantity, detail_ord_item_price;

        public CustomEventAdapter(FragmentActivity activity, List<OrderItem> list) {
            super(activity, R.layout.order_details_list_item);
            this.list = list;
            this.activity = activity;

        }


    /*@Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }*/

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

   /* @Override
    public boolean hasStableIds() {
        return false;
    }*/

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

            OrderItem item = list.get(position);
            detail_ord_item_name.setText(item.getProductName());
            detail_ord_item_quantity.setText(item.getQuantity());
            detail_ord_item_price.setText(Config.formatCurrency(item.getFinal_price()));
        }

        private void initializeIds(View view) {

            detail_ord_item_name = (TextView) view.findViewById(R.id.detail_ord_item_name);
            detail_ord_item_quantity = (TextView) view.findViewById(R.id.detail_ord_item_quantity);
            detail_ord_item_price = (TextView) view.findViewById(R.id.detail_ord_item_price);

        }
    }
}