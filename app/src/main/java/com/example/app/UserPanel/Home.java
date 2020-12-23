package com.example.app.UserPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.app.Cart.AddToCart;
import com.example.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    List<ModelCLass> modelCLassList = new ArrayList<>();
    String id,title, disc, image;
    RecyclerView recyclerView;
    Adapter adapter;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recylerView);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);


        reference = FirebaseDatabase.getInstance().getReference().child("Products");
        Query query = reference.orderByChild("title");

        query.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {


                    for (DataSnapshot d : snapshot.getChildren()) {

                        String key = d.getKey();

                        id = snapshot.child(key).child("pid").getValue(String.class);
                        title = snapshot.child(key).child("title").getValue(String.class);
                        disc = snapshot.child(key).child("description").getValue(String.class);
                        image = snapshot.child(key).child("Image").getValue(String.class);

                        ModelCLass modelCLass = new ModelCLass(title, disc, image,id);


                        modelCLassList.add(modelCLass);

                    }
                     adapter = new Adapter(modelCLassList,Home.this);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
/*

                    .setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {


                            adapter.getFilter().filter(newText);
                            return true;
                        }
                    });
*/


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.optionmenu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        SearchView searchView;
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                adapter.getFilter().filter(newText);
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.cart)
        {
            Intent intent= new Intent(getApplicationContext(), AddToCart.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
