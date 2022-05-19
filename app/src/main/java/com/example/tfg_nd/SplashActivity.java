package com.example.tfg_nd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);
        access();

    }

    private void loading(){
        try {
            Thread.sleep(700);
        }catch (InterruptedException e){}
    }

    public void access(){
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar.post(new Runnable() {
                    @Override
                    public void run() {progressBar.setProgress(0);}
                });
                for(int i=0;i<=10;i++){
                    loading();
                    progressBar.post(new Runnable() {
                        @Override
                        public void run() {progressBar.incrementProgressBy(10);}
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this,HomeMenuActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }).start();

    }

}