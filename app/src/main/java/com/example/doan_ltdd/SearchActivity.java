package com.example.doan_ltdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.doan_ltdd.Adapter.ProductAdapter;
import com.example.doan_ltdd.Adapter.ProductSearchAdapter;
import com.example.doan_ltdd.Class.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements ProductSearchAdapter.Listener {

    Toolbar toolbar;
    SearchView searchView;
    ProductSearchAdapter productSearchAdapter;
    ArrayList<Product> arrayList;
    RecyclerView rcProductFilter;
    LinearLayout linearEmpty;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar =findViewById(R.id.toolbar);
        searchView =findViewById(R.id.searchView);
        linearEmpty =findViewById(R.id.linearEmpty);

        rcProductFilter =findViewById(R.id.rcProductFilter);

        arrayList = new ArrayList<>();

        getDataProduct();
        productSearchAdapter = new ProductSearchAdapter(arrayList,this);
        rcProductFilter.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rcProductFilter.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        rcProductFilter.setAdapter(productSearchAdapter);
        productSearchAdapter.notifyDataSetChanged();
        setVisible(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productSearchAdapter.getFilter().filter(newText);
                if(productSearchAdapter.arrayListFilter.size()==0 || newText.isEmpty())
                {
                    setVisible(false);
                }
                else
                {
                    setVisible(true);
                }
                return false;
            }
        });
    }

    public void setVisible(boolean flag)
    {
        if(!flag)
        {
            linearEmpty.setVisibility(View.VISIBLE);
            rcProductFilter.setVisibility(View.GONE);
        }
        else
        {
            linearEmpty.setVisibility(View.GONE);
            rcProductFilter.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(Product product) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        myRef.child("DbUser").child(mAuth.getCurrentUser().getUid()).child("ListViewHistory").child(product.productID).setValue(product);

        Intent intent = new Intent(getApplicationContext(), DetailProductActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    // Lay product tu database
    public void getDataProduct(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DbProduct");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()){
                    Product product = data.getValue(Product.class);
                    arrayList.add(product);
                }

                productSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Không tải được dữ liệu từ firebase"
                        +error.toString(),Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCancelled"+ error.toString());
            }
        });
    }
}