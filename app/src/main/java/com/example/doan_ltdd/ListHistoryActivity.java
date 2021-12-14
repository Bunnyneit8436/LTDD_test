package com.example.doan_ltdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.doan_ltdd.Adapter.ProductAdapter;
import com.example.doan_ltdd.Class.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListHistoryActivity extends AppCompatActivity implements ProductAdapter.Listener{

    Toolbar toolbar;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase database;

    RecyclerView rc_product_list_history;

    ArrayList<Product> productList;

    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history);

        toolbar = findViewById(R.id.toolbar);
        rc_product_list_history = findViewById(R.id.rc_product_list_history);

        productList = new ArrayList<>();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sản phẩm đã xem gần đây");

        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser= mAuth.getCurrentUser();
        String userId = mUser.getUid();

        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        getDataProduct();
        rc_product_list_history.setLayoutManager(new GridLayoutManager(this,2));
        productAdapter = new ProductAdapter(productList,this);
        rc_product_list_history.setAdapter(productAdapter);
    }

    @Override
    public void onClick(Product product) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        myRef.child("DbUser").child(mAuth.getCurrentUser().getUid()).child("ListViewHistory").child(product.productID).setValue(product);

        Intent intent = new Intent(ListHistoryActivity.this, DetailProductActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    // Lay product tu database
    public void getDataProduct(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DbUser");

        myRef.child(mAuth.getCurrentUser().getUid()).child("ListViewHistory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()){
                    Product product = data.getValue(Product.class);
                        productList.add(product);
                }

                productAdapter.notifyDataSetChanged();
//                setProductSearchAdapter(mListProduct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListHistoryActivity.this,"Không tải được dữ liệu từ firebase"
                        +error.toString(),Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCancelled"+ error.toString());
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
}