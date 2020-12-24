package com.dalakoti07.android.notestaking.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.dalakoti07.android.notestaking.room.models.NoteModel;

public class ParcelableNote implements Parcelable {
    private int notesId;

    private String noteTitle;

    private String notesDescription;

    private String updatedOn;

    private Boolean isArchived;

    public ParcelableNote(Parcel in) {
        notesId = in.readInt();
        noteTitle = in.readString();
        notesDescription = in.readString();
        updatedOn = in.readString();
        byte tmpIsArchived = in.readByte();
        isArchived = tmpIsArchived == 0 ? null : tmpIsArchived == 1;
    }

    public ParcelableNote(){

    }

    public static final Creator<ParcelableNote> CREATOR = new Creator<ParcelableNote>() {
        @Override
        public ParcelableNote createFromParcel(Parcel in) {
            return new ParcelableNote(in);
        }

        @Override
        public ParcelableNote[] newArray(int size) {
            return new ParcelableNote[size];
        }
    };

    public void copyDataToItself(NoteModel noteModel){
        this.notesId= noteModel.notesId;
        this.noteTitle= noteModel.noteTitle;
        this.notesDescription= noteModel.notesDescription;
        this.updatedOn= noteModel.updatedOn;
        this.isArchived= noteModel.isArchived;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(notesId);
        parcel.writeString(noteTitle);
        parcel.writeString(notesDescription);
        parcel.writeString(updatedOn);
        parcel.writeByte((byte) (isArchived == null ? 0 : isArchived ? 1 : 2));
    }

    public int getNotesId() {
        return notesId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNotesDescription() {
        return notesDescription;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public Boolean getArchived() {
        return isArchived;
    }
}
