package com.kng.app.kngapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kng.app.kngapp.Config;
import com.kng.app.kngapp.R;
import com.kng.app.kngapp.helper.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Fragment_Feedback extends Fragment {

    RatingBar ratingBar;
    Button btn_feedback_submit;
    EditText et_cmp_feedback;
    ProgressDialog pd;
    private String TAG = Fragment_Feedback.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = getProgressBar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__feedback, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__feedback, container, false);


                intioliseId(view);
                setLisetners();
            }
        }catch (Exception e) {
            e.getMessage();}
        return view;
    }

   /* LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
    stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
    stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
    stars.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
    */


    private ProgressDialog getProgressBar() {
        ProgressDialog pd = new ProgressDialog(getActivity());
        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Set the progress dialog title and message
        pd.setMessage(Config.getContext().getResources().getString(R.string.loading));
        // Set the progress dialog background color
        //pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        // Finally, show the progress dialog
        return pd;
    }


    private void setLisetners() {
        btn_feedback_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postFeedback();
            }
        });
    }

    public void postFeedback(){
        try{
            JsonObjectRequest feedbackReq = new JsonObjectRequest(Request.Method.POST, Config.FEEDBACK_URL, getFeedbackJsonBody(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.dismiss();
                    try{
                        Log.d(TAG, "onResponse: "+response.toString() );
                        boolean isSuccess = response.getBoolean("status");
                        if(isSuccess){
                            Toast.makeText(getActivity(),R.string.thanks_for_feedback,Toast.LENGTH_LONG).show();
                            Config.callWebUrl(getContext(),Config.PLAY_STORE_FEED_URL+""+getContext().getPackageName());
                        }else{
                            Toast.makeText(getActivity(),R.string.error_general_error,Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        Log.e(TAG, "onResponse: ",e );
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d(TAG, "Error: " + volleyError.getMessage());

                    // hide the progress dialog
                    pd.dismiss();
                    if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof AuthFailureError || volleyError instanceof ParseError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError)
                        Toast.makeText(getActivity(),R.string.error_no_internet_conenction, Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(),R.string.error_general_error,Toast.LENGTH_SHORT).show();

                }
            });
            feedbackReq.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT,Config.WEB_RETRY_COUNT,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getActivity()).add(feedbackReq);
            pd.show();
        }catch(Exception e){
            Log.e(TAG, "postFeedback: ", e);
        }
    }

    private JSONObject getFeedbackJsonBody() throws JSONException {
        return new JSONObject("{\"customer_id\" : "+new PrefManager(Config.getContext()).getCustomerId()+",\"rating\" : \"" + ratingBar.getRating() + "\",\"feedback\" : \"" + et_cmp_feedback.getText().toString() + "\"}");
    }

    private void intioliseId(View view) {

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        btn_feedback_submit = (Button) view.findViewById(R.id.btn_feedback_submit);
        et_cmp_feedback = (EditText) view.findViewById(R.id.et_cmp_feedback);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (!getActivity().isFinishing() && pd != null) {
            pd.dismiss();
        }
    }
}
