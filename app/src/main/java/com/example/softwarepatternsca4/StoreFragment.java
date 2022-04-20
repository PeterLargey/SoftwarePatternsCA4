package com.example.softwarepatternsca4;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class StoreFragment extends Fragment {

    private FirebaseFirestore db;
    private storeAdapter storeAdapter;
    private RecyclerView storeCatalogue;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ArrayList<Items> items;
    private SearchView search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View storeView = inflater.inflate(R.layout.fragment_store, container, false);

        db = FirebaseFirestore.getInstance();

        storeCatalogue = storeView.findViewById(R.id.catalogueRecycler);
        storeCatalogue.addItemDecoration(new DividerItemDecoration(storeView.getContext(), DividerItemDecoration.VERTICAL));

        db.collection("Catalogue").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    items = new ArrayList<>();
                    for(DocumentSnapshot doc: task.getResult()){
                        items.add(doc.toObject(Items.class));
                    }
                    setUpRecycler(items);

                }
            }
        });

        search = storeView.findViewById(R.id.customerSearch);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(items.contains(query)){
                    storeAdapter.getFilter().filter(query);
                }else {
                    Toast.makeText(storeView.getContext(), "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return storeView;
    }

    private void setUpRecycler(ArrayList<Items> items) {
        storeAdapter = new storeAdapter(items);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        storeCatalogue.setLayoutManager(staggeredGridLayoutManager);
        storeCatalogue.setAdapter(storeAdapter);
    }
}