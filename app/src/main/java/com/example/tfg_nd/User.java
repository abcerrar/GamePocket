package com.example.tfg_nd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class User {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "Utils_db.java";

    private final String email;
    private int dinero;

    public User(String email){
        this.email = email;
        db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                dinero = Integer.parseInt(documentSnapshot.getData().get("dinero")+"");
            }
        });
    }

    public boolean incrementarDinero(int incremento){

        if((dinero + incremento) < 0) return false;
        else{

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
        return true;
    }

    public void incrementarExperiencia(int incremento){
        Log.d(TAG, "incrementado experiencia");
        db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int exp = Integer.parseInt(documentSnapshot.getData().get("exp")+"");
                int nivel = Integer.parseInt(documentSnapshot.getData().get("nivel")+"");
                int suma = exp + incremento;
                Log.d(TAG, "experiencia: " + exp);
                Log.d(TAG, "suma: " + suma);
                if(suma >= 100){
                    nivel++;
                    suma = suma - 100;
                    db.collection("users").document(email).update("nivel", nivel+"");
                }
                db.collection("users").document(email).update("exp", suma+"")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Experiencia incrementada correctamente");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error al incrementar la experiencia: " + e.getMessage());

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

    public void subirNivel(String gamemode, int nivel_actual){
        DocumentReference docRef = db.collection(gamemode).document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Subir un nivel
                        int nivel;
                        nivel = Integer.parseInt(document.get("nivel")+"");
                        Log.w(TAG, nivel_actual +"--"+nivel);
                        if(nivel == nivel_actual){
                            Log.w(TAG, email);
                            //Poner este nivel en completado
                            db.collection(gamemode).document(email).collection("datos_nivel").document(nivel+"").update("completado", true);
                            nivel++;
                            db.collection(gamemode).document(email).update("nivel", nivel+"");
                        }
                    } else {
                        //Si no existe crea un usuario nuevo con ese nombre
                        Log.d(TAG, "Error en subir nivel");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public AlertDialog alertFinalPartida(String titulo, String titulo2, int num_estrellas, int dinero, int experiencia, Activity activity, View.OnClickListener listenner1, View.OnClickListener listenner2_1, View.OnClickListener listenner2_2, View.OnClickListener listenner3, AlertDialog dialog, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View custom_layout = activity.getLayoutInflater().inflate(R.layout.end_game, null);
        ImageView reloadGame, backMenu, nextLevel;
        TextView tit, tit2, tvDinero, tvExperiencia;
        RatingBar rb;

        reloadGame = custom_layout.findViewById(R.id.reloadGame);
        backMenu = custom_layout.findViewById(R.id.backMenu);
        nextLevel = custom_layout.findViewById(R.id.nextLevel);
        tit = custom_layout.findViewById(R.id.tvTitulo);
        tit2 = custom_layout.findViewById(R.id.tvTitulo2);
        tvDinero = custom_layout.findViewById(R.id.tvGanaDinero);
        tvExperiencia = custom_layout.findViewById(R.id.tvGanaExperiencia);
        rb = custom_layout.findViewById(R.id.ratingBar);

        tit.setText(titulo);
        tit2.setText(titulo2);
        tvDinero.setText("+ " + dinero);
        tvExperiencia.setText("+ " +    experiencia+"");
        rb.setProgress(num_estrellas);

        reloadGame.setOnClickListener(listenner1);

        if(num_estrellas==0){
            nextLevel.setOnClickListener(listenner2_1);
        }else {
            nextLevel.setOnClickListener(listenner2_2);
        }
        backMenu.setOnClickListener(listenner3);

        builder.setCancelable(true);
        builder.setView(custom_layout);
        dialog = builder.create();
        return dialog;
    }

    public void addProduct(String nombre){
        Map<String, Object> nada = new HashMap<>();
        db.collection("users").document(email).collection("productos").document(nombre).set(nada);
    }

    public void selectProduct(String nombre, String juego){
        switch (juego){
            case "memory":
                db.collection("users").document(email).update("current_dorso", nombre);
                break;
            case "tresraya":
                db.collection("users").document(email).update("current_ficha", nombre);
                break;
        }
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
