package com.example.tfg_nd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class puzzle_memory extends Fragment {

    //Constantes utiles
    private final String TAG = "puzzle_memory.java";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private manejadorPreferencias mPref;
    private final String gamemode = "memory";
    private String email;
    private User user;

    //Variables ingame
    private TextView tvMovimientos, tvParejas;
    private int movimientos = 0, last_pressed, parejas_completadas = 0;
    private boolean comprobar = false;


    //Constantes juego
    private int nivel_actual = 0;
    private final int total_cartas = 20;
    private final int total_imagenes = 10;
    private ImageView[] cartas;
    private ArrayList<String> imagenes;
    private int max_parejas;



    public puzzle_memory() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_puzzle_memory, container, false);

        mPref = new manejadorPreferencias("pref", getActivity());
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            email = currentUser.getEmail();
            user = new User(email);
        }

        nivel_actual = Integer.parseInt(mPref.get("nivel", "0"));
        cartas = new ImageView[total_cartas];
        imagenes = new ArrayList<>(total_imagenes);

        cartas[0] = v.findViewById(R.id.catrta1);
        cartas[1] = v.findViewById(R.id.catrta2);
        cartas[2] = v.findViewById(R.id.catrta3);
        cartas[3] = v.findViewById(R.id.catrta4);
        cartas[4] = v.findViewById(R.id.catrta5);
        cartas[5] = v.findViewById(R.id.catrta6);
        cartas[6] = v.findViewById(R.id.catrta7);
        cartas[7] = v.findViewById(R.id.catrta8);
        cartas[8] = v.findViewById(R.id.catrta9);
        cartas[9] = v.findViewById(R.id.catrta10);
        cartas[10] = v.findViewById(R.id.catrta11);
        cartas[11] = v.findViewById(R.id.catrta12);
        cartas[12] = v.findViewById(R.id.catrta13);
        cartas[13] = v.findViewById(R.id.catrta14);
        cartas[14] = v.findViewById(R.id.catrta15);
        cartas[15] = v.findViewById(R.id.catrta16);
        cartas[16] = v.findViewById(R.id.catrta17);
        cartas[17] = v.findViewById(R.id.catrta18);
        cartas[18] = v.findViewById(R.id.catrta19);
        cartas[19] = v.findViewById(R.id.catrta20);
        //Luce contraproducente el meterlo en un arraylist pero es por el shuffle
        imagenes.add(R.drawable.memoryimage1+"");
        imagenes.add(R.drawable.memoryimage1+"");
        imagenes.add(R.drawable.memoryimage2+"");
        imagenes.add(R.drawable.memoryimage2+"");
        imagenes.add(R.drawable.memoryimage3+"");
        imagenes.add(R.drawable.memoryimage3+"");
        imagenes.add(R.drawable.memoryimage4+"");
        imagenes.add(R.drawable.memoryimage4+"");
        imagenes.add(R.drawable.memoryimage5+"");
        imagenes.add(R.drawable.memoryimage5+"");
        imagenes.add(R.drawable.memoryimage6+"");
        imagenes.add(R.drawable.memoryimage6+"");

        tvMovimientos = v.findViewById(R.id.tvMovimientos);
        tvParejas = v.findViewById(R.id.tvParejas);

        tvMovimientos.setText("0");

        switch (nivel_actual){
            case 1: case 2: case 3: case 4:
                gamemode1();
                break;
            case 5: case 6: case 7: case 8:
                gamemode2();
                break;
            case 9: case 10: case 11: case 12:
                gamemode3();
                break;
        }

        return v;
    }

    public void gamemode1(){
        cartas[12].setVisibility(View.INVISIBLE);
        cartas[13].setVisibility(View.INVISIBLE);
        cartas[14].setVisibility(View.INVISIBLE);
        cartas[15].setVisibility(View.INVISIBLE);
        cartas[16].setVisibility(View.INVISIBLE);
        cartas[17].setVisibility(View.INVISIBLE);
        cartas[18].setVisibility(View.INVISIBLE);
        cartas[19].setVisibility(View.INVISIBLE);
        cargarImagenes(12);

        max_parejas = 6;
    }
    public void gamemode2(){
        cartas[16].setVisibility(View.INVISIBLE);
        cartas[17].setVisibility(View.INVISIBLE);
        cartas[18].setVisibility(View.INVISIBLE);
        cartas[19].setVisibility(View.INVISIBLE);
        cargarImagenes(16);
        max_parejas = 8;
    }
    public void gamemode3(){
        max_parejas = 10;
        cargarImagenes(20);
    }

    public void cargarImagenes(int cantidad){
        if(cantidad == 16){
            imagenes.add(R.drawable.memoryimage7+"");
            imagenes.add(R.drawable.memoryimage7+"");
            imagenes.add(R.drawable.memoryimage8+"");
            imagenes.add(R.drawable.memoryimage8+"");
        }else if(cantidad == 20){
            imagenes.add(R.drawable.memoryimage7+"");
            imagenes.add(R.drawable.memoryimage7+"");
            imagenes.add(R.drawable.memoryimage8+"");
            imagenes.add(R.drawable.memoryimage8+"");
            imagenes.add(R.drawable.memoryimage9+"");
            imagenes.add(R.drawable.memoryimage9+"");
            imagenes.add(R.drawable.memoryimage10+"");
            imagenes.add(R.drawable.memoryimage10+"");
        }

        Collections.shuffle(imagenes);
        for(int i=0; i<cantidad; i++){
            cartas[i].setImageResource(R.drawable.dorso);
            onClickRevelar(i);
        }
    }

    public void onClickRevelar(int num_carta){
        cartas[num_carta].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cod_carta = Integer.parseInt(imagenes.get(num_carta));
                cartas[num_carta].setImageResource(cod_carta);

                if(comprobar){
                    if(cartas[num_carta].getDrawable().getConstantState().equals(cartas[last_pressed].getDrawable().getConstantState())){
                        parejas_completadas++;
                        tvParejas.setText(parejas_completadas+"");
                    }else{
                        deshabilitar_botones();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cartas[num_carta].setImageResource(R.drawable.dorso);
                                cartas[last_pressed].setImageResource(R.drawable.dorso);
                                habilitar_botones();
                            }
                        }, 1000);
                    }
                    movimientos++;
                    tvMovimientos.setText(movimientos+"");
                    comprobar = false;
                }else{
                    last_pressed =  num_carta;
                    comprobar = true;
                }

            }
        });
    }

    public void deshabilitar_botones(){
        for(int i=0; i<total_cartas; i++){
            cartas[i].setEnabled(false);
        }
    }
    public void habilitar_botones(){
        for(int i=0; i<total_cartas; i++){
            cartas[i].setEnabled(true);
        }
    }




}