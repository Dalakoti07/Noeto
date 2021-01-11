package com.dalakoti07.android.notestaking.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.dalakoti07.android.notestaking.NotesApp;
import com.dalakoti07.android.notestaking.R;
import com.dalakoti07.android.notestaking.databinding.FragmentHomeBinding;
import com.dalakoti07.android.notestaking.room.NotesDao;
import com.dalakoti07.android.notestaking.room.NotesDatabase;
import com.dalakoti07.android.notestaking.room.models.NoteModel;
import com.dalakoti07.android.notestaking.ui.MainActivity;
import com.dalakoti07.android.notestaking.ui.adapters.NoteAdapter;
import com.dalakoti07.android.notestaking.ui.adapters.SimpleItemTouchHelperCallback;
import com.dalakoti07.android.notestaking.utils.Constants;
import com.dalakoti07.android.notestaking.utils.ParcelableNote;
import com.dalakoti07.android.notestaking.viewModels.HomeFragmentViewModel;
import com.dalakoti07.android.notestaking.viewModels.ViewModelProviderFactory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HomeFragment extends Fragment implements NoteAdapter.NotesClickListener{
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private NavController navController;
    private NoteAdapter noteAdapter;
    private PopupMenu popupMenu;
    private HomeFragmentViewModel viewModel;

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
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noteAdapter=new NoteAdapter(getContext(),this);
        binding.rvNotes.setAdapter(noteAdapter);
        viewModel= ViewModelProviders.of(getActivity(),viewModelFactory).get(HomeFragmentViewModel.class);
        viewModel.getToolBarName().observe(getViewLifecycleOwner(), s -> binding.tvToolbarTitle.setText(s));
        viewModel.getNotesList().observe(getViewLifecycleOwner(), resp->{respondToNotesList(resp);});

        binding.ivOption.setOnClickListener(arch -> {
            popupMenu=new PopupMenu(getContext(),binding.ivOption);
            popupMenu.inflate(R.menu.main_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.all:
                        if(! viewModel.isViewingAllNotes()){
                            binding.progressBar.setVisibility(View.VISIBLE);
                            viewModel.optionSelected("all");
                        }
                        break;
                    case R.id.archived:
                        if(viewModel.isViewingAllNotes()){
                            binding.progressBar.setVisibility(View.VISIBLE);
                            viewModel.optionSelected("archived");
                        }
                        break;
                }
                return true;
            });
            popupMenu.show();
        });

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(noteAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(binding.rvNotes);

        navController= NavHostFragment.findNavController(this);
        binding.btnAdd.setOnClickListener(btn->{
            Bundle bundle= new Bundle();
            bundle.putBoolean(Constants.isNewKey,true);
            navController.navigate(R.id.action_homeFragment_to_editNoteFragment,bundle);
        });
        binding.ivProfile.setOnClickListener(profile->{
            navController.navigate(R.id.action_homeFragment_to_profileFragment);
        });
    }

    // listening to response from mutable livedata
    private void respondToNotesList(List<NoteModel> noteModels){
        if(noteModels.size()==0 ||noteModels==null){
            noteAdapter.clearData();
            binding.llEmpty.setVisibility(View.VISIBLE);
            if(viewModel.isViewingAllNotes())
                binding.tvNoNotesMsg.setText("You have no notes!");
            else
                binding.tvNoNotesMsg.setText("You haven't archived yet!");
        }else{
            binding.llEmpty.setVisibility(View.GONE);
            noteAdapter.addData((ArrayList<NoteModel>) noteModels);
        }
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }

    // notes click listeners to open edit screen
    @Override
    public void noteClicked(NoteModel note) {
        Bundle bundle= new Bundle();
        bundle.putBoolean(Constants.isNewKey,false);
        ParcelableNote pNote=new ParcelableNote();
        pNote.copyDataToItself(note);
        bundle.putParcelable(Constants.editNoteKey,pNote);
        navController.navigate(R.id.action_homeFragment_to_editNoteFragment,bundle);
    }

    //toggling archiving and un-archiving notes
    @Override
    public void archiveNote(NoteModel note) {
        if(note.isArchived==null)
            Snackbar.make(binding.getRoot(),"Archived", Snackbar.LENGTH_SHORT).show();
        else
            Snackbar.make(binding.getRoot(),"UnArchived", Snackbar.LENGTH_SHORT).show();
        viewModel.toggleTheArchiveStatus(note);
    }

    // open popUpMenu when more btn is clicked
    @Override
    public void moreOptionClicked(View view, NoteModel note) {
        PopupMenu rvPopupMenu=new PopupMenu(getContext(),view);
        rvPopupMenu.inflate(R.menu.rv_item_menu);
        rvPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.delete:
                        new MaterialAlertDialogBuilder(context)
                                .setTitle("Delete")
                                .setMessage("Would you like to delete this?")
                                .setPositiveButton("Yes", (dialogInterface, i) ->{
                                        viewModel.deleteNote(note);
                                        Snackbar.make(binding.getRoot(),"Deleted",Snackbar.LENGTH_SHORT).show();
                                        }
                                )
                                .setNeutralButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                        break;
                    case R.id.share:
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        String shareBody = note.noteTitle+"\n\n"+note.notesDescription;
                        intent.setType("text/plain");
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Noeto");
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(intent, "Share Notes via"));
                        break;
                    case R.id.cancel:
                        break;
                }
                return true;
            }
        });
        rvPopupMenu.show();
    }
}
