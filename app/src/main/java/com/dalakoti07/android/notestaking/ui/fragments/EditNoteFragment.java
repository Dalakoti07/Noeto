package com.dalakoti07.android.notestaking.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.dalakoti07.android.notestaking.databinding.FragmentEditNoteBinding;
import com.dalakoti07.android.notestaking.ui.MainActivity;
import com.dalakoti07.android.notestaking.utils.Constants;
import com.dalakoti07.android.notestaking.utils.ParcelableNote;
import com.dalakoti07.android.notestaking.viewModels.EditNoteViewModel;
import com.dalakoti07.android.notestaking.viewModels.ViewModelProviderFactory;

import javax.inject.Inject;

/**
 * act as a fragment which can create a new note or edit an existing note
*/
public class EditNoteFragment extends Fragment {
    private static final String TAG = "EditNoteFragment";
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
        binding.tvToolbarTitle.setText("Creating Note");
        if(!creatingANewNote){
            parcelableNote=bundle.getParcelable(Constants.editNoteKey);
            binding.tvToolbarTitle.setText("Editing Note");
            setTheDataToUI();
        }
        navController= NavHostFragment.findNavController(this);
        binding.ivBack.setOnClickListener(view -> {
            navController.navigateUp();
        });
        viewModel.getDescriptionError().observe(getViewLifecycleOwner(), s -> {
            binding.descriptionInputLayout.setError(s);
            Log.d(TAG, "getting error message "+s);
        });
        viewModel.getTitleError().observe(getViewLifecycleOwner(), s -> binding.titleInputLayout.setError(s));

        binding.etTitle.addTextChangedListener(viewModel.getTitleTextWatcher());
        binding.etDescription.addTextChangedListener(viewModel.getDescriptionTextWatcher());

        binding.btnDone.setOnClickListener(view -> {
            if(! viewModel.validateData())
                return;
            if(creatingANewNote){
                viewModel.createNewNote(MainActivity.userId);
            }else{
                viewModel.updateTheNote(parcelableNote);
            }
            //todo close keyboard before going back
            hideKeyboard(getActivity());
            navController.navigateUp();
        });
    }

    private void setTheDataToUI() {
        binding.tvUpdatedOn.setVisibility(View.VISIBLE);
        binding.tvUpdatedOn.append(parcelableNote.getUpdatedOn());
        binding.etTitle.setText(parcelableNote.getNoteTitle());
        binding.etDescription.setText(parcelableNote.getNotesDescription());
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}
