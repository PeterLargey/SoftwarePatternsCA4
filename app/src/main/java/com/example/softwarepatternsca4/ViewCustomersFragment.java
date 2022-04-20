package com.example.softwarepatternsca4;

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


public class ViewCustomersFragment extends Fragment {

    private RecyclerView customers;
    private FirebaseFirestore db;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private CustomerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View displayCustomersView = inflater.inflate(R.layout.fragment_view_customers, container, false);
        db = FirebaseFirestore.getInstance();

        customers = displayCustomersView.findViewById(R.id.customerRecycler);
        customers.addItemDecoration(new DividerItemDecoration(displayCustomersView.getContext(), DividerItemDecoration.VERTICAL));

        setUpRecycler();

        return displayCustomersView;
    }

    private void setUpRecycler() {
        Query query = db.collection("Customers").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Customer> options = new FirestoreRecyclerOptions.Builder<Customer>().setQuery(query, Customer.class).build();
        adapter = new CustomerAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        customers.setLayoutManager(staggeredGridLayoutManager);
        customers.setAdapter(adapter);
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