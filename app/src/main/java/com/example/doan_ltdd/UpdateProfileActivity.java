package com.example.doan_ltdd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {

    ImageView image_account_profile;
    Toolbar toolbar;
    EditText edt_username_profile, edt_name_profile, edt_phone_profile, edt_address_profile;
    TextView tv_email_profile;
    Button btn_update, btn_back;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    StorageReference storageReference;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        toolbar=findViewById(R.id.toolbar);
        edt_username_profile=findViewById(R.id.edt_username_profile);
        edt_name_profile=findViewById(R.id.edt_name_profile);
        edt_phone_profile=findViewById(R.id.edt_phone_profile);
        edt_address_profile=findViewById(R.id.edt_address_profile);
        tv_email_profile=findViewById(R.id.tv_email_profile);
        image_account_profile=findViewById(R.id.image_account_profile);
        btn_update = findViewById(R.id.btn_update);
        btn_back = findViewById(R.id.btn_back);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cập nhật thông tin");

        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);

        ///
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        storageReference = FirebaseStorage.getInstance().getReference();

//        image_account_profile.setImageURI(mAuth.getCurrentUser().getPhotoUrl());

        myRef.child("DbUser").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username=snapshot.child("username").getValue().toString();
                String name=snapshot.child("name").getValue().toString();
                String phone=snapshot.child("phone").getValue().toString();
                String email=snapshot.child("email").getValue().toString();
                String address=snapshot.child("address").getValue().toString();
                String urlImage=snapshot.child("urlImage").getValue().toString();

//                imageUri=Uri.parse(urlImage);

                edt_username_profile.setText(username);
                edt_name_profile.setText(name);
                edt_phone_profile.setText(phone);
                edt_address_profile.setText(address);
                tv_email_profile.setText(email);
                Glide.with(getApplicationContext()).load(Uri.parse(urlImage)).into(image_account_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        image_account_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri != null)
                {
                    uploadToFirebase(imageUri);
                    updateProfile();
                    Toast.makeText(UpdateProfileActivity.this,"Đã cập nhật",Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    imageUri= Uri.parse("https://firebasestorage.googleapis.com/v0/b/do-an-ltdd.appspot.com/o/1638781757579.jpg?alt=media&token=9319ec2c-ae0f-407f-99d0-fd7268e896a7");
//                    uploadToFirebase(imageUri);
                    updateProfile();
                    Toast.makeText(UpdateProfileActivity.this,"Đã cập nhật",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void selectImage()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 100 && resultCode == RESULT_OK && data != null)
        {
            imageUri = data.getData();
//            image_account_profile.setImageURI(imageUri);
            Glide.with(UpdateProfileActivity.this).load(imageUri).into(image_account_profile);
        }
    }

    public  void uploadToFirebase(Uri uri)
    {
        StorageReference fileRef = storageReference.child("image").child("user").child(mAuth.getCurrentUser().getUid()).child(System.currentTimeMillis()+"."+getFileExtension(uri));
        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        myRef.child("DbUser").child(mAuth.getCurrentUser().getUid()).child("urlImage").setValue(uri.toString());
//                        Toast.makeText(UpdateProfileActivity.this,"Đã cập nhật",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void updateProfile()
    {
        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        if(edt_username_profile.getText().toString() != null)
        {
            myRef.child("DbUser").child(mAuth.getCurrentUser().getUid()).child("username").setValue(edt_username_profile.getText().toString());
        }

        if(edt_name_profile.getText().toString() != null)
        {
            myRef.child("DbUser").child(mAuth.getCurrentUser().getUid()).child("name").setValue(edt_name_profile.getText().toString());
        }

        if(edt_phone_profile.getText().toString() != null)
        {
            myRef.child("DbUser").child(mAuth.getCurrentUser().getUid()).child("phone").setValue(edt_phone_profile.getText().toString());
        }

        if(edt_address_profile.getText().toString() != null)
        {
            myRef.child("DbUser").child(mAuth.getCurrentUser().getUid()).child("address").setValue(edt_address_profile.getText().toString());
        }


    }

    public String getFileExtension(Uri muri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(muri));
    }

}