package com.rakib.contact.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.rakib.contact.ContactDetailsActivity;
import com.rakib.contact.R;
import com.rakib.contact.db.ContactDB;
import com.rakib.contact.entities.Contact;
import java.util.List;

public class ContactRVAdapter extends RecyclerView.Adapter<ContactRVAdapter.ContactViewHolder> {
    private Context context;
    private List<Contact> contactList;

    public ContactRVAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.contact_row,parent,false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, final int position) {
        //step 3
        holder.nameTV.setText(contactList.get(position).getName());
        holder.numberTV.setText(contactList.get(position).getNumber());

        String path = contactList.get(position).getPhoto();

        if (path!=null){
            Bitmap bitmap = BitmapFactory.decodeFile(contactList.get(position).getPhoto());
            holder.photo.setImageBitmap(bitmap);
        }




        final int pp =  position;

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDeleteNoteDialog(position);
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Contact contact = contactList.get(position);
               Intent intent = new Intent(context, ContactDetailsActivity.class);
               intent.putExtra("contact",contact);
               context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV,numberTV;
        ImageView photo;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            //step 2
            nameTV = itemView.findViewById(R.id.row_name);
            numberTV = itemView.findViewById(R.id.row_number);
            photo = itemView.findViewById(R.id.profile_image);
        }
    }

    public void updateList(List<Contact> contacts){
        this.contactList = contacts;
        notifyDataSetChanged();
    }

    private void deleteNote(Contact contact) {
        int deletedRow = ContactDB.getInstance(context).getContactDao().deleteContact(contact);
        if (deletedRow > 0){
            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
            contactList.remove(contact);
            notifyDataSetChanged();
        }
    }


    private void showDeleteNoteDialog(final int p) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.delete_contact_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("");
        builder.setView(view);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Contact contact = contactList.get(p);
                deleteNote(contact);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}
