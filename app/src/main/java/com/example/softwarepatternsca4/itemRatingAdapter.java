package com.example.softwarepatternsca4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

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
        holder.comment.setText(model.getComment());
        holder.rating.setRating(Float.parseFloat(model.getRating()));
        holder.reviewer.setText(model.getEmail());
    }

    @NonNull
    @Override
    public ItemRatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating_recycler_layout, parent, false);
        return new ItemRatingViewHolder(view);
    }

    public class ItemRatingViewHolder extends RecyclerView.ViewHolder {

        private TextView comment;
        private RatingBar rating;
        private TextView reviewer;
        public ItemRatingViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            rating = itemView.findViewById(R.id.rating);
            reviewer = itemView.findViewById(R.id.ratingEmail);
        }
    }
}
