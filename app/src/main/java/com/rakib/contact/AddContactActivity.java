package com.rakib.contact;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rakib.contact.adapter.ContactRVAdapter;
import com.rakib.contact.db.ContactDB;
import com.rakib.contact.entities.Contact;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddContactActivity extends AppCompatActivity {
    private EditText name, number,email;
    private ImageView profilepic;
    private String currentPhotoPath;
    private final int REQUEST_STORAGE_CODE = 456;
    private final int REQUEST_CAMERA_CODE = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        setTitle("Add Contact");
        initialization();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.saveCon:
                saveContact();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission accepted", Toast.LENGTH_SHORT).show();
            dispatchCameraIntent();
        }
    }

    private void saveContact(){
        if (checkEmptyField()){
            String nameText = name.getText().toString();
            String numberText = number.getText().toString();
            String emailText = email.getText().toString();
            String photoPath = currentPhotoPath;

            Contact contact = new Contact(nameText,numberText,emailText,photoPath);

            long insertedRow = ContactDB.getInstance(this).getContactDao().insertContact(contact);

            if (insertedRow>0){
                Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show();
                List<Contact> contacts = ContactDB.getInstance(this).getContactDao().getAllContact();
                ContactRVAdapter rvAdapter = new ContactRVAdapter(this,contacts);
                rvAdapter.updateList(contacts);
                startActivity(new Intent(this,MainActivity.class));
            }

        }
    }


    private boolean checkEmptyField(){
        if (name.getText().toString().isEmpty()){
            name.setError("Please enter your name");
            return false;
        }

        if (number.getText().toString().isEmpty()){
            number.setError("Please enter your mobile number");
            return false;
        }
        return true;
    }

    private void initialization(){
        name = findViewById(R.id.nameIn);
        number = findViewById(R.id.numberIn);
        email = findViewById(R.id.emailIn);
        profilepic = findViewById(R.id.photo_up);
    }

    public void showCameraPreview(View view) {
        if (checkStoragePermission()){
            dispatchCameraIntent();
        }
    }



    private boolean checkStoragePermission(){
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            requestPermissions(permissions, REQUEST_STORAGE_CODE);
            return false;
        }
        return true;
    }

    private void dispatchCameraIntent(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.rakib.contact",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK){
            Bitmap bmp = BitmapFactory.decodeFile(currentPhotoPath);
            profilepic.setImageBitmap(bmp);
        }
    }



}
