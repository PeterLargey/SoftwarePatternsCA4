package com.example.softwarepatternsca4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class checkoutAdapter extends RecyclerView.Adapter<checkoutAdapter.CheckoutViewHolder> {

    private ArrayList<Items> items;

    public checkoutAdapter(ArrayList<Items> items){
        this.items = items;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_recycler_layout, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        holder.name.setText(items.get(position).getName());
        holder.size.setText(items.get(position).getSize());
        holder.price.setText(items.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CheckoutViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView size;
        private TextView price;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.checkoutItemName);
            size = itemView.findViewById(R.id.checkoutItemSize);
            price = itemView.findViewById(R.id.checkoutItemPrice);
        }
    }
}
