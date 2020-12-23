package com.example.app.ProductDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Productdetails extends AppCompatActivity {

    Button addtoCart;
    ImageView imageView;
    TextView title;
    TextView desc;
    TextView price,pease;

    public String ProductID = "";
    private DatabaseReference reference;
    private String email;
    private String roll;
    private String Image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);


        addtoCart = findViewById(R.id.button);
        title = findViewById(R.id.Productname);
        desc = findViewById(R.id.productDisc);
        imageView = findViewById(R.id.Pic);
        price = findViewById(R.id.price);
        pease = findViewById(R.id.quantity);


        SharedPreferences getShared = getApplicationContext().getSharedPreferences("loginName", MODE_PRIVATE);
        email = getShared.getString("Email", "Save a note and it will show up here");


        SharedPreferences getShared1 = getApplicationContext().getSharedPreferences("RollName", MODE_PRIVATE);
        roll = getShared1.getString("Roll", "Save a note and it will show up here");


        ProductID = getIntent().getStringExtra("pid");
        Image=getIntent().getStringExtra("image");


        getProductDetails(ProductID);

        addtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddtoCart();
            }
        });
    }

    private void getProductDetails(String productID) {
        reference = FirebaseDatabase.getInstance().getReference().child("Products");
        Query query = reference.orderByChild(productID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            private String key;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        key = d.getKey();
                    }
                    //ModelCLass modelCLass=snapshot.getValue(ModelCLass.class);
                    title.setText(snapshot.child(key).child("title").getValue(String.class));
                    desc.setText(snapshot.child(key).child("description").getValue(String.class));
                    price.setText(snapshot.child(key).child("price").getValue(String.class));
                    pease.setText(snapshot.child(key).child("quantity").getValue(String.class));
                    Picasso.get().load(snapshot.child(key).child("Image").getValue(String.class)).into(imageView);


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
/*

    private void StoreProductInformation() {



        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,YYYY");
        saveCurrentDate = dateFormat.format(calendar.getTime());

        SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm:ss a");
        SaveCurrentTime = TimeFormat.format(calendar.getTime());
        productRandomKey = saveCurrentDate + SaveCurrentTime;

        StorageReference filepath = ProductImageRef.child(imageURI.getLastPathSegment() + productRandomKey + "jpg");
        final UploadTask task = filepath.putFile(imageURI);
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(getApplicationContext(), "Error" + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Product Image Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }
                        downloadImageURL = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            downloadImageURL=task.getResult().toString();
                            Toast.makeText(getApplicationContext(), "getting product image URL sucessfully", Toast.LENGTH_SHORT).show();

                            AddtoCart();
                        }
                    }
                });
            }
        });


    }

*/

    private void AddtoCart() {
        String d0=ProductID.toString();
        String d1 = title.getText().toString();
        String d2 = desc.getText().toString();
        String d3 = price.getText().toString();
        String d4 = pease.getText().toString();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email",email);
        hashMap.put("pid", d0);
        hashMap.put("description", d2);
        hashMap.put("title", d1);
        hashMap.put("price", d3);
        hashMap.put("Image",Image);
        hashMap.put("quantity",d4);

        reference = FirebaseDatabase.getInstance().getReference().child("Carts");
        reference.child(d0).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Product is adedd sucessfully", Toast.LENGTH_SHORT).show();

                } else {
                    String msg = task.getException().toString();

                    Toast.makeText(getApplicationContext(), "Error" + msg.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}