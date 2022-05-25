package com.example.tfg_nd;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


public class StartGame extends Activity {

    AlertDialog dialog;
    GameView gameView;
    String email, record;

    public StartGame activity;
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView= new GameView(this);
        setContentView(gameView);
    }

    public StartGame(){
        FirebaseUser current_user = mAuth.getCurrentUser();
        if(current_user!=null) email = current_user.getEmail();
        database.collection("flappy").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists() && documentSnapshot!=null){
                    record = documentSnapshot.getData().get("record")+"";
                }else{
                    Map<String, Object> datos = new HashMap<>();
                    datos.put("record", 0);
                    database.collection("flappy").document(email).set(datos);
                }
            }
        });
    }

    public int getRecord(){
        int record = 0;
        try{
            record = Integer.parseInt(this.record);
        }catch(Exception e){}
        return  record;
    }

    public void setRecord(int record){
        FirebaseUser current_user = mAuth.getCurrentUser();
        if(current_user!=null) email = current_user.getEmail();
        database.collection("flappy").document(email).update("record", record);
    }

    public void exitGame(){
        System.exit(0);
    }

}