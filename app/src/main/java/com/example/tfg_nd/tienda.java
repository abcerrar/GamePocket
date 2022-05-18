package com.example.tfg_nd;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class tienda extends Fragment {

    List<ImageView> imagenes = new ArrayList<ImageView>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String email;

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

        imagenes.add(v.findViewById(R.id.mem_dorso_dollar));

        if(mAuth.getCurrentUser() != null) email = mAuth.getCurrentUser().getEmail();
        else email = "sin_email";

        for(int i=0; i<imagenes.size(); i++){
            asignarOnClick(i);
        }


        return v;
    }

    public void asignarOnClick(int num){
        imagenes.get(num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable imagen = imagenes.get(num).getDrawable();
                String nombre = imagenes.get(num).getTag().toString();
                ver_producto(nombre, imagen);
            }
        });
    }

    public void ver_producto(String nombre, Drawable imagen){
        DocumentReference docRef = db.collection("users").document(email).collection("productos").document(nombre);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(!documentSnapshot.exists()){
                    Toast.makeText(getContext(), "Hola chicos", Toast.LENGTH_SHORT).show();
                    db.collection("productos").document(nombre).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            View custom_layout = getActivity().getLayoutInflater().inflate(R.layout.end_game, null);

                            TextView titulo, precio;
                            Button comprar = custom_layout.findViewById(R.id.btComprar);
                            ImageView desc_imagen = custom_layout.findViewById(R.id.desc_imagen);

                            titulo = custom_layout.findViewById(R.id.desc_titulo);
                            precio = custom_layout.findViewById(R.id.desc_precio);
                            //desc_imagen.setImageDrawable(imagen);

                            titulo.setText(nombre);
                            precio.setText(documentSnapshot.getData().get("precio")+"");
                            builder.setCancelable(true);
                            builder.setView(custom_layout);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "Ya lo tienes tonto", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });


    }

}