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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class AdminHomeFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView catalogue;
    private AdminCatalogueAdapter adapter;
    private SearchView search;
    private ArrayList<Items> items;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View adminHomeView = inflater.inflate(R.layout.fragment_admin_home, container, false);
        db = FirebaseFirestore.getInstance();

        catalogue = adminHomeView.findViewById(R.id.adminCatalogueRecycler);
        catalogue.addItemDecoration(new DividerItemDecoration(adminHomeView.getContext(), DividerItemDecoration.VERTICAL));

        search = adminHomeView.findViewById(R.id.adminSearch);

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

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });


        return adminHomeView;
    }


    private void setUpRecycler(ArrayList<Items> items){
        adapter = new AdminCatalogueAdapter(items);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        catalogue.setLayoutManager(staggeredGridLayoutManager);
        catalogue.setAdapter(adapter);
    }
}