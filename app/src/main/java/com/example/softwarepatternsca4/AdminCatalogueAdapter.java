package com.example.softwarepatternsca4;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class AdminCatalogueAdapter extends RecyclerView.Adapter<AdminCatalogueAdapter.AdminViewHolder> implements Filterable {

    private ArrayList<Items> items;
    private ArrayList<Items> filteredItems;
    private String docId;
    private AlertDialog.Builder builder;
    private Button sort;

    public AdminCatalogueAdapter(ArrayList<Items> items, Button sort){
        this.items = items;
        this.filteredItems = items;
        this.sort = sort;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_catalogue_recycler_layout, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {

        ImageView deleteButton = holder.itemView.findViewById(R.id.deleteCatalogueItem);

        String id = filteredItems.get(position).getId();
        String image = filteredItems.get(position).getImage();

        holder.id.setText(new StringBuilder("ID: ").append(filteredItems.get(position).getId()));
        holder.name.setText(filteredItems.get(position).getName());
        holder.category.setText(filteredItems.get(position).getCategory());
        holder.manufacturer.setText(filteredItems.get(position).getManufacturer());
        holder.size.setText(filteredItems.get(position).getSize());
        holder.price.setText(new StringBuilder("€").append(filteredItems.get(position).getPrice()));
        holder.stock.setText(filteredItems.get(position).getStock());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ItemDetails.class);
                i.putExtra("id", id);
                i.putExtra("name", holder.name.getText().toString());
                i.putExtra("category", holder.category.getText().toString());
                i.putExtra("manufacturer", holder.manufacturer.getText().toString());
                i.putExtra("size", holder.size.getText().toString());
                i.putExtra("price", holder.price.getText().toString());
                i.putExtra("stock", holder.stock.getText().toString());
                i.putExtra("image", image);
                view.getContext().startActivity(i);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Catalogue").whereEqualTo("id", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                docId = doc.getId();
                            }
                            builder = new AlertDialog.Builder(view.getContext());
                            builder.setMessage("Would you like to delete this catalogue item?").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            DocumentReference docRef = db.collection("Catalogue").document(docId);
                                            docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(view.getContext(), "Item removed from Catalogue", Toast.LENGTH_LONG).show();
                                                    for(Items i : filteredItems){
                                                        if(i.getId().equalsIgnoreCase(id)){
                                                            filteredItems.remove(i);
                                                        }
                                                    }
                                                    notifyDataSetChanged();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(view.getContext(), "Failed: Item was not removed from Catalogue", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                });
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

                sort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder = new AlertDialog.Builder(view.getContext());
                        builder.setMessage("Choose how you want to sort the list").setCancelable(false)
                                .setPositiveButton("Ascending", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Collections.sort(filteredItems, Items.NameAscendingComparator);
                                        notifyDataSetChanged();
                                    }
                                }).setNegativeButton("Descending", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Collections.sort(filteredItems, Items.NameDescendingComparator);
                                notifyDataSetChanged();
                            }
                        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
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
