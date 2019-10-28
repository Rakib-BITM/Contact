package com.rakib.contact;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rakib.contact.adapter.ContactRVAdapter;
import com.rakib.contact.db.ContactDB;
import com.rakib.contact.entities.Contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView contactRV;
    private ContactRVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabIconButton();

        contactRV =findViewById(R.id.contactRV);

        List<Contact> contacts = ContactDB.getInstance(this).getContactDao().getAllContact();

        rvAdapter = new ContactRVAdapter(this,contacts);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        //GridLayoutManager glm = new GridLayoutManager(this, 2);
        contactRV.setLayoutManager(llm);
        contactRV.setAdapter(rvAdapter);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_input:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fabIconButton(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Contact List");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddContactActivity.class));
            }
        });
    }


}
