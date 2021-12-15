package com.example.doan_ltdd.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_ltdd.Class.Product;
import com.example.doan_ltdd.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductVH> {


    public ArrayList<Product> arrayList;
    Listener listener;
    DecimalFormat format = new DecimalFormat("###,###,###");

    public ProductAdapter(ArrayList<Product> arrayList, Listener listener){
        this.arrayList=arrayList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ProductVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductVH holder, int position) {

        Product product = arrayList.get(position);

        if (product == null) {
            return;
        }
        else
        {
            holder.tv_product_name.setText(product.productName);
            holder.tv_product_price.setText(format.format(product.productPrice)+ " VNĐ");
            Glide.with(holder.img_product.getContext()).load(Uri.parse(product.productImage)).into(holder.img_product);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(product);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ProductVH extends RecyclerView.ViewHolder{

        TextView tv_product_name, tv_product_price;
        ImageView img_product;

        public ProductVH(@NonNull View itemView) {
            super(itemView);

            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            tv_product_price = (TextView) itemView.findViewById(R.id.tv_product_price);
            img_product = (ImageView) itemView.findViewById(R.id.img_product);
        }
    }

    public interface Listener{
        void onClick(Product product);
    }
}
