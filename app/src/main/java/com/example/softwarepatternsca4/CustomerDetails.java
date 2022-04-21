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
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CustomerDetails extends AppCompatActivity {

    private Intent data;
    private String name , email, address;
    private TextView customerName, customerEmail, customerAddress;
    private RecyclerView customerPurchases;
    private salesAdapter adapter;
    private FirebaseFirestore db;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        data = getIntent();
        db = FirebaseFirestore.getInstance();

        name = data.getStringExtra("name");
        email = data.getStringExtra("email");
        address = data.getStringExtra("address");

        customerName = findViewById(R.id.customerName);
        customerEmail = findViewById(R.id.customerEmail);
        customerAddress = findViewById(R.id.customerAddress);

        customerName.setText(name);
        customerEmail.setText(email);
        customerAddress.setText(address);

        customerPurchases = findViewById(R.id.customerPurchases);
        customerPurchases.addItemDecoration(new DividerItemDecoration(CustomerDetails.this, DividerItemDecoration.VERTICAL));

        setUpPurchasesRecycler(name, address);

    }

    private void setUpPurchasesRecycler(String name, String address) {
        Query query = db.collection("Sales").whereEqualTo("customerEmail", email).orderBy("date", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Sales> options = new FirestoreRecyclerOptions.Builder<Sales>().setQuery(query, Sales.class).build();
        adapter = new salesAdapter(options, name, address);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        customerPurchases.setLayoutManager(staggeredGridLayoutManager);
        customerPurchases.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter != null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.back){
            Intent i = new Intent(CustomerDetails.this, AdminMain.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}