package com.example.tfg_nd;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import org.jetbrains.annotations.Nullable;


public class StartGame extends Activity {

    GameView gameView;

    public StartGame activity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView= new GameView(this);
        setContentView(gameView);

    }

    public void exitGame(){
        System.exit(0);
    }

}