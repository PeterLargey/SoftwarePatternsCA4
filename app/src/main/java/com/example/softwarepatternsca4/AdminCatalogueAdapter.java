package com.example.softwarepatternsca4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminCatalogueAdapter extends RecyclerView.Adapter<AdminCatalogueAdapter.AdminViewHolder> implements Filterable {

    private ArrayList<Items> items;
    private ArrayList<Items> filteredItems;

    public AdminCatalogueAdapter(ArrayList<Items> items){
        this.items = items;
        this.filteredItems = items;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_catalogue_recycler_layout, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        holder.id.setText(filteredItems.get(position).getId());
        holder.name.setText(filteredItems.get(position).getName());
        holder.category.setText(filteredItems.get(position).getCategory());
        holder.manufacturer.setText(filteredItems.get(position).getManufacturer());
        holder.size.setText(filteredItems.get(position).getSize());
        holder.price.setText(filteredItems.get(position).getPrice());
        holder.stock.setText(filteredItems.get(position).getStock());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ItemDetails.class);
                i.putExtra("id", holder.id.getText().toString());
                i.putExtra("name", holder.name.getText().toString());
                i.putExtra("category", holder.category.getText().toString());
                i.putExtra("manufacturer", holder.manufacturer.getText().toString());
                i.putExtra("size", holder.size.getText().toString());
                i.putExtra("price", holder.price.getText().toString());
                i.putExtra("stock", holder.stock.getText().toString());
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                final FilterResults filterResults = new FilterResults();
                ArrayList<Items> results = new ArrayList<>();
                if(filteredItems == null){
                    filteredItems = items;
                }
                if(!charString.isEmpty()){
                    if(filteredItems != null && filteredItems.size() > 0){
                        for(Items i: filteredItems){
                            Log.d("TAG", "Item Name:" + i.getName());
                            if(i.getCategory().toLowerCase().contains(charString) || i.getName().toLowerCase().contains(charString) || i.getManufacturer().toLowerCase().contains(charString)){
                                results.add(i);
                            }
                        }
                    }
                    filterResults.values = results;
                }
//                if(charString.isEmpty()){
//                    filteredItems = items;
//                } else {
//                    for(Items i: filteredItems){
//                        Log.d("TAG", "Item Name:" + i.getName());
//                        if(i.getCategory().toLowerCase().contains(charString) || i.getName().toLowerCase().contains(charString) || i.getManufacturer().toLowerCase().contains(charString)){
//                            results.add(i);
//                        }
//                    }
//                    filterResults.values = results;
//                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if(filterResults.values == null){
                    filteredItems = items;
                    notifyDataSetChanged();
                } else {
                    filteredItems = (ArrayList<Items>) filterResults.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {

        private TextView id;
        private TextView name;
        private TextView category;
        private TextView manufacturer;
        private TextView size;
        private TextView price;
        private TextView stock;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.adminItemID);
            name = itemView.findViewById(R.id.adminItemName);
            category = itemView.findViewById(R.id.adminCategory);
            manufacturer = itemView.findViewById(R.id.adminManufacturer);
            size = itemView.findViewById(R.id.adminSize);
            price = itemView.findViewById(R.id.adminPrice);
            stock = itemView.findViewById(R.id.adminStock);
        }
    }


}
