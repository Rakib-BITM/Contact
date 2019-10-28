package com.rakib.contact.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rakib.contact.dao.ContactDao;
import com.rakib.contact.entities.Contact;

@Database(entities = {Contact.class},version = 1,exportSchema = false)
public abstract class ContactDB extends RoomDatabase {
    private static ContactDB db;
    public abstract ContactDao getContactDao();

    public static ContactDB getInstance(Context context){
        if (db != null){
            return db;
        }

        db = Room
                .databaseBuilder(context, ContactDB.class, "contact_db")
                .allowMainThreadQueries()
                .build();
        return db;
    }
}
