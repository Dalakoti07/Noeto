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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//act as a fragment which can create a new note or edit an existing note
public class EditNoteFragment extends Fragment {
    private FragmentEditNoteBinding binding;
    private NotesDatabase notesDatabase;
    private NotesDao notesDao;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentEditNoteBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController= NavHostFragment.findNavController(this);
        notesDatabase= NotesApp.getNotesDatabase();
        notesDao=notesDatabase.notesDao();
        binding.btnDone.setOnClickListener(view -> {
            NotesDatabase.databaseWriteExecutor.execute(() -> {
                notesDao.insertNote(new NoteModel(binding.etTitle.getText().toString(),
                        binding.etDescription.getText().toString(),
                        getTheDateAndTime()));
            });
            navController.navigateUp();
        });
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
