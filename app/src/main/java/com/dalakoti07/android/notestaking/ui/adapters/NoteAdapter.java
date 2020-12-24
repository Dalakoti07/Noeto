package com.dalakoti07.android.notestaking.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dalakoti07.android.notestaking.R;
import com.dalakoti07.android.notestaking.databinding.RvItemNoteBinding;
import com.dalakoti07.android.notestaking.room.models.NoteModel;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NotesHolder> {
    private ArrayList<NoteModel> allNotes= new ArrayList<>();
    private Context context;

    public NoteAdapter(Context context){
        this.context=context;
    }

    public void addData(ArrayList<NoteModel> arrayList){
        this.allNotes.addAll(arrayList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_note,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesHolder holder, int position) {
        holder.binData(allNotes.get(position));
    }

    @Override
    public int getItemCount() {
        return allNotes.size();
    }

    public class NotesHolder extends RecyclerView.ViewHolder {
        RvItemNoteBinding binding;

        public NotesHolder(@NonNull View itemView) {
            super(itemView);
            binding=RvItemNoteBinding.bind(itemView);
        }

        public void binData(NoteModel note){
            binding.tvTitle.setText(note.noteTitle);
            binding.tvNote.setText(note.notesDescription);
            binding.tvModified.setText(note.updatedOn);
        }
    }
}
