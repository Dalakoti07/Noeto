package com.dalakoti07.android.notestaking.ui.fragments;

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
import com.dalakoti07.android.notestaking.ui.adapters.NoteAdapter;
import com.dalakoti07.android.notestaking.ui.adapters.SimpleItemTouchHelperCallback;
import com.dalakoti07.android.notestaking.utils.Constants;
import com.dalakoti07.android.notestaking.utils.ParcelableNote;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements NoteAdapter.NotesClickListener{
    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;
    private NavController navController;
    private NoteAdapter noteAdapter;
    private NotesDatabase notesDatabase;
    private NotesDao notesDao;
    private boolean isArchivedList=false;
    private PopupMenu popupMenu;

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
                isArchivedList=false;
                Log.d(TAG, "live data onChanged: "+noteModels.size());
                noteAdapter.addData((ArrayList<NoteModel>) noteModels);
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        noteAdapter=new NoteAdapter(getContext(),this);
        binding.rvNotes.setAdapter(noteAdapter);

        binding.ivOption.setOnClickListener(arch -> {
            /*binding.progressBar.setVisibility(View.VISIBLE);
            noteAdapter.clearData();
            isArchivedList=true;
            notesDao.fetchArchivedNotes().observe(getViewLifecycleOwner(), new Observer<List<NoteModel>>() {
                @Override
                public void onChanged(List<NoteModel> noteModels) {
                    Log.d(TAG, "live data onChanged: "+noteModels.size());
                    noteAdapter.addData((ArrayList<NoteModel>) noteModels);
                    binding.progressBar.setVisibility(View.GONE);
                }
            });*/
            popupMenu=new PopupMenu(getContext(),binding.ivOption);
            popupMenu.inflate(R.menu.main_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    return true;
                }
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

    // todo just toggle archived and un-archived
    @Override
    public void archiveNote(NoteModel note) {
        if(!isArchivedList){
            Snackbar.make(binding.getRoot(),"Archived", Snackbar.LENGTH_SHORT).show();
            NotesDatabase.databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    note.setArchived(true);
                    notesDao.updateNote(note);
                }
            });
        }else{
            Snackbar.make(binding.getRoot(),"UnArchived", Snackbar.LENGTH_SHORT).show();
            NotesDatabase.databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    note.setArchived(null);
                    notesDao.updateNote(note);
                }
            });
        }
    }

    @Override
    public void moreOptionClicked(View view, NoteModel note) {
        PopupMenu rvPopupMenu=new PopupMenu(getContext(),view);
        rvPopupMenu.inflate(R.menu.rv_item_menu);
        rvPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.delete:
                        NotesDatabase.databaseWriteExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                notesDao.deleteNote(note);
                            }
                        });
                        Snackbar.make(binding.getRoot(),"Deleted",Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.share:
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
