package com.example.doan_ltdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.doan_ltdd.Adapter.OrderHistoryAdapter;
import com.example.doan_ltdd.Adapter.ProductCartAdapter;
import com.example.doan_ltdd.Class.Order;
import com.example.doan_ltdd.Class.ProductCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity implements OrderHistoryAdapter.Listener {

    Toolbar toolbar;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase database;

    ArrayList<ProductCart> listCartProduct;
    ArrayList<Order> listOrder;

    RecyclerView rc_hitory_order;

    OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        toolbar = findViewById(R.id.toolbar);
        rc_hitory_order = findViewById(R.id.rc_hitory_order);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser= mAuth.getCurrentUser();
        String userId = mUser.getUid();

        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        listCartProduct = new ArrayList<>();
        listOrder = new ArrayList<>();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lịch sử mua hàng");

        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);

        myRef.child("DbOrder").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    Order order = data.getValue(Order.class);
                    listOrder.add(order);
                    listCartProduct=order.listDetailOrder;
                }
                orderHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rc_hitory_order.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        orderHistoryAdapter = new OrderHistoryAdapter(listOrder,this);
        rc_hitory_order.setAdapter(orderHistoryAdapter);
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

    @Override
    public void onClick(Order order) {
        Intent intent =new Intent(OrderHistoryActivity.this,OrderDetailActivity.class);
        intent.putExtra("Order",order);
        startActivity(intent);
    }
}