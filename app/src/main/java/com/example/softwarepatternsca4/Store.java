package com.example.softwarepatternsca4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Store extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private storeAdapter itemAdapter;
    private RecyclerView items;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        getSupportActionBar().setTitle("Store");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        items = findViewById(R.id.catalogueRecycler);
        items.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        setUpRecycler();
    }

    private void setUpRecycler() {
        Query query = db.collection("Catalogue").orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Items> options = new FirestoreRecyclerOptions.Builder<Items>().setQuery(query, Items.class).build();
        itemAdapter = new storeAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        items.setLayoutManager(staggeredGridLayoutManager);
        items.setAdapter(itemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.logout){
            mAuth.signOut();
            finish();
            Intent i = new Intent(Store.this, MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}