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

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> user;

    private final String TAG = "User.java";
    private String email = "....";
    public int dinero = 0;

    public User(){}

    public User(String correo){
        DocumentReference docRef = db.collection("users").document(correo);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Si existe rellena el usuario con los datos
                        email = correo;
                        dinero = Integer.parseInt(document.get("dinero")+"");
                    } else {
                        //Si no existe crea un usuario nuevo con ese nombre
                        email = correo;
                     }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        Log.d(TAG, dinero+" pollo " + getDinero());
    }

    public int getDinero(){
        return this.dinero;
    }

    public void setDinero(int dinero){
        this.dinero = dinero;
    }

    //Aplica los datos actuales de la clase user actual a la base de datos
    public void commit(){
        user = new HashMap<>();

        user.put("dinero", dinero);

        //Si usas add te crea un documento con un ID autogenerado
        db.collection("users").document(email).set(user)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("TAG", "setDinero realizado correctamente");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("TAG", "Error en setDinero()", e);
                }
            });
    }



    public String getEmail(){
        return email;
    }

}
