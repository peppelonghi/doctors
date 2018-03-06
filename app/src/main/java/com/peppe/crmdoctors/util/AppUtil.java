package com.peppe.crmdoctors.util;


import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ComponentName;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.widget.EditText;

import com.peppe.crmdoctors.model.Address;
import com.peppe.crmdoctors.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by peppe on 21/02/2018.
 */

public class AppUtil {


    public static JSONArray getDefaultJsonAvaibility() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 7; i++) {
            jsonArray.put(setDefaultDay(i));
        }
        return jsonArray;
    }

    public static JSONArray removeElementFromArray(JSONArray jarray, int pos) {

        JSONArray newJsonArray = new JSONArray();
        try {
            for (int i = 0; i < jarray.length(); i++) {
                if (i != pos)
                    newJsonArray.put(jarray.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newJsonArray;

    }

    private static JSONObject setDefaultDay(int i) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Const.DAY, AppUtil.getDay(i));
            jsonObject.put(Const.MORNING + Const.OPEN, Const.DEFAULT_HOUR);
            jsonObject.put(Const.MORNING + Const.CLOSED, Const.DEFAULT_HOUR);
            jsonObject.put(Const.AFTERNOON + Const.OPEN, Const.DEFAULT_HOUR);
            jsonObject.put(Const.AFTERNOON + Const.CLOSED, Const.DEFAULT_HOUR);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONArray concatArray(JSONArray... arrs)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                result.put(arr.get(i));
            }
        }
        return result;
    }

    public static GradientDrawable getDrawableRelevance(int relevance) {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setCornerRadius(0f);
        gd.setStroke(1, Color.DKGRAY);
        switch (relevance) {
            case 1:
                gd.setColor(Color.BLUE);
                break;
            case 2:
                gd.setColor(Color.GREEN);
                break;
            case 3:
                gd.setColor(Color.YELLOW);
                break;
            case 4:
                gd.setColor(Color.RED);
                break;
            default:
                gd.setColor(Color.TRANSPARENT);
                break;
        }
        return gd;
    }

    public static Drawable getDrawableFromColor(String hexColor) {
        Random rnd = new Random();
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setCornerRadius(0f);
        gd.setStroke(1, Color.DKGRAY);
        //gd.setColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        if (hexColor != null)
            gd.setColor(Color.parseColor(hexColor));

        return gd;
    }

    public static String compareHrsAndMintsOnly(Date startSH, Date now, Date stopSH) {
        Calendar startSHCalendar = Calendar.getInstance();
        startSHCalendar.setTime(startSH);
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(now);
        Calendar stopSHCalendar = Calendar.getInstance();
        stopSHCalendar.setTime(stopSH);

        int startSHhour = startSHCalendar.get(Calendar.HOUR_OF_DAY);
        int startSHmin = startSHCalendar.get(Calendar.MINUTE);
        int timeStart = startSHhour * 60 + startSHmin;  //this

        int nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY);
        int nowMin = nowCalendar.get(Calendar.MINUTE);
        int timeNow = nowHour * 60 + nowMin;  //this

        int stopSHhour = stopSHCalendar.get(Calendar.HOUR_OF_DAY);
        int stopSHmin = stopSHCalendar.get(Calendar.MINUTE);
        int timeStop = stopSHhour * 60 + stopSHmin;  //this

        if (timeStart <= timeNow && timeNow <= timeStop) {
            return Const.OPEN;
        } else {
            return Const.CLOSED;
        }
    }


    //
    public static String getRandomColor() {
        Random ra = new Random();
        int r, g, b;
        r = ra.nextInt(255);
        g = ra.nextInt(255);
        b = ra.nextInt(255);

        String hexColor = String.format("#%02x%02x%02x", r, g, b);
        return hexColor;
    }

    public static String[] getListDay_HoursFromList(String listDays_hours) {
        listDays_hours.split(Const.DIVIDER);
        return listDays_hours.split(Const.DIVIDER);
    }

    public static String getDay(final int position) {
        switch (position) {
            case 0:
                return "Lunedì";
            case 1:
                return "Martedì";
            case 2:
                return "Mercoledì";
            case 3:
                return "Giovedì";
            case 4:
                return "Venerdì";
            case 5:
                return "Sabato";
            case 6:
                return "Domenica";
            default:
                return "";
        }
    }


    public static JSONObject getCurrentAvaibility(JSONArray jsonArray) {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("it"));
        String[] weekdays = dfs.getWeekdays();
        String currentDay;
        currentDay = weekdays[dayOfWeek];
        JSONObject j;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                j = jsonArray.getJSONObject(i);
                if (j.getString(Const.DAY).equalsIgnoreCase(currentDay))
                    return j;
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return null;
    }

    public static String checkAvaibility(JSONObject jsonObject) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String open;
        String closed;
        String constT;
        if (hour >= 13) {
            constT = Const.AFTERNOON;
        } else {
            constT = Const.MORNING;
        }
        try {
            open = jsonObject.getString(constT + Const.OPEN);
            closed = jsonObject.getString(constT + Const.CLOSED);
            if (open.equalsIgnoreCase(Const.DEFAULT_HOUR) && closed.equalsIgnoreCase(Const.DEFAULT_HOUR)) {
                return Const.NOT_AVAIBLE;
            } else {
                Date now = new Date();
                DateFormat format = new SimpleDateFormat("HH:mm");
                try {
                    Date startSH = format.parse(open);
                    Date stopSH = format.parse(closed);
                    return compareHrsAndMintsOnly(startSH, now, stopSH);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    //PRIORITY 1 = blue;
    // ....... 2 = green;
    //........ 3 = yellow;
    //........ 4 = red;


    // Diabetologia  = 1
    // Medico dello sport   = 2
    //Traumatologo = 3

}
