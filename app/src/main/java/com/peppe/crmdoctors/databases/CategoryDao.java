package com.peppe.crmdoctors.databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.peppe.crmdoctors.model.Category;
import com.peppe.crmdoctors.model.Doctor;
import com.peppe.crmdoctors.util.Const;

import java.util.List;

/**
 * Created by peppe on 20/02/2018.
 */
@Dao
public interface CategoryDao {

    @Query("SELECT COUNT(*) FROM " + Const.TABLE_CATEGORIES)
    int count();

    /**
     * Inserts a cheese into the table.
     *
     * @param category A new cheese.
     * @return The row ID of the newly inserted cheese.
     */
    @Insert
    long insert(Category category);

    /**
     * Inserts multiple cheeses into the database
     *
     * @param categories An array of new cheeses.
     * @return The row IDs of the newly inserted cheeses.
     */
    @Insert
    long[] insertAll(Category[] categories);

    /**
     * Select all cheeses.
     *
     * @return A {@link Cursor} of all the cheeses in the table.
     */
    @Query("SELECT * FROM " +  Const.TABLE_CATEGORIES)
    Cursor selectAll();

    /**
     * Select a cheese by the ID.
     *
     * @param id The row ID.
     * @return A {@link Cursor} of the selected cheese.
     */
    @Query("SELECT * FROM " + Const.TABLE_CATEGORIES + " WHERE " + BaseColumns._ID + " = :id")
    Cursor selectById(long id);

    /**
     * Delete a cheese by the ID.
     *
     * @param id The row ID.
     * @return A number of cheeses deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + Const.TABLE_CATEGORIES + " WHERE " + Doctor.COLUMN_ID + " = :id")
    int deleteById(long id);

    /**
     * Update the cheese. The cheese is identified by the row ID.
     *
     * @param category The cheese to update.
     * @return A number of cheeses updated. This should always be {@code 1}.
     */
    @Update
    int update(Category category);


}
