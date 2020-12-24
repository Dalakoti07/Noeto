package com.dalakoti07.android.notestaking.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.dalakoti07.android.notestaking.NotesApp;
import com.dalakoti07.android.notestaking.room.NotesDao;
import com.dalakoti07.android.notestaking.room.NotesDatabase;
import com.dalakoti07.android.notestaking.room.models.NoteModel;

import java.util.ArrayList;
import java.util.List;

public class NotesRepository {
    private static final String TAG = "NotesRepository";
    private static volatile NotesRepository notesRepository;
    private NotesDatabase notesDatabase;
    private NotesDao notesDao;
    private LiveData<List<NoteModel>> allNotes;
    private LiveData<List<NoteModel>> allArchivedNotes;

    private NotesRepository(NotesDatabase database){
        Log.d(TAG, "NotesRepository: created");
        notesDatabase=database;
        notesDao=notesDatabase.notesDao();
        allNotes=notesDao.fetchAllNotes();
        allArchivedNotes=notesDao.fetchArchivedNotes();
    }

    public static NotesRepository getNotesRepositoryInstance(){
        if(notesRepository==null){
            synchronized (NotesRepository.class){
                notesRepository=new NotesRepository(NotesApp.getNotesDatabase());
            }
        }
        return notesRepository;
    }

    public void addANote(NoteModel note){
        NotesDatabase.databaseWriteExecutor.execute(() -> {
            notesDao.insertNote(note);
        });
    }

    public void updateNote(NoteModel note){
        NotesDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                notesDao.updateNote(note);
            }
        });
    }

    public LiveData<List<NoteModel>> getAllNotes(){
        return allNotes;
    }

    public LiveData<List<NoteModel>> getArchivedNotes(){
        return allArchivedNotes;
    }

    public void deleteANote(NoteModel note){
        NotesDatabase.databaseWriteExecutor.execute(() -> notesDao.deleteNote(note));
    }
}
