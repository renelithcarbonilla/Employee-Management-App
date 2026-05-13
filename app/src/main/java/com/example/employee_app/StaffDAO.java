package com.example.employee_app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface StaffDAO {
    // Gamiton nato ang 'getAllStaff' imbis 'getAll'
    @Query("SELECT * FROM staff_table")
    List<Staff> getAllStaff();

    // Gamiton nato ang simple nga 'insert', 'update', 'delete'
    @Insert
    void insert(Staff staff);

    @Update
    void update(Staff staff);

    @Delete
    void delete(Staff staff);
}