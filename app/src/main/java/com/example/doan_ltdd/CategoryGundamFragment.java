package com.example.doan_ltdd;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doan_ltdd.Adapter.ProductAdapter;
import com.example.doan_ltdd.Class.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryGundamFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CategoryGundamFragment extends Fragment implements ProductAdapter.Listener{

    RecyclerView rc_product_cate;
    ArrayList<Product> productList;
    ProductAdapter productAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashBoardDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryGundamFragment newInstance(String param1, String param2) {
        CategoryGundamFragment fragment = new CategoryGundamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryGundamFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_dash_board_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rc_product_cate = view.findViewById(R.id.rc_product_cate);

        getDataProduct();
        rc_product_cate.setLayoutManager(new GridLayoutManager(getContext(),2));
        productAdapter = new ProductAdapter(productList,this);
        rc_product_cate.setAdapter(productAdapter);
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
                    if(product.categoryName.equals("Gundam"))
                    {
                        productList.add(product);
                    }
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