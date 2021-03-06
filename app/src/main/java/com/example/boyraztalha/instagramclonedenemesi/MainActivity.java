package com.example.boyraztalha.instagramclonedenemesi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import com.example.boyraztalha.instagramclonedenemesi.Fragment.HomeFragment;
import com.example.boyraztalha.instagramclonedenemesi.Fragment.NotificationFragment;
import com.example.boyraztalha.instagramclonedenemesi.Fragment.ProfileFragment;
import com.example.boyraztalha.instagramclonedenemesi.Fragment.SearchFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Fragment selectedFragment = null;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navSelector);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container
                ,new HomeFragment()).commit();

    }

    BottomNavigationView.OnNavigationItemSelectedListener navSelector = new BottomNavigationView
            .OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch(menuItem.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_add:
                    selectedFragment = null;
                    startActivity(new Intent(MainActivity.this,PostActivity.class));
                    break;
                case R.id.nav_favorite:
                    selectedFragment = new NotificationFragment();
                    break;
                case R.id.nav_person:
                    SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                    editor.putString("profileId",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.apply();
                    selectedFragment = new ProfileFragment();
                    break;
            }

            if (selectedFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container
                        ,selectedFragment).commit();
            }

            return false;
        }
    };
}
