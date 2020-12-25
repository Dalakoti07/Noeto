package com.dalakoti07.android.notestaking.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.dalakoti07.android.notestaking.repository.NotesRepository;
import com.dalakoti07.android.notestaking.room.models.NoteModel;

import java.util.List;

public class HomeFragmentViewModel extends ViewModel {
    private static final String TAG = "HomeFragmentViewModel";
    private LiveData<List<NoteModel>> currentNotesList;
    private final MutableLiveData<String> toolBarHeading=new MutableLiveData<>("Your Notes");
    private boolean viewingAllNotes=true;

    public boolean isViewingAllNotes(){
        return viewingAllNotes;
    }

    public LiveData<String> getToolBarName(){
        return toolBarHeading;
    }

    private final NotesRepository notesRepository;

    public HomeFragmentViewModel(){
        Log.d(TAG, "HomeFragmentViewModel: created");
        notesRepository=NotesRepository.getNotesRepositoryInstance();
        currentNotesList=notesRepository.getAllNotes();
    }

    public LiveData<List<NoteModel>> getNotesList(){
        return currentNotesList;
    }

    //todo mediator would observer archived and non archived live data and make changes to exposed
    // livedata to fragment as per current option selected
    public LiveData<List<NoteModel>> optionSelected(String option){
        if(option.equals("archived")){
            viewingAllNotes=false;
            toolBarHeading.setValue("Archived notes");
             return currentNotesList=notesRepository.getArchivedNotes();
        }else{
            viewingAllNotes=true;
            toolBarHeading.setValue("Your Notes");
            return currentNotesList=notesRepository.getAllNotes();
        }
    }

    public void toggleTheArchiveStatus(NoteModel note){
        if(note.isArchived==null){
            note.setArchived(true);
        }else{
            note.setArchived(null);
        }
        notesRepository.updateNote(note);
    }

    public void deleteNote(NoteModel note){
        notesRepository.deleteANote(note);
    }
}
