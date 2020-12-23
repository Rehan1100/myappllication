package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.app.loginSignin.Login;

public class MainActivity extends AppCompatActivity {


    Animation topanim,bottomanim,lefttoright,righttoleft;
    ImageView imageView;
    Button loginSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topanim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomanim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        lefttoright= AnimationUtils.loadAnimation(this,R.anim.left_to_right);
        righttoleft= AnimationUtils.loadAnimation(this,R.anim.righhttoleft);
        //Hooks
        imageView=findViewById(R.id.logo);
        //Signup
        loginSignup=findViewById(R.id.signup);
        //Party

        imageView.setAnimation(topanim);
        loginSignup.setAnimation(lefttoright);


        loginSignup=findViewById(R.id.signup);
        loginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent= new Intent(getApplicationContext(), Login.class);
                startActivity(intent);


            }
        });
    }
}