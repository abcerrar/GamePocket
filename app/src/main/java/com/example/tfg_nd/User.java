package com.example.tfg_nd;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "Utils_db.java";

    private final String email;

    public User(String email){
        this.email = email;
    }

    public void incrementarDinero(int incremento){
        db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int dinero = 0;
                try{
                    dinero = Integer.parseInt(documentSnapshot.getData().get("dinero")+"");
                }catch(NullPointerException e){
                    Log.d(TAG, "Error al acceder al dinero de " + email);
                }
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

    public void actualizarEstrellas(int estrellas, String gamemode, int nivel){
        DocumentReference docRef = db.collection(gamemode).document(email).collection("datos_nivel").document(nivel+"");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int estrellas_actuales = Integer.parseInt(documentSnapshot.getData().get("estrellas")+"");

                if(estrellas > estrellas_actuales){
                    Log.d(TAG, estrellas+"");
                    db.collection(gamemode).document(email).collection("datos_nivel").document(nivel+"").update("estrellas", estrellas)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Estrellas incrementadas correctamente");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error al incrementar las estrellas de " + email + ": " + e);
                            }
                        });
                    }
                }

        });
    }
    /*

    public void actualizarEstrellas(){
        DocumentReference docRef = db.collection(gamemode).document(email).collection("datos_nivel").document(nivel_actual+"");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try{
                    int estrellas = Integer.parseInt(documentSnapshot.getData().get("estrellas")+"");
                    if(num_estrellas > estrellas){
                        db.collection(gamemode).document(email).collection("datos_nivel").document(nivel_actual+"").update("estrellas", num_estrellas);

                    }
                }catch(Exception e){
                    Log.w(TAG, "Error al acceder a las etrellas");
                }
            }
        });
    }


     */

}
