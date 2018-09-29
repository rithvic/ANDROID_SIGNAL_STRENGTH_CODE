package com.quadrobay.qbayApps.networkproject.ActivityClasses;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quadrobay.qbayApps.networkproject.R;
import com.quadrobay.qbayApps.networkproject.SingletonClass.AppController;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by benchmark on 05/01/18.
 */

public class ReportListClass extends AppCompatActivity {

    Toolbar reportlisttoolbar;

    String deviceid;

    JSONArray reportlist;
    ListView reportistview;
    ArrayList<String> datelist;

    PopupWindow popupWindow;
    View mCustomView;
    private LinearLayout pop_view_lay;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_list_layout);

        context = this;
        reportistview = (ListView) findViewById(R.id.report_list_view);
        datelist = new ArrayList<String>();

        pop_view_lay = (LinearLayout) findViewById(R.id.report_layout_id);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mCustomView = layoutInflater.inflate(R.layout.report_pop_up_screen,null,false);
        popupWindow = new PopupWindow(mCustomView, WindowManager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        reportlisttoolbar = (Toolbar) findViewById(R.id.history_details_toolbar);
        setSupportActionBar(reportlisttoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Report list");

        deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        int SocketTimeout = 30000;
        RetryPolicy retry = new DefaultRetryPolicy(SocketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        int metint = Request.Method.POST;

        JSONObject postd = new JSONObject();
        try {
            postd.put("Device",deviceid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://quadrobay.co.in/Signal_App/public/api/showroom/get-feedback", postd,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Server Res", response.toString());
                        try {
                            reportlist = response.getJSONArray("Response");
                            if (reportlist.length() != 0) {
                                for (int ii = 0; ii <= reportlist.length(); ii++) {
                                    JSONObject datedata = reportlist.getJSONObject(ii);
                                    datelist.add(datedata.getString("Updated_Time"));
                                    ReloadList();
                                }
                            }else {
                                datelist.add("No results found");
                                ReloadList();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("", response.toString());

                        Log.e("","");
                        Log.e("Request", "Success");

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Server err", error.toString());
                    }
                }
       );

        request.setRetryPolicy(retry);
        AppController.getInstance().addToRequestQueue(request, "json_req_method");


    }

    public void ReloadList(){
        ArrayAdapter<String> solutionsadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datelist);
        reportistview.setAdapter(solutionsadapter);

        reportistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    JSONObject selectdata = reportlist.getJSONObject(position);
                    if ("".equals(selectdata.getString("Screen"))){
                        ((TextView)popupWindow.getContentView().findViewById(R.id.report_pop_up_screen_txtview)).setText("No data");
                    }else {
                        ((TextView)popupWindow.getContentView().findViewById(R.id.report_pop_up_screen_txtview)).setText(selectdata.getString("Screen"));
                    }

                    if ("".equals(selectdata.getString("Summary"))){
                        ((TextView)popupWindow.getContentView().findViewById(R.id.report_pop_up_summary_txtview)).setText("No data");
                    }else {
                        ((TextView)popupWindow.getContentView().findViewById(R.id.report_pop_up_summary_txtview)).setText(selectdata.getString("Summary"));
                    }

                    if ("".equals(selectdata.getString("Suggestion"))){
                        ((TextView)popupWindow.getContentView().findViewById(R.id.report_pop_up_suggestions_txtview)).setText("No data");
                    }else {
                        ((TextView)popupWindow.getContentView().findViewById(R.id.report_pop_up_suggestions_txtview)).setText(selectdata.getString("Suggestion"));
                    }

                    if ("".equals(selectdata.getString("Screenshot"))){
                    }else {
                        Picasso.with(context).load(selectdata.getString("Screenshot")).placeholder(R.drawable.notavailablegray).into(((ImageView)popupWindow.getContentView().findViewById(R.id.report_imageview)));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((Button)popupWindow.getContentView().findViewById(R.id.report_pop_up_close_btn)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                popupWindow.showAtLocation(pop_view_lay, Gravity.CENTER, 0, 0);
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                    Log.i("Navigation", "You have forgotten to specify the parentActivityName in the AndroidManifest!");
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
