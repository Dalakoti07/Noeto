package com.dalakoti07.android.notestaking.room;

import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dalakoti07.android.notestaking.room.models.NoteModel;

import java.util.List;

@Dao
public interface NotesDao {
    @Insert
    void insertNote(NoteModel note);

    @Update
    void updateNote(NoteModel note);

    @Delete
    void deleteNote(NoteModel note);

    @Query("SELECT * from notes where archived is NULL and  userId =:uId")
    LiveData<List<NoteModel>> fetchAllNotes(String uId);

    @Query("SELECT * from notes where archived is NOT NULL and userId=:userId")
    LiveData<List<NoteModel>> fetchArchivedNotes(String userId);
}
