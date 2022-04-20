package com.example.softwarepatternsca4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerMain extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private NavigationBarView bottomNav;
    private String email;
    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        getSupportActionBar().setTitle("Customer Home");
        mAuth = FirebaseAuth.getInstance();

        data = getIntent();
        email = data.getStringExtra("email");

        Bundle bundle = new Bundle();
        bundle.putString("email", email);

        Fragment startingFragment = new StoreFragment();
        startingFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.customerFragmentContainer, startingFragment).commit();

        bottomNav = findViewById(R.id.customerBottomNav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch(item.getItemId()){
                    case(R.id.store):
                        fragment = new StoreFragment();
                        fragment.setArguments(bundle);
                        break;
                    case(R.id.cart):
                        fragment = new CartFragment();
                        fragment.setArguments(bundle);
                        break;
                    case(R.id.ratings):
                        fragment = new RatingsFragment();
                        fragment.setArguments(bundle);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.customerFragmentContainer, fragment).commit();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.logout){
            mAuth.signOut();
            finish();
            Intent i = new Intent(CustomerMain.this, MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}