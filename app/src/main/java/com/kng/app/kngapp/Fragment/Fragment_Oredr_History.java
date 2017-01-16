package com.kng.app.kngapp.Fragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ServiceCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kng.app.kngapp.Config;
import com.kng.app.kngapp.R;


public class Fragment_Oredr_History extends Fragment {

    String[] order_id={"8QW895EW45","QE8654QD54","QEW4579W65"};
    String[] total_item={"3","8","2"};
    String[] date={"22-05-2016","17/12/2016","22/12/2016"};
    String[] price={"1025","2035","5010"};
    private ListView order_list;
    String TAG="";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__oredr__history, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__oredr__history, container, false);


                intioliseId(view);
                setListners();
                getList();


            }
        }catch (Exception e) {
            e.getMessage();}
        return view;
    }



    private void getList() {
        CustomEventAdapter order_list_adapter = new CustomEventAdapter(getActivity(), order_id, total_item, date, price);
        order_list.setAdapter(order_list_adapter);
        try {
                order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                        String ord_id = ((TextView) view.findViewById(R.id.order_id)).getText().toString();
                        String date = ((TextView) view.findViewById(R.id.date)).getText().toString();

                        Log.d(TAG, "onItemClick: "+ord_id);
                        Log.d(TAG, "onItemClick: "+date);


                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString("ord_id", String.valueOf(ord_id));
                        bundle.putString("ord_date", String.valueOf(date));
                        Order_History_Detail fg=  new Order_History_Detail();
                        fg.setArguments(bundle);
                        ft.replace(R.id.main_activity_fl, fg).addToBackStack(Config.KEY_FRAGMENT_LIST);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                    }
                });

        } catch (Exception e) {
            e.getMessage();

        }
    }
    private void setListners() {


    }

    private void intioliseId(View view) {
        order_list = (ListView) view.findViewById(R.id.order_list);

    }

    public static class CustomEventAdapter extends ArrayAdapter implements ListAdapter {
        private FragmentActivity activity;
        String[] orderId;
        String[] totalItem;
        String[] ord_date;
        String[] ord_price;

        private  TextView order_id,total_item,date,price;


        public CustomEventAdapter(FragmentActivity activity, String[] orderId, String[] totalItem, String[] ord_date, String[] ord_price) {
            super(activity, R.layout.order_list_item);
            this.orderId = orderId;
            this.totalItem = totalItem;
            this.ord_date = ord_date;
            this.ord_price = ord_price;
            this.activity = activity;

        }


        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        public int getCount() {
            return orderId.length;
        }

        public Object getItem(int position) {
            return orderId[position];
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

                    view = li.inflate(R.layout.order_list_item, null);
                }
                initializeIds(view);
                setItems(position);

            } catch (Exception e) {

            }
            return view;
        }


        private void setItems(int position) {

            order_id.setText(orderId[position]);
            total_item.setText(totalItem[position]);
            date.setText(ord_date[position]);
            price.setText(ord_price[position]);
        }

        private void initializeIds(View view) {

            order_id = (TextView) view.findViewById(R.id.order_id);
            total_item = (TextView) view.findViewById(R.id.total_item);
            date = (TextView) view.findViewById(R.id.date);
            price = (TextView) view.findViewById(R.id.price);

        }
    }
}
