package com.example.app.AdminPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.AdminControl.AddAdmin;
import com.example.app.AdminControl.AddProducts;
import com.example.app.R;
import com.example.app.UserPanel.Adapter;
import com.example.app.UserPanel.Home;
import com.example.app.UserPanel.ModelCLass;
import com.example.app.loginSignin.Login;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class dashBoard extends AppCompatActivity {


    CardView admin,products;
    TextView textView;

    //Drawer Work
    Toolbar toolbar;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    private String Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = findViewById(R.id.navMenu);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home1:

                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.addAdmin:

                      drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent= new Intent(getApplicationContext(),AddAdmin.class);
                        startActivity(intent);
                        return true;
                    case R.id.logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2= new Intent(getApplicationContext(), Login.class);
                        startActivity(intent2);
                        return true;
                    case R.id.ViewProducts:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent3= new Intent(getApplicationContext(), Home.class);
                        startActivity(intent3);

                }
                return true;
            }
        });

        // now over code is Start from here!

        admin=findViewById(R.id.cardView6);
        products=findViewById(R.id.cardView5);
        textView=findViewById(R.id.textView4);


        SharedPreferences getShared3 = getApplicationContext().getSharedPreferences("name", MODE_PRIVATE);
        Name = getShared3.getString("Name", "Save a note and it will show up here");



        textView.setText("Welcome"+" "+Name.toString());

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getApplicationContext(), AddAdmin.class);
                startActivity(intent);

            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), AddProducts.class);
                startActivity(intent);

            }
        });

    }
}