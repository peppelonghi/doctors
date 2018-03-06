package com.peppe.crmdoctors.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;

import com.peppe.crmdoctors.util.Const;

/**
 * Created by peppe on 20/02/2018.
 */
@Entity(tableName = Const.TABLE_CATEGORIES)

public class Category {
    public static final String COLUMN_CATEGORY_NAME = "categoryName";
    public static final String COLUMN_ID = BaseColumns._ID;
     @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public int id;

    @ColumnInfo(name = COLUMN_CATEGORY_NAME)
    public String categoryName;

    public String getName() {
        return categoryName;
    }

    public void setName(String name) {
        this.categoryName = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static final String[] categories = {
            "Diabetologia", "Medico dello sport", "Chirurgo", "Traumatologo"
    };

    /*
    * Diabetologia
    * Medico dello sport
    * Chirurgo
    * Traumatologo
    *
    * */


}
