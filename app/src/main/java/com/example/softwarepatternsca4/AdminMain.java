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

public class AdminMain extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private NavigationBarView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        getSupportActionBar().setTitle("Admin Home");
        mAuth = FirebaseAuth.getInstance();

        Fragment startingFragment = new AdminHomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.adminFragmentContainer, startingFragment).commit();

        bottomNav = findViewById(R.id.adminBottomNav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch(item.getItemId()){
                    case(R.id.adminHome):
                        fragment = new AdminHomeFragment();
                        break;
                    case(R.id.adminUpdate):
                        fragment = new UpdateStockFragment();
                        break;
                    case(R.id.viewCustomers):
                        fragment = new ViewCustomersFragment();
                        break;
                    case(R.id.adminPurchase):
                        fragment = new PurchaseStockFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.adminFragmentContainer, fragment).commit();
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
            Intent i = new Intent(AdminMain.this, MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}