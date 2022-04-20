package com.example.softwarepatternsca4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class soldItemsAdapter extends RecyclerView.Adapter<soldItemsAdapter.SoldItemsViewHolder> {

    private List<Items> itemList;

    public soldItemsAdapter(List<Items> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public SoldItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sold_items_recycler_layout, parent, false);
        return new SoldItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoldItemsViewHolder holder, int position) {
        holder.name.setText(itemList.get(position).getName());
        holder.category.setText(itemList.get(position).getCategory());
        holder.manufacturer.setText(itemList.get(position).getManufacturer());
        holder.size.setText(itemList.get(position).getSize());
        holder.price.setText(itemList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class SoldItemsViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView category;
        private TextView manufacturer;
        private TextView size;
        private TextView price;


        public SoldItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.soldItemName);
            category = itemView.findViewById(R.id.soldItemCategory);
            manufacturer = itemView.findViewById(R.id.soldItemManufacturer);
            size = itemView.findViewById(R.id.soldItemSize);
            price = itemView.findViewById(R.id.soldItemPrice);
        }
    }

}
