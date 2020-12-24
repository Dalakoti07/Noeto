package com.dalakoti07.android.notestaking.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import com.dalakoti07.android.notestaking.NotesApp;
import com.dalakoti07.android.notestaking.R;
import com.dalakoti07.android.notestaking.databinding.FragmentHomeBinding;
import com.dalakoti07.android.notestaking.room.NotesDao;
import com.dalakoti07.android.notestaking.room.NotesDatabase;
import com.dalakoti07.android.notestaking.room.models.NoteModel;
import com.dalakoti07.android.notestaking.ui.adapters.NoteAdapter;
import com.dalakoti07.android.notestaking.utils.Constants;
import com.dalakoti07.android.notestaking.utils.ParcelableNote;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements NoteAdapter.NotesClickListener{
    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;
    private NavController navController;
    private NoteAdapter noteAdapter;
    private NotesDatabase notesDatabase;
    private NotesDao notesDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notesDatabase= NotesApp.getNotesDatabase();
        notesDao=notesDatabase.notesDao();

        notesDao.fetchAllNotes().observe(getViewLifecycleOwner(), new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(List<NoteModel> noteModels) {
                Log.d(TAG, "live data onChanged: "+noteModels.size());
                noteAdapter.addData((ArrayList<NoteModel>) noteModels);
            }
        });

        noteAdapter=new NoteAdapter(getContext(),this);
        binding.rvNotes.setAdapter(noteAdapter);
        navController= NavHostFragment.findNavController(this);
        binding.btnAdd.setOnClickListener(btn->{
            Bundle bundle= new Bundle();
            bundle.putBoolean(Constants.isNewKey,true);
            navController.navigate(R.id.action_homeFragment_to_editNoteFragment,bundle);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }

    @Override
    public void noteClicked(NoteModel note) {
        Bundle bundle= new Bundle();
        bundle.putBoolean(Constants.isNewKey,false);
        ParcelableNote pNote=new ParcelableNote();
        pNote.copyDataToItself(note);
        bundle.putParcelable(Constants.editNoteKey,pNote);
        navController.navigate(R.id.action_homeFragment_to_editNoteFragment,bundle);
    }
}
