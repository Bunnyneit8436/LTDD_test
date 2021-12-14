package com.example.doan_ltdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        display(R.id.mnuHome);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                display(item.getItemId());
                return true;
            }
        });

    }

    void display (int id)
    {
        Fragment fragment =null;
        switch (id)
        {
            case R.id.mnuHome:
                toolbar.setTitle("Trang chủ");
                fragment = new HomeFragment();
                break;
            case R.id.mnuDashboard:
                toolbar.setTitle("Danh mục");
                fragment = new DashBoardFragment();
                break;
            case R.id.mnuNotification:
                toolbar.setTitle("Thông báo");
                fragment = new NotificationFragment();
                break;
            case R.id.mnuAccount:
                toolbar.setTitle("Tài khoản");
                fragment = new AccountFragment();
                break;
        }
        FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}