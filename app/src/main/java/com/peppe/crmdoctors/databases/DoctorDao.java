package com.peppe.crmdoctors.databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.peppe.crmdoctors.model.Doctor;
import com.peppe.crmdoctors.util.Const;

/**
 * Created by peppe on 20/02/2018.
 */

@Dao
public interface DoctorDao {

    /**
     * Counts the number of cheeses in the table.
     *
     * @return The number of cheeses.
     */
    @Query("SELECT COUNT(*) FROM " + Const.TABLE_DOCTOR)
    int count();

    /**
     * Inserts a cheese into the table.
     *
     * @param doctor A new cheese.
     * @return The row ID of the newly inserted cheese.
     */
    @Insert
    long insert(Doctor doctor);

    /**
     * Inserts multiple cheeses into the database
     *
     * @param doctors An array of new cheeses.
     * @return The row IDs of the newly inserted cheeses.
     */
    @Insert
    long[] insertAll(Doctor[] doctors);

    /**
     * Select all cheeses.
     *
     * @return A {@link Cursor} of all the cheeses in the table.
     */
    @Query("SELECT * FROM " + Const.TABLE_DOCTOR)
    Cursor selectAll();

    /**
     * Select a cheese by the ID.
     *
     * @param id The row ID.
     * @return A {@link Cursor} of the selected cheese.
     */
    @Query("SELECT * FROM " + Const.TABLE_DOCTOR + " WHERE " + Doctor.COLUMN_ID + " = :id")
    Cursor selectById(long id);

    @Query("SELECT * FROM " + Const.TABLE_DOCTOR + " WHERE " + Doctor.COLUMN_SURNAME + " LIKE :surname")
    Cursor selectBySurname(String surname);





    @Query("DELETE  FROM doctor_table where name LIKE  :name")
    void deleteByName(String name);


    /**
     * Delete a cheese by the ID.
     *
     * @param id The row ID.
     * @return A number of cheeses deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + Const.TABLE_DOCTOR + " WHERE " + Doctor.COLUMN_ID + " = :id")
    int deleteById(long id);


    /**
     * Update the cheese. The cheese is identified by the row ID.
     *
     * @param doctor The cheese to update.
     * @return A number of cheeses updated. This should always be {@code 1}.
     */
    @Update
    int update(Doctor doctor);

    @Delete
    void deleteDoctor(Doctor doctor);
}