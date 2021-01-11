package com.dalakoti07.android.notestaking.room;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dalakoti07.android.notestaking.room.models.NoteModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NoteModel.class},version = 4)
public abstract class NotesDatabase extends RoomDatabase {
    private static final String TAG = "NotesDatabase";
    public abstract NotesDao notesDao();

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //object creation would be handled by dagger see room-module
}
