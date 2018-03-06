package com.peppe.crmdoctors.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import com.peppe.crmdoctors.databases.CategoryDao;
import com.peppe.crmdoctors.databases.DoctorDao;
import com.peppe.crmdoctors.databases.DoctorDatabases;
import com.peppe.crmdoctors.model.Doctor;
import com.peppe.crmdoctors.util.Const;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by peppe on 20/02/2018.
 */

public class Provider extends ContentProvider {

    /**
     * The authority of this content provider.
     */
    public static final String AUTHORITY = "com.peppe.android.contentprovidersample.provider";

    /**
     * The URI for the Cheese table.
     */
    public static final Uri URI_DOCTOR = Uri.parse(
            "content://" + AUTHORITY + "/" + Const.TABLE_DOCTOR);

    public static final Uri URI_CATEGORY = Uri.parse(
            "content://" + AUTHORITY + "/" + Const.TABLE_CATEGORIES);
    /**
     * The match code for some items in the  table.
     */
    private static final int CODE_DOCTOR_DIR = 1;
    /**
     * The match code for an item in the  table.
     */
    public static final int CODE_DOCTOR_ITEM = 2;
    private static final int CODE_DOCTOR_SURNAME = 3;
    private static final int CODE_CATEGORIES_DIR = 4;


    /**
     * The URI matcher.
     */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, Const.TABLE_DOCTOR, CODE_DOCTOR_DIR);
        MATCHER.addURI(AUTHORITY, Const.TABLE_DOCTOR + "/*", CODE_DOCTOR_ITEM);
        MATCHER.addURI(AUTHORITY, Const.TABLE_CATEGORIES, CODE_CATEGORIES_DIR);

    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_DOCTOR_DIR || code == CODE_DOCTOR_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            DoctorDao doctorDao = DoctorDatabases.getInstance(context).doctorDao();
            final Cursor cursor;
            if (code == CODE_DOCTOR_DIR) {
                cursor = doctorDao.selectAll();
            } else {
                if (projection[0].equalsIgnoreCase(Doctor.COLUMN_SURNAME)) {
                    Log.d(TAG, "query: ");
                    cursor = doctorDao.selectBySurname(selectionArgs[0]);
                } else
                    cursor = doctorDao.selectById(ContentUris.parseId(uri));

            }
            cursor.setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        } else {
            if (code == CODE_CATEGORIES_DIR) {
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                CategoryDao categoryDao = DoctorDatabases.getInstance(context).categoryDao();
                final Cursor cursor;
                if (code == CODE_CATEGORIES_DIR) {
                    cursor = categoryDao.selectAll();
                    cursor.setNotificationUri(context.getContentResolver(), uri);
                    return cursor;
                }

            }
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_DOCTOR_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + Const.TABLE_DOCTOR;
            case CODE_DOCTOR_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + Const.TABLE_DOCTOR;
            case CODE_DOCTOR_SURNAME:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + Const.TABLE_DOCTOR;
            case CODE_CATEGORIES_DIR:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + Const.TABLE_CATEGORIES;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_DOCTOR_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = DoctorDatabases.getInstance(context).doctorDao()
                        .insert(Doctor.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_DOCTOR_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_DOCTOR_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_DOCTOR_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = DoctorDatabases.getInstance(context).doctorDao()
                        .deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_DOCTOR_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_DOCTOR_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final Doctor doctor = Doctor.fromContentValues(values);
                doctor.id = ContentUris.parseId(uri);
                final int count = DoctorDatabases.getInstance(context).doctorDao()
                        .update(doctor);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final DoctorDatabases database = DoctorDatabases.getInstance(context);
        database.beginTransaction();
        try {
            final ContentProviderResult[] result = super.applyBatch(operations);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        switch (MATCHER.match(uri)) {
            case CODE_DOCTOR_DIR:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final DoctorDatabases database = DoctorDatabases.getInstance(context);
                final Doctor[] cheeses = new Doctor[valuesArray.length];
                for (int i = 0; i < valuesArray.length; i++) {
                    cheeses[i] = Doctor.fromContentValues(valuesArray[i]);
                }
                return database.doctorDao().insertAll(cheeses).length;
            case CODE_DOCTOR_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

}
