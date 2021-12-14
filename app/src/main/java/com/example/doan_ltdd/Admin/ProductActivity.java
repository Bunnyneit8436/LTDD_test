package com.example.doan_ltdd.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doan_ltdd.Class.Product;
import com.example.doan_ltdd.R;
import com.example.doan_ltdd.UpdateProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText edt_product_name, edt_product_price, edt_product_quantity, edt_product_desc, edt_product_category;
    ImageView image_product;
    Button btn_add_product;

    FirebaseDatabase database;
    StorageReference storageReference;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        edt_product_name = findViewById(R.id.edt_product_name);
        edt_product_price =findViewById(R.id.edt_product_price);
        edt_product_quantity = findViewById(R.id.edt_product_quantity);
        edt_product_desc = findViewById(R.id.edt_product_desc);
        edt_product_category = findViewById(R.id.edt_product_category);
        image_product = findViewById(R.id.image_product);
        btn_add_product  = findViewById(R.id.btn_add_product);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thêm sản phẩm");

        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);


        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        storageReference = FirebaseStorage.getInstance().getReference();

        image_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri != null)
                {
                    uploadToFirebase(imageUri);
//                    updateProfile();

                }
                else
                {
//                    imageUri=Uri.parse(String.valueOf(R.drawable.hinh1));
                    uploadToFirebase(imageUri);

                }
            }
        });

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
            Glide.with(ProductActivity.this).load(imageUri).into(image_product);
        }
//        else
//        {
//            imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/do-an-ltdd.appspot.com/o/%5BErai-raws%5D%20SSSS.Dynazenon%20-%2009%20%5Bv0%5D%5B1080p%5D%5BMultiple%20Subtitle%5D_Moment(2).jpg?alt=media&token=aeb08f35-7202-4417-9356-66ac7ecaaebe");
//            Glide.with(ProductActivity.this).load(imageUri).into(image_product);
//        }
    }

    public  void uploadToFirebase(Uri uri)
    {
        String productName = edt_product_name.getText().toString();
        String productPrice = edt_product_price.getText().toString();
        String productQuantity = edt_product_quantity.getText().toString();
        String productDesc = edt_product_desc.getText().toString();
        String productCategory = edt_product_category.getText().toString();

        if(productName.isEmpty())
        {
            edt_product_name.setError("Nhập tên sản phẩm");
            return;
        }

        if(productPrice.isEmpty())
        {
            edt_product_price.setError("Nhập giá sản phẩm");
            return;
        }
        else if (Integer.parseInt(productPrice)<0)
        {
            productPrice = "100000";
        }

        if(productQuantity.isEmpty())
        {
            edt_product_quantity.setError("Nhập số lượng sản phẩm");
            return;
        }
        else if (Integer.parseInt(productQuantity)<0)
        {
            productQuantity = "1";
        }

        if(productDesc.isEmpty())
        {
            productDesc = "Cập nhật sau";
        }

        if(productCategory.isEmpty())
        {
            productCategory = "Chưa phân loại";
        }

        String productID = productName+"_"+productQuantity;

        StorageReference fileRef = storageReference.child("image").child("product").child(edt_product_name.getText().toString()).child(System.currentTimeMillis()+"."+getFileExtension(uri));

        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        Product product = new Product(productID,uri.toString(),productName,productDesc,Integer.parseInt(productPrice),Integer.parseInt(productQuantity),productCategory);

        myRef.child("DbProduct").child(productID).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProductActivity.this,"Đã cập nhật",Toast.LENGTH_SHORT).show();
            }
        });

//                        myRef.child("DbProduct").child(productName).child("productName").setValue(productName);
//                        myRef.child("DbProduct").child(productName).child("productPrice").setValue(Integer.parseInt(productPrice));
//                        myRef.child("DbProduct").child(productName).child("productQuantity").setValue(Integer.parseInt(productQuantity));
//                        myRef.child("DbProduct").child(productName).child("productDesc").setValue(productDesc);
//                        myRef.child("DbProduct").child(productName).child("productCate").setValue(productCategory);
//
//                        myRef.child("DbProduct").child(edt_product_name.getText().toString()).child("urlImage").setValue(uri.toString());
    }

    public String getFileExtension(Uri muri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(muri));
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
}