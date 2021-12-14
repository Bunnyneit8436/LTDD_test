package com.example.doan_ltdd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doan_ltdd.Class.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase database;
    Button btn_logout, btn_account_setting, btn_history, btn_listview, btn_help;
    TextView tv_name_account, tv_email_account;
    CircleImageView image_account;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_logout = view.findViewById(R.id.btn_logout);
        tv_name_account = view.findViewById(R.id.tv_name_account);
        tv_email_account = view.findViewById(R.id.tv_email_account);
        btn_account_setting = view.findViewById(R.id.btn_account_setting);
        image_account = view.findViewById(R.id.image_account);
        btn_history = view.findViewById(R.id.btn_history);
        btn_listview = view.findViewById(R.id.btn_listview);
        btn_help = view.findViewById(R.id.btn_help);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser= mAuth.getCurrentUser();
        String userId = mUser.getUid();

        database= FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        tv_email_account.setText(mUser.getEmail());
//        image_account.setImageURI(mUser.getPhotoUrl());


        myRef.child("DbUser").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username=snapshot.child("username").getValue().toString();
                tv_name_account.setText(username);
                String urlImage=snapshot.child("urlImage").getValue().toString();
                Glide.with(getContext()).load(Uri.parse(urlImage)).into(image_account);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(),SigninActivity.class);
                startActivity(intent);
            }
        });

        btn_account_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),UpdateProfileActivity.class);
                startActivity(intent);
            }
        });


        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),OrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        btn_listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ListHistoryActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        menuInflater.inflate(R.menu.account_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mnuSetting){
            Intent intent = new Intent(getContext(),UpdateProfileActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.mnuCart){
            Intent intent = new Intent(getContext(),CartActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.mnuFavorite){
            Intent intent = new Intent(getContext(),FavoriteActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.mnuLogout){
            mAuth.signOut();
            Intent intent = new Intent(getContext(),SigninActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}