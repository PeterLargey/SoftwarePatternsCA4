package com.example.softwarepatternsca4;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RatingsFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView ratingRecycler;
    private ratingAdapter adapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private String email, address;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ratingsView = inflater.inflate(R.layout.fragment_ratings, container, false);

        db = FirebaseFirestore.getInstance();

        if(getArguments() != null){
            email = getArguments().getString("email");
            address = getArguments().getString("address");
        }

        ratingRecycler = ratingsView.findViewById(R.id.ratingsRecycler);
        ratingRecycler.addItemDecoration(new DividerItemDecoration(ratingsView.getContext(), DividerItemDecoration.VERTICAL));

        setUpRecycler(email, address, ratingsView.getContext());

        return ratingsView;
    }

    private void setUpRecycler(String email, String address, Context context) {
        Query query = db.collection("Catalogue").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Items> options = new FirestoreRecyclerOptions.Builder<Items>().setQuery(query, Items.class).build();
        adapter = new ratingAdapter(options, email, address, context);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        ratingRecycler.setLayoutManager(staggeredGridLayoutManager);
        ratingRecycler.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }
}