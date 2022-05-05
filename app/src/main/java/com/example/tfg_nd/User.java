package com.example.tfg_nd;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class User {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "Utils_db.java";

    private String email;

    public User(){}

    public User(String email){
        this.email = email;
    }

    public void incrementarDinero(int incremento){
        db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int dinero = Integer.parseInt(documentSnapshot.getData().get("dinero")+"");
                db.collection("users").document(email).update("dinero", (dinero + incremento)+"")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "dinero incrementado correctamente");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error al incrementar el dinero de " + email + ": " + e);
                        }
                    });
            }
        });
    }

    public void incrementarExperiencia(int incremento){
        db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int exp = Integer.parseInt(documentSnapshot.getData().get("exp")+"");
                db.collection("users").document(email).update("exp", (exp + incremento)+"")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "experiencia incrementada correctamente");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error al incrementar la experiencia de " + email + ": " + e);
                        }
                    });
            }
        });
    }




}
