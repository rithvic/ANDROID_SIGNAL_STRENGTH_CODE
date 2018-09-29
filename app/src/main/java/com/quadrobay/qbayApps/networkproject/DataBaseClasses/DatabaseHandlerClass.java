package com.quadrobay.qbayApps.networkproject.DataBaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benchmark on 09/10/17.
 */

public class DatabaseHandlerClass extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserNetworkLogs";

    // Contacts table name
    private static final String TABLE_NAME = "NetworkLogDetails";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String DEVICE_ID = "device_id";
    private static final String DATE_AND_TIME = "date_and_time";
    private static final String MOBILE_PRODUCT = "mobile_product";
    private static final String MOBILE_MODEL = "mobile_model";
    private static final String NET_OPERATOR = "net_operator";
    private static final String SECOND_SIM_NET_OPERATOR = "second_sim_net_operator";
    private static final String NET_TYPE = "net_type";
    private static final String SECOND_SIM_NET_TYPE = "second_sim_net_type";
    private static final String NET_STRENGTH = "net_strength";
    private static final String SECOND_SIM_NET_STRENGTH = "second_sim_network_strength";
    private static final String NET_COUNTRY = "net_country";
    private static final String SECOND_SIM_NET_COUNTRY = "second_sim_net_country";
    private static final String SIM_OPERATOR = "sim_operator";
    private static final String SECOND_SIM_OPERATOR = "second_sim_operator";
    private static final String SIM_COUNTRY = "sim_country";
    private static final String SECOND_SIM_COUNTRY = "second_sim_country";
    private static final String WIFI_SSID = "wifi_ssif";
    private static final String WIFI_SPEED = "wifi_speed";
    private static final String WIFI_STRENGTH = "wifi_strength";
    private static final String WIFI_FREQUENCY = "wifi_frequency";
    private static final String USER_LATITUDE = "user_latitude";
    private static final String USER_LONGITUDE = "user_longitude";
    private static final String USER_COUNTRY = "user_country";
    private static final String USER_STATE = "user_state";
    private static final String USER_CITY = "user_city";
    private static final String USER_AREA = "user_area";
    private static final String TOWER_LATITUDE = "tower_latitude";
    private static final String TOWER_LONGITUDE = "tower_longitude";
    private static final String TOWER_ADDRESS = "tower_address";
    private static final String UPDATED = "updated";

    public DatabaseHandlerClass(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("database : ","Database oncreate method");
        String Create_NetworkLogDetails_Table = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"+ DEVICE_ID + " TEXT," + DATE_AND_TIME+ " TEXT," + MOBILE_PRODUCT + " TEXT," + MOBILE_MODEL + " TEXT," + NET_OPERATOR + " TEXT," +NET_TYPE+ " TEXT," +NET_STRENGTH + " TEXT," +NET_COUNTRY + " TEXT," +SIM_OPERATOR + " TEXT," +SIM_COUNTRY + " TEXT," + SECOND_SIM_NET_OPERATOR + " TEXT,"+ SECOND_SIM_NET_TYPE + " TEXT,"+ SECOND_SIM_NET_STRENGTH + " TEXT,"+ SECOND_SIM_NET_COUNTRY + " TEXT," + SECOND_SIM_OPERATOR + " TEXT,"+ SECOND_SIM_COUNTRY + " TEXT," + WIFI_SSID + " TEXT," +WIFI_SPEED + " TEXT," +WIFI_STRENGTH + " TEXT," + WIFI_FREQUENCY + " TEXT," + USER_LATITUDE + " TEXT," + USER_LONGITUDE + " TEXT," + USER_COUNTRY + " TEXT," + USER_STATE + " TEXT," + USER_CITY + " TEXT," + USER_AREA + " TEXT," + TOWER_LATITUDE + " TEXT," + TOWER_LONGITUDE + " TEXT," + TOWER_ADDRESS + " TEXT," + UPDATED + " TEXT" + ")" ;
        db.execSQL(Create_NetworkLogDetails_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);

    }

    public void addDetails(DataGetterSetterClass dataGetterSetterClass, String updated){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DEVICE_ID, dataGetterSetterClass.get_Device_ID());
        values.put(DATE_AND_TIME, dataGetterSetterClass.get_DateAndTime());
        values.put(MOBILE_PRODUCT, dataGetterSetterClass.get_MobileProductName());
        values.put(MOBILE_MODEL, dataGetterSetterClass.get_MobileModel());
        values.put(NET_OPERATOR, dataGetterSetterClass.get_NetOperator());
        values.put(NET_TYPE, dataGetterSetterClass.get_NetType());
        values.put(NET_STRENGTH, dataGetterSetterClass.get_NetStrength());
        values.put(NET_COUNTRY, dataGetterSetterClass.get_NetCountry());
        values.put(SIM_OPERATOR, dataGetterSetterClass.get_SimOperator());
        values.put(SIM_COUNTRY, dataGetterSetterClass.get_SimCountry());
        values.put(SECOND_SIM_NET_OPERATOR, dataGetterSetterClass.get_SecondSimNetOperator());
        values.put(SECOND_SIM_NET_TYPE, dataGetterSetterClass.get_SecondSimNetType());
        values.put(SECOND_SIM_NET_COUNTRY, dataGetterSetterClass.get_SecondSimNetCountry());
        values.put(SECOND_SIM_NET_STRENGTH, dataGetterSetterClass.get_SecondSimNetStrength());
        values.put(SECOND_SIM_OPERATOR, dataGetterSetterClass.get_SecondSimSimOperator());
        values.put(SECOND_SIM_COUNTRY, dataGetterSetterClass.get_SecondSimSimCountry());
        values.put(WIFI_SSID, dataGetterSetterClass.get_WIFISSID());
        values.put(WIFI_SPEED, dataGetterSetterClass.get_WIFISpeed());
        values.put(WIFI_STRENGTH, dataGetterSetterClass.get_WIFIStrength());
        values.put(WIFI_FREQUENCY, dataGetterSetterClass.get_WIFIFrequency());
        values.put(USER_LATITUDE, dataGetterSetterClass.get_UserLatitude());
        values.put(USER_LONGITUDE, dataGetterSetterClass.get_UserLongitude());
        values.put(USER_COUNTRY, dataGetterSetterClass.get_UserCountry());
        values.put(USER_STATE, dataGetterSetterClass.get_UserState());
        values.put(USER_CITY, dataGetterSetterClass.get_UserCity());
        values.put(USER_AREA, dataGetterSetterClass.get_UserArea());
        values.put(TOWER_LATITUDE, dataGetterSetterClass.get_TowerLatitude());
        values.put(TOWER_LONGITUDE, dataGetterSetterClass.get_TowerLongitude());
        values.put(TOWER_ADDRESS, dataGetterSetterClass.get_TowerAddress());
        values.put(UPDATED, updated);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public DataGetterSetterClass getDetail(int id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, DEVICE_ID, DATE_AND_TIME, MOBILE_PRODUCT, MOBILE_MODEL, NET_OPERATOR, NET_TYPE, NET_STRENGTH, NET_COUNTRY, SIM_OPERATOR, SIM_COUNTRY, SECOND_SIM_NET_OPERATOR, SECOND_SIM_NET_TYPE, SECOND_SIM_NET_STRENGTH, SECOND_SIM_NET_COUNTRY, SECOND_SIM_OPERATOR, SECOND_SIM_COUNTRY, WIFI_SSID, WIFI_SPEED, WIFI_STRENGTH, WIFI_FREQUENCY, USER_LATITUDE, USER_LONGITUDE, USER_COUNTRY, USER_STATE, USER_CITY, USER_AREA, TOWER_LATITUDE, TOWER_LONGITUDE, TOWER_ADDRESS, UPDATED}, KEY_ID + "=?", new String[] {String.valueOf(id)},null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

            DataGetterSetterClass dataGetterSetterClass = new DataGetterSetterClass(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),cursor.getString(14),cursor.getString(15),cursor.getString(16),cursor.getString(17),cursor.getString(18),cursor.getString(19),cursor.getString(20),cursor.getString(21),cursor.getString(22),cursor.getString(23),cursor.getString(24),cursor.getString(25),cursor.getString(26),cursor.getString(27),cursor.getString(28),cursor.getString(29),cursor.getString(30));

            return dataGetterSetterClass;

    }

    public JSONArray getNotUpdatedDetails() {

        JSONArray arr = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, DEVICE_ID, DATE_AND_TIME, MOBILE_PRODUCT, MOBILE_MODEL, NET_OPERATOR, NET_TYPE, NET_STRENGTH, NET_COUNTRY, SIM_OPERATOR, SIM_COUNTRY, SECOND_SIM_NET_OPERATOR, SECOND_SIM_NET_TYPE, SECOND_SIM_NET_STRENGTH, SECOND_SIM_NET_COUNTRY, SECOND_SIM_OPERATOR, SECOND_SIM_COUNTRY, WIFI_SSID, WIFI_SPEED, WIFI_STRENGTH, WIFI_FREQUENCY, USER_LATITUDE, USER_LONGITUDE, USER_COUNTRY, USER_STATE, USER_CITY, USER_AREA, TOWER_LATITUDE, TOWER_LONGITUDE, TOWER_ADDRESS, UPDATED}, UPDATED + "=?", new String[] {"No"},null,null,null);

       if ((cursor != null) && (cursor.getCount() > 0)) {
           cursor.moveToFirst();
           while (!cursor.isAfterLast()) {
               JSONObject postdata = new JSONObject();
               try {
                   postdata.put("Device_ID", cursor.getString(1));
                   postdata.put("Date_And_Time", cursor.getString(2));
                   postdata.put("Mobile_Product", cursor.getString(3));
                   postdata.put("Mobile_Model", cursor.getString(4));
                   postdata.put("Sim1_Net_Operator", cursor.getString(5));
                   postdata.put("Sim1_Net_Type", cursor.getString(6));
                   postdata.put("Sim1_Net_Strength", cursor.getString(7));
                   postdata.put("Sim1_Net_Country", cursor.getString(8));
                   postdata.put("Sim1_Sim_Operator", cursor.getString(9));
                   postdata.put("Sim1_Sim_Country", cursor.getString(10));
                   postdata.put("Sim2_Net_Operator", cursor.getString(11));
                   postdata.put("Sim2_Net_Type", cursor.getString(12));
                   postdata.put("Sim2_Net_Strength", cursor.getString(13));
                   postdata.put("Sim2_Net_Country", cursor.getString(14));
                   postdata.put("Sim2_Sim_Operator", cursor.getString(15));
                   postdata.put("Sim2_Sim_Country", cursor.getString(16));
                   postdata.put("WIFI_SSID", cursor.getString(17));
                   postdata.put("WIFI_Speed", cursor.getString(18));
                   postdata.put("WIFI_Strength", cursor.getString(19));
                   postdata.put("WIFI_Frequency", cursor.getString(20));
                   postdata.put("User_Latitude", cursor.getString(21));
                   postdata.put("User_Longitude", cursor.getString(22));
                   postdata.put("User_Country", cursor.getString(23));
                   postdata.put("User_State", cursor.getString(24));
                   postdata.put("User_City", cursor.getString(25));
                   postdata.put("User_Area", cursor.getString(26));
                   postdata.put("Tower_Latitude", cursor.getString(27));
                   postdata.put("Tower_Longitude", cursor.getString(28));
                   postdata.put("Tower_Address", cursor.getString(29));
               } catch (JSONException e) {
                   e.printStackTrace();
               }

               arr.put(postdata);

               cursor.moveToNext();
           }
           return arr;
       }else {
           arr = null;
           return arr;
       }
    }

    public Cursor getSingleHistoryDetail(String colname){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, DEVICE_ID, DATE_AND_TIME, MOBILE_PRODUCT, MOBILE_MODEL, NET_OPERATOR, NET_TYPE, NET_STRENGTH, NET_COUNTRY, SIM_OPERATOR, SIM_COUNTRY, SECOND_SIM_NET_OPERATOR, SECOND_SIM_NET_TYPE, SECOND_SIM_NET_STRENGTH, SECOND_SIM_NET_COUNTRY, SECOND_SIM_OPERATOR, SECOND_SIM_COUNTRY, WIFI_SSID, WIFI_SPEED, WIFI_STRENGTH, WIFI_FREQUENCY, USER_LATITUDE, USER_LONGITUDE, USER_COUNTRY, USER_STATE, USER_CITY, USER_AREA, TOWER_LATITUDE, TOWER_LONGITUDE, TOWER_ADDRESS, UPDATED}, DATE_AND_TIME + "=?", new String[] {colname},null,null,null);
        return cursor;
    }

    public Cursor getColumnValueList(String column_name){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select  "+column_name+" From " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query,null);
        return cursor;

    }

    public boolean isExist(String date_and_time){

        String whereClause = DATE_AND_TIME+" = ?";
        String[] whereArgs = new String[]{date_and_time};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.query(TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            boolean exist = (cur.getCount() > 0);
            cur.close();
            db.close();
            return exist;
    }

    public List<DataGetterSetterClass> getAllDetails(){

        List<DataGetterSetterClass> dataGetterSetterClassList = new ArrayList<DataGetterSetterClass>();

        String selectquery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()){
            do {
                DataGetterSetterClass dataGetterSetterClass = new DataGetterSetterClass();
                dataGetterSetterClass.set_id(Integer.parseInt(cursor.getString(0)));
                dataGetterSetterClass.set_Device_ID(cursor.getString(1));
                dataGetterSetterClass.set_DateAndTime(cursor.getString(2));
                dataGetterSetterClass.set_MobileProductName(cursor.getString(3));
                dataGetterSetterClass.set_MobileModel(cursor.getString(4));
                dataGetterSetterClass.set_NetOperator(cursor.getString(5));
                dataGetterSetterClass.set_NetType(cursor.getString(6));
                dataGetterSetterClass.set_NetStrength(cursor.getString(7));
                dataGetterSetterClass.set_NetCountry(cursor.getString(8));
                dataGetterSetterClass.set_SimOperator(cursor.getString(9));
                dataGetterSetterClass.set_SimCountry(cursor.getString(10));
                dataGetterSetterClass.set_SecondSimNetOperator(cursor.getString(11));
                dataGetterSetterClass.set_SecondSimNetType(cursor.getString(12));
                dataGetterSetterClass.set_SecondSimNetStrength(cursor.getString(13));
                dataGetterSetterClass.set_SecondSimNetCountry(cursor.getString(14));
                dataGetterSetterClass.set_SecondSimSimOperator(cursor.getString(15));
                dataGetterSetterClass.set_SecondSimSimCountry(cursor.getString(16));
                dataGetterSetterClass.set_WIFISSID(cursor.getString(17));
                dataGetterSetterClass.set_WIFISpeed(cursor.getString(18));
                dataGetterSetterClass.set_WIFIStrength(cursor.getString(19));
                dataGetterSetterClass.set_WIFIFrequency(cursor.getString(20));
                dataGetterSetterClass.set_UserLatitude(cursor.getString(21));
                dataGetterSetterClass.set_UserLongitude(cursor.getString(22));
                dataGetterSetterClass.set_UserCountry(cursor.getString(23));
                dataGetterSetterClass.set_UserState(cursor.getString(24));
                dataGetterSetterClass.set_UserCity(cursor.getString(25));
                dataGetterSetterClass.set_UserArea(cursor.getString(26));
                dataGetterSetterClass.set_TowerLatitude(cursor.getString(27));
                dataGetterSetterClass.set_TowerLongitude(cursor.getString(28));
                dataGetterSetterClass.set_TowerAddress(cursor.getString(29));
                dataGetterSetterClass.set_Updated(cursor.getString(30));

                dataGetterSetterClassList.add(dataGetterSetterClass);


            }while (cursor.moveToNext());

        }

        return dataGetterSetterClassList;
    }

    public Cursor getAllLogDetails(){

        String selectquery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectquery, null);

        return cursor;
    }

    public int getDetailsCount(){

        String countquery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countquery, null);
        cursor.close();

        return cursor.getCount();

    }

    public int updateDetails(String updated, String date_and_time){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(UPDATED, updated);

        return db.update(TABLE_NAME, values, DATE_AND_TIME + "= ?", new String[] {date_and_time});

    }

    public void deleteDetail(DataGetterSetterClass dataGetterSetterClass){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "= ?", new String[] { String.valueOf(dataGetterSetterClass.get_id())});
        db.close();

    }
}
