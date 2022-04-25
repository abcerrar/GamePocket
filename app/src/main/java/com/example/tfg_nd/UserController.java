package com.example.tfg_nd;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class UserController {

    private final String TAG = "UserController.java";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> user;

    private String email;
    private int dinero = 0;

    public UserController(){}

    public UserController(String vEmail){
        DocumentReference docRef = db.collection("users").document(vEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Si existe rellena el usuario con los datos
                        email = vEmail;
                        dinero = Integer.parseInt(document.get("dinero")+"");
                    } else {
                        //Si no existe crea un usuario nuevo con ese nombre
                        email = vEmail;
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}
