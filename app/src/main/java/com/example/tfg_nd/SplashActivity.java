package com.example.tfg_nd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Animation topAnimation, bottomAnimation;
    ImageView imageView;
    ImageView[] figuras;
    TextView textView;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        imageView = findViewById(R.id.img_logo);
        textView = findViewById(R.id.txt_logo);
        figuras = new ImageView[7];
        figuras[0] = findViewById(R.id.figure1);
        figuras[1] = findViewById(R.id.figure2);
        figuras[2] = findViewById(R.id.figure3);
        figuras[3] = findViewById(R.id.figure4);
        figuras[4] = findViewById(R.id.figure5);
        figuras[5] = findViewById(R.id.figure6);
        figuras[6] = findViewById(R.id.figure7);

        for(int i=0; i<figuras.length; i++){
            figuras[i].setColorFilter(getResources().getColor(R.color.white));
        }

        imageView.setAnimation(topAnimation);
        textView.setAnimation(bottomAnimation);

        new CountDownTimer(4000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                startActivity(new Intent(SplashActivity.this,HomeMenuActivity.class));
                finish();
            }
        }.start();

    }


}