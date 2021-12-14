package com.example.doan_ltdd.Adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_ltdd.CartActivity;
import com.example.doan_ltdd.Class.Product;
import com.example.doan_ltdd.Class.ProductCart;
import com.example.doan_ltdd.R;

import java.util.ArrayList;
import java.util.List;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.ProductCartVH> {

    public ArrayList<ProductCart> listCartProduct;

    public ProductCartAdapter(ArrayList<ProductCart> listCartProduct)
    {
        this.listCartProduct = listCartProduct;
    }

    @NonNull
    @Override
    public ProductCartVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        return new ProductCartVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCartVH holder, int position) {

        ProductCart product = listCartProduct.get(position);
        if (product == null) {
            return;
        } else {
            Glide.with(holder.img_photo_cart.getContext()).load(Uri.parse(product.productImage)).into(holder.img_photo_cart);
            holder.tv_name_product_cart.setText(product.productName);
            holder.tv_price_product_cart.setText(product.productPrice + " VNĐ");
            holder.tv_count_cart.setText(String.valueOf(product.numProduct));
            holder.img_remove_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listCartProduct.size();
    }


    public class ProductCartVH extends RecyclerView.ViewHolder{

        ImageView img_photo_cart,img_remove_cart;
        TextView tv_name_product_cart, tv_price_product_cart, tv_count_cart;

        public ProductCartVH(@NonNull View itemView) {
            super(itemView);

            img_photo_cart = itemView.findViewById(R.id.img_photo_cart);
//            img_minus_cart = itemView.findViewById(R.id.img_minus_cart);
//            img_plus_cart = itemView.findViewById(R.id.img_plus_cart);
            img_remove_cart = itemView.findViewById(R.id.img_remove_cart);
            tv_name_product_cart = itemView.findViewById(R.id.tv_name_product_cart);
            tv_price_product_cart = itemView.findViewById(R.id.tv_price_product_cart);
            tv_count_cart = itemView.findViewById(R.id.tv_count_cart);

        }
    }
}


