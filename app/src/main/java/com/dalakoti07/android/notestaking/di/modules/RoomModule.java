package com.dalakoti07.android.notestaking.di.modules;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.dalakoti07.android.notestaking.repository.NotesRepository;
import com.dalakoti07.android.notestaking.room.NotesDao;
import com.dalakoti07.android.notestaking.room.NotesDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
    private static final String TAG = "RoomModule";
    private NotesDatabase notesDatabase;

    public RoomModule(Application mApplication) {
        Log.d(TAG, "databaseCreated created ");
        notesDatabase = Room.databaseBuilder(mApplication,
                NotesDatabase.class, "notes_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    NotesDatabase providesRoomDatabase() {
        return notesDatabase;
    }

    @Singleton
    @Provides
    NotesDao provideNotesDao(NotesDatabase demoDatabase) {
        return demoDatabase.notesDao();
    }

    @Singleton
    @Provides
    NotesRepository productRepository(NotesDao productDao) {
        return new NotesRepository(productDao);
    }

}
