package com.example.doan_ltdd;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends Fragment {

    ImageView img_cate_gundam, img_cate_figure;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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
        return inflater.inflate(R.layout.fragment_dash_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        img_cate_gundam = view.findViewById(R.id.img_cate_gundam);
        img_cate_figure = view.findViewById(R.id.img_cate_figure);

        img_cate_gundam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryGundamFragment Fragment = new CategoryGundamFragment();
//                Bundle arguments = new Bundle();
//                Fragment.setArguments(arguments);

                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.content,Fragment);
                ft.addToBackStack(" ");
                ft.commit();

            }
        });

        img_cate_figure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryFigureFragment Fragment = new CategoryFigureFragment();
//                Bundle arguments = new Bundle();
//                Fragment.setArguments(arguments);

                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.content,Fragment);
                ft.addToBackStack(" ");
                ft.commit();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        menuInflater.inflate(R.menu.home_menu_1, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mnuFavorite){
            Intent intent = new Intent(getContext(),FavoriteActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.mnuCart){
            Intent intent = new Intent(getContext(),CartActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}