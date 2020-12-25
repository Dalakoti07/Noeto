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

import javax.inject.Inject;

public class HomeFragmentViewModel extends ViewModel {
    private static final String TAG = "HomeFragmentViewModel";

    private LiveData<List<NoteModel>> allBooks= new MutableLiveData<>();
    private LiveData<List<NoteModel>> archivedBooks= new MutableLiveData<>();

    private final MediatorLiveData<List<NoteModel>> exposedNotes= new MediatorLiveData<>();

    private final MutableLiveData<String> toolBarHeading=new MutableLiveData<>("Your Notes");
    private boolean viewingAllNotes=true;

    public boolean isViewingAllNotes(){
        return viewingAllNotes;
    }

    public LiveData<String> getToolBarName(){
        return toolBarHeading;
    }

    private NotesRepository notesRepository;

    @Inject
    public HomeFragmentViewModel(NotesRepository repository){
        this.notesRepository=repository;
        Log.d(TAG, "HomeFragmentViewModel: created");
        //repo provided by dagger
        allBooks=notesRepository.getAllNotes();
        archivedBooks=notesRepository.getArchivedNotes();

        exposedNotes.addSource(allBooks, new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(List<NoteModel> noteModels) {
                // inform only if we are viewing all notes
                if(viewingAllNotes){
                    exposedNotes.setValue(noteModels);
                }
            }
        });
        exposedNotes.addSource(archivedBooks, new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(List<NoteModel> noteModels) {
                if(!viewingAllNotes){
                    exposedNotes.setValue(noteModels);
                }
            }
        });

    }

    public LiveData<List<NoteModel>> getNotesList(){
        return exposedNotes;
    }

    // credits https://proandroiddev.com/mediatorlivedata-to-the-rescue-5d27645b9bc3
    public void optionSelected(String option){
        if(option.equals("archived")){
            viewingAllNotes=false;
            toolBarHeading.setValue("Archived notes");
            exposedNotes.setValue(archivedBooks.getValue());
        }else{
            viewingAllNotes=true;
            toolBarHeading.setValue("Your Notes");
            exposedNotes.setValue(allBooks.getValue());
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
