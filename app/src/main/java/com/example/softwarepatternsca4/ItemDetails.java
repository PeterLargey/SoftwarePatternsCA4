package com.example.softwarepatternsca4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ItemDetails extends AppCompatActivity {

    private Intent data;
    private String id, name, category, manufacturer, size, price, stock;
    private EditText itemName, itemCategory, itemManufacturer, itemSize, itemPrice, itemStock;
    private Button update;
    private FirebaseFirestore db;
    private String docId;
    private final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        db = FirebaseFirestore.getInstance();
        data = getIntent();
        id = data.getStringExtra("id");
        name = data.getStringExtra("name");
        category = data.getStringExtra("category");
        manufacturer = data.getStringExtra("manufacturer");
        size = data.getStringExtra("size");
        price = data.getStringExtra("price");
        stock = data.getStringExtra("stock");

        itemName = findViewById(R.id.itemName);
        itemCategory = findViewById(R.id.itemCategory);
        itemManufacturer = findViewById(R.id.itemManufacturer);
        itemSize = findViewById(R.id.itemSize);
        itemPrice = findViewById(R.id.itemPrice);
        itemStock = findViewById(R.id.itemStock);

        itemName.setText(name);
        itemCategory.setText(category);
        itemManufacturer.setText(manufacturer);
        itemSize.setText(size);
        itemPrice.setText(price);
        itemStock.setText(stock);


        update = findViewById(R.id.updateItem);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameString = itemName.getText().toString();
                String categoryString = itemCategory.getText().toString();
                String manufacturerString = itemManufacturer.getText().toString();
                String sizeString = itemSize.getText().toString();
                String priceString = itemPrice.getText().toString();
                String stockString = itemStock.getText().toString();
                if(nameString.isEmpty() || categoryString.isEmpty() || manufacturerString.isEmpty() || sizeString.isEmpty() || priceString.isEmpty() || stockString.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields Required", Toast.LENGTH_LONG).show();
                } else {
                    db.collection("Catalogue").whereEqualTo("id", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot doc : task.getResult()){
                                    docId = doc.getId();
                                    Log.d(TAG, "Document ID: " + docId);
                                }

                                updateCatalogueItem(docId, id, nameString, categoryString, manufacturerString, sizeString, priceString, stockString);
                            }
                        }
                    });
                }

            }
        });


    }

    private void updateCatalogueItem(String docId, String id, String nameString, String categoryString, String manufacturerString, String sizeString, String priceString, String stockString) {
        DocumentReference docRef = db.collection("Catalogue").document(docId);
        Map<String, Object> update = new HashMap<>();
        update.put("id", id);
        update.put("name", nameString);
        update.put("category", categoryString);
        update.put("manufacturer", manufacturerString);
        update.put("size", sizeString);
        update.put("price", priceString);
        update.put("stock", stockString);
        docRef.set(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Item Updated Successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ItemDetails.this, AdminMain.class);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}