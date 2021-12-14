package com.example.doan_ltdd;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doan_ltdd.Adapter.ProductAdapter;
import com.example.doan_ltdd.Adapter.SlidePhotoAdapter;
import com.example.doan_ltdd.Class.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ProductAdapter.Listener{

    RecyclerView rc_product;
    ArrayList<Product> productList;
    ProductAdapter productAdapter;


    ViewPager mViewPager;

    // images array
    int[] images = {R.drawable.slider1, R.drawable.slide2, R.drawable.slider3, R.drawable.slider4};

    // Creating Object of ViewPagerAdapter
    SlidePhotoAdapter slidePhotoAdapter;

    CircleIndicator circleIndicator;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(mViewPager.getCurrentItem() == images.length-1)
            {
                mViewPager.setCurrentItem(0);
            }
            else
            {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
            }

        }
    };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        productList = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        circleIndicator=view.findViewById(R.id.circle_indicator);

        // Initializing the ViewPager Object
        mViewPager = (ViewPager)view.findViewById(R.id.vp_slide_photo);

        // Initializing the ViewPagerAdapter
        slidePhotoAdapter = new SlidePhotoAdapter(getContext(), images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(slidePhotoAdapter);

        circleIndicator.setViewPager(mViewPager);

        // product list
        rc_product = view.findViewById(R.id.rc_product);
        getDataProduct();
        rc_product.setLayoutManager(new GridLayoutManager(getContext(),2));
        productAdapter = new ProductAdapter(productList,this);
        rc_product.setAdapter(productAdapter);


        handler.postDelayed(runnable,2000);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,5000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        menuInflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mnuSearch){
            Intent intent = new Intent(getContext(),SearchActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.mnuFavorite){
            Intent intent = new Intent(getContext(),FavoriteActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.mnuCart){
            Intent intent = new Intent(getContext(),CartActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable,5000);
    }

    @Override
    public void onClick(Product product) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        myRef.child("DbUser").child(mAuth.getCurrentUser().getUid()).child("ListViewHistory").child(product.productID).setValue(product);

        Intent intent = new Intent(getActivity(), DetailProductActivity.class);
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
//                    product.setId(data.getKey());
                    productList.add(product);
                }

                productAdapter.notifyDataSetChanged();
//                setProductSearchAdapter(mListProduct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Không tải được dữ liệu từ firebase"
                        +error.toString(),Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCancelled"+ error.toString());
            }
        });
    }
}