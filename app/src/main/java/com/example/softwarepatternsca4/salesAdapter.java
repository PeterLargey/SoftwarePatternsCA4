package com.example.softwarepatternsca4;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class salesAdapter extends FirestoreRecyclerAdapter<Sales, salesAdapter.SalesViewHolder> {

    private String name;
    private String address;

    public salesAdapter(@NonNull FirestoreRecyclerOptions<Sales> options, String name, String address) {
        super(options);
        this.name = name;
        this.address = address;
    }

    @Override
    protected void onBindViewHolder(@NonNull SalesViewHolder holder, int position, @NonNull Sales model) {
        holder.date.setText(model.getDate());
        holder.total.setText("€" + model.getTotal());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), SalesDetails.class);
                i.putExtra("date", model.getDate());
                i.putExtra("total", model.getTotal());
                i.putExtra("email", model.getCustomerEmail());
                i.putExtra("name", name);
                i.putExtra("address", address);
                i.putParcelableArrayListExtra("items", (ArrayList<Items>) model.getItems());

                view.getContext().startActivity(i);

            }
        });
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_recycler_layout, parent, false);
        return new SalesViewHolder(view);
    }

    public class SalesViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView total;

        public SalesViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.saleDate);
            total = itemView.findViewById(R.id.saleTotal);
        }
    }

}
