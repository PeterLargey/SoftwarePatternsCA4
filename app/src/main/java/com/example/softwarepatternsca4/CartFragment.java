package com.example.softwarepatternsca4;

import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class CartFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView cartRecycler;
    private Button checkout;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private cartAdapter adapter;
    private String email, address;
    private ArrayList<Items> items;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View cartView = inflater.inflate(R.layout.fragment_cart, container, false);

        db = FirebaseFirestore.getInstance();
        if(getArguments() != null){
            email = getArguments().getString("email");
            address = getArguments().getString("address");
        }

        cartRecycler = cartView.findViewById(R.id.cartRecycler);
        cartRecycler.addItemDecoration(new DividerItemDecoration(cartView.getContext(), DividerItemDecoration.VERTICAL));
        setUpRecycler();

        checkout = cartView.findViewById(R.id.checkout);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            items = new ArrayList<>();
                            for(QueryDocumentSnapshot doc: task.getResult()){
                                items.add(doc.toObject(Items.class));
                            }

                            double total = 0.00;
                            for(Items s : items){
                                double amount = Double.parseDouble(s.getPrice());
                                total = total + amount;
                            }

                            String totalString = String.valueOf(total);

                            Intent i = new Intent(cartView.getContext(), Checkout.class);
                            i.putParcelableArrayListExtra("items", (ArrayList<Items>) items);
                            i.putExtra("email", email);
                            i.putExtra("address", address);
                            i.putExtra("total", totalString);
                            view.getContext().startActivity(i);
                        }
                    }
                });
            }
        });

        return cartView;
    }

    private void setUpRecycler() {
        Query query = db.collection("Cart").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Items> options = new FirestoreRecyclerOptions.Builder<Items>().setQuery(query, Items.class).build();
        adapter = new cartAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        cartRecycler.setLayoutManager(staggeredGridLayoutManager);
        cartRecycler.setAdapter(adapter);

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