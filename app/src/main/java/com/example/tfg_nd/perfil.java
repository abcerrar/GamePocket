package com.example.tfg_nd;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    Button btnCerrarSesion, btnBorrarCuenta;
    TextView tvPerfil, tvDinero, tvNombre, tvNivel, tvExp;
    ProgressBar pb;

    private String current_dorso, current_ficha;

    public perfil() {

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
        btnBorrarCuenta = v.findViewById(R.id.btBorrarCuenta);
        tvPerfil = v.findViewById(R.id.textView6);
        tvDinero = v.findViewById(R.id.profile_dinero);
        tvNombre = v.findViewById(R.id.profile_nombre);
        tvNivel = v.findViewById(R.id.profile_nivel);
        tvExp = v.findViewById(R.id.profile_exp);
        pb = v.findViewById(R.id.profile_pb);

        if(currentUser!=null){

            manejadorPreferencias mPref = new manejadorPreferencias("pref", getActivity());
            email = currentUser.getEmail();
            //Si hay otro correo aqui es que se quiere consultar el perfil de otro usuario
            String email_externo = mPref.get("email_externo", email);
            if(!email_externo.equals(email)) {
                email = email_externo;
                //Se pone de nuevo el email del usuario logeado por si quiere consultar su perfil
                mPref.put("email_externo", currentUser.getEmail());
            }

            DocumentReference docRef = db.collection("users").document(email);
            tvPerfil.setText(email);

            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {
                    if(snapshot!=null && snapshot.exists()){
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
                        tvExp.setText(exp+"/100");
                        pb.setProgress(Integer.parseInt(exp));
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

        btnBorrarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarUsuario();
            }
        });

        return v;
    }

    public void borrarUsuario(){
        db.collection("puzzle_1").document(email).delete();
        for(int i=1; i<12; i++){
            try{
                db.collection("puzzle_1").document(email).collection("datos_nivel").document(i+"").delete();
            }catch (Exception e){
                Log.d(TAG, "Error en el borrado");
            }
        }
        db.collection("puzzle_2").document(email).delete();
        for(int i=1; i<12; i++){
            try{
                db.collection("puzzle_2").document(email).collection("datos_nivel").document(i+"").delete();
            }catch (Exception e){
                Log.d(TAG, "Error en el borrado");
            }
        }
        db.collection("users").document(email).delete();
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "Usuario borrado");
            }
        });
        startActivity(new Intent(getContext(), HomeMenuActivity.class));
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