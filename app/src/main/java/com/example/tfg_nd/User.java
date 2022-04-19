package com.example.tfg_nd;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class User {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> user;

    String username;
    int dinero = 0;

    public User(){}

    public User(String user){
        DocumentReference docRef = db.collection("users").document(user);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Si existe rellena el usuario con los datos
                        username = user;
                        dinero = Integer.parseInt(document.get("dinero")+"");
                        //Log.d("TAG", "DocumentSnapshot data: " + document.get("born"));
                    } else {
                        //Si no existe crea un usuario nuevo con ese nombre
                        Log.d("TAG", "Creando nuevo usuario");
                        username = user;
                    }
                } else {
                    Log.d("TIG", "get failed with ", task.getException());
                }
            }
        });
    }

    public int getDinero(){
        return dinero;
    }

    public void setDinero(int dinero){
        user = new HashMap<>();
        user.put("dinero", dinero);
        //Si usas add te crea un documento con un ID autogenerado
        db.collection("users").document(username).set(user);
    }

}
