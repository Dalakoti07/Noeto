package com.dalakoti07.android.notestaking.viewModels;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dalakoti07.android.notestaking.repository.NotesRepository;
import com.dalakoti07.android.notestaking.room.models.NoteModel;
import com.dalakoti07.android.notestaking.utils.ParcelableNote;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

public class EditNoteViewModel extends ViewModel {

    private NotesRepository notesRepository;
    private static final String TAG = "EditNoteViewModel";
    private MutableLiveData<String> titleError=new MutableLiveData<>();
    private MutableLiveData<String> descriptionError= new MutableLiveData<>();
    private String noteTitle="",noteDescription="";
    private TextWatcher titleTextWatcher,descriptionTextWatcher;

    public TextWatcher getTitleTextWatcher() {
        return titleTextWatcher;
    }

    public TextWatcher getDescriptionTextWatcher() {
        return descriptionTextWatcher;
    }

    public void setTitleString(String title){
        noteTitle=title;
        validateTitle();
    }

    public void validateTitle(){
        if(noteTitle.length()==0){
            titleError.setValue("Title cannot be empty");
        }else
            titleError.setValue("");
    }

    public void setNoteDescription(String description){
        noteDescription=description;
        validateDescription();
    }

    public void validateDescription(){
        if(noteDescription.length()==0){
            descriptionError.setValue("Description cannot be empty");
        }
        if(noteDescription.length()<5){
            descriptionError.setValue("Must be atleast 5 characters");
        }
        else
            descriptionError.setValue("");
    }

    public MutableLiveData<String> getTitleError() {
        return titleError;
    }

    public MutableLiveData<String> getDescriptionError() {
        return descriptionError;
    }

    @Inject
    public EditNoteViewModel(NotesRepository repository){
        //injection done by dagger
        this.notesRepository=repository;
        setUpTextWatchers();
        Log.d(TAG, "EditNoteViewModel: created");
    }

    private void setUpTextWatchers() {
        titleTextWatcher= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setTitleString(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        descriptionTextWatcher= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setNoteDescription(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    public boolean validateData(){
        validateTitle();
        validateDescription();

        if(titleError.getValue().length()==0 && descriptionError.getValue().length()==0)
            return true;
        return false;
    }

    public void createNewNote(){
        notesRepository.addANote(
                new NoteModel(noteTitle,
                        noteDescription,
                        getTheDateAndTime())
        );
    }

    public void updateTheNote(ParcelableNote pNote){
        NoteModel noteModel= new NoteModel(noteTitle,noteDescription,
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
