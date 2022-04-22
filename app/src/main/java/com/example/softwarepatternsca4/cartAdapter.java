package com.example.softwarepatternsca4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class cartAdapter extends FirestoreRecyclerAdapter<Items, cartAdapter.CartViewHolder> {

    private Context context;

    public cartAdapter(@NonNull FirestoreRecyclerOptions<Items> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Items model) {
        ImageView deleteButton = holder.itemView.findViewById(R.id.deleteCartItem);

        holder.name.setText(model.getName());
        holder.size.setText(new StringBuilder("Size: ").append(model.getSize()));
        holder.price.setText(new StringBuilder("Price: ").append(model.getPrice()));

        Glide.with(this.context).load(model.getImage()).into(holder.image);

        String docId = getSnapshots().getSnapshot(position).getId();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Cart").document(docId);
                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(view.getContext(), "Item removed from Cart", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), "Item failed to be removed from Cart", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_recycler_layout, parent, false);
        return new CartViewHolder(view);
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView size;
        private TextView price;
        private ImageView image;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cartItemImage);
            name = itemView.findViewById(R.id.cartItemName);
            size = itemView.findViewById(R.id.cartItemSize);
            price = itemView.findViewById(R.id.cartItemPrice);
        }
    }
}
