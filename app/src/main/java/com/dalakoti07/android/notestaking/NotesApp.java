package com.dalakoti07.android.notestaking;

import android.app.Application;
import android.content.Context;

import com.dalakoti07.android.notestaking.room.NotesDatabase;

public class NotesApp extends Application {
    private static NotesApp context;
    private static NotesDatabase notesDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        notesDatabase=NotesDatabase.getNotesDatabase(context);
    }

    public static NotesDatabase getNotesDatabase(){
        return notesDatabase;
    }

    public static Context getAppContext(){
        return context;
    }
}
