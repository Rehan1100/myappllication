package com.example.app.AdminControl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.app.AdminPanel.dashBoard;
import com.example.app.R;
import com.example.app.loginSignin.Login;
import com.example.app.loginSignin.Signup;
import com.example.app.loginSignin.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddAdmin extends AppCompatActivity {

    ProgressBar progressBar;
    EditText name, email;
    TextInputEditText password;
    Button SignUp;
    public int id;
    private RadioGroup radioGroup;
    private RadioButton selectedRadioButton;
    ImageView profileImage;
    private Uri filepath;
    FirebaseStorage storage;
    StorageReference reference;
    String role="admin";
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingbar = new ProgressDialog(this);

        name = findViewById(R.id.name);
        email = findViewById(R.id.emailtb);
        password = findViewById(R.id.passwordTb);
        SignUp = findViewById(R.id.SignupBtn);
        progressBar = findViewById(R.id.prpgressbar);
        name.addTextChangedListener(CreateTextWatcher);
        email.addTextChangedListener(CreateTextWatcher);
        password.addTextChangedListener(CreateTextWatcher);
        radioGroup = findViewById(R.id.rdGroup);




        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {
                    if (!Character.isLetter(source.charAt(i)) &&
                            !Character.toString(source.charAt(i)).equals("_") &&
                            !Character.toString(source.charAt(i)).equals("-")) {
                        return "";
                    }
                }
                return null;
            }
        };
        name.setFilters(new InputFilter[]{filter});


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                SignUpUser(v);





            }
        });
    }


    private Boolean valideUsername() {

        String regix = "[0-9]+";
        String val = name.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            name.setError("Field can't be Empty");
            name.setFocusable(true);
            return false;
        } else if (val.length() >= 15) {
            name.setError("UserName is Too Long");
            name.setFocusable(true);
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            name.setError("White Space are not Allowed");
            name.setFocusable(true);
            return false;
        } else {
            name.setError(null);
            return true;
        }


    }


    private Boolean valideEmailAdress() {

        String val = email.getText().toString();
        //  String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        if (val.isEmpty()) {
            email.setError("Field can't be Empty");
            email.setFocusable(true);
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Thats Not Email Pattern");
            email.setFocusable(true);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
        {
            email.setError("Please provide valid email");
            email.setFocusable(true);
            return false;

        }
        else {
            email.setError(null);
            return true;
        }
    }


    private Boolean validePassword() {
        String val = password.getText().toString();
        String pass = "^" +
                //   "(?=.*[0-9])" + //atLeast one Digit
                //   "(?=.*[a-z])" + //atleast one letter
                //   "(?=.*[A-Z])" + //atleast one capitalletter
                "(?=.*[a-zA-Z])" + //any letter
                "(?=.*[@#$%^&+=])" + //atleast 1 Speacial character
                //   "(?=\\$+$)" +    //no white space
                ".{4,}" +        //at lest 4 charachter
                "$";
        if (val.isEmpty()) {
            password.setError("Field can't be Empty");
            return false;
        } else if (!val.matches(pass)) {
            password.setError("Password too weak");
            return false;

        } else {
            email.setError(null);
            return true;
        }

    }


    public void SignUpUser(View v) {
        if (!valideUsername() | !validePassword() | !valideEmailAdress() ) {

            SignUp.setEnabled(false);
            loadingbar.dismiss();
            return;
        }
        else {
            SignUp.setEnabled(true);
            isUser();

        }
    }


    private TextWatcher CreateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String Name = name.getText().toString().trim();
            String Email = email.getText().toString().trim();
            String Pass = password.getText().toString().trim();
            //Drawable image= profileImage.getDrawable();


            SignUp.setEnabled(!Name.isEmpty() && !Email.isEmpty() && !Pass.isEmpty());


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void isUser() {

        loadingbar.setTitle("Add Admin");
        loadingbar.setMessage("Please wait for Registration");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference node = db.getReference("Users");

        String d1 = name.getText().toString();
        String d2 = email.getText().toString();
        String d3 = password.getText().toString();
        selectedRadioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
        String d4 = selectedRadioButton.getText().toString();
        String d5 = role;

        DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("Users");
        Query query = db2.orderByChild("email").equalTo(d2);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loadingbar.dismiss();
                    email.setError("This Mail is Already Exist Try Another One");
                    email.setFocusable(true);
                    SignUp.setEnabled(false);

                } else {


                    //DataInsert
                    SignUp.setEnabled(true);
                    Users users = new Users(d1, d2, d3, d4, d5);
                    node.push().setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

//                            Toast.makeText(getApplicationContext(), "Sucessfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            loadingbar.dismiss();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();
                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Registered SucessFully", Toast.LENGTH_SHORT).show();

                                        auth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {

                                                            Toast.makeText(getApplicationContext(), "Registered Sucessfully Please Check Your Email for Registered", Toast.LENGTH_SHORT).show();

                                                            name.setText("");
                                                            email.setText("");
                                                            password.setText("");


                                                        } else {

                                                            loadingbar.dismiss();
                                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                        //Sucessfully Msg
                                    } else {

                                        loadingbar.dismiss();
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                    Intent intent = new Intent(getApplicationContext(), dashBoard.class);
                    startActivity(intent);


                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}