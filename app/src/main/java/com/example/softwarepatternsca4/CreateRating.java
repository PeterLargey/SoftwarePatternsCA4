package com.example.softwarepatternsca4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateRating extends AppCompatActivity {

    private FirebaseFirestore db;
    private Intent data;
    private String id, email, name, category, manufacturer, size, price, address, image;
    private EditText comment;
    private RatingBar rating;
    private Button submitRating;
    private final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rating);

        db = FirebaseFirestore.getInstance();

        data = getIntent();

        id = data.getStringExtra("itemId");
        name = data.getStringExtra("name");
        category = data.getStringExtra("category");
        manufacturer = data.getStringExtra("manufacturer");
        size = data.getStringExtra("size");
        price = data.getStringExtra("price");
        email = data.getStringExtra("email");
        address = data.getStringExtra("address");
        image = data.getStringExtra("image");

        comment = findViewById(R.id.ratingComment);
        rating = findViewById(R.id.ratingBar);
        submitRating = findViewById(R.id.submitRating);

        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentString = comment.getText().toString();
                String ratingString = String.valueOf(rating.getRating());

                if(commentString.isEmpty() || ratingString.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are required!", Toast.LENGTH_LONG).show();
                } else {
                    inputDataIntoRatingDB(id, email, commentString, ratingString);
                    Toast.makeText(getApplicationContext(), "Rating added to the Database!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(CreateRating.this, RatingDetails.class);
                    i.putExtra("id", id);
                    i.putExtra("name", name);
                    i.putExtra("category", category);
                    i.putExtra("manufacturer", manufacturer);
                    i.putExtra("size", size);
                    i.putExtra("price", price);
                    i.putExtra("email", email);
                    i.putExtra("address", address);
                    i.putExtra("image", image);
                    startActivity(i);
                }
            }
        });

    }

    private void inputDataIntoRatingDB(String id, String email, String commentString, String ratingString) {
        DocumentReference docRef = db.collection("Ratings").document();
        Map<String, Object> rating = new HashMap<>();
        rating.put("itemId", id);
        rating.put("email", email);
        rating.put("comment", commentString);
        rating.put("rating", ratingString);
        docRef.set(rating).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Rating added to the database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed: Rating was not added to the database");
            }
        });
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
            Intent i = new Intent(CreateRating.this, RatingDetails.class);
            i.putExtra("id", id);
            i.putExtra("name", name);
            i.putExtra("category", category);
            i.putExtra("manufacturer", manufacturer);
            i.putExtra("size", size);
            i.putExtra("price", price);
            i.putExtra("email", email);
            i.putExtra("address", address);
            i.putExtra("image", image);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}