package com.peppe.crmdoctors.model;

/**
 * Created by peppe on 20/02/2018.
 */

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.util.Log;

import com.peppe.crmdoctors.util.Const;

import static android.content.ContentValues.TAG;

@Entity(tableName = Const.TABLE_DOCTOR)
public class Doctor implements Parcelable{
    /**
     * The categoryName of the ID column.
     */
    public static final String COLUMN_ID = BaseColumns._ID;

    /**
     * The categoryName of the categoryName column.
     */
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";

    public static final String COLUMN_CATEGORIES = "listCategories";
    public static final String COLUMN_ADDRESSES = "listAddresses";
    public static final String COLUMN_DAY_HOURS = "listDays_Hours";
    public static final String COLUMN_RELEVANCE = "relevance ";
    public static final String COLUMN_NUMB_VISIT = "numbVisit ";
    public static final String COLUMN_PHONE = "phone ";
    public static final String COLUMN_COLOR = "color ";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_APPOINTMENT = "appointment";
    public static final String COLUMN_DATE_LAST_VISIT = "last_visit";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;
    @ColumnInfo(name = COLUMN_DATE_LAST_VISIT)
    public String last_visit;
    @ColumnInfo(name = COLUMN_APPOINTMENT)
    public String appointment;
    @ColumnInfo(name = COLUMN_NAME)
    public String name;

    @ColumnInfo(name = COLUMN_SURNAME)
    public String surname;


    @ColumnInfo(name = COLUMN_RELEVANCE)
    public int relevance;


    @ColumnInfo(name = COLUMN_CATEGORIES)
    public String listCategories;


    @ColumnInfo(name = COLUMN_ADDRESSES)
    public String listAddresses;

    @ColumnInfo(name = COLUMN_DAY_HOURS)
    public String listDays_Hours;

    @ColumnInfo(name = COLUMN_NUMB_VISIT)
    public int numbVisit;

    @ColumnInfo(name = COLUMN_PHONE)
    public String phoneList;
    @ColumnInfo(name = COLUMN_COLOR)
    public String color;

    @ColumnInfo(name = COLUMN_NOTE)
    public String note;

    public Doctor(){}

    protected Doctor(Parcel in) {
        id = in.readLong();
        name = in.readString();
        surname = in.readString();
        relevance = in.readInt();
        listCategories = in.readString();
        listAddresses = in.readString();
        listDays_Hours = in.readString();
        numbVisit = in.readInt();
        phoneList = in.readString();
        color = in.readString();
        note = in.readString();
        appointment = in.readString();
        last_visit = in.readString();

    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public String getLast_visit() {
        return last_visit;
    }

    public void setLast_visit(String last_visit) {
        this.last_visit = last_visit;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getListCategories() {
        return listCategories;
    }

    public void setListCategories(String listCategories) {
        this.listCategories = listCategories;
    }

    public String getListAddresses() {
        return listAddresses;
    }

    public void setListAddresses(String listAddresses) {
        this.listAddresses = listAddresses;
    }

    public String getListDays_Hours() {
        return listDays_Hours;
    }

    public void setListDays_Hours(String listDays_Hours) {
        this.listDays_Hours = listDays_Hours;
    }

    public int getRelevance() {
        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    public int getNumbVisit() {
        return numbVisit;
    }

    public void setNumbVisit(int numbVisit) {
        this.numbVisit = numbVisit;
    }

    public String getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(String phoneList) {
        this.phoneList = phoneList;
    }


    public  String getColor() {
        return color;
    }

    public void setColor( String color) {
        this.color = color;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static Doctor fromContentValues(ContentValues values) {
        final Doctor doctor = new Doctor();
        if (values.containsKey(COLUMN_ID)) {
            doctor.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_NAME)) {
            doctor.name = values.getAsString(COLUMN_NAME);
        }
        if (values.containsKey(COLUMN_SURNAME)) {
            doctor.surname = values.getAsString(COLUMN_SURNAME);
        }
        if (values.containsKey(COLUMN_CATEGORIES)) {
            doctor.listCategories = values.getAsString(COLUMN_CATEGORIES);
        }
        if (values.containsKey(COLUMN_ADDRESSES)) {
            doctor.listAddresses = values.getAsString(COLUMN_ADDRESSES);
        }
        if (values.containsKey(COLUMN_DAY_HOURS)) {
            doctor.listDays_Hours = values.getAsString(COLUMN_DAY_HOURS);
        }
        if (values.containsKey(COLUMN_RELEVANCE)) {
            doctor.relevance = values.getAsInteger(COLUMN_RELEVANCE);
        }
        if (values.containsKey(COLUMN_NUMB_VISIT)) {
            doctor.numbVisit = values.getAsInteger(COLUMN_NUMB_VISIT);
        }
        if (values.containsKey(COLUMN_PHONE)) {
            doctor.phoneList = values.getAsString(COLUMN_PHONE);
        } if (values.containsKey(COLUMN_COLOR)) {
            doctor.color = values.getAsString(COLUMN_COLOR);
        }
        if (values.containsKey(COLUMN_NOTE)) {
            doctor.note = values.getAsString(COLUMN_NOTE);
        }
        if (values.containsKey(COLUMN_APPOINTMENT)) {
            doctor.appointment = values.getAsString(COLUMN_APPOINTMENT);
        }
        if (values.containsKey(COLUMN_DATE_LAST_VISIT)) {
            doctor.last_visit = values.getAsString(COLUMN_DATE_LAST_VISIT);
        }
        Log.d(TAG, "contentValuesFromDoctor: "+doctor.toString());

        return doctor;
    }
    public ContentValues contentValuesFromDoctor(Doctor doctor){
        ContentValues values = new ContentValues();
        values.put(Doctor.COLUMN_ID, doctor.getId());
        values.put(Doctor.COLUMN_NAME, doctor.getName());
        values.put(Doctor.COLUMN_SURNAME, doctor.getSurname());
        values.put(Doctor.COLUMN_ADDRESSES, doctor.getListAddresses());
        values.put(Doctor.COLUMN_CATEGORIES, doctor.getListCategories());
        values.put(Doctor.COLUMN_DAY_HOURS, doctor.getListDays_Hours());
        values.put(Doctor.COLUMN_NUMB_VISIT, doctor.getNumbVisit());
        values.put(Doctor.COLUMN_RELEVANCE, doctor.getRelevance());
        values.put(Doctor.COLUMN_PHONE, doctor.getPhoneList());
        values.put(Doctor.COLUMN_COLOR, doctor.getColor());
        values.put(Doctor.COLUMN_NOTE, doctor.getNote());
        values.put(Doctor.COLUMN_APPOINTMENT, doctor.getAppointment());
        values.put(Doctor.COLUMN_DATE_LAST_VISIT, doctor.getLast_visit());
        Log.d(TAG, "contentValuesFromDoctor: "+doctor.toString());
        return values;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(surname);
        parcel.writeInt(relevance);
        parcel.writeString(listCategories);
        parcel.writeString(listAddresses);
        parcel.writeString(listDays_Hours);
        parcel.writeInt(numbVisit);
        parcel.writeString(phoneList);
        parcel.writeString(color);
        parcel.writeString(note);
        parcel.writeString(appointment);
        parcel.writeString(last_visit);

    }

    @Override
    public String toString() {
         return "Name: '" + this.name + "', relevance: '" + this.relevance + "', listCategories: '" + this.listCategories +
                 "listAddresses: '" + this.listAddresses +"listDays_Hours: '" + this.listDays_Hours +
                 "numbVisit: '" + this.numbVisit  +"'\n"+
                 "phoneList: '" + this.phoneList  +"'\n"+
                 "color: '" + this.color +"'\n"+
                 "surname: '" + this.surname +"'\n"+
                 "note: '" + this.note +"'\n"+
                 "last visit: "+this.last_visit+
                 "appointment: "+this.appointment;

    }
}