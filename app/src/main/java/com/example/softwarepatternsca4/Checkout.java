package com.example.softwarepatternsca4;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Checkout extends AppCompatActivity {


    private Intent data;
    private String email, total, address;
    private ArrayList<Items> items;
    private FirebaseFirestore db;
    private RecyclerView checkoutRecycler;
    private checkoutAdapter adapter;
    private TextView checkoutTotal, checkoutAddress, checkoutEmail;
    private EditText cardNumber, cardExpiry, cardCVV, discountCode;
    private Button pay;
    private final String TAG = "TAG";
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private String docId, itemStock;
    private Button applyDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = FirebaseFirestore.getInstance();
        data = getIntent();
        email = data.getStringExtra("email");
        address = data.getStringExtra("address");
        total = data.getStringExtra("total");

        items = data.getParcelableArrayListExtra("items");

        checkoutTotal = findViewById(R.id.checkoutTotal);
        checkoutAddress = findViewById(R.id.checkoutAddress);
        checkoutEmail = findViewById(R.id.checkoutEmail);

        checkoutTotal.setText(new StringBuilder("Total: €").append(total));
        checkoutAddress.setText(address);
        checkoutEmail.setText(email);

        discountCode = findViewById(R.id.discountCode);

        applyDiscount = findViewById(R.id.applyDiscount);

        applyDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = discountCode.getText().toString();
                if(code.equalsIgnoreCase("Monday15")){
                    double orderTotal = Double.parseDouble(total);
                    double discount = orderTotal * 0.15;
                    double discountedTotal = orderTotal - discount;
                    double totalRounded = (double) Math.round(discountedTotal * 100) / 100;
                    String discountedTotalString = String.valueOf(totalRounded);
                    checkoutTotal.setText(new StringBuilder("Total: €").append(discountedTotalString));
                } else {
                    Toast.makeText(Checkout.this, "Invalid Discount Code", Toast.LENGTH_LONG).show();
                    checkoutTotal.setText(new StringBuilder("Total: €").append(total));
                }
            }
        });


        cardNumber = findViewById(R.id.checkoutCardNumber);
        cardExpiry = findViewById(R.id.checkoutCardExpiry);
        cardCVV = findViewById(R.id.checkoutCardCVV);

        checkoutRecycler = findViewById(R.id.checkoutRecycler);
        checkoutRecycler.addItemDecoration(new DividerItemDecoration(Checkout.this, DividerItemDecoration.VERTICAL));
        setUpRecycler(items, Checkout.this);

        pay = findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String cardNumString = cardNumber.getText().toString();
                String cardExpiryString = cardExpiry.getText().toString();
                String cardCVVString = cardCVV.getText().toString();

                if(cardNumString.isEmpty() || cardExpiryString.isEmpty() || cardCVVString.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else {
                    LocalDate now = LocalDate.now();
                    String date = String.valueOf(now);
                    inputDataIntoSales(items, checkoutTotal.getText().toString(), checkoutEmail.getText().toString(), date);
                    updateStockLevel(items);
                    clearCart();
                    Toast.makeText(getApplicationContext(), "Sale Complete", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Checkout.this, CustomerMain.class);
                    i.putExtra("email", email);
                    startActivity(i);
                }
            }
        });


    }

    private void updateStockLevel(ArrayList<Items> items) {

        ArrayList<StockItem> stockItems = new ArrayList<>();


        for(Items i: items){
            if(stockItems.isEmpty()){
                StockItem item = new StockItem(i.getId(), "1");
                stockItems.add(item);
            } else {
                for(StockItem s: stockItems){
                    if(i.getId().equalsIgnoreCase(s.getId())){
                        int amount = Integer.parseInt(s.getAmount());
                        amount = amount + 1;
                        s.setAmount(String.valueOf(amount));
                    } else {
                        StockItem newItem = new StockItem(i.getId(), "1");
                        stockItems.add(newItem);
                    }
                }
            }

        }

        for(StockItem h : stockItems){
            Log.d(TAG, "Stock Items: " + h.getId() + " " + h.getAmount());
            String itemId = h.getId();
            int itemAmount = Integer.parseInt(h.getAmount());

            db.collection("Catalogue").whereEqualTo("id", itemId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc: task.getResult()){
                            docId = doc.getId();
                            Map<String, Object> docData = doc.getData();
                            itemStock = (String) docData.get("stock");

                        }

                        int stock = Integer.parseInt(itemStock);
                        int adjustedStock = stock - itemAmount;
                        String newStock = String.valueOf(adjustedStock);

                        DocumentReference docRef = db.collection("Catalogue").document(docId);
                        docRef.update("stock", newStock).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Stock level adjusted");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Failed: Stock level was not adjusted");
                            }
                        });

                    }
                }
            });
        }
    }

    private void clearCart() {
        db.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        String docId = doc.getId();
                        DocumentReference docRef = db.collection("Cart").document(docId);
                        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Cart Document deleted");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Cart failed to clear document");
                            }
                        });
                    }

                    Log.d(TAG, "Cart Cleared");
                }
            }
        });
    }

    private void inputDataIntoSales(ArrayList<Items> items, String total, String email, String date) {
        DocumentReference docRef = db.collection("Sales").document();
        Map<String, Object> sale = new HashMap<>();
        sale.put("items", items);
        sale.put("customerEmail", email);
        sale.put("total", total);
        sale.put("date", date);
        docRef.set(sale).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Sale successfully added to the database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed: Sale was not added to the database");
            }
        });
    }

    private void setUpRecycler(ArrayList<Items> itemList, Context context) {
        adapter = new checkoutAdapter(itemList, context);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        checkoutRecycler.setLayoutManager(staggeredGridLayoutManager);
        checkoutRecycler.setAdapter(adapter);
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
            Intent i = new Intent(Checkout.this, CustomerMain.class);
            i.putExtra("email", email);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}