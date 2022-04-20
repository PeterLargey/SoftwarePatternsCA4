package com.example.softwarepatternsca4;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AddStockItemFragment extends Fragment {

    private EditText id, name, manufacturer, category, price, size, stock, image;
    private Button add;
    private FirebaseFirestore db;
    private final String TAG = "TAG";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View addStockItemView = inflater.inflate(R.layout.fragment_add_stock_item, container, false);
        db = FirebaseFirestore.getInstance();

        id = addStockItemView.findViewById(R.id.newItemId);
        name = addStockItemView.findViewById(R.id.newItemName);
        manufacturer = addStockItemView.findViewById(R.id.newItemManufacturer);
        category = addStockItemView.findViewById(R.id.newItemCategory);
        price = addStockItemView.findViewById(R.id.newItemPrice);
        size = addStockItemView.findViewById(R.id.newItemSize);
        stock = addStockItemView.findViewById(R.id.newItemStock);
        image = addStockItemView.findViewById(R.id.newItemImage);

        add = addStockItemView.findViewById(R.id.addItem);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idString = id.getText().toString();
                String nameString = name.getText().toString();
                String manufacturerString = manufacturer.getText().toString();
                String categoryString = category.getText().toString();
                String priceString = price.getText().toString();
                String sizeString = size.getText().toString();
                String imageString = image.getText().toString();
                String stockString = stock.getText().toString();

                if(idString.isEmpty() || nameString.isEmpty() || manufacturerString.isEmpty() || categoryString.isEmpty() || priceString.isEmpty() || sizeString.isEmpty() || imageString.isEmpty() || stockString.isEmpty()){
                    Toast.makeText(view.getContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(view.getContext(), "Item inputted into the Database", Toast.LENGTH_LONG).show();
                    inputDataIntoCatalogue(idString, nameString, manufacturerString, categoryString, priceString, sizeString, imageString, stockString);
                    Intent i = new Intent(view.getContext(), AdminMain.class);
                    view.getContext().startActivity(i);
                }
            }
        });
        return addStockItemView;
    }

    private void inputDataIntoCatalogue(String id, String name, String manufacturer, String category, String price, String size, String image, String stock) {

        DocumentReference docRef = db.collection("Catalogue").document();
        Map<String, Object> item = new HashMap<>();
        item.put("id", id);
        item.put("name", name);
        item.put("manufacturer", manufacturer);
        item.put("category", category);
        item.put("price", price);
        item.put("size", size);
        item.put("image", image);
        item.put("stock", stock);

        docRef.set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Item added to the Database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Item was not added to the Database");
            }
        });
    }
}