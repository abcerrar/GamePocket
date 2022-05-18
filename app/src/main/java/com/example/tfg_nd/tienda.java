package com.example.tfg_nd;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class tienda extends Fragment {

    private final String TAG = "tienda.java";

    private List<ImageView> img_memory = new ArrayList<>();
    private List<ImageView> fichas_tresraya = new ArrayList<>();

    private manejadorPreferencias mPref;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String email, gamemode;
    private User user;
    private ListenerRegistration listener;
    private TextView tvDinero;
    private AlertDialog dialog;

    public tienda() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tienda, container, false);
        mPref = new manejadorPreferencias("pref", getActivity());
        if(mAuth.getCurrentUser() != null){
            email = mAuth.getCurrentUser().getEmail();
            user = new User(email);
        }
        else email = "sin_email";


        img_memory.add(v.findViewById(R.id.mem_dorso_dollar));
        img_memory.add(v.findViewById(R.id.mem_dorso_sombra));
        img_memory.add(v.findViewById(R.id.mem_dorso_rata));

        fichas_tresraya.add(v.findViewById(R.id.tresraya_fichas_normales));
        fichas_tresraya.add(v.findViewById(R.id.tresraya_fichas_rojas));

        tvDinero = v.findViewById(R.id.desc_dinero);

        fichas_tresraya.get(0).setColorFilter(getResources().getColor(R.color.black));
        for(int i=0; i<img_memory.size(); i++){
            asignarOnClick(i, "memory");
        }
        for(int i=0; i<fichas_tresraya.size(); i++){
            asignarOnClick(i, "tresraya");
        }


        DocumentReference docRef = db.collection("users").document(email);
        listener = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    tvDinero.setText(snapshot.getData().get("dinero")+"");
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        return v;
    }

    public void asignarOnClick(int num, String juego){
        //Ya pensaré como optimizar esto
        switch (juego){
            case "memory":
                img_memory.get(num).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gamemode = "memory";
                        Drawable imagen = img_memory.get(num).getDrawable();
                        String nombre = "";
                        try{
                            nombre = img_memory.get(num).getTag().toString();
                        }catch(NullPointerException e) {
                            Toast.makeText(getContext(), "Error leyendo el tag de la imagen", Toast.LENGTH_SHORT).show();
                        }
                        ver_producto(nombre, imagen);
                    }
                });
                break;
            case "tresraya":
                fichas_tresraya.get(num).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gamemode = "tresraya";
                        Drawable imagen = fichas_tresraya.get(num).getDrawable();
                        String nombre = "";
                        try{
                            nombre = fichas_tresraya.get(num).getTag().toString();
                        }catch(NullPointerException e) {
                            Toast.makeText(getContext(), "Error leyendo el tag de la imagen", Toast.LENGTH_SHORT).show();
                        }
                        ver_producto(nombre, imagen);
                    }
                });
                break;
        }

    }

    public void ver_producto(String nombre, Drawable imagen){
        DocumentReference docRef = db.collection("users").document(email).collection("productos").document(nombre);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                boolean sw = true; //Comprobar si ya lo tiene
                if(!documentSnapshot.exists()) sw = true;
                else sw = false;

                mostrar_producto(sw, nombre, imagen);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "No tienes internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mostrar_producto(boolean sw, String nombre, Drawable imagen){
        db.collection("productos").document(nombre).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nom = nombre;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View custom_layout = getActivity().getLayoutInflater().inflate(R.layout.desc_producto, null);

                TextView titulo;
                Button comprar = custom_layout.findViewById(R.id.btComprar);
                ImageView desc_imagen = custom_layout.findViewById(R.id.desc_imagen);

                titulo = custom_layout.findViewById(R.id.desc_titulo);
                desc_imagen.setImageDrawable(imagen);
                nom = nombre.replace("_", " ");
                titulo.setText(nom);


                if(sw){
                    try{
                        int precio = Integer.parseInt(documentSnapshot.getData().get("precio")+"");
                        comprar.setText(precio+"");
                        comprar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                comprarProducto(nombre, precio);
                                dialog.dismiss();
                            }
                        });
                    }catch(NullPointerException e){
                        Toast.makeText(getContext(), "Error al leer el precio de " + nombre, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    comprar.setText("Seleccionar");
                    comprar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            seleccionarProducto(nombre);
                            dialog.dismiss();
                        }
                    });
                }

                builder.setCancelable(true);
                builder.setView(custom_layout);
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void comprarProducto(String nombre, int precio){
        Toast.makeText(getContext(), "Comprando " + nombre + " por " + precio + " monedas", Toast.LENGTH_SHORT).show();
        if(!user.incrementarDinero(-precio)) Toast.makeText(getContext(), "No tienes tanto dinero", Toast.LENGTH_SHORT).show();
        else user.addProduct(nombre);
    }
    public void seleccionarProducto(String nombre){
        Toast.makeText(getContext(), "Seleccionando " + nombre, Toast.LENGTH_SHORT).show();
        user.selectProduct(nombre, gamemode);

        switch (gamemode){
            case "memory":
                mPref.put("dorso_memory", nombre);
                break;
            case "tresraya":
                mPref.put("fichas_tresraya", nombre);
                break;
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try{
            listener.remove();
        }catch(Exception e){
            Log.d(TAG, "Error al remover el listenner de la tienda");
        }
    }



}