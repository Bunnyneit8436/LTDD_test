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

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryVH> {

    public ArrayList<Order> listOrder;
    Listener listener;

    public OrderHistoryAdapter(ArrayList<Order> listOrder, Listener listener){
        this.listOrder=listOrder;
        this.listener=listener;
    }

    @NonNull
    @Override
    public OrderHistoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history,parent,false);
        return new OrderHistoryAdapter.OrderHistoryVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.OrderHistoryVH holder, int position) {
        Order order = listOrder.get(position);

        if(order == null)
        {
            return;
        }
        else
        {
            holder.tv_hitory_product_orderNo.setText(order.OrderNo);
            holder.tv_hitory_user_name.setText(order.custName);
            holder.tv_hitory_product_num.setText(String.valueOf(order.numProduct));
            holder.tv_hitory_product_price.setText(order.totalPrice+ " VND");
            holder.tv_hitory_product_date.setText(order.dateOrder);
            holder.tv_hitory_product_status.setText(order.status);

            ArrayList<ProductCart> productCarts = order.listDetailOrder;

            Glide.with(holder.img_hitory_product.getContext()).load(Uri.parse(productCarts.get(0).productImage)).into(holder.img_hitory_product);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(order);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(listOrder.size()>0)
        {
            return listOrder.size();
        }
        else return 0;
    }

    class OrderHistoryVH extends RecyclerView.ViewHolder{

        ImageView img_hitory_product;
        TextView tv_hitory_user_name,tv_hitory_product_num,tv_hitory_product_price,tv_hitory_product_date
                ,tv_hitory_product_status,tv_hitory_product_orderNo;

        public OrderHistoryVH(@NonNull View itemView) {
            super(itemView);

            img_hitory_product = itemView.findViewById(R.id.img_hitory_product);
            tv_hitory_user_name = itemView.findViewById(R.id.tv_hitory_user_name);
            tv_hitory_product_num = itemView.findViewById(R.id.tv_hitory_product_num);
            tv_hitory_product_price = itemView.findViewById(R.id.tv_hitory_product_price);
            tv_hitory_product_date = itemView.findViewById(R.id.tv_hitory_product_date);
            tv_hitory_product_status = itemView.findViewById(R.id.tv_hitory_product_status);
            tv_hitory_product_orderNo = itemView.findViewById(R.id.tv_hitory_product_orderNo);

        }
    }

    public interface Listener{
        void onClick(Order order);
    }
}
