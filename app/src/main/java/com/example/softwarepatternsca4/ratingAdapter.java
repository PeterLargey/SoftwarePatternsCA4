package com.example.softwarepatternsca4;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ratingAdapter extends FirestoreRecyclerAdapter<Items, ratingAdapter.RatingViewHolder> {

    private String email;
    private String address;
    private Context context;

    public ratingAdapter(@NonNull FirestoreRecyclerOptions<Items> options, String email, String address, Context context) {
        super(options);
        this.email = email;
        this.address = address;
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RatingViewHolder holder, int position, @NonNull Items model) {

        holder.name.setText(new StringBuilder("Name: ").append(model.getName()));
        holder.category.setText(new StringBuilder("Category: ").append(model.getCategory()));
        holder.manufacturer.setText(new StringBuilder("Manufacturer: ").append(model.getManufacturer()));
        holder.size.setText(new StringBuilder("Size: ").append(model.getSize()));
        holder.price.setText(new StringBuilder("Price: ").append(model.getPrice()));

        Glide.with(this.context).load(model.getImage()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), RatingDetails.class);
                i.putExtra("id", model.getId());
                i.putExtra("name", model.getName());
                i.putExtra("category", model.getCategory());
                i.putExtra("manufacturer", model.getManufacturer());
                i.putExtra("size", model.getSize());
                i.putExtra("price", model.getPrice());
                i.putExtra("email", email);
                i.putExtra("address", address);
                i.putExtra("image", model.getImage());

                view.getContext().startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalogue_rating_recycler_layout, parent, false);
        return new RatingViewHolder(view);
    }

    public class RatingViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView category;
        private TextView manufacturer;
        private TextView size;
        private TextView price;
        private ImageView image;

        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ratingCatalogueImage);
            name = itemView.findViewById(R.id.ratingsCatalogueName);
            category = itemView.findViewById(R.id.ratingsCatalogueCategory);
            manufacturer = itemView.findViewById(R.id.ratingsCatalogueManufacturer);
            size = itemView.findViewById(R.id.ratingsCatalogueSize);
            price = itemView.findViewById(R.id.ratingsCataloguePrice);
        }
    }
}
