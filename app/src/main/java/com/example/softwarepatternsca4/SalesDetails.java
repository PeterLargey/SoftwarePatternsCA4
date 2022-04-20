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

import java.util.ArrayList;

public class SalesDetails extends AppCompatActivity {

    private Intent data;
    private String name, address, date, total, email;
    private ArrayList<Items> items;
    private TextView saleDate, saleTotal;
    private RecyclerView saleItems;
    private soldItemsAdapter adapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_details);

        data = getIntent();
        name = data.getStringExtra("name");
        address = data.getStringExtra("address");
        date = data.getStringExtra("date");
        total = data.getStringExtra("total");
        email = data.getStringExtra("email");

        items = data.getParcelableArrayListExtra("items");

        saleDate = findViewById(R.id.dateOfSale);
        saleTotal = findViewById(R.id.saleDetailsTotal);

        saleDate.setText(date);
        saleTotal.setText(total);

        saleItems = findViewById(R.id.saleDetailsRecycler);
        saleItems.addItemDecoration(new DividerItemDecoration(SalesDetails.this, DividerItemDecoration.VERTICAL));

        setUpRecycler();

    }

    private void setUpRecycler() {
        adapter = new soldItemsAdapter(items);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        saleItems.setLayoutManager(staggeredGridLayoutManager);
        saleItems.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.back){
            Intent i = new Intent(SalesDetails.this, CustomerDetails.class);
            i.putExtra("name", name);
            i.putExtra("address", address);
            i.putExtra("email", email);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}