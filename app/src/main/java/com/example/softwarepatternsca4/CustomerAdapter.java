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

public class CustomerAdapter extends FirestoreRecyclerAdapter<Customer, CustomerAdapter.CustomerViewHolder> {

    public CustomerAdapter(@NonNull FirestoreRecyclerOptions<Customer> options){
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CustomerViewHolder holder, int position, @NonNull Customer model) {
        holder.name.setText(model.getName());
        holder.address.setText(model.getAddress());
        holder.email.setText(model.getEmail());

        //String docId = getSnapshots().getSnapshot(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CustomerDetails.class);
                i.putExtra("name", model.getName());
                i.putExtra("address", model.getAddress());
                i.putExtra("email", model.getEmail());
                //i.putExtra("docId", docId);
                view.getContext().startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_recycler_layout, parent, false);
        return new CustomerViewHolder(view);
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView address;
        private TextView email;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.customerName);
            address = itemView.findViewById(R.id.customerAddress);
            email = itemView.findViewById(R.id.customerEmail);
        }
    }
}
