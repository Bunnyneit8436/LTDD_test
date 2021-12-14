package com.example.doan_ltdd.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.doan_ltdd.R;
import com.example.doan_ltdd.SigninActivity;

public class CategoryActivity extends AppCompatActivity {

    Button btn_them, btn_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        btn_them = findViewById(R.id.btn_them);
        btn_product = findViewById(R.id.btn_product);

        btn_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(CategoryActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });
    }
}