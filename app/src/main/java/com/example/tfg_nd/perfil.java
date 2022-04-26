package com.example.tfg_nd;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class perfil extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email;
    private final String TAG = "Perfil.java";

    FirebaseUser currentUser;
    Button btnCerrarSesion;
    TextView tvPerfil, tvDinero, tvNombre, tvNivel;
    ProgressBar pb;

    public perfil() {

    }

    public static perfil newInstance(String param1, String param2) {
        perfil fragment = new perfil();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        currentUser = mAuth.getCurrentUser();
        btnCerrarSesion = v.findViewById(R.id.profile_signout);
        tvPerfil = v.findViewById(R.id.textView6);
        tvDinero = v.findViewById(R.id.profile_dinero);
        tvNombre = v.findViewById(R.id.profile_nombre);
        tvNivel = v.findViewById(R.id.profile_nivel);
        pb = v.findViewById(R.id.profile_pb);

        if(currentUser!=null){
            email = currentUser.getEmail();
            DocumentReference docRef = db.collection("users").document(email);
            tvPerfil.setText(email);

            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        String dinero, nombre, nivel, exp;

                        dinero = snapshot.getData().get("dinero")+"";
                        if(dinero.equals("null")) dinero = "0";
                        nombre = snapshot.getData().get("nombre")+"";
                        if(nombre.equals("null")) nombre = "error";
                        nivel = snapshot.getData().get("nivel")+"";
                        if(nivel.equals("null")) nivel = "0";
                        exp = snapshot.getData().get("exp")+"";
                        if(exp.equals("null")) exp = "0";

                        tvDinero.setText(dinero);
                        tvNombre.setText(nombre);
                        tvNivel.setText(nivel);
                        pb.setProgress(Integer.parseInt(exp));
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        return v;
    }

    public void cerrarSesion(){
        if(currentUser==null){
            Toast.makeText(getContext(), "No hay ninguna sesión iniciada", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signOut();
            startActivity(new Intent(getContext(), HomeMenuActivity.class));
        }
    }

}