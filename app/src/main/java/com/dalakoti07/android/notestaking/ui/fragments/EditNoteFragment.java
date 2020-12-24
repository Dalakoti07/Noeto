package com.dalakoti07.android.notestaking.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.dalakoti07.android.notestaking.NotesApp;
import com.dalakoti07.android.notestaking.databinding.FragmentEditNoteBinding;
import com.dalakoti07.android.notestaking.room.NotesDao;
import com.dalakoti07.android.notestaking.room.NotesDatabase;
import com.dalakoti07.android.notestaking.room.models.NoteModel;
import com.dalakoti07.android.notestaking.ui.adapters.NoteAdapter;
import com.dalakoti07.android.notestaking.utils.Constants;
import com.dalakoti07.android.notestaking.utils.ParcelableNote;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//act as a fragment which can create a new note or edit an existing note
public class EditNoteFragment extends Fragment {
    //todo create a viewmodel
    private FragmentEditNoteBinding binding;
    private NotesDatabase notesDatabase;
    private NotesDao notesDao;
    private NavController navController;
    private Boolean creatingANewNote;
    private ParcelableNote parcelableNote;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentEditNoteBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle=getArguments();
        creatingANewNote=bundle.getBoolean(Constants.isNewKey);
        if(!creatingANewNote){
            parcelableNote=bundle.getParcelable(Constants.editNoteKey);
            setTheDataToUI();
        }
        navController= NavHostFragment.findNavController(this);
        notesDatabase= NotesApp.getNotesDatabase();
        notesDao=notesDatabase.notesDao();
        binding.btnDone.setOnClickListener(view -> {
            if(creatingANewNote){
                NotesDatabase.databaseWriteExecutor.execute(() -> {
                    notesDao.insertNote(new NoteModel(binding.etTitle.getText().toString(),
                            binding.etDescription.getText().toString(),
                            getTheDateAndTime()));
                });
            }else{
                NotesDatabase.databaseWriteExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        NoteModel noteModel= new NoteModel(binding.etTitle.getText().toString(),binding.etDescription.getText().toString(),
                                getTheDateAndTime());
                        noteModel.setNotesId(parcelableNote.getNotesId());
                        noteModel.setArchived(parcelableNote.getArchived());
                        notesDao.updateNote(noteModel);
                    }
                });
            }
            navController.navigateUp();
        });
    }

    private void setTheDataToUI() {
        binding.tvUpdatedOn.setVisibility(View.VISIBLE);
        binding.tvUpdatedOn.append(parcelableNote.getUpdatedOn());
        binding.etTitle.setText(parcelableNote.getNoteTitle());
        binding.etDescription.setText(parcelableNote.getNotesDescription());
    }

    private String getTheDateAndTime() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, hh:mm a");
        return dateFormat.format(date);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}
