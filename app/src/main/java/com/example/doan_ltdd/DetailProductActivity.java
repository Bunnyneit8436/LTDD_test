package com.example.doan_ltdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doan_ltdd.Class.Product;
import com.example.doan_ltdd.Class.ProductCart;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailProductActivity extends AppCompatActivity {

    TextView tvDetailProductName,tvDetailProductPrice,tvDetailProductDescription;
    Button btnDetailProductBuy, btnDetailProductOrder;
    ImageView imgDetailProductPhoto;
    Toolbar toolbar;
    ImageButton subquantity, addquantity;
    TextView tv_quantity;

    DecimalFormat format = new DecimalFormat("###,###,###");

    public Product detailProduct;
    public ArrayList<Product> listCartProduct;
    public boolean isAddToCart;
    public int countProduct;
    int quantity, numProduct, total;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        tvDetailProductName = findViewById(R.id.tv_detail_product_name);
        tvDetailProductPrice = findViewById(R.id.tv_detail_product_price);
        tvDetailProductDescription = findViewById(R.id.tv_detail_product_description);
        imgDetailProductPhoto = findViewById(R.id.img_detail_product_photo);
        btnDetailProductBuy = findViewById(R.id.btn_detail_product_buy);
        btnDetailProductOrder = findViewById(R.id.btn_detail_product_order);
        toolbar = findViewById(R.id.toolbar);
        tv_quantity = findViewById(R.id.tv_quantity);
        subquantity = findViewById(R.id.subquantity);
        addquantity = findViewById(R.id.addquantity);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông tin sản phẩm");

        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);

        detailProduct = new Product();

        Intent intent=getIntent();
        detailProduct= (Product) intent.getSerializableExtra("product");

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser= mAuth.getCurrentUser();
        String userId = mUser.getUid();

        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        if(listCartProduct == null){
            listCartProduct = new ArrayList<>();
        }

        setValueItem();


        addquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                displayQuantity();
            }
        });

        subquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // because we dont want the quantity go less than 0
                if (quantity == 0) {
//                    Toast.makeText(DetailProductActivity.this, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    quantity--;
                    displayQuantity();
                }
            }
        });
    }


    private void displayQuantity() {
        tv_quantity.setText(String.valueOf(quantity));
    }

    private void setValueItem(){
        if (detailProduct != null){
            tvDetailProductName.setText(detailProduct.productName);
            tvDetailProductPrice.setText(format.format(detailProduct.productPrice) + " VNĐ");
            Glide.with(this).load(Uri.parse(detailProduct.productImage)).into(imgDetailProductPhoto);
            tvDetailProductDescription.setText(detailProduct.productDesc);

            mAuth=FirebaseAuth.getInstance();
            FirebaseUser mUser= mAuth.getCurrentUser();
            String userId = mUser.getUid();

            database= FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();

            btnDetailProductBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(tv_quantity.getText().toString())!=0)
                    {
                        if (isAddToCart){
                            myRef.child("DbUser").child(userId).child("UserCart").child(detailProduct.productID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    numProduct=Integer.parseInt(snapshot.child("numProduct").getValue().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            numProduct=numProduct+quantity;
                            total = numProduct*detailProduct.productPrice;
                            myRef.child("DbUser").child(userId).child("UserCart").child(detailProduct.productID).child("numProduct")
                                    .setValue(numProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent =new Intent(DetailProductActivity.this,CartActivity.class);
                                    startActivity(intent);
                                    tv_quantity.setText("0");
                                    quantity=0;
                                    myRef.child("DbUser").child(userId).child("UserCart").child(detailProduct.productID).child("totalPrice")
                                            .setValue(total);
                                    Toast.makeText(DetailProductActivity.this,"Đã thêm sản phẩm vào giỏ hàng",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            isAddToCart = true;
                            numProduct=quantity;
                            total = numProduct*detailProduct.productPrice;
                            ProductCart productCart = new ProductCart(detailProduct.productID,detailProduct.productImage,detailProduct.productName,
                                    detailProduct.productDesc,detailProduct.productPrice,numProduct,detailProduct.categoryName,total);
                            myRef.child("DbUser").child(userId).child("UserCart").child(detailProduct.productID).setValue(productCart).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent =new Intent(DetailProductActivity.this,CartActivity.class);
                                    startActivity(intent);
                                    tv_quantity.setText("0");
                                    quantity=0;
                                    Toast.makeText(DetailProductActivity.this,"Đã thêm sản phẩm vào giỏ hàng",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                }
            });

            btnDetailProductOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(tv_quantity.getText().toString())!=0)
                    {
                        if (isAddToCart){
                            myRef.child("DbUser").child(userId).child("UserCart").child(detailProduct.productID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    numProduct=Integer.parseInt(snapshot.child("numProduct").getValue().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            numProduct=numProduct+quantity;
                            total = numProduct*detailProduct.productPrice;
                            myRef.child("DbUser").child(userId).child("UserCart").child(detailProduct.productID).child("numProduct")
                                    .setValue(numProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    tv_quantity.setText("0");
                                    quantity=0;
                                    myRef.child("DbUser").child(userId).child("UserCart").child(detailProduct.productID).child("totalPrice")
                                            .setValue(total);
                                    Toast.makeText(DetailProductActivity.this,"Đã thêm sản phẩm vào giỏ hàng",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            isAddToCart = true;
                            numProduct=quantity;
                            total = numProduct*detailProduct.productPrice;
                            ProductCart productCart = new ProductCart(detailProduct.productID,detailProduct.productImage,detailProduct.productName,
                                    detailProduct.productDesc,detailProduct.productPrice,numProduct,detailProduct.categoryName,total);
                            myRef.child("DbUser").child(userId).child("UserCart").child(detailProduct.productID).setValue(productCart).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    tv_quantity.setText("0");
                                    quantity=0;
                                    Toast.makeText(DetailProductActivity.this,"Đã thêm sản phẩm vào giỏ hàng",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                }
            });
        }
    }

//    public void addToListCartProdct(Product product)
//    {
//        listCartProduct.add(product);
//    }
//
//    public int getCountProduct()
//    {
//        return countProduct;
//    }
//
//    public void setCountForProduct(int possion, int countProduct){
//        listCartProduct.get(possion).numProduct=countProduct;
//    }

//    // Set số lượng các sản phẩm trong giỏ hàng
//    public void setCountProductInCart(int count){
//        countProduct = count;
////        AHNotification notification = new AHNotification.Builder()
////                .setText(String.valueOf(count))
////                .setBackgroundColor(ContextCompat.getColor(Home.this, R.color.red))
////                .setTextColor(ContextCompat.getColor(Home.this, R.color.white))
////                .build();
////        ahBotNavHome.setNotification(notification, 1);
//    }

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