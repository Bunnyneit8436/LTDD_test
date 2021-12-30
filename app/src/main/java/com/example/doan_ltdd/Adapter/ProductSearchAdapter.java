package com.example.doan_ltdd.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_ltdd.Class.Product;
import com.example.doan_ltdd.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.ProductSearchVH> implements Filterable {


    public ArrayList<Product> arrayListFilter;
    public ArrayList<Product> arrayList;
    ProductSearchAdapter.Listener listener;
    DecimalFormat format = new DecimalFormat("###,###,###");

    public ProductSearchAdapter(ArrayList<Product> arrayList, ProductSearchAdapter.Listener listener){
        this.arrayList=arrayList;
        this.arrayListFilter=arrayList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ProductSearchAdapter.ProductSearchVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_search,parent,false);
        return new ProductSearchAdapter.ProductSearchVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSearchAdapter.ProductSearchVH holder, int position) {

        Product product = arrayListFilter.get(position);

        if (product == null) {
            return;
        }
        else
        {
            holder.tv_product_name_search.setText(product.productName);
            holder.tv_product_price_search.setText(format.format(product.productPrice)+ " VNĐ");
            Glide.with(holder.img_product_search.getContext()).load(Uri.parse(product.productImage)).into(holder.img_product_search);
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
        return arrayListFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new ProductFilter();
    }

    class ProductSearchVH extends RecyclerView.ViewHolder{

        TextView tv_product_name_search, tv_product_price_search;
        ImageView img_product_search;

        public ProductSearchVH(@NonNull View itemView) {
            super(itemView);

            tv_product_name_search = (TextView) itemView.findViewById(R.id.tv_name_product_search);
            tv_product_price_search = (TextView) itemView.findViewById(R.id.tv_price_product_search);
            img_product_search = (ImageView) itemView.findViewById(R.id.img_photo_search);
        }
    }

    public interface Listener{
        void onClick(Product product);
    }

    class ProductFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String charString=charSequence.toString();
            if(charString.isEmpty())
            {
                arrayListFilter=arrayList;
            }
            else
            {
                ArrayList<Product> filteredList=new ArrayList<>();
                for (Product row : arrayList)
                {
                    if(row.productName.toLowerCase().contains(charString.toLowerCase()))
                    {
                        filteredList.add(row);
                    }
                }
                arrayListFilter=filteredList;
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=arrayListFilter;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            arrayListFilter=(ArrayList<Product>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
