package com.peppe.crmdoctors.task;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;

import com.peppe.crmdoctors.model.Doctor;
import com.peppe.crmdoctors.provider.Provider;
import com.peppe.crmdoctors.util.Const;

/**
 * Created by peppe on 21/02/2018.
 */

public class DoctorTask extends AsyncTask<Integer, Void, Void> {
    private OnDone mOnDone;
    private ContentValues mContentValues;
    ContentResolver mContentResolver;


    public DoctorTask(OnDone onDone, ContentValues contentValues, ContentResolver contentResolver) {
        mOnDone = onDone;
        mContentValues = contentValues;
        mContentResolver = contentResolver;
    }
 

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mOnDone.onDone();
    }

    @Override
    protected Void doInBackground(Integer... arg0) {
        int what = arg0[0];
        switch (what) {
            case Const.ADD_DOCTOR:
                mContentResolver.insert(Provider.URI_DOCTOR, mContentValues);
                break;
            case Const.UPDATE_DOCTOR:
                mContentResolver.update(Uri.parse(Provider.URI_DOCTOR+"/"+Long.toString(mContentValues.getAsLong(Doctor.COLUMN_ID))), mContentValues, Doctor.COLUMN_ID + " = ?", new String[]{Long.toString(mContentValues.getAsLong(Doctor.COLUMN_ID))});
                break;
            case Const.DELETE_DOCTOR:
                mContentResolver.delete(Uri.parse(Provider.URI_DOCTOR+"/"+Long.toString(mContentValues.getAsLong(Doctor.COLUMN_ID))), Doctor.COLUMN_ID + " = ?", new String[]{Long.toString(mContentValues.getAsLong(Doctor.COLUMN_ID))});
                break;
            }
            return null;

    }
}