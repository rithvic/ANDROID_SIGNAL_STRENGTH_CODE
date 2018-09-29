package com.quadrobay.qbayApps.networkproject.ActivityClasses;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.quadrobay.qbayApps.networkproject.AdapterClasses.CustomSpinnerAdapter;
import com.quadrobay.qbayApps.networkproject.R;
import com.quadrobay.qbayApps.networkproject.Services.ConnectivityReceiver;
import com.quadrobay.qbayApps.networkproject.SingletonClass.AppController;
import com.quadrobay.qbayApps.networkproject.SingletonClass.VolleyMultiPartRequest;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sairaj on 12/12/17.
 */

public class FeedbackClass extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    Context context = this;
    private Toolbar historytoolbar;
    ImageView screenshotImageview;
    Spinner screenSpinner;
    EditText summaryEdittext,suggestionEdittext;
    Button submitButton;
    ArrayList<String> screenList = new ArrayList<>();
    CustomSpinnerAdapter screenAdapter;
    private static final int SELECT_FILE = 0;
    Bitmap bm=null;
    String spinnerStr = "",summaryStr = "",descriptionStr = "",suggestionStr = "",unique_id = "";

    private PopupWindow mPopupWindow;
    private View mCustomView;
    private LinearLayout pop_view_lay;
    ViewGroup windcontainer;

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    ImageButton reportlistbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mCustomView = getLayoutInflater().inflate(R.layout.loading_pop_up,null);
        pop_view_lay = (LinearLayout) findViewById(R.id.feedback_main_view);

        mPopupWindow = new PopupWindow(
                mCustomView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                false
        );
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Toast);
        mPopupWindow.setElevation(5);
        mPopupWindow.setFocusable(false);
        mPopupWindow.update();
        windcontainer = (ViewGroup) getWindow().getDecorView().getRootView();



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        historytoolbar = (Toolbar) findViewById(R.id.history_details_toolbar);
        setSupportActionBar(historytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Feedback");

        reportlistbtn = (ImageButton) historytoolbar.findViewById(R.id.reportlist_btn);
        reportlistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReportListClass.class);
                startActivity(intent);
            }
        });

        screenSpinner = (Spinner) findViewById(R.id.screen_spinner);
        summaryEdittext = (EditText) findViewById(R.id.summary_text);
        suggestionEdittext = (EditText) findViewById(R.id.suggestion_text);
        screenshotImageview = (ImageView) findViewById(R.id.screenshot);
        submitButton = (Button) findViewById(R.id.submit_button);
        screenSpinner.setOnItemSelectedListener(this);
        screenshotImageview.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        spinnerSetup();
    }

    private void spinnerSetup() {

        screenList.add("Home");
        screenList.add("Wifi Info");
        screenList.add("Map View");
        screenList.add("Speed Test");
        screenList.add("Device Info");
        screenList.add("History List");
        screenList.add("History Detail");
        screenList.add("Settings");
        screenList.add("Others");

        screenAdapter = new CustomSpinnerAdapter<String>(context, R.layout.custom_spinner_item);
        screenAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        screenAdapter.add("Screens");
        screenAdapter.addAll(screenList);
        screenSpinner.setAdapter(screenAdapter);
        screenSpinner.setSelection(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public static void applyDim(@NonNull ViewGroup parent, float dimAmount){
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
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

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.screenshot:
                grandPermission();
                break;
            case R.id.submit_button:
                postRequest();
                break;
        }

    }

    private void grandPermission() {

        if(Build.VERSION.SDK_INT>22){
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else{
            galleryIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(context, "Please Grant the Access Media Storage Permission", Toast.LENGTH_SHORT).show();
                }else {
                    galleryIntent();
                }
            }
        }
    }

    private void galleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        super.startActivityForResult(i, GALLERY_IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null ){

            Uri  extras = data.getData();
            if (extras != null) {
                String path = getPathFromURI(extras);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                bm = BitmapFactory.decodeFile(path, options);
                screenshotImageview.setImageBitmap(bm);


            }else {


            }
        }
        else {


        }
    }


    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }



    private String getPathFromUri(Uri extras) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(extras,  proj, null, null, null);
        if (cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) adapterView;
        int sp_id = spinner.getId();
        switch (sp_id) {
            case R.id.screen_spinner:
                if (i != 0) {
                    spinnerStr = screenList.get(i - 1);
                    Log.e("screen", spinnerStr);
                    ((TextView) spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white));
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public byte[] getFileDataFromDrawableJpg(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 99, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private Boolean checkConnection() {

        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;

    }

    private void postRequest() {

        if ("".equals(summaryEdittext.getText().toString()) && "".equals(suggestionEdittext.getText().toString())) {

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            builder.setTitle("Please specify the error")
                    .setMessage("Please fill up the Error summary or Suggestions")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else{

            if (checkConnection()) {

                Calendar calendar = Calendar.getInstance();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String dateformatstr = dateFormat.format(calendar.getTime());

                unique_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

                int SocketTimeout = 50000;
                RetryPolicy retry = new DefaultRetryPolicy(SocketTimeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                );

                summaryStr = summaryEdittext.getText().toString();
                suggestionStr = suggestionEdittext.getText().toString();

                ((LinearLayout) mPopupWindow.getContentView().findViewById(R.id.success_layout)).setVisibility(View.GONE);
                ((LinearLayout) mPopupWindow.getContentView().findViewById(R.id.failure_layout)).setVisibility(View.GONE);
                ((LinearLayout) mPopupWindow.getContentView().findViewById(R.id.warning_layout)).setVisibility(View.GONE);

                ((AVLoadingIndicatorView) mPopupWindow.getContentView().findViewById(R.id.avi)).setVisibility(View.VISIBLE);
                ((TextView) mPopupWindow.getContentView().findViewById(R.id.Info_Txtview)).setVisibility(View.VISIBLE);

                applyDim(windcontainer,0.7f);
                mPopupWindow.showAtLocation(pop_view_lay, Gravity.CENTER, 0, 0);

                //our custom volley request
                VolleyMultiPartRequest volleyMultipartRequest = new VolleyMultiPartRequest(Request.Method.POST, "http://quadrobay.co.in/Signal_App/public/api/showroom/add-feedback",
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                try {

                                    String res = new String(response.data);
                                    Log.e("response", res);
                                    ((AVLoadingIndicatorView) mPopupWindow.getContentView().findViewById(R.id.avi)).setVisibility(View.GONE);
                                    ((TextView) mPopupWindow.getContentView().findViewById(R.id.Info_Txtview)).setVisibility(View.GONE);

                                    ((LinearLayout) mPopupWindow.getContentView().findViewById(R.id.success_layout)).setVisibility(View.VISIBLE);
                                    ((Button) mPopupWindow.getContentView().findViewById(R.id.success_btn)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mPopupWindow.dismiss();
                                            clearDim(windcontainer);
                                        }
                                    });

                                    bm=null;
                                    screenshotImageview.setImageDrawable(getResources().getDrawable(R.drawable.addimage));
                                    summaryEdittext.setText("");
                                    suggestionEdittext.setText("");

                                    screenAdapter = new CustomSpinnerAdapter<String>(context, R.layout.custom_spinner_item);
                                    screenAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                                    screenAdapter.add("Screens");
                                    screenAdapter.addAll(screenList);
                                    screenSpinner.setAdapter(screenAdapter);
                                    screenSpinner.setSelection(0);

                                    JSONObject obj = new JSONObject(new String(response.data));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, "Error Adding Feedback", Toast.LENGTH_SHORT).show();
                                ((AVLoadingIndicatorView) mPopupWindow.getContentView().findViewById(R.id.avi)).setVisibility(View.GONE);
                                ((TextView) mPopupWindow.getContentView().findViewById(R.id.Info_Txtview)).setVisibility(View.GONE);

                                ((LinearLayout) mPopupWindow.getContentView().findViewById(R.id.success_layout)).setVisibility(View.GONE);
                                ((LinearLayout) mPopupWindow.getContentView().findViewById(R.id.warning_layout)).setVisibility(View.GONE);
                                ((LinearLayout) mPopupWindow.getContentView().findViewById(R.id.failure_layout)).setVisibility(View.VISIBLE);
                                ((Button) mPopupWindow.getContentView().findViewById(R.id.failure_cancel_button)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPopupWindow.dismiss();
                                        clearDim(windcontainer);
                                    }
                                });
                                ((Button) mPopupWindow.getContentView().findViewById(R.id.failure_retry_button)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPopupWindow.dismiss();
                                        clearDim(windcontainer);
                                        postRequest();

                                    }
                                });


                            }
                        }) {


                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Device", unique_id);
                        params.put("Screen", spinnerStr);
                        params.put("Summary", summaryStr);
                        params.put("Description", descriptionStr);
                        params.put("Suggestion", suggestionStr);
                        params.put("Date", dateformatstr);
                        return params;
                    }


                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        if (bm != null) {
                            String uniqueID = UUID.randomUUID().toString();
                            params.put("pic", new DataPart(uniqueID + ".png", getFileDataFromDrawableJpg(bm)));
                        }
                        return params;
                    }
                };

                volleyMultipartRequest.setRetryPolicy(retry);
                AppController.getInstance().addToRequestQueue(volleyMultipartRequest);
            } else {

                applyDim(windcontainer,0.7f);
                mPopupWindow.showAtLocation(pop_view_lay, Gravity.CENTER, 0, 0);

                ((AVLoadingIndicatorView) mPopupWindow.getContentView().findViewById(R.id.avi)).setVisibility(View.GONE);
                ((TextView) mPopupWindow.getContentView().findViewById(R.id.Info_Txtview)).setVisibility(View.GONE);

                ((LinearLayout) mPopupWindow.getContentView().findViewById(R.id.failure_layout)).setVisibility(View.GONE);
                ((LinearLayout) mPopupWindow.getContentView().findViewById(R.id.success_layout)).setVisibility(View.GONE);

                ((LinearLayout) mPopupWindow.getContentView().findViewById(R.id.warning_layout)).setVisibility(View.VISIBLE);
                ((Button) mPopupWindow.getContentView().findViewById(R.id.warning_cancel_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearDim(windcontainer);
                        mPopupWindow.dismiss();
                    }
                });
                ((Button) mPopupWindow.getContentView().findViewById(R.id.warning_retry_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                        clearDim(windcontainer);
                        postRequest();

                    }
                });

            }
        }
    }
}
