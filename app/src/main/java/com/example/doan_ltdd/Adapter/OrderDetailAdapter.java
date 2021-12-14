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
import com.example.doan_ltdd.Class.Order;
import com.example.doan_ltdd.Class.Product;
import com.example.doan_ltdd.Class.ProductCart;
import com.example.doan_ltdd.R;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailVH> {

    ArrayList<ProductCart> listProduct;
    Listener listener;

    public OrderDetailAdapter(ArrayList<ProductCart> listProduct, Listener listener){
        this.listProduct=listProduct;
        this.listener=listener;
    }

    @NonNull
    @Override
    public OrderDetailVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_product,parent,false);
        return new OrderDetailAdapter.OrderDetailVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailVH holder, int position) {

        ProductCart productCart = listProduct.get(position);

        if(productCart == null)
        {
            return;
        }
        else
        {
            Glide.with(holder.img_photo_order.getContext()).load(Uri.parse(productCart.productImage)).into(holder.img_photo_order);
            holder.tv_name_product_order.setText(productCart.productName);
            holder.tv_price_product_order.setText(productCart.productPrice + " VNĐ");
            holder.tv_count_order.setText(String.valueOf(productCart.numProduct));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(productCart);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }


    class OrderDetailVH extends RecyclerView.ViewHolder{

        ImageView img_photo_order;
        TextView tv_name_product_order,tv_price_product_order,tv_count_order;

        public OrderDetailVH(@NonNull View itemView) {
            super(itemView);

            img_photo_order = itemView.findViewById(R.id.img_photo_order);
            tv_name_product_order = itemView.findViewById(R.id.tv_name_product_order);
            tv_price_product_order = itemView.findViewById(R.id.tv_price_product_order);
            tv_count_order = itemView.findViewById(R.id.tv_count_order);
        }
    }

    public interface Listener{
        void onClick(ProductCart productCart);
    }
}
