package com.example.softwarepatternsca4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class checkoutAdapter extends RecyclerView.Adapter<checkoutAdapter.CheckoutViewHolder> {

    private ArrayList<Items> items;
    private Context context;

    public checkoutAdapter(ArrayList<Items> items, Context context){
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_recycler_layout, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        holder.name.setText(new StringBuilder("Name: ").append(items.get(position).getName()));
        holder.size.setText(new StringBuilder("Size: ").append(items.get(position).getSize()));
        holder.price.setText(new StringBuilder("Price: ").append(items.get(position).getPrice()));
        Glide.with(this.context).load(items.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CheckoutViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView size;
        private TextView price;
        private ImageView image;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.checkoutItemName);
            size = itemView.findViewById(R.id.checkoutItemSize);
            price = itemView.findViewById(R.id.checkoutItemPrice);
            image = itemView.findViewById(R.id.checkoutImage);
        }
    }
}
