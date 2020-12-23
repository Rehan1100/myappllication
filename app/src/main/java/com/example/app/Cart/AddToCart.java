package com.example.app.Cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddToCart extends AppCompatActivity {

    private String id,title,image,disc,price;
    private RecyclerView recyclerView;
    private Button NextProcessBtn;
    public List<cart> modelCLassList=new ArrayList<>();
    CartAdapter adapter;
    private String quantity;
    private String email;
    private String LoginUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        SharedPreferences getShared = getApplicationContext().getSharedPreferences("loginName", MODE_PRIVATE);
        LoginUserEmail = getShared.getString("Email", "Save a note and it will show up here");


        recyclerView=findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        NextProcessBtn=findViewById(R.id.next);
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference().child("Carts");
        Query query = reference.orderByChild("email").equalTo(LoginUserEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot d : snapshot.getChildren()) {

                        String key = d.getKey();

                        id = snapshot.child(key).child("pid").getValue(String.class);
                        title = snapshot.child(key).child("title").getValue(String.class);
                        disc = snapshot.child(key).child("description").getValue(String.class);
                        price = snapshot.child(key).child("price").getValue(String.class);
                        image = snapshot.child(key).child("Image").getValue(String.class);
                        quantity = snapshot.child(key).child("quantity").getValue(String.class);
                        email = snapshot.child(key).child("email").getValue(String.class);

                        cart cart1 = new cart(id,title,disc,price,quantity);

                            modelCLassList.add(cart1);
                    }
                    adapter= new CartAdapter(modelCLassList,AddToCart.this);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
