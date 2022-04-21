package com.example.softwarepatternsca4;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class storeAdapter extends RecyclerView.Adapter<storeAdapter.ItemViewHolder> implements Filterable {

    private ArrayList<Items> items;
    private ArrayList<Items> filteredItems;

    public storeAdapter(ArrayList<Items> items){
        this.items = items;
        this.filteredItems = items;
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
                        for(Items i : filteredItems){
                            Log.d("TAG", "Item Name:" + i.getName());
                            if(i.getCategory().toLowerCase().contains(charString) || i.getName().toLowerCase().contains(charString) || i.getManufacturer().toLowerCase().contains(charString)){
                                results.add(i);
                            }
                        }
                    }
                    filterResults.values = results;
                }
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

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_catalogue_recycler_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //Sort out Image
        String stockAvailability;

        ImageView addButton = holder.itemView.findViewById(R.id.addButton);

        String id = filteredItems.get(position).getId();
        String stock = filteredItems.get(position).getStock();

        holder.name.setText(filteredItems.get(position).getName());
        holder.category.setText(new StringBuilder("Category: ").append(filteredItems.get(position).getCategory()).toString());
        holder.manufacturer.setText(new StringBuilder("Manufacturer: ").append(filteredItems.get(position).getManufacturer()).toString());
        holder.size.setText(new StringBuilder("Size: ").append(filteredItems.get(position).getSize()).toString());
        holder.price.setText(new StringBuilder("Price: €").append(filteredItems.get(position).getPrice()).toString());
        int stockLevel = Integer.parseInt(filteredItems.get(position).getStock());
        if(stockLevel > 0){
            stockAvailability = "Available";
        } else{
            stockAvailability = "Unavailable";
            addButton.setEnabled(false);
        }
        holder.status.setText(new StringBuilder("Status: ").append(stockAvailability).toString());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = holder.category.getText().toString();
                String manufacturer = holder.manufacturer.getText().toString();
                String size = holder.size.getText().toString();
                String price = holder.price.getText().toString();
                String[] categorySplit = category.split(": ");
                String[] manufacturerSplit = manufacturer.split(": ");
                String[] sizeSplit = size.split(": ");
                String[] priceSplit = price.split(": ");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Cart").document();
                Map<String, Object> add = new HashMap<>();
                add.put("name", holder.name.getText().toString());
                add.put("category", categorySplit[1]);
                add.put("manufacturer", manufacturerSplit[1]);
                add.put("size", sizeSplit[1]);
                add.put("price", priceSplit[1]);
                add.put("id", id);
                add.put("stock", stock);

                docRef.set(add).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(view.getContext(), "Item added to Cart", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView name;
        private TextView category;
        private TextView manufacturer;
        private TextView size;
        private TextView price;
        private TextView status;

        public ItemViewHolder(@NonNull View itemView) {

            super(itemView);
            image = itemView.findViewById(R.id.itemImage);
            name = itemView.findViewById(R.id.customerItemName);
            category = itemView.findViewById(R.id.customerCategory);
            manufacturer = itemView.findViewById(R.id.customerManufacturer);
            size = itemView.findViewById(R.id.customerSize);
            price = itemView.findViewById(R.id.customerPrice);
            status = itemView.findViewById(R.id.customerStatus);
        }
    }

}
