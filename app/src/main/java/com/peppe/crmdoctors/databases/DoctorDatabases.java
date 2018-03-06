package com.peppe.crmdoctors.databases;

/**
 * Created by peppe on 20/02/2018.
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.peppe.crmdoctors.model.Category;
import com.peppe.crmdoctors.model.Doctor;
import com.peppe.crmdoctors.util.AppUtil;
import com.peppe.crmdoctors.util.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;


/**
 * The Room database.
 */
@Database(entities = {Doctor.class, Category.class}, version = 3)
public abstract class DoctorDatabases extends RoomDatabase {

    /**
     * @return The DAO for the  table.
     */
    @SuppressWarnings("WeakerAccess")
    public abstract DoctorDao doctorDao();

    public abstract CategoryDao categoryDao();

    /**
     * The only instance
     */
    private static DoctorDatabases sInstance;

    /**
     * Gets the singleton instance of SampleDatabase.
     *
     * @param context The context.
     * @return The singleton instance of SampleDatabase.
     */
    public static synchronized DoctorDatabases getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), DoctorDatabases.class, Const.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
            sInstance.populateInitialData();
        }
        return sInstance;
    }

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                DoctorDatabases.class).build();
    }

    /**
     * Inserts the dummy data into the database if it is currently empty.
     */
    private void populateInitialData() {
        if (categoryDao().count() == 0) {
            Category category = new Category();

            beginTransaction();
            try {
                for (int i = 0; i < Category.categories.length; i++) {
                    category.setName(Category.categories[i]);
                    categoryDao().insert(category);
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }

        }

        if (doctorDao().count() == 0) {
            Doctor doc = new Doctor();
            beginTransaction();
            try {

                doc.setName("Giulio");
                doc.setSurname("Frascati");
                doc.setListAddresses(setJsonAddress());
                doc.setListCategories(setJsonCategory(1));
                doc.setPhoneList(setJsonPhoneNumber(3));
                doc.setRelevance(1);
                doc.setColor(AppUtil.getRandomColor());
                doc.setListDays_Hours(setJsonAvaibility());
                doc.setNote("Ciao Giulio");
                doctorDao().insert(doc);

                doc = new Doctor();
                doc.setName("Lalla");
                doc.setSurname("Venturi");
                doc.setListAddresses(setJsonAddress());
                doc.setListCategories(setJsonCategory(1));
                doc.setPhoneList(setJsonPhoneNumber(4));
                doc.setRelevance(3);
                doc.setColor(AppUtil.getRandomColor());
                doc.setListDays_Hours(setJsonAvaibility());
                doc.setNote("CIAO LALLA");
                doctorDao().insert(doc);

                doc = new Doctor();
                doc.setName("Erminio");
                doc.setSurname("Lubaldi");
                doc.setListAddresses(setJsonAddress());
                doc.setListCategories(setJsonCategory(1));
                doc.setPhoneList(setJsonPhoneNumber(2));
                doc.setRelevance(5);
                doc.setColor(AppUtil.getRandomColor());
                doc.setListDays_Hours(setJsonAvaibility());
                doc.setNote("CIAO Erminio");
                doctorDao().insert(doc);

                doc = new Doctor();
                doc.setName("Zaira");
                doc.setSurname("Emil");
                doc.setListAddresses(setJsonAddress());
                doc.setListCategories(setJsonCategory(3));
                doc.setPhoneList(setJsonPhoneNumber(2));
                doc.setRelevance(4);
                doc.setColor(AppUtil.getRandomColor());
                doc.setListDays_Hours(setJsonAvaibility());
                doc.setNote("CIAO Zaira");
                doctorDao().insert(doc);

                doc = new Doctor();
                doc.setName("Tiberio");
                doc.setSurname("Emil");
                doc.setListAddresses(setJsonAddress());
                doc.setListCategories(setJsonCategory(3));
                doc.setPhoneList(setJsonPhoneNumber(2));
                doc.setRelevance(5);
                doc.setColor(AppUtil.getRandomColor());
                doc.setListDays_Hours(setJsonAvaibility());
                doc.setNote("CIAO Tiberio");
                doctorDao().insert(doc);
                Log.d(TAG, "populateInitialData: " + doc.toString());
                setTransactionSuccessful();

            } finally {
                endTransaction();
            }
        }

    }

    private String setJsonPhoneNumber(int i) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        Random random = new Random();
        try {
            for (int j = 0; j < i; j++) {
                jsonObject.put(Const.PHONE, random.nextLong());
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    private String setJsonCategory(int i) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        Random random = new Random();
        ArrayList<Integer> arrayList = new ArrayList<>();
        int r = random.nextInt(3);
        jsonObject = new JSONObject();
        try {
            jsonObject.put(Category.COLUMN_ID,  r );
            jsonObject.put(Category.COLUMN_CATEGORY_NAME, Category.categories[ r ]);
        } catch (JSONException e) {
        }
        jsonArray.put(jsonObject);

        return jsonArray.toString();
}

    /*
            * Diabetologia
        * Medico dello sport
        * Chirurgo
        * Traumatologo
        *
                * */
    private String setJsonAddress() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Const.CITY, "Catania");
            jsonObject.put(Const.STREET, "Via Francesco Cilea");
            jsonObject.put(Const.CIVIC, "119");
            jsonArray.put(jsonObject);
            jsonObject.put(Const.CITY, "Siracusa");
            jsonObject.put(Const.STREET, "Via Teracati");
            jsonObject.put(Const.CIVIC, "119");
            jsonArray.put(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    private String setJsonAvaibility() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 7; i++) {
            jsonArray.put(setDay(i));
        }
        return jsonArray.toString();
    }

    private JSONObject setDay(int i) {

        //   JSONArray jsonArrayDay = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Const.DAY, AppUtil.getDay(i));
            jsonObject.put(Const.MORNING + Const.OPEN, "8:30");
            jsonObject.put(Const.MORNING + Const.CLOSED, "12:30");
            jsonObject.put(Const.AFTERNOON + Const.OPEN, "13:30");
            jsonObject.put(Const.AFTERNOON + Const.CLOSED, "18:30");
            //  jsonArrayDay.put(jsonObject);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


}
