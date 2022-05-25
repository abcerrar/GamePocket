package com.example.tfg_nd;

import android.app.AlertDialog;
import android.app.Dialog;
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
    private List<ImageView> pajaros_flappy = new ArrayList<>();

    private manejadorPreferencias mPref;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String email="sin_email", gamemode;
    private User user;
    private ListenerRegistration listener;
    private TextView tvDinero;
    private AlertDialog dialog;
    private String dorso_seleccionado, ficha_seleccionada, pajaro_seleccionado;

    Dialog anim_dialog;
    private final int PADDING = 25;

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
        anim_dialog =  new comp_estadisticas().getAnim(getContext());

        if(mAuth.getCurrentUser() != null){
            email = mAuth.getCurrentUser().getEmail();
            user = new User(email);
        }
        else email = "sin_email";
        mPref = new manejadorPreferencias(email, getActivity());


        img_memory.add(v.findViewById(R.id.mem_dorso_dollar));
        img_memory.add(v.findViewById(R.id.mem_dorso_rata));
        img_memory.add(v.findViewById(R.id.mem_dorso_sombra));

        fichas_tresraya.add(v.findViewById(R.id.tresraya_fichas_normales));
        fichas_tresraya.add(v.findViewById(R.id.tresraya_fichas_rojas));
        fichas_tresraya.add(v.findViewById(R.id.tresraya_fichas_rojazul));

        pajaros_flappy.add(v.findViewById(R.id.pajaro_pou));
        pajaros_flappy.add(v.findViewById(R.id.pajaro_azul));
        pajaros_flappy.add(v.findViewById(R.id.pajaro_hamb));

        tvDinero = v.findViewById(R.id.desc_dinero);

        fichas_tresraya.get(0).setColorFilter(getResources().getColor(R.color.black));

        dorso_seleccionado = mPref.get("dorso_memory", "dorso_rata");
        for(int i=0; i<img_memory.size(); i++){
            asignarOnClick(i, "memory");
            if(!img_memory.get(i).getTag().equals(dorso_seleccionado)) img_memory.get(i).setPadding(PADDING, PADDING, PADDING, PADDING);
            else img_memory.get(i).setPadding(0, 0, 0, 0);
        }
        ficha_seleccionada = mPref.get("fichas_tresraya", "fichas_normales");
        for(int i=0; i<fichas_tresraya.size(); i++){
            asignarOnClick(i, "tresraya");
            if(!fichas_tresraya.get(i).getTag().equals(ficha_seleccionada)) fichas_tresraya.get(i).setPadding(PADDING, PADDING, PADDING, PADDING);
            else fichas_tresraya.get(i).setPadding(0, 0, 0, 0);
        }

        pajaro_seleccionado = mPref.get("current_pajaro", "pajaro_azul");
        for(int i=0; i<pajaros_flappy.size(); i++){
            asignarOnClick(i, "flappy");
            if(!pajaros_flappy.get(i).getTag().equals(pajaro_seleccionado)) pajaros_flappy.get(i).setPadding(PADDING, PADDING, PADDING, PADDING);
            else pajaros_flappy.get(i).setPadding(0, 0, 0, 0);
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
                        anim_dialog.show();
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
                        anim_dialog.show();
                        ver_producto(nombre, imagen);
                    }
                });
                break;
            case "flappy":
                pajaros_flappy.get(num).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gamemode = "flappy";
                        Drawable imagen = pajaros_flappy.get(num).getDrawable();
                        String nombre = "";
                        try{
                            nombre = pajaros_flappy.get(num).getTag().toString();
                        }catch(NullPointerException e) {
                            Toast.makeText(getContext(), "Error leyendo el tag de la imagen", Toast.LENGTH_SHORT).show();
                        }
                        anim_dialog.show();
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
                anim_dialog.dismiss();
                String nom = nombre;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View custom_layout = getActivity().getLayoutInflater().inflate(R.layout.desc_producto, null);

                TextView titulo;
                Button comprar = custom_layout.findViewById(R.id.btComprar);
                ImageView desc_imagen = custom_layout.findViewById(R.id.desc_imagen);
                ImageView moneda = custom_layout.findViewById(R.id.moneda);

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
                    moneda.setImageDrawable(null);
                    switch (gamemode){
                        case "memory":
                            if(nombre.equals(dorso_seleccionado)){
                                comprar.setText("Seleccionado");
                                comprar.setEnabled(false);
                            }else{
                                comprar.setText("Seleccionar");
                                dorso_seleccionado = nombre;
                                comprar.setEnabled(true);
                            }
                            break;
                        case "tresraya":
                            if(nombre.equals(ficha_seleccionada)){
                                comprar.setText("Seleccionado");
                                comprar.setEnabled(false);
                            }else{
                                comprar.setText("Seleccionar");
                                ficha_seleccionada = nombre;
                                comprar.setEnabled(true);
                            }
                            break;
                        case "flappy":
                            if(nombre.equals(pajaro_seleccionado)){
                                comprar.setText("Seleccionado");
                                comprar.setEnabled(false);
                            }else{
                                comprar.setText("Seleccionar");
                                pajaro_seleccionado = nombre;
                                comprar.setEnabled(true);
                            }
                            break;

                    }

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
        if(email.equals("sin_email")) Toast.makeText(getContext(), "No puedes comprar ni seleccionar productos sin iniciar sesión", Toast.LENGTH_SHORT).show();
        else if(!user.incrementarDinero(-precio)) Toast.makeText(getContext(), "No tienes tanto dinero", Toast.LENGTH_SHORT).show();
        else{
            user.addProduct(nombre);
            seleccionarProducto(nombre);
            Toast.makeText(getContext(), "Comprando " + nombre + " por " + precio + " monedas", Toast.LENGTH_SHORT).show();
        }
    }
    public void seleccionarProducto(String nombre){

        user.selectProduct(nombre, gamemode);
        if(email.equals("sin_email")) Toast.makeText(getContext(), "No puedes comprar ni seleccionar productos sin iniciar sesión", Toast.LENGTH_SHORT).show();
        else{
            switch (gamemode){
                case "memory":
                    mPref.put("dorso_memory", nombre);
                    for(int i=0; i<img_memory.size(); i++) {
                        if(!img_memory.get(i).getTag().equals(nombre)) img_memory.get(i).setPadding(PADDING, PADDING, PADDING, PADDING);
                        else img_memory.get(i).setPadding(0, 0, 0, 0);
                    }
                    break;
                case "tresraya":
                    mPref.put("fichas_tresraya", nombre);
                    for(int i=0; i<fichas_tresraya.size(); i++) {
                        if(!fichas_tresraya.get(i).getTag().equals(nombre)) fichas_tresraya.get(i).setPadding(PADDING, PADDING, PADDING, PADDING);
                        else fichas_tresraya.get(i).setPadding(0, 0, 0, 0);
                    }
                    break;
                case "flappy":
                    mPref.put("pajaro", nombre);
                    for(int i=0; i<pajaros_flappy.size(); i++) {
                        if(!pajaros_flappy.get(i).getTag().equals(nombre)) pajaros_flappy.get(i).setPadding(PADDING, PADDING, PADDING, PADDING);
                        else pajaros_flappy.get(i).setPadding(0, 0, 0, 0);
                    }
                    break;
            }
            Toast.makeText(getContext(), "Seleccionando " + nombre, Toast.LENGTH_SHORT).show();
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