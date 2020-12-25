package com.dalakoti07.android.notestaking.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.dalakoti07.android.notestaking.NotesApp;
import com.dalakoti07.android.notestaking.databinding.FragmentEditNoteBinding;
import com.dalakoti07.android.notestaking.room.NotesDao;
import com.dalakoti07.android.notestaking.room.NotesDatabase;
import com.dalakoti07.android.notestaking.room.models.NoteModel;
import com.dalakoti07.android.notestaking.ui.MainActivity;
import com.dalakoti07.android.notestaking.ui.adapters.NoteAdapter;
import com.dalakoti07.android.notestaking.utils.Constants;
import com.dalakoti07.android.notestaking.utils.ParcelableNote;
import com.dalakoti07.android.notestaking.viewModels.EditNoteViewModel;
import com.dalakoti07.android.notestaking.viewModels.ViewModelProviderFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

//act as a fragment which can create a new note or edit an existing note
public class EditNoteFragment extends Fragment {
    private FragmentEditNoteBinding binding;
    private EditNoteViewModel viewModel;
    private NavController navController;
    private Boolean creatingANewNote;
    private ParcelableNote parcelableNote;

    @Inject
    Context context;

    @Inject
    ViewModelProviderFactory viewModelFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(getActivity()!=null){
            ((MainActivity)getActivity()).mainComponent.inject(this);
        }
    }

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
        viewModel= ViewModelProviders.of(getActivity(),viewModelFactory).get(EditNoteViewModel.class);
        if(!creatingANewNote){
            parcelableNote=bundle.getParcelable(Constants.editNoteKey);
            setTheDataToUI();
        }
        navController= NavHostFragment.findNavController(this);
        viewModel.getDescriptionError().observe(getViewLifecycleOwner(), s -> binding.etDescription.setError(s));
        viewModel.getTitleError().observe(getViewLifecycleOwner(), s -> binding.etTitle.setError(s));
        binding.btnDone.setOnClickListener(view -> {
            if(! viewModel.validateData(binding.etTitle.getText().toString(),binding.etDescription.getText().toString()))
                return;
            if(creatingANewNote){
                viewModel.createNewNote(binding.etTitle.getText().toString(),binding.etDescription.getText().toString());
            }else{
                viewModel.updateTheNote(parcelableNote,binding.etTitle.getText().toString(),binding.etDescription.getText().toString());
            }
            //todo close keyboard before going back
            navController.navigateUp();
        });
    }

    private void setTheDataToUI() {
        binding.tvUpdatedOn.setVisibility(View.VISIBLE);
        binding.tvUpdatedOn.append(parcelableNote.getUpdatedOn());
        binding.etTitle.setText(parcelableNote.getNoteTitle());
        binding.etDescription.setText(parcelableNote.getNotesDescription());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}
