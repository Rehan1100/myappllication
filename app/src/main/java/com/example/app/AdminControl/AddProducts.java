package com.example.app.AdminControl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.app.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddProducts extends AppCompatActivity {

    private DatabaseReference reference;
    private StorageReference ProductImageRef;
    ImageView product;
    EditText InputProductTitle, InputProductDescription,InputProductPrice,InputQuantity;
    Button post;
    String email, roll;
    String title, description, saveCurrentDate, SaveCurrentTime;
    private Uri imageURI;
    String productRandomKey, downloadImageURL;
    private ProgressDialog loadingbar;
    private String price;
    private String pease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        product = findViewById(R.id.product);
        InputProductTitle = findViewById(R.id.title);
        InputProductDescription = findViewById(R.id.discreption);
        InputProductPrice = findViewById(R.id.price);
        InputQuantity = findViewById(R.id.peaces);

        post = findViewById(R.id.Post);

        loadingbar = new ProgressDialog(this);

        SharedPreferences getShared = getApplicationContext().getSharedPreferences("loginName", MODE_PRIVATE);
        email = getShared.getString("Email", "Save a note and it will show up here");


        SharedPreferences getShared1 = getApplicationContext().getSharedPreferences("RollName", MODE_PRIVATE);
        roll = getShared1.getString("Roll", "Save a note and it will show up here");


        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Image");
        reference = FirebaseDatabase.getInstance().getReference().child("Products");
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post();

            }
        });
        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }

    public void post() {

        vaildateProduct();
    }

    public void vaildateProduct() {
        title = InputProductTitle.getText().toString();
        description = InputProductDescription.getText().toString();
        price=InputProductPrice.getText().toString();
        pease=InputQuantity.getText().toString();
        if (imageURI == null) {
            Toast.makeText(this, "Product Image is Menidatry...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please write product description", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please write product name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Please write product Price", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(pease)) {
            Toast.makeText(this, "Please write product Price", Toast.LENGTH_SHORT).show();
        } else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

        loadingbar.setTitle("Adding new Product");
        loadingbar.setMessage("Please wait while we are adding the new Product.");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
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
                loadingbar.dismiss();
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

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });


    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pid", productRandomKey);
        hashMap.put("date", saveCurrentDate);
        hashMap.put("time", SaveCurrentTime);
        hashMap.put("price",price);
        hashMap.put("description", description);
        hashMap.put("title", title);
        hashMap.put("Image", downloadImageURL);
        hashMap.put("quantity",pease);
        reference.child(productRandomKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    loadingbar.dismiss();
                    Toast.makeText(getApplicationContext(), "Product is adedd sucessfully", Toast.LENGTH_SHORT).show();

                } else {
                    loadingbar.dismiss();
                    String msg = task.getException().toString();

                    Toast.makeText(getApplicationContext(), "Error" + msg.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageURI = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                product.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}