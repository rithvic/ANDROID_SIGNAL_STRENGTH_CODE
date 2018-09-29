package com.quadrobay.qbayApps.networkproject.DataBaseClasses;

/**
 * Created by benchmark on 07/10/17.
 */

public class DataGetterSetterClass {

    int _id;
    String _Device_ID;
    String _DateAndTime;
    String _MobileProductName;
    String _MobileModel;
    String _NetOperator;
    String _NetType;
    String _NetStrength;
    String _NetCountry;
    String _SimOperator;
    String _SimCountry;
    String _WIFISSID;
    String _WIFISpeed;
    String _WIFIStrength;
    String _WIFIFrequency;
    String _SecondSimNetOperator;
    String _SecondSimNetType;
    String _SecondSimNetStrength;
    String _SecondSimNetCountry;
    String _SecondSimSimOperator;
    String _SecondSimSimCountry;
    String _UserLatitude;
    String _UserLongitude;
    String _UserCountry;
    String _UserState;
    String _UserCity;
    String _UserArea;
    String _TowerLatitude;
    String _TowerLongitude;
    String _TowerAddress;
    String _Updated;

    public DataGetterSetterClass() {

    }

    public DataGetterSetterClass(int id, String deviceid, String dateandtime,String mobileProductName, String mobilemodel, String netoperator, String nettype, String netstrength, String netcountry, String simoperator, String simcountry, String secondsimnetoperator, String secondsimnettype, String secondsimstrength, String secondsimnetcountry, String secondsimoperator, String secondsimcountry, String wifissid, String wifispeed, String wifistrength, String wififrequency, String userlatitude, String userlongitude, String usercountry, String userstate, String usercity, String userarea, String towerlatitude, String towerlongitude, String toweraddress, String updated){

        this._id = id;
        this._Device_ID = deviceid;
        this._DateAndTime = dateandtime;
        this._MobileProductName = mobileProductName;
        this._MobileModel = mobilemodel;
        this._NetOperator = netoperator;
        this._SecondSimNetOperator = secondsimnetoperator;
        this._NetType = nettype;
        this._SecondSimNetType = secondsimnettype;
        this._NetStrength = netstrength;
        this._SecondSimNetStrength = secondsimstrength;
        this._NetCountry = netcountry;
        this._SecondSimNetCountry = secondsimnetcountry;
        this._SimOperator = simoperator;
        this._SecondSimSimOperator = secondsimoperator;
        this._SimCountry = simcountry;
        this._SecondSimSimCountry = secondsimcountry;
        this._WIFISSID = wifissid;
        this._WIFISpeed = wifispeed;
        this._WIFIStrength = wifistrength;
        this._WIFIFrequency = wififrequency;
        this._UserLatitude = userlatitude;
        this._UserLongitude = userlongitude;
        this._UserCountry = usercountry;
        this._UserState = userstate;
        this._UserCity = usercity;
        this._UserArea = userarea;
        this._TowerLatitude = towerlatitude;
        this._TowerLongitude = towerlongitude;
        this._TowerAddress = toweraddress;
        this._Updated = updated;

    }

    public DataGetterSetterClass(String deviceid, String dateandtime, String mobileProductName, String mobileModel, String netoperator, String nettype, String netstrength, String netcountry, String simoperator, String simcountry, String secondsimnetoperator, String secondsimnettype, String secondsimstrength,  String secondsimnetcountry, String secondsimoperator, String secondsimcountry, String wifissid, String wifispeed, String wifistrength, String wififrequency, String userlatitude, String userlongitude, String usercountry, String userstate, String usercity, String userarea, String towerlatitude, String towerlongitude, String toweraddress, String updated){

        this._Device_ID = deviceid;
        this._DateAndTime = dateandtime;
        this._MobileProductName = mobileProductName;
        this._MobileModel = mobileModel;
        this._NetOperator = netoperator;
        this._SecondSimNetOperator = secondsimnetoperator;
        this._NetType = nettype;
        this._SecondSimNetType = secondsimnettype;
        this._NetCountry = netcountry;
        this._SecondSimNetCountry = secondsimnetcountry;
        this._NetStrength = netstrength;
        this._SecondSimNetStrength = secondsimstrength;
        this._SimOperator = simoperator;
        this._SecondSimSimOperator = secondsimoperator;
        this._SimCountry = simcountry;
        this._SecondSimSimCountry = secondsimcountry;
        this._WIFISSID = wifissid;
        this._WIFISpeed = wifispeed;
        this._WIFIStrength = wifistrength;
        this._WIFIFrequency = wififrequency;
        this._UserLatitude = userlatitude;
        this._UserLongitude = userlongitude;
        this._UserCountry = usercountry;
        this._UserState = userstate;
        this._UserCity = usercity;
        this._UserArea = userarea;
        this._TowerLatitude = towerlatitude;
        this._TowerLongitude = towerlongitude;
        this._TowerAddress = toweraddress;
        this._Updated = updated;

    }

    public int get_id(){
        return this._id;
    }

    public String get_Device_ID(){
        return this._Device_ID;
    }

    public String get_DateAndTime(){
        return this._DateAndTime;
    }

    public String get_MobileProductName(){
        return this._MobileProductName;
    }

    public String get_MobileModel(){
        return this._MobileModel;
    }

    public String get_NetOperator(){
        return this._NetOperator;
    }

    public String get_SecondSimNetOperator() {
        return  this._SecondSimNetOperator;
    }

    public String get_NetType(){
        return this._NetType;
    }

    public  String get_SecondSimNetType() {
        return this._SecondSimNetType;
    }

    public String get_NetStrength(){
        return this._NetStrength;
    }

    public  String get_SecondSimNetStrength() {
        return  this._SecondSimNetStrength;
    }

    public String get_NetCountry(){
        return this._NetCountry;
    }

    public String get_SecondSimNetCountry() {
        return this._SecondSimNetCountry;
    }

    public String get_SimOperator(){
        return this._SimOperator;
    }

    public String get_SecondSimSimOperator() {
        return this._SecondSimSimOperator;
    }

    public String get_SimCountry(){
        return this._SimCountry;
    }

    public String get_SecondSimSimCountry() {
        return this._SecondSimSimCountry;
    }

    public String get_WIFISSID(){
        return this._WIFISSID;
    }

    public String get_WIFISpeed(){
        return this._WIFISpeed;
    }

    public String get_WIFIStrength(){
        return this._WIFIStrength;
    }

    public String get_WIFIFrequency(){
        return this._WIFIFrequency;
    }

    public String get_UserLatitude(){
        return this._UserLatitude;
    }

    public String get_UserLongitude(){
        return this._UserLongitude;
    }

    public String get_UserArea(){
        return this._UserArea;
    }

    public String get_UserCity(){
        return this._UserCity;
    }

    public String get_UserState() {
        return this._UserState;
    }

    public String get_UserCountry() {
        return this._UserCountry;
    }

    public String get_TowerLatitude(){
        return this._TowerLatitude;
    }

    public String get_TowerLongitude(){
        return this._TowerLongitude;
    }

    public String get_TowerAddress(){
        return this._TowerAddress;
    }

    public String get_Updated(){
        return this._Updated;
    }

    public void set_id(int id){
        this._id = id;
    }

    public void set_Device_ID(String device_id){
        this._Device_ID = device_id;
    }

    public void set_DateAndTime(String dateAndTime){
        this._DateAndTime = dateAndTime;
    }

    public void set_MobileProductName(String mobileproductname){
        this._MobileProductName = mobileproductname;
    }

    public void set_MobileModel(String mobilemodel){
        this._MobileModel = mobilemodel;
    }

    public void set_NetOperator(String netOperator){
        this._NetOperator = netOperator;
    }

    public void set_SecondSimNetOperator(String secondSimNetOperator) {
        this._SecondSimNetOperator = secondSimNetOperator;
    }

    public void set_NetType(String netType){
        this._NetType = netType;
    }

    public void set_SecondSimNetType(String secondSimNetType) {
        this._SecondSimNetType = secondSimNetType;
    }

    public void set_NetStrength(String netStrength){
        this._NetStrength = netStrength;
    }

    public void set_SecondSimNetStrength(String secondSimNetStrength) {
        this._SecondSimNetStrength = secondSimNetStrength;
    }

    public void set_NetCountry(String netCountry){
        this._NetCountry = netCountry;
    }

    public void set_SecondSimNetCountry(String secondSimNetCountry) {
        this._SecondSimNetCountry = secondSimNetCountry;
    }

    public void set_SimOperator(String simOperator){
        this._SimOperator = simOperator;
    }

    public void set_SecondSimSimOperator(String secondSimSimOperator) {
        this._SecondSimSimOperator = secondSimSimOperator;
    }

    public void set_SimCountry(String simCountry){
        this._SimCountry = simCountry;
    }

    public void set_SecondSimSimCountry(String secondSimSimCountry) {
        this._SecondSimSimCountry = secondSimSimCountry;
    }

    public void set_WIFISSID(String wifissid){
        this._WIFISSID = wifissid;
    }

    public void set_WIFISpeed(String wifiSpeed){
        this._WIFISpeed = wifiSpeed;
    }

    public void set_WIFIStrength(String wifiStrength){
        this._WIFIStrength = wifiStrength;
    }

    public void set_WIFIFrequency(String wifiFrequency){
        this._WIFIFrequency = wifiFrequency;
    }

    public void set_UserLatitude(String latitude) {
        this._UserLatitude = latitude;
    }

    public void set_UserLongitude(String longitude) {
        this._UserLongitude = longitude;
    }

    public void set_UserArea(String userArea){
        this._UserArea = userArea;
    }

    public void set_UserCity(String userCity) {
        this._UserCity = userCity;
    }

    public void set_UserState(String userState) {
        this._UserState = userState;
    }

    public void set_UserCountry(String userCountry) {
        this._UserCountry = userCountry;
    }

    public void set_TowerLatitude(String towerLatitude){
        this._TowerLatitude = towerLatitude;
    }

    public void set_TowerLongitude(String towerLongitude){
        this._TowerLongitude = towerLongitude;
    }

    public void set_TowerAddress(String towerAddress){
        this._TowerAddress = towerAddress;
    }

    public void set_Updated(String updated){
        this._Updated = updated;
    }

}
