package com.dalakoti07.android.notestaking.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dalakoti07.android.notestaking.repository.NotesRepository;
import com.dalakoti07.android.notestaking.room.models.NoteModel;
import com.dalakoti07.android.notestaking.utils.ParcelableNote;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditNoteViewModel extends ViewModel {
    private NotesRepository notesRepository;
    private static final String TAG = "EditNoteViewModel";
    private MutableLiveData<String> titleError=new MutableLiveData<>();
    private MutableLiveData<String> descriptionError= new MutableLiveData<>();

    public MutableLiveData<String> getTitleError() {
        return titleError;
    }

    public MutableLiveData<String> getDescriptionError() {
        return descriptionError;
    }

    public EditNoteViewModel(){
        Log.d(TAG, "EditNoteViewModel: created");
        notesRepository=NotesRepository.getNotesRepositoryInstance();
    }

    public boolean validateData(String noteTitle,String noteDescription){
        boolean hasError=false;
        if(noteTitle.length()==0){
            hasError=true;
            titleError.setValue("Title cannot be empty");
        }
        if(noteDescription.length()==0){
            hasError=true;
            descriptionError.setValue("Description cannot be empty");
        }
        if(hasError)
            return false;
        return true;
    }

    public void createNewNote(String noteTitle,String noteDescription){
        notesRepository.addANote(
                new NoteModel(noteTitle,
                        noteDescription,
                        getTheDateAndTime())
        );
    }

    public void updateTheNote(ParcelableNote pNote,String title,String description){
        NoteModel noteModel= new NoteModel(title,description,
                getTheDateAndTime());
        noteModel.setNotesId(pNote.getNotesId());
        noteModel.setArchived(pNote.getArchived());
        notesRepository.updateNote(noteModel);
    }

    private String getTheDateAndTime() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, hh:mm a");
        return dateFormat.format(date);
    }
}
