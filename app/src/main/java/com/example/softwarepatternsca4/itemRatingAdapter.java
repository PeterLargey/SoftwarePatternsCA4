package com.example.softwarepatternsca4;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class itemRatingAdapter extends FirestoreRecyclerAdapter<Rating, itemRatingAdapter.ItemRatingViewHolder> {

    public itemRatingAdapter(@NonNull FirestoreRecyclerOptions<Rating> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemRatingViewHolder holder, int position, @NonNull Rating model) {

    }

    @NonNull
    @Override
    public ItemRatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class ItemRatingViewHolder extends RecyclerView.ViewHolder {

        public ItemRatingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
