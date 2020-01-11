package com.labawsrh.aws.introscreen;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.provider.ContactsContract;

import java.util.List;

public interface NoteDao {



    @Delete
    void delete(ContactsContract.CommonDataKinds.Note note);
}
