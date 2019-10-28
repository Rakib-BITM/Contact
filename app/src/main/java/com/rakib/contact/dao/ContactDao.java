package com.rakib.contact.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rakib.contact.entities.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert
    long insertContact(Contact contact);

    @Query("select * from Contact order by name asc")
    List<Contact> getAllContact();

    @Query("select * from Contact where id ==:contactID")
    Contact getContactByID(long contactID);

    @Update
    int updateContact(Contact contact);

    @Delete
    int deleteContact(Contact contact);
}
