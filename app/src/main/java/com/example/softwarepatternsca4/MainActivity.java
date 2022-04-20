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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Customer Login");
        mAuth = FirebaseAuth.getInstance();

        EditText email = findViewById(R.id.loginEmail);
        EditText password = findViewById(R.id.loginPassword);
        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.navigateToRegister);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                if(emailString.isEmpty() || passwordString.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                }else{
                    mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(MainActivity.this, CustomerMain.class);
                                i.putExtra("email", emailString);
                                startActivity(i);
                            }else{
                                Toast.makeText(getApplicationContext(), "Login Failed, Account doesn't exist", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

       register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(MainActivity.this, Register.class);
               startActivity(i);
           }
       });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_login_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.admin_login){
            Intent i = new Intent(MainActivity.this, AdminLogin.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}