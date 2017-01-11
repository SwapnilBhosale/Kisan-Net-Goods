package com.kng.app.kngapp.Fragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
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

import com.kng.app.kngapp.R;


public class Fragment_Oredr_History extends Fragment {

    String[] order_id={"8QW895EW45","QE8654QD54","QEW4579W65"};
    String[] total_item={"3","8","2"};
    String[] date={"22-05-2016","17/12/2016","22/12/2016"};
    String[] price={"1025","2035","5010"};
    private ListView order_list;

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
        CustomEventAdapter order_list_adapter = new CustomEventAdapter(getActivity(),order_id, total_item,date,price);
        order_list.setAdapter(order_list_adapter);
        try {
        order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String ord_id = ((TextView) view.findViewById(R.id.order_id)).getText().toString();
                String ord_item = ((TextView) view.findViewById(R.id.total_item)).getText().toString();
                String date = ((TextView) view.findViewById(R.id.date)).getText().toString();
                String price = ((TextView) view.findViewById(R.id.price)).getText().toString();


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                // ...Irrelevant code for customizing the buttons and title
                dialogBuilder.setTitle("Order History");
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.order_list_pop_up, null);
                dialogBuilder.setView(dialogView);

                TextView order_id_pop = (TextView) dialogView.findViewById(R.id.order_id_pop);
                TextView ord_date_pop = (TextView) dialogView.findViewById(R.id.ord_date_pop);
                TextView ord_price_pop = (TextView) dialogView.findViewById(R.id.ord_price_pop);
                TextView ord_item_pop = (TextView) dialogView.findViewById(R.id.ord_item_pop);
                Button popup_button = (Button) dialogView.findViewById(R.id.popup_button);
                try {
                    order_id_pop.setText(ord_id);
                    ord_date_pop.setText(date);
                    ord_price_pop.setText(price);
                    ord_item_pop.setText(ord_item);
                } catch (Exception e) {
                    e.getMessage();
                }
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(alertDialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                alertDialog.show();
                try {
                    popup_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                }catch (Exception e){
                    e.getMessage();
                }
            }
        }); }catch (Exception e){
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
