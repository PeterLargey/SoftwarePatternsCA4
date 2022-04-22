package com.example.softwarepatternsca4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;


public class StoreFragment extends Fragment {

    private FirebaseFirestore db;
    private storeAdapter adapter;
    private RecyclerView storeCatalogue;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ArrayList<Items> items;
    private SearchView search;
    private Button sort;
    private AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View storeView = inflater.inflate(R.layout.fragment_store, container, false);

        db = FirebaseFirestore.getInstance();

        storeCatalogue = storeView.findViewById(R.id.catalogueRecycler);
        storeCatalogue.addItemDecoration(new DividerItemDecoration(storeView.getContext(), DividerItemDecoration.VERTICAL));

        sort = storeView.findViewById(R.id.sortCustomerCatalogue);

        search = storeView.findViewById(R.id.customerSearch);

        db.collection("Catalogue").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    items = new ArrayList<>();
                    for(DocumentSnapshot doc: task.getResult()){
                        items.add(doc.toObject(Items.class));
                    }
                    setUpRecycler(items, sort, storeView.getContext());
                    sortItems(items);
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

        return storeView;
    }

    private void sortItems(ArrayList<Items> items) {
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Choose how you want to sort the list").setCancelable(false)
                        .setPositiveButton("Ascending", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Collections.sort(items, Items.NameAscendingComparator);
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("Descending", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Collections.sort(items, Items.NameDescendingComparator);
                        adapter.notifyDataSetChanged();
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

    private void setUpRecycler(ArrayList<Items> items, Button sort, Context context) {
        adapter = new storeAdapter(items, sort, context);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        storeCatalogue.setLayoutManager(staggeredGridLayoutManager);
        storeCatalogue.setAdapter(adapter);
    }
}