package com.example.softwarepatternsca4;

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
                if(charString.isEmpty()){
                    filteredItems = items;
                } else {
                    ArrayList<Items> filteringItems = new ArrayList<>();
                    for(Items i: items){
                        if(i.getCategory().toLowerCase().contains(charString) || i.getName().toLowerCase().contains(charString) || i.getManufacturer().toLowerCase().contains(charString)){
                            filteringItems.add(i);
                        }
                    }
                    filteredItems = filteringItems;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredItems;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredItems = (ArrayList<Items>) filterResults.values;
                notifyDataSetChanged();
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

        holder.name.setText(filteredItems.get(position).getName());
        holder.category.setText(new StringBuilder("Category: ").append(filteredItems.get(position).getCategory()).toString());
        holder.manufacturer.setText(new StringBuilder("Manufacturer: ").append(filteredItems.get(position).getManufacturer()).toString());
        holder.size.setText(new StringBuilder("Size: ").append(filteredItems.get(position).getSize()).toString());
        holder.price.setText(new StringBuilder("Price: ").append(filteredItems.get(position).getPrice()).toString());
        if(filteredItems.get(position).getStockLevel() > 0){
            stockAvailability = "Available";
        } else{
            stockAvailability = "Unavailable";
            addButton.setEnabled(false);
        }
        holder.status.setText(new StringBuilder("Status: ").append(stockAvailability).toString());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Cart").document();
                Map<String, Object> add = new HashMap<>();
                add.put("name", holder.name.getText().toString());
                add.put("category", holder.category.getText().toString());
                add.put("manufacturer", holder.manufacturer.getText().toString());
                add.put("size", holder.size.getText().toString());
                add.put("price", holder.price.getText().toString());

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
