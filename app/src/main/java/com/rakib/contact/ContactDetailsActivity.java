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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rakib.contact.entities.Contact;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContactDetailsActivity extends AppCompatActivity {
    private ImageView profilePic;
    private TextView name,number,email;
    private Contact contact;
    private String currentPhotoPath;
    private final int REQUEST_CALL_PHONE_CODE = 123;
    private final int REQUEST_STORAGE_CODE = 456;
    private final int REQUEST_CAMERA_CODE = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        setTitle("Contact Details");
        initialization();
        contact = (Contact) getIntent().getSerializableExtra("contact");
        setValues();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void updateCameraPreview(View view) {
        if (checkStoragePermission()){
            dispatchCameraIntent();
        }
    }

    private void initialization(){
        name = findViewById(R.id.nameD);
        number = findViewById(R.id.numberD);
        email = findViewById(R.id.emailD);
        profilePic = findViewById(R.id.photo_up);
    }

    private void setValues(){
        name.setText(contact.getName());
        number.setText(contact.getNumber());
        email.setText(contact.getEmail());
    }

    public void callContact(View view) {
        initiatePhoneCall();
    }


    private boolean checkCallPermission(){
        String[] permissions = {Manifest.permission.CALL_PHONE};
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED){
            requestPermissions(permissions, REQUEST_CALL_PHONE_CODE);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE_CODE && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
            initiatePhoneCall();
        }else if (requestCode == REQUEST_STORAGE_CODE && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission accepted", Toast.LENGTH_SHORT).show();
            dispatchCameraIntent();
        }
    }

    private void initiatePhoneCall(){
        String phoneNumber = contact.getNumber();
        Uri phoneUri = Uri.parse("tel:"+phoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_CALL, phoneUri);
        if (callIntent.resolveActivity(getPackageManager()) != null){
            if (checkCallPermission()){
                startActivity(callIntent);
            }
        }else{
            Toast.makeText(this, "no components found", Toast.LENGTH_SHORT).show();
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
            profilePic.setImageBitmap(bmp);
        }
    }

    public void sendEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, contact.getEmail());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }
}
