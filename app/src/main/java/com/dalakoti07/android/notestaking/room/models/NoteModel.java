package com.dalakoti07.android.notestaking.room.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

//todo what if titles are same
@Entity(tableName = "notes",indices = {
        @Index( value = {"noteTitle"},unique = true)
})
public class NoteModel {

    @PrimaryKey(autoGenerate = true)
    public int notesId;

    @ColumnInfo(name = "noteTitle")
    public String noteTitle;

    @ColumnInfo(name = "noteDescription")
    public String notesDescription;

    @ColumnInfo(name = "updatedOn")
    public String updatedOn;

    //default value was "0"
    @ColumnInfo(name = "archived",defaultValue = "false")
    public Boolean isArchived;

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }

    public int getNotesId() {
        return notesId;
    }

    public void setNotesId(int notesId) {
        this.notesId = notesId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNotesDescription() {
        return notesDescription;
    }

    public void setNotesDescription(String notesDescription) {
        this.notesDescription = notesDescription;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public NoteModel(String noteTitle, String notesDescription, String updatedOn) {
        this.noteTitle = noteTitle;
        this.notesDescription = notesDescription;
        this.updatedOn = updatedOn;
    }

    @NonNull
    @Override
    public String toString() {
        return noteTitle+" -> "+updatedOn;
    }
}
