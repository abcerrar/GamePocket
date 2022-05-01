package com.example.tfg_nd;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class niveles extends Fragment {

    private final int MAX_NIVELES = 12;
    private final String TAG = "Niveles.java";
    private TextView[] niveles;
    private manejadorPreferencias mPref;
    private String gamemode, email;
    private int current_level;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public niveles() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_niveles, container, false);
        mPref = new manejadorPreferencias("pref", getActivity());
        FirebaseUser current_user = mAuth.getCurrentUser();

        niveles = new TextView[MAX_NIVELES];
        niveles[0] = v.findViewById(R.id.nivel1);
        niveles[1] = v.findViewById(R.id.nivel2);
        niveles[2] = v.findViewById(R.id.nivel3);
        niveles[3] = v.findViewById(R.id.nivel4);
        niveles[4] = v.findViewById(R.id.nivel5);
        niveles[5] = v.findViewById(R.id.nivel6);
        niveles[6] = v.findViewById(R.id.nivel7);
        niveles[7] = v.findViewById(R.id.nivel8);
        niveles[8] = v.findViewById(R.id.nivel9);
        niveles[9] = v.findViewById(R.id.nivel10);
        niveles[10] = v.findViewById(R.id.nivel11);
        niveles[11] = v.findViewById(R.id.nivel12);

        gamemode = mPref.get("gamemode", "error");

        if(!gamemode.equals("error") && current_user != null){
            email = current_user.getEmail();
            DocumentReference docRef = db.collection(gamemode).document(email);
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
                        current_level = Integer.parseInt(snapshot.getData().get("nivel")+"");
                        //Pintar los niveles completados
                        for(int i=0; i<MAX_NIVELES; i++){
                            if(i<current_level) niveles[i].setBackgroundColor(getResources().getColor(R.color.cuadro_nivel_completo));
                            else niveles[i].setBackgroundColor(getResources().getColor(R.color.cuadro_nivel));
                        }
                    } else {
                        //Si el ususario no tiene ninguna partida en este modo de jueg, la creas
                        Map<String, Object> game = new HashMap<>();
                        current_level = 1;
                        game.put("nivel", 1);

                        db.collection(gamemode).document(email).set(game)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "nueva sesion en "+ gamemode +" realizada correctamente");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error en la creación de sesion en: "+ gamemode, e);
                                }
                            });
                    }
                }
            });
        }else if(gamemode.equals("error")){
            Toast.makeText(getContext(), "Fallo al cargar el modo de juego seleccionado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Debes iniciar sesión para los juegos con progreso", Toast.LENGTH_SHORT).show();
        }




        return v;
    }
}