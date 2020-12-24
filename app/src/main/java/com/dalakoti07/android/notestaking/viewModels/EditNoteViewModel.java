package com.dalakoti07.android.notestaking.viewModels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.dalakoti07.android.notestaking.repository.NotesRepository;
import com.dalakoti07.android.notestaking.room.models.NoteModel;
import com.dalakoti07.android.notestaking.utils.ParcelableNote;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//todo handle validation
public class EditNoteViewModel extends ViewModel {
    private NotesRepository notesRepository;
    private static final String TAG = "EditNoteViewModel";

    public EditNoteViewModel(){
        Log.d(TAG, "EditNoteViewModel: created");
        notesRepository=NotesRepository.getNotesRepositoryInstance();
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
