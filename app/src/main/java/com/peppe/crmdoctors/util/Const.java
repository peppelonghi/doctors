package com.peppe.crmdoctors.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by peppe on 21/02/2018.
 */

public class Const {
    public static final String DB_NAME = "db";
    public static final String TABLE_DOCTOR = "doctor_table";
    public static final String TABLE_CATEGORIES = "category_table";
    public static final String DIVIDER = "//";
    public static final String MORNING = "MORNING";
    public static final String AFTERNOON = "AFTERNOON";
    public static final int ADD_DOCTOR = 0;
    public static final int UPDATE_DOCTOR = 1;
    public static final int DELETE_DOCTOR = 2;

    public static final String DOCTOR = "DOCTOR";
    public static final String PHONE = "PHONE";
    public static String CITY = "CITY";
    public static String STREET = "STREET";
    public static String CIVIC = "CIVIC";

    public static String EMPTY = "EMPTY";
    public static int DETAILS_ACTIVITY = 0;
    public static int ADD_MODOFY_ACTIVITY = 1;
    public static String DEFAULT_HOUR = "00:00";
    public static String DEFAULT_HOUR_DAY = Const.MORNING + ": 00:00-00:00," + Const.AFTERNOON + ": 00:00-00:00" + Const.DIVIDER +
            Const.MORNING + ": 00:00-00:00," + Const.AFTERNOON + ": 00:00-00:00" + Const.DIVIDER +
            Const.MORNING + ": 00:00-00:00," + Const.AFTERNOON + ": 00:00-00:00" + Const.DIVIDER +
            Const.MORNING + ": 00:00-00:00," + Const.AFTERNOON + ": 00:00-00:00" + Const.DIVIDER +
            Const.MORNING + ": 00:00-00:00," + Const.AFTERNOON + ": 00:00-00:00" + Const.DIVIDER +
            Const.MORNING + ": 00:00-00:00," + Const.AFTERNOON + ": 00:00-00:00" + Const.DIVIDER;


  public static final int FILTER = 1 ;


    public static final String NOT_AVAIBLE = "NOT_AVAIBLE";
    public static String DAY = "DAY";
    public static final String OPEN = "OPEN";
    public static final String CLOSED = "CLOSED";


    public static String LAT = "LATITUDE";
    public static String LONG = "LONGITUDE";
    public static String MONTH = "MONTH";
    public static String YEAR = "YEAR";
    public static String HOUR = "HOUR";
    public static String MINUTE = "MINUTE";

}
