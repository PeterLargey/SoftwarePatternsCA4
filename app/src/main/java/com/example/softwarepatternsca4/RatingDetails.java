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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RatingDetails extends AppCompatActivity {

    private FirebaseFirestore db;
    private Intent data;
    private String name, category, manufacturer, size, price, id, email, address;
    private RecyclerView itemRatingRecycler;
    private itemRatingAdapter adapter;
    private TextView itemName, itemCategory, itemManufacturer, itemSize, itemPrice;
    private Button createReview;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_details);

        db = FirebaseFirestore.getInstance();
        data = getIntent();

        id = data.getStringExtra("id");
        name = data.getStringExtra("name");
        category = data.getStringExtra("category");
        manufacturer = data.getStringExtra("manufacturer");
        size = data.getStringExtra("size");
        price = data.getStringExtra("price");
        email = data.getStringExtra("email");
        address = data.getStringExtra("address");

        itemName = findViewById(R.id.ratingDetailName);
        itemCategory = findViewById(R.id.ratingDetailCategory);
        itemManufacturer = findViewById(R.id.ratingDetailManufacturer);
        itemSize = findViewById(R.id.ratingDetailSize);
        itemPrice = findViewById(R.id.ratingDetailPrice);

        itemName.setText(new StringBuilder("Name: ").append(name));
        itemCategory.setText(new StringBuilder("Category: ").append(category));
        itemManufacturer.setText(new StringBuilder("Manufacturer: ").append(manufacturer));
        itemSize.setText(new StringBuilder("Size: ").append(size));
        itemPrice.setText(new StringBuilder("Price: €").append(price));

        itemRatingRecycler = findViewById(R.id.itemRatingRecycler);
        itemRatingRecycler.addItemDecoration(new DividerItemDecoration(RatingDetails.this, DividerItemDecoration.VERTICAL));

        setUpRecycler(id);

        createReview = findViewById(R.id.createReview);
        createReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RatingDetails.this, CreateRating.class);
                i.putExtra("itemId", id);
                i.putExtra("name", name);
                i.putExtra("category", category);
                i.putExtra("manufacturer", manufacturer);
                i.putExtra("size", size);
                i.putExtra("price", price);
                i.putExtra("email", email);
                i.putExtra("address", address);
                startActivity(i);
            }
        });

    }

    private void setUpRecycler(String id) {
        Query query = db.collection("Ratings").whereEqualTo("itemId", id).orderBy("email", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Rating> options = new FirestoreRecyclerOptions.Builder<Rating>().setQuery(query, Rating.class).build();
        adapter = new itemRatingAdapter(options);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        itemRatingRecycler.setLayoutManager(staggeredGridLayoutManager);
        itemRatingRecycler.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.back){
            Intent i = new Intent(RatingDetails.this, CustomerMain.class);
            i.putExtra("email", email);
            i.putExtra("address", address);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter != null){
            adapter.startListening();
        }
    }
}