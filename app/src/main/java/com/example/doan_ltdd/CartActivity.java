package com.example.doan_ltdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doan_ltdd.Adapter.ProductCartAdapter;
import com.example.doan_ltdd.Class.Order;
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

import java.sql.Date;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase database;
    Toolbar toolbar;
    EditText edt_cart_cust_name,edt_cart_cust_address,edt_cart_cust_email,edt_cart_cust_phone;
    TextView tv_cart_total_price;

    public int totalPrice, totalNum;
    RelativeLayout rl_cart_empty, rl_cart;
//    RelativeLayout rl_cart;
    ArrayList<ProductCart> listCartProduct;
    RecyclerView rc_cart_product;
    Button btn_cart_order;

    Order custOrder;

    ProductCartAdapter productCartAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar=findViewById(R.id.toolbar);
        edt_cart_cust_name = findViewById(R.id.edt_cart_cust_name);
        edt_cart_cust_address = findViewById(R.id.edt_cart_cust_address);
        edt_cart_cust_email = findViewById(R.id.edt_cart_cust_email);
        edt_cart_cust_phone = findViewById(R.id.edt_cart_cust_phone);
        tv_cart_total_price = findViewById(R.id.tv_cart_total_price);
        rl_cart = findViewById(R.id.rl_cart);
        rl_cart_empty = findViewById(R.id.rl_cart_empty);
        rc_cart_product =findViewById(R.id.rc_cart_product);
        btn_cart_order = findViewById(R.id.btn_cart_order);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Giỏ hàng");

        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser= mAuth.getCurrentUser();
        String userId = mUser.getUid();

        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        listCartProduct = new ArrayList<>();

        myRef.child("DbUser").child(userId).child("UserCart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()){
                    ProductCart productCart = data.getValue(ProductCart.class);
                    listCartProduct.add(productCart);
                    totalPrice= totalPrice+productCart.totalPrice;
                    totalNum = totalNum+productCart.numProduct;
                }

                productCartAdapter.notifyDataSetChanged();
                tv_cart_total_price.setText( totalPrice +" VNĐ" );
                if(listCartProduct.size()==0)
                {
                    setVisible(false);
                }
                else
                {
                    setVisible(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this,"Không tải được dữ liệu từ firebase"
                        +error.toString(),Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCancelled"+ error.toString());
            }
        });




        rc_cart_product.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        productCartAdapter = new ProductCartAdapter(listCartProduct);
        rc_cart_product.setAdapter(productCartAdapter);

        myRef.child("DbUser").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("name").getValue().toString();
                edt_cart_cust_name.setText(name);
                String address=snapshot.child("address").getValue().toString();
                edt_cart_cust_address.setText(address);
                String email=snapshot.child("email").getValue().toString();
                edt_cart_cust_email.setText(email);
                String phone=snapshot.child("phone").getValue().toString();
                edt_cart_cust_phone.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btn_cart_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cust_name = edt_cart_cust_name.getText().toString();
                String cust_email = edt_cart_cust_email.getText().toString();
                String cust_address = edt_cart_cust_address.getText().toString();
                String cust_phone= edt_cart_cust_phone.getText().toString();
                Date date = new Date(System.currentTimeMillis());
                String status = "Đang chờ xác nhận";
                String orderNo = userId+"_"+date.toString()+"_"+date.getTime();

                if(!cust_name.isEmpty() && !cust_email.isEmpty() && !cust_address.isEmpty() && !cust_phone.isEmpty() && listCartProduct.size()>0)
                {
                    custOrder = new Order(orderNo,cust_email,cust_address,cust_name,cust_phone,date.toString(),status,totalNum,totalPrice,listCartProduct);
                    myRef.child("DbOrder").child(userId).child(orderNo).setValue(custOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            myRef.child("DbUser").child(userId).child("UserCart").removeValue();
                            listCartProduct.clear();

                            productCartAdapter.notifyDataSetChanged();
                            Intent intent = new Intent(CartActivity.this,OrderSuccessActivity.class);
                            startActivity(intent);
                            Toast.makeText(CartActivity.this,"Đặt hàng thành công",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public void setVisible(boolean flag)
    {
        if(!flag)
        {
            rl_cart_empty.setVisibility(View.VISIBLE);
            rl_cart.setVisibility(View.GONE);
        }
        else
        {
            rl_cart_empty.setVisibility(View.GONE);
            rl_cart.setVisibility(View.VISIBLE);
        }
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