package com.example.doan_ltdd;

import static java.lang.String.format;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan_ltdd.Adapter.OrderDetailAdapter;
import com.example.doan_ltdd.Adapter.OrderHistoryAdapter;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity implements OrderDetailAdapter.Listener{

    Toolbar toolbar;

    DecimalFormat format = new DecimalFormat("###,###,###");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase database;

    ArrayList<ProductCart> listCartProduct;
    Order order, orderAgain;

    RecyclerView rc_order_info;

    TextView tv_order_info_no,tv_order_info_date,tv_order_info_cust_name,tv_order_info_cust_address,tv_order_info_cust_phone,tv_order_info_num,tv_order_info_total,tv_order_info_status;
    Button btn_order_info_back, btn_order_info_cancel;

    OrderDetailAdapter orderDetailAdapter;
    OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);


        tv_order_info_no = findViewById(R.id.tv_order_info_no);
        tv_order_info_date = findViewById(R.id.tv_order_info_date);
        tv_order_info_cust_name = findViewById(R.id.tv_order_info_cust_name);
        tv_order_info_cust_address = findViewById(R.id.tv_order_info_cust_address);
        tv_order_info_cust_phone = findViewById(R.id.tv_order_info_cust_phone);
        tv_order_info_num = findViewById(R.id.tv_order_info_num);
        tv_order_info_total = findViewById(R.id.tv_order_info_total);
        tv_order_info_status = findViewById(R.id.tv_order_info_status);
        btn_order_info_back = findViewById(R.id.btn_order_info_back);
        btn_order_info_cancel = findViewById(R.id.btn_order_info_cancel);

        toolbar = findViewById(R.id.toolbar);

        rc_order_info = findViewById(R.id.rc_order_info);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Th??ng tin ????n h??ng");

        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser= mAuth.getCurrentUser();
        String userId = mUser.getUid();

        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        listCartProduct = new ArrayList<>();
        order = new Order();


        Intent intent =getIntent();
        order = (Order) intent.getSerializableExtra("Order");

        tv_order_info_no.setText(order.OrderNo);
        tv_order_info_date.setText(order.dateOrder);
        tv_order_info_cust_name.setText(order.custName);
        tv_order_info_cust_address.setText(order.custAddress);
        tv_order_info_cust_phone.setText(order.custPhone);
        tv_order_info_num.setText(String.valueOf(order.numProduct));
        tv_order_info_total.setText(format.format(order.totalPrice)+" VN??");
        tv_order_info_status.setText(order.status);

        listCartProduct = order.listDetailOrder;

        Date date = new Date(System.currentTimeMillis());
        String status = "??ang ch??? x??c nh???n";
        String orderNo = userId+"_"+date.toString()+"_"+date.getTime();

        orderAgain = new Order(orderNo,order.custEmail,order.custAddress,order.custName,order.custPhone,date.toString(),status,order.numProduct,order.totalPrice,order.listDetailOrder);

        rc_order_info.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        orderDetailAdapter = new OrderDetailAdapter(listCartProduct,this);
        rc_order_info.setAdapter(orderDetailAdapter);
        orderDetailAdapter.notifyDataSetChanged();


        btn_order_info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(order.status.equals("???? h???y") || order.status.equals("Giao h??ng th??nh c??ng"))
        {
//            btn_order_info_cancel.setVisibility(View.GONE);
            btn_order_info_cancel.setText("?????t l???i ????n h??ng");
            btn_order_info_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child("DbOrder").child(userId).child(orderNo).setValue(orderAgain).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            orderDetailAdapter.notifyDataSetChanged();
                            Intent intent = new Intent(OrderDetailActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(OrderDetailActivity.this,"?????t h??ng th??nh c??ng",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else
        {
            btn_order_info_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child("DbOrder").child(userId).child(order.OrderNo).child("status").setValue("???? h???y").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(OrderDetailActivity.this,MainActivity.class);
                            orderDetailAdapter.notifyDataSetChanged();
                            startActivity(intent);
                            Toast.makeText(OrderDetailActivity.this,"H???y ????n h??ng th??nh c??ng",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
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

    @Override
    public void onClick(ProductCart productCart) {

    }
}