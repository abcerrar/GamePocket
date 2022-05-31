package com.example.tfg_nd;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.rpc.context.AttributeContext;

import java.util.HashMap;
import java.util.Map;

public class niveles extends Fragment {

    private final int MAX_NIVELES = 12;
    private final String TAG = "Niveles.java";
    private TextView[] niveles;
    private TextView[] estrellas;
    private manejadorPreferencias mPref;
    private String gamemode, email = "sin_email";
    private int current_level;
    private Button btReset, btJvj;
    private Window window;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //Colores
    int cuadro_nivel, cuadro_nivelCompleto;


    private Dialog dialog;

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

        dialog = new comp_estadisticas().getAnim(getContext());
        niveles = new TextView[MAX_NIVELES];
        estrellas = new TextView[MAX_NIVELES];
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
        estrellas[0] = v.findViewById(R.id.stars1);
        estrellas[1] = v.findViewById(R.id.stars2);
        estrellas[2] = v.findViewById(R.id.stars3);
        estrellas[3] = v.findViewById(R.id.stars4);
        estrellas[4] = v.findViewById(R.id.stars5);
        estrellas[5] = v.findViewById(R.id.stars6);
        estrellas[6] = v.findViewById(R.id.stars7);
        estrellas[7] = v.findViewById(R.id.stars8);
        estrellas[8] = v.findViewById(R.id.stars9);
        estrellas[9] = v.findViewById(R.id.stars10);
        estrellas[10] = v.findViewById(R.id.stars11);
        estrellas[11] = v.findViewById(R.id.stars12);
        btReset = v.findViewById(R.id.btReset);
        btJvj = v.findViewById(R.id.btJvj);
        window = getActivity().getWindow();
        dialog.show();

        FirebaseUser current_user = mAuth.getCurrentUser();
        if(current_user!=null) email = current_user.getEmail();
        mPref = new manejadorPreferencias(email, getActivity());

        window.setStatusBarColor(getActivity().getResources().getColor(R.color.azul));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a047")));

        gamemode = mPref.get("gamemode", "error");

        //Elegir los colores en funcion del juego
        ActionBar actionBar;
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        int color1=0, color2=0;
        switch (gamemode){
            case "porcentajes":
                color1 = getResources().getColor(R.color.morado);
                color2 = getResources().getColor(R.color.amarillo);
                break;
            case "memory":
                color1 = getResources().getColor(R.color.naranja);
                color2 = getResources().getColor(R.color.azul);
                break;
            case "tresraya":
                color1 = getResources().getColor(R.color.amarillo_2);
                color2 = getResources().getColor(R.color.rosa);
                break;
            default:
                Toast.makeText(getContext(), "Error cargando el gamemode", Toast.LENGTH_SHORT).show();
        }
        cuadro_nivel = color1;
        cuadro_nivelCompleto = color2;
        actionBar.setBackgroundDrawable(new ColorDrawable(color2));

        if(!gamemode.equals("error") && current_user != null){
            //Asignar en onClick a cada TextView
            for(int i = 0; i<MAX_NIVELES; i++){
                setOnClickLevel(i);
            }

            DocumentReference docRef = db.collection(gamemode).document(email);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {
                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        current_level = Integer.parseInt(snapshot.getData().get("nivel")+"");
                        //Pintar los niveles completados
                        for(int i=0; i<MAX_NIVELES; i++){
                            if(i<current_level){
                                niveles[i].setBackgroundColor(cuadro_nivel);
                                estrellas[i].setBackgroundColor(cuadro_nivel);
                                asignatEstrellas((i+1));
                                colorButton(niveles[i], estrellas[i], cuadro_nivel, cuadro_nivelCompleto);
                                //★
                            }else{
                                niveles[i].setBackgroundColor(cuadro_nivelCompleto);
                                estrellas[i].setBackgroundColor(cuadro_nivelCompleto);
                            }
                        }
                        dialog.dismiss();
                    } else {
                        crearPartida();
                    }
                }
            });

        }else if(gamemode.equals("error")){
            Toast.makeText(getContext(), "Fallo al cargar el modo de juego seleccionado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Debes iniciar sesión para los juegos con progreso", Toast.LENGTH_SHORT).show();
        }

        if(gamemode.equals("tresraya")) btJvj.setVisibility(View.VISIBLE);
        btJvj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToGame(-2); //Para pasarle -1
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPartida();
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.niveles);
            }
        });


        return v;
    }

    public void crearPartida(){
        //Si el ususario no tiene ninguna partida en este modo de jueg, la creas
        Map<String, Object> game = new HashMap<>();
        current_level = 1;
        game.put("nivel", 1);
        niveles[0].setBackgroundColor(cuadro_nivel);
        estrellas[0].setBackgroundColor(cuadro_nivel);
        for(int i=1; i<niveles.length; i++){
            niveles[i].setBackgroundColor(cuadro_nivelCompleto);
            estrellas[i].setBackgroundColor(cuadro_nivelCompleto);
        }

        DocumentReference docRef = db.collection(gamemode).document(email);
        docRef.set(game)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "nueva sesion en "+ gamemode +" realizada correctamente");
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error en la creación de sesion en: "+ gamemode, e);
                    }
                });
        Map<String, Object> datos_nivel = new HashMap<>();
        for(int i=0; i<MAX_NIVELES; i++){
            docRef = db.collection(gamemode).document(email).collection("datos_nivel").document((i+1)+"");
            datos_nivel.put("estrellas", 0);
            datos_nivel.put("num_nivel", (i+1));
            datos_nivel.put("completado", false);
            docRef.set(datos_nivel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "Datos de nivel cargados correctamente");
                }
            });
        }
    }

    public void setOnClickLevel(int nivel){
        //esto sta en una función porque si no se ejecutaba antes el bucle que la clase interna
        niveles[nivel].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nivel < current_level) {
                    jumpToGame(nivel);
                }
                else Toast.makeText(getContext(), "Debes pasarte los niveles anteriores ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void jumpToGame(int nivel){
        switch(gamemode){
            case "porcentajes":
                mPref.put("nivel", (nivel+1)+"");
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.puzzle_porcenaje);
                break;
            case "memory":
                mPref.put("nivel", (nivel+1)+"");
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.puzzle_memory);
                break;
            case "tresraya":
                mPref.put("nivel", (nivel+1)+"");
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.puzzle_tresraya);
                break;
            case "error":
                Toast.makeText(getContext(), "Error al seleccionar un modo de juego", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Error al seleccionar un modo de juego");
        }
    }

    public void asignatEstrellas(int nivel){
        DocumentReference docRef = db.collection(gamemode).document(email).collection("datos_nivel").document(nivel+"");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try{
                    int num_estrellas = Integer.parseInt(documentSnapshot.getData().get("estrellas")+"");
                    int num_nivel = Integer.parseInt(documentSnapshot.getData().get("num_nivel")+"");
                    switch(num_estrellas){
                        case 1:
                            estrellas[num_nivel-1].setText("★");
                            break;
                        case 2:
                            estrellas[num_nivel-1].setText("★★");
                            break;
                        case 3:
                            estrellas[num_nivel-1].setText("★★★");
                            break;

                    }
                }catch(Exception e){
                    Log.w(TAG, "Error al acceder a las etrellas");
                }
            }
        });
    }

    public void colorButton (TextView boton, TextView estrellas, int color1, int color2){
        boton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_UP:
                        boton.setBackgroundColor(color1);
                        estrellas.setBackgroundColor(color1);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        boton.setBackgroundColor(color2);
                        estrellas.setBackgroundColor(color2);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT: case MotionEvent.ACTION_CANCEL:
                        boton.setBackgroundColor(color1);
                        estrellas.setBackgroundColor(color1);
                        break;

                }
                return false;
            }
        });
    }

}
