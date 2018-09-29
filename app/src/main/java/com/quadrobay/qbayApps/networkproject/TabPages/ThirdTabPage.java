package com.quadrobay.qbayApps.networkproject.TabPages;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quadrobay.qbayApps.networkproject.R;
import com.quadrobay.qbayApps.networkproject.SingletonClass.AppController;
import com.quadrobay.qbayApps.networkproject.SingletonClass.DataParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

//import org.apache.http.util.EntityUtils;

//import org.apache.http.HttpResponse;

//import org.apache.http.client.HttpClient;

//import org.apache.http.client.methods.HttpGet;

//import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by benchmark on 03/10/17.
 */

public class ThirdTabPage extends Fragment implements OnMapReadyCallback, LocationListener {

    Boolean currentfirtloc;
    LatLng currentlocation;
    Polyline towerroute;

    LinearLayout map_loading_layout;
    RelativeLayout progress_layout;
    View constrview;
    LinearLayout lay;

    TextView infoview;

    private PopupWindow mPopupWindow;
    private View mCustomView;

    private GoogleMap googleMap;
    LocationManager locationManager;
    TelephonyManager telephonyManager;
    OpenCellID opencellid;

    Boolean continouszooming;

    public ThirdTabPage() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        lay = (LinearLayout) inflater.inflate(R.layout.third_tab_page, container, false);

        map_loading_layout=lay.findViewById(R.id.linearmap_layout);
        progress_layout=lay.findViewById(R.id.loadingview);
        continouszooming = true;
        currentfirtloc = true;
        mCustomView = getLayoutInflater().inflate(R.layout.loading_pop_up,null);
        mPopupWindow = new PopupWindow(
                mCustomView,
                500,
                500
        );
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Toast);
        mPopupWindow.setElevation(5);
        mPopupWindow.update();
        infoview = (TextView) mPopupWindow.getContentView().findViewById(R.id.Info_Txtview);
        locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {

        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // return TODO;


        }

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.TowerMap));
        telephonyManager = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);



        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(final GoogleMap googlemap) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                // TODO Auto-generated method stub
                googleMap = googlemap;
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.



                    return;
                }else {
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.getUiSettings().setCompassEnabled(true);
                    googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                    googleMap.setBuildingsEnabled(true);
                    googleMap.setIndoorEnabled(true);
                    JSONObject cellObj = new JSONObject();
                    try {
                        List<CellInfo> cellLocation = telephonyManager.getAllCellInfo();
                        for (int i = 0; i < cellLocation.size(); ++i) {

                            CellInfo cellinfo = cellLocation.get(i);
                            Log.d("CellLocation", cellinfo.toString());
                            if (cellinfo instanceof CellInfoGsm) {

                                CellIdentityGsm identityGsm = ((CellInfoGsm) cellinfo).getCellIdentity();
                                String networkOperator = telephonyManager.getNetworkOperator();
                                if (!"".equals(networkOperator)) {
                                    String gsmnetmcc = networkOperator.substring(0, 3);
                                    String gsmnetmnc = networkOperator.substring(3);

                                    int gsmlac = identityGsm.getLac();
                                    int gsmcid = identityGsm.getCid();
                                    int gsmmcc = identityGsm.getMcc();
                                    int gsmmnc = identityGsm.getMnc();

                                    JSONArray gsmreqarr = new JSONArray();
                                    JSONObject gsmreqobject = new JSONObject();
                                    try {
                                        gsmreqobject.put("lac", gsmlac);
                                        gsmreqobject.put("cid", gsmcid);
                                        gsmreqarr.put(gsmreqobject);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    opencellid = new OpenCellID();

                                    opencellid.setMcc(gsmnetmcc);
                                    opencellid.setMnc(gsmnetmnc);
                                    opencellid.setCallID(gsmcid);
                                    opencellid.setCallLac(gsmlac);
                                    opencellid.setRadio("gsm");
                                    opencellid.requestarray = gsmreqarr;


                                    try {
                                        opencellid.GetOpenCellID();
                                        if (!opencellid.isError()) {
                                            Log.e("Opencellid", opencellid.getLocation());
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }


                            } else if (cellinfo instanceof CellInfoLte) {


                                CellIdentityLte identityLte = ((CellInfoLte) cellinfo).getCellIdentity();
                                int lac = identityLte.getTac();
                                int cid = identityLte.getCi();
                                int mcc = identityLte.getMcc();
                                int mnc = identityLte.getMnc();
                                int psc = identityLte.getPci();

                                JSONArray reqarr = new JSONArray();
                                JSONObject reqobject = new JSONObject();
                                try {
                                    reqobject.put("lac", lac);
                                    reqobject.put("cid", cid);
                                    reqarr.put(reqobject);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                String networkOperator = telephonyManager.getNetworkOperator();
                                if (!"".equals(networkOperator)) {
                                    String netmcc = networkOperator.substring(0, 3);
                                    String netmnc = networkOperator.substring(3);

                                    opencellid = new OpenCellID();

                                    opencellid.setMcc(netmcc);
                                    opencellid.setMnc(netmnc);
                                    opencellid.setCallID(cid);
                                    opencellid.setCallLac(lac);
                                    opencellid.setRadio("lte");
                                    opencellid.requestarray = reqarr;


                                    try {
                                        opencellid.GetOpenCellID();
                                        if (!opencellid.isError()) {
                                            Log.e("Opencellid", opencellid.getLocation());
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else if (cellinfo instanceof CellInfoCdma) {

                                CellIdentityCdma identityCdma = ((CellInfoCdma) cellinfo).getCellIdentity();

                                String networkOperator = telephonyManager.getNetworkOperator();
                                if (!"".equals(networkOperator)) {
                                    String cdmanetmcc = networkOperator.substring(0, 3);
                                    String cdmanetmnc = networkOperator.substring(3);

                                    int cdmalat = identityCdma.getLongitude();
                                    int cdmalon = identityCdma.getLongitude();

                                    LatLng towerlocation = null;
                                    towerlocation = new LatLng(cdmalat, cdmalon);
                                    googleMap.addMarker(new MarkerOptions().position(towerlocation).title(getCompleteAddressString(cdmalat, cdmalon)));
                                }
                            } else if (cellinfo instanceof CellInfoWcdma) {

                                CellIdentityWcdma identityWcdma = ((CellInfoWcdma) cellinfo).getCellIdentity();

                                String networkOperator = telephonyManager.getNetworkOperator();
                                if (!"".equals(networkOperator)) {
                                    String wcdmanetmcc = networkOperator.substring(0, 3);
                                    String wcdmanetmnc = networkOperator.substring(3);

                                    int wcdmalac = identityWcdma.getLac();
                                    int wcdmacid = identityWcdma.getCid();
                                    int wcdmamcc = identityWcdma.getMcc();
                                    int wcdmamnc = identityWcdma.getMnc();


                                    JSONArray reqarr = new JSONArray();
                                    JSONObject reqobject = new JSONObject();
                                    try {
                                        reqobject.put("lac", wcdmalac);
                                        reqobject.put("cid", wcdmacid);
                                        reqarr.put(reqobject);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    opencellid = new OpenCellID();

                                    opencellid.setMcc(wcdmanetmcc);
                                    opencellid.setMnc(wcdmanetmnc);
                                    opencellid.setCallID(wcdmacid);
                                    opencellid.setCallLac(wcdmalac);
                                    opencellid.setRadio("cdma");
                                    opencellid.requestarray = reqarr;


                                    try {
                                        opencellid.GetOpenCellID();
                                        if (!opencellid.isError()) {
                                            Log.e("Opencellid", opencellid.getLocation());
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                       LatLng towerdest =  marker.getPosition();
                        if (currentlocation != null) {

                            if (towerroute != null){
                                towerroute.remove();
                            }

                            String url = getDirectionsUrl(currentlocation, towerdest);

                            Object[] ar = new Object[]{url};

                            DownloadTask downloadTask = new DownloadTask();

                            //Start downloading json data format from Google Dirrections API
                            downloadTask.execute(url);
                        }

                        return true;
                    }

                });


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {


                    }
                });
            }
        });


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // return TODO;




        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }



        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lay;

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        //Origin of the source
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        //Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        //Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        //Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        //Output format
        String output = "json";

        //Building the url to web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try{
                data = downloadUrl(String.valueOf(url[0]));

            }catch (Exception e){
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            //Invokes the thread for parsing the JSON data
            parserTask.execute(result);


        }

    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            if (result != null) {
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.RED);

                    Log.d("onPostExecute", "onPostExecute lineoptions decoded");

                }

                // Drawing polyline in the Google Map for the i-th route
                if (lineOptions != null) {
                    towerroute = googleMap.addPolyline(lineOptions);
                } else {
                    Log.d("onPostExecute", "without Polylines drawn");
                }
            }
        }
    }

    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream istream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            istream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(istream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        }catch (Exception e){
            Log.d("Exception", e.toString());
        }finally {
            istream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction addr", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction addr", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction addr", "Canont get Address!");
        }
        return strAdd;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


    }

    @Override
    public void onResume() {
        super.onResume();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                // infoview.setText("Location access is required. Please enable location access to continue");
                // mPopupWindow.showAtLocation(lay,Gravity.CENTER,0,0);

                return;
            }else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }

    }

    @Override
    public void onStop() {
        super.onStop();

        mPopupWindow.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPopupWindow.dismiss();
         }

    @Override
    public void onDestroy() {
        super.onDestroy();
         }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
           }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("Latitude",String.valueOf(location.getLatitude()));
        Log.e("Latitude",String.valueOf(location.getLongitude()));



        if (continouszooming) {
            LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(coordinate).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            currentlocation = new LatLng(location.getLatitude(), location.getLongitude());
            map_loading_layout.setVisibility(View.VISIBLE);
            progress_layout.setVisibility(View.GONE);
            continouszooming = false;
        }

        LatLng pathloc = new LatLng(location.getLatitude(), location.getLongitude());

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }



    public class OpenCellID {
        String mcc; //Mobile Country Code
        String mnc; //mobile network code
        String cellid; //Cell ID
        String lac; //Location Area Code
        Boolean error;
        String strURLSent;
        String GetOpenCellID_fullresult;
        String latitude;
        String longitude;
        JSONArray requestarray;
        String radio;

        public void setRadio(String radiotype){
            radio = radiotype;
        }

        public Boolean isError(){
            return error;
        }

        public void setRequestarray(JSONArray requestarr){
            requestarray = requestarr;
        }

        public void setMcc(String value){
            mcc = value;
        }

        public void setMnc(String value){
            mnc = value;
        }

        public void setCallID(int value){
            cellid = String.valueOf(value);
        }

        public void setCallLac(int value){
            lac = String.valueOf(value);
        }

        public String getLocation(){
            return(latitude + " : " + longitude);
        }
        public void groupURLSent(){
            strURLSent = "https://us2.unwiredlabs.com/v2/process.php";
        }

        public String getstrURLSent(){
            return strURLSent;
        }

        public String getGetOpenCellID_fullresult(){
            return GetOpenCellID_fullresult;
        }

        public void GetOpenCellID() throws Exception {

            int SocketTimeout = 30000;
            RetryPolicy retry= new DefaultRetryPolicy(SocketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            );
            int metint = Request.Method.POST;
            JSONObject finalreqobj = new JSONObject();
            finalreqobj.put("token","9a810d6f60efd9");
            finalreqobj.put("radio",radio);
            finalreqobj.put("mcc",mcc);
            finalreqobj.put("mnc",mnc);
            finalreqobj.put("cells",requestarray);
            finalreqobj.put("address","1");
            groupURLSent();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://us2.unwiredlabs.com/v2/process.php", finalreqobj,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Server Res", response.toString());

                            LatLng towerlocation = null;
                            try {
                                towerlocation = new LatLng(response.getDouble("lat"), response.getDouble("lon"));
                                // create marker
                                MarkerOptions newmarker = new MarkerOptions().position(towerlocation).title(response.getString("address"));

                                int height = 120;
                                int width = 120;
                                try {
                                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.towermapantenna);
                                    Bitmap b=bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                    // Changing marker icon
                                    newmarker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                                    googleMap.addMarker(newmarker);

                                }catch (Exception e){

                                }
                                } catch(JSONException e){
                                    e.printStackTrace();
                                }
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
            AppController.getInstance().addToRequestQueue(request,"json_req_method");

        }

    }
}

