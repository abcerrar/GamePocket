package com.example.tfg_nd;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.Collections;

public class puzzle_memory extends Fragment {

    //Constantes utiles
    private final String TAG = "puzzle_memory.java";
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private manejadorPreferencias mPref;
    private final String gamemode = "memory";
    private String email;
    private User user;
    private AlertDialog dialog;
    private View.OnClickListener listenerReload, listenerMenu, listenerNext, listenerNext2;

    //Variables ingame
    private TextView tvMovimientos, tvParejas, tvTitulo;
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
        tvTitulo = v.findViewById(R.id.tvTitulo_2);
        listenerReload = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadGame();
            }
        };
        listenerNext2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Tu flipas", Toast.LENGTH_SHORT).show();
            }
        };
        listenerNext = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadGame();
                if(nivel_actual==12){
                    Toast.makeText(getContext(), "Ya te has pasado todos los niveles", Toast.LENGTH_SHORT).show();
                    assert getParentFragment() != null;
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.niveles);
                }else{
                    mPref.put("nivel", (nivel_actual+1)+"");
                    assert getParentFragment() != null;
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.puzzle_memory);
                }
            }
        };
        listenerMenu = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getParentFragment() != null;
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.niveles);
                dialog.dismiss();
            }
        };


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
        tvParejas.setText(max_parejas+"");
        tvMovimientos.setText("0");
        tvTitulo.setText("Nivel: " + nivel_actual);

        Toast.makeText(getContext(), "size: " + imagenes.size(), Toast.LENGTH_SHORT).show();
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

        tvParejas.setText(max_parejas+"");
        tvMovimientos.setText("0");
        tvTitulo.setText("Nivel: " + nivel_actual);

        parejas_completadas = 0;
        movimientos = 0;
        max_parejas = 6;
    }
    public void gamemode2(){
        cartas[16].setVisibility(View.INVISIBLE);
        cartas[17].setVisibility(View.INVISIBLE);
        cartas[18].setVisibility(View.INVISIBLE);
        cartas[19].setVisibility(View.INVISIBLE);
        cargarImagenes(16);

        tvParejas.setText(max_parejas+"");
        tvMovimientos.setText("0");
        tvTitulo.setText("Nivel: " + nivel_actual);

        parejas_completadas = 0;
        movimientos = 0;
        max_parejas = 8;
    }
    public void gamemode3(){
        tvParejas.setText(max_parejas+"");
        tvMovimientos.setText("0");
        tvTitulo.setText("Nivel: " + nivel_actual);

        parejas_completadas = 0;
        movimientos = 0;
        max_parejas = 10;
        cargarImagenes(20);
    }

    public void cargarImagenes(int cantidad){
        if(cantidad == 16 && imagenes.size() == 12){
            imagenes.add(R.drawable.memoryimage7+"");
            imagenes.add(R.drawable.memoryimage7+"");
            imagenes.add(R.drawable.memoryimage8+"");
            imagenes.add(R.drawable.memoryimage8+"");
        }else if(cantidad == 20 && imagenes.size() == 12){
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
                if(cartas[num_carta].getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.dorso).getConstantState())){
                    int cod_carta = Integer.parseInt(imagenes.get(num_carta));
                    cartas[num_carta].setImageResource(cod_carta);

                    if(comprobar){
                        if(cartas[num_carta].getDrawable().getConstantState().equals(cartas[last_pressed].getDrawable().getConstantState())){
                            parejas_completadas++;
                            int parejas_restantes = max_parejas - parejas_completadas;
                            tvParejas.setText(parejas_restantes+"");
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

                        if(max_parejas - parejas_completadas == 0){
                            victoria();
                        }
                    }else{
                        last_pressed =  num_carta;
                        comprobar = true;
                    }
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

    public void reloadGame(){
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
        dialog.dismiss();
    }

    public void victoria(){
        int max_movimientos, estrellas, dinero, experiencia;
        String titulo, titutlo2;

        max_movimientos= max_parejas + (max_parejas/2);
        Toast.makeText(getContext(), "Max movimientos: " + max_movimientos, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "movimientos: " + movimientos, Toast.LENGTH_SHORT).show();

        if(movimientos <= max_movimientos){
            titulo = "Perfecto";
            titutlo2 = "Te lo has pasado en pocos movimientos";
            estrellas = 3;
            dinero = 10;
            experiencia = 20;
        }else if(movimientos <= max_movimientos+2){
            titulo = "Bien";
            titutlo2 = "Puedes pasar al siguiente nivel";
            estrellas = 2;
            dinero = 5;
            experiencia = 10;
        }else if(movimientos <= max_movimientos+4){
            titulo = "Por poco";
            titutlo2 = "Por poco, pero lo has superado";
            estrellas = 1;
            dinero = 1;
            experiencia = 2;
        }else{
            titulo = "Fallaste!";
            titutlo2 = "Has hecho demasiados movimientos";
            estrellas = 0;
            dinero = 0;
            experiencia = 0;
        }
        user.incrementarExperiencia(experiencia);
        user.incrementarDinero(dinero);
        user.actualizarEstrellas(estrellas, gamemode, nivel_actual);
        if(estrellas>0) user.subirNivel(gamemode, nivel_actual);
        dialog = user.alertFinalPartida(titulo, titutlo2, estrellas*2, dinero, experiencia, getActivity(), listenerReload, listenerNext2, listenerNext, listenerMenu, dialog, getContext());
        dialog.show();
    }
}