package com.example.app.loginSignin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.AdminPanel.dashBoard;
import com.example.app.R;
import com.example.app.UserPanel.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    TextView createnewLink, ForgetPass;
    TextInputEditText email, password;
    Button Loginbtn;
    ArrayList<String> dbData = new ArrayList<>();
    private String key;
    private ProgressDialog loadingbar;
    private String Roll;
    private String LoginUserEmail;
    private String Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.emailtb);
        password = findViewById(R.id.passTb);
        Loginbtn = findViewById(R.id.loginBtn);
        createnewLink = findViewById(R.id.CreateNewLink);
        ForgetPass = findViewById(R.id.ForgetPass);
        loadingbar = new ProgressDialog(this);


        SharedPreferences getShared = getApplicationContext().getSharedPreferences("loginName", MODE_PRIVATE);
        LoginUserEmail = getShared.getString("Email", "Save a note and it will show up here");


        SharedPreferences getShared2 = getApplicationContext().getSharedPreferences("RollName", MODE_PRIVATE);
        Roll = getShared2.getString("Roll", "Save a note and it will show up here");

        SharedPreferences getShared3 = getApplicationContext().getSharedPreferences("name", MODE_PRIVATE);
        Name = getShared3.getString("Name", "Save a note and it will show up here");


          Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginUser(v);

            /*HashMap<String,Object> map= new HashMap<>();
            map.put("name",email.getText().toString());
            map.put("password",password.getText().toString());

            FirebaseDatabase.getInstance().getReference()
                    .child("User1")
                    .setValue(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

//                        Log.i("this","onComplete");
                            Toast.makeText(getApplicationContext(),"Sucessfull",Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //  Log.i("this","Onfailure"+e.toString());
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();

                }
            });*/


            }
        });

        createnewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);

            }
        });
        ForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                Intent intent = new Intent(getApplicationContext(), emialAuthentication.class);
                startActivity(intent);*/

            }
        });

    }

    private Boolean valideUsername() {
        String val = email.getText().toString();
        if (val.isEmpty()) {
            email.setError("Field can't be Empty");
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Please provide valid email");
            email.setFocusable(true);
            return false;

        } else {
            email.setError(null);
            return true;

        }
    }


    private Boolean validePassword() {
        String val = password.getText().toString();
        if (val.isEmpty()) {
            email.setError("Field can't be Empty");
            return false;
        } else {
            email.setError(null);
            return true;
        }

    }

    public void LoginUser(View v) {
        if (!valideUsername() | !validePassword()) {
            return;
        } else {
            checkConnection();
        }
    }

    private void isUser() {


        String UserenterEmail = email.getText().toString();
        String UserenterPassword = password.getText().toString();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
        Query query = db.orderByChild("email").equalTo(UserenterEmail);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    loadingbar.setTitle("Login");
                    loadingbar.setMessage("Please wait while for a while...");
                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();


                    for (DataSnapshot ds : snapshot.getChildren()) {
                        key = ds.getKey();
                        Log.d("Tag", key);
                    }
                    String emailFromDb = snapshot.child(key).child("email").getValue(String.class);
                    String Roll = snapshot.child(key).child("role").getValue(String.class);
                    String name = snapshot.child(key).child("name").getValue(String.class);

                    //LoginUserEmail
                    SharedPreferences shrd=getSharedPreferences("loginName", MODE_PRIVATE);
                    SharedPreferences.Editor editor = shrd.edit();
                    editor.putString("Email",emailFromDb);
                    editor.apply();
                    //LoginRoll
                    SharedPreferences shrd1=getSharedPreferences("RollName", MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = shrd1.edit();
                    editor1.putString("Roll",Roll);
                    editor1.apply();
                    //LoginUserName
                    SharedPreferences shrd2= getSharedPreferences("name",MODE_PRIVATE);
                    SharedPreferences.Editor editor2=shrd2.edit();
                    editor2.putString("Name",name);
                    editor2.apply();

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        loadingbar.dismiss();
                                        if (auth.getCurrentUser().isEmailVerified()) {
                                            if (Roll.equals("admin"))
                                            {
                                                Intent intent = new Intent(getApplicationContext(), dashBoard.class);
                                                startActivity(intent);
                                                finish();

                                            }else {
                                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Please Verify Your Email Address", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        loadingbar.dismiss();
                                        password.setError("Enter the correct Password");
                                        password.setFocusable(true);
                                        // Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    //                      intent.putExtra("fullpassword", passFromDb);
                    //                    intent.putExtra("genderFromDb", genderFromDb);




                } else {
                    email.setError("No such email exist");
                    email.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private TextWatcher CreateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String Email = email.getText().toString().trim();
            String Pass = password.getText().toString().trim();

            Loginbtn.setEnabled(!Email.isEmpty() && !Pass.isEmpty());


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void checkConnection()
    {
        ConnectivityManager manager= (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
        if (null!=activeNetwork)
        {
            if (activeNetwork.getType()==ConnectivityManager.TYPE_WIFI || activeNetwork.getType()==ConnectivityManager.TYPE_MOBILE)
            {
                isUser();
            }
        }
        else {
            Toast.makeText(this, "No InterNetConnection", Toast.LENGTH_SHORT).show();
        }
    }
}
