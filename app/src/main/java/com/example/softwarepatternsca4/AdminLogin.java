package com.example.softwarepatternsca4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class AdminLogin extends AppCompatActivity {

    private FirebaseFirestore db;
    private Map<String, Object> dbData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        getSupportActionBar().setTitle("Admin Login");

        db = FirebaseFirestore.getInstance();
        EditText username = findViewById(R.id.adminUserName);
        EditText password = findViewById(R.id.adminPassword);
        Button login = findViewById(R.id.adminLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameString = username.getText().toString();
                String passwordString = password.getText().toString();
                if(usernameString.isEmpty() || passwordString.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                }else{
                    db.collection("Admin").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if(task.isSuccessful()){
                               for(DocumentSnapshot doc : task.getResult()){
                                   dbData = doc.getData();

                               }

                               Object[] values = dbData.values().toArray();
                               String username = values[1].toString();
                               String password = values[0].toString();
                               if(username.equalsIgnoreCase(usernameString) && password.equalsIgnoreCase(passwordString)){
                                   Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                                   Intent i = new Intent(AdminLogin.this, AdminMain.class);
                                   startActivity(i);
                               } else{
                                   Toast.makeText(getApplicationContext(), "Login Failed, Account doesn't exist", Toast.LENGTH_LONG).show();
                               }

                           }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_to_customer_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.customer_login){
            Intent i = new Intent(AdminLogin.this, MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}