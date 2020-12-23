package com.example.app.loginSignin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class emailAuthentication extends AppCompatActivity {

    EditText email;
    Button Next;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_authentication);
        loadingbar = new ProgressDialog(this);

        email=findViewById(R.id.emailText);
        Next=findViewById(R.id.Next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginUser(v);
            }
        });

    }

    private void checkEmailisExist(View v) {

        loadingbar.setTitle("Forget PassWord");
        loadingbar.setMessage("Please Wait for a while..");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        FirebaseDatabase db= FirebaseDatabase.getInstance();
        DatabaseReference node=db.getReference("Users");

        Query query=node.orderByChild("email").equalTo(email.getText().toString());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    loadingbar.dismiss();
                    FirebaseAuth auth=FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                loadingbar.dismiss();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Check your email to resst your password", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                            else {
                                loadingbar.dismiss();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Check your email to resst your password", Snackbar.LENGTH_LONG);
                                snackbar.show();

                            }
                        }
                    });

                    Toast.makeText(emailAuthentication.this, "ok", Toast.LENGTH_SHORT).show();
                }
                else
                {      /*Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "There is no Email", Snackbar.LENGTH_LONG);
                        snackbar.show();
*/                  loadingbar.dismiss();
                    email.setError("There is no Email exist in our data");
                    email.setFocusable(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private Boolean valideUsername() {
        String val = email.getText().toString();
        if (val.isEmpty()) {
            email.setError("Field can't be Empty");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
        {
            email.setError(null);
            return true;

        }else
        {
            email.setError(null);
            return true;
        }
    }
    public void LoginUser(View v) {
        if (!valideUsername()) {
            return;
        } else {
            checkEmailisExist(v);
        }
    }

}
