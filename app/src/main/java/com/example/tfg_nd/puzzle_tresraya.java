package com.example.tfg_nd;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.util.Log;
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
import java.util.List;

public class puzzle_tresraya extends Fragment {

    //Constantes utiles
    private final String TAG = "puzzle_tresraya.java";
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private manejadorPreferencias mPref;
    private final String gamemode = "tresraya";
    private String email = "sin_email";
    private User user;
    private AlertDialog dialog;
    private int nivel_actual = 0;
    private View.OnClickListener listenerReload, listenerMenu, listenerNext2, listenerNext;
    private TextView lineaAspa, lineaCirculo, j1, j2, tvNivel, tvDificultad;
    private FirebaseUser currentUser;

    //Constantes juego
    private ImageView fichas[];
    private int turno = 1;
    private ImageView btAspa, btCirculo;
    private  int imgcirculo, imgaspa;

    //Variables pve
    private int imagenj1, imagenj2;

    //Variables jvj
    private boolean player1 = true;


    public puzzle_tresraya() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_puzzle_tresraya, container, false);
        currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            email = currentUser.getEmail();
            user = new User(email);
        }else user = new User();
        mPref = new manejadorPreferencias(email, getActivity());

        nivel_actual = Integer.parseInt(mPref.get("nivel", "0"));
        Toast.makeText(getContext(), "Nivel: " + nivel_actual, Toast.LENGTH_SHORT).show();
        fichas = new ImageView[9];
        fichas[0] = v.findViewById(R.id.ficha11);
        fichas[1] = v.findViewById(R.id.ficha12);
        fichas[2] = v.findViewById(R.id.ficha13);
        fichas[3] = v.findViewById(R.id.ficha21);
        fichas[4] = v.findViewById(R.id.ficha22);
        fichas[5] = v.findViewById(R.id.ficha23);
        fichas[6] = v.findViewById(R.id.ficha31);
        fichas[7] = v.findViewById(R.id.ficha32);
        fichas[8] = v.findViewById(R.id.ficha33);
        btAspa = v.findViewById(R.id.btAspa);
        btCirculo = v.findViewById(R.id.btCirculo);
        lineaAspa = v.findViewById(R.id.lineaAspa);
        lineaCirculo = v.findViewById(R.id.lineaCirculo);
        tvNivel = v.findViewById(R.id.tituloNivelTresraya);
        tvDificultad = v.findViewById(R.id.dificultadtresraya);
        j1 = v.findViewById(R.id.j1);
        j2 = v.findViewById(R.id.j2);

        String ficha_actual = mPref.get("fichas_tresraya", "fichas_normales");
        switch (ficha_actual){
            case "fichas_normales":
                imgcirculo = R.drawable.circulo;
                imgaspa = R.drawable.aspa;
                break;
            case "fichas_rojas":
                imgcirculo = R.drawable.circulorojo;
                imgaspa = R.drawable.asparoja;
                btAspa.setImageResource(R.drawable.asparoja);
                btCirculo.setImageResource(R.drawable.circulorojo);
                break;
            case "fichas_rosazul":
                imgcirculo = R.drawable.circuloazul;
                imgaspa = R.drawable.asparosa;
                btAspa.setImageResource(R.drawable.asparosa);
                btCirculo.setImageResource(R.drawable.circuloazul);
                break;
            default:
                imgcirculo = R.drawable.circulo;
                imgaspa = R.drawable.aspa;
                break;
        }
        //Toast.makeText(getContext(), "Fichas: " + imgcirculo + ", " + imgaspa, Toast.LENGTH_SHORT).show();
        imagenj1 = imgaspa;
        imagenj2 = imgcirculo;
        listenerReload = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar_tablero();
                dialog.dismiss();
            }
        };
        listenerMenu = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser == null){
                    Intent i = new Intent(getContext(), HomeMenuActivity.class);
                    startActivity(i);
                }else{
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.niveles);
                    dialog.dismiss();
                }
            }
        };
        listenerNext = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nivel_actual==12){
                    Toast.makeText(getContext(), "Ya te has pasado todos los niveles", Toast.LENGTH_SHORT).show();
                    assert getParentFragment() != null;
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.niveles);
                }else{
                    mPref.put("nivel", (nivel_actual+1)+"");
                    nivel_actual++;
                    tvNivel.setText("Nivel: " + nivel_actual);
                    limpiar_tablero();
                }
                dialog.dismiss();
            }
        };
        listenerNext2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Tu flipas", Toast.LENGTH_SHORT).show();
            }
        };

        btAspa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenj2 = imgcirculo;
                imagenj1 = imgaspa;
                lineaAspa.setVisibility(View.VISIBLE);
                lineaCirculo.setVisibility(View.INVISIBLE);
            }
        });
        btCirculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenj2 = imgaspa;
                imagenj1 = imgcirculo;
                lineaAspa.setVisibility(View.INVISIBLE);
                lineaCirculo.setVisibility(View.VISIBLE);
            }
        });

        tvNivel.setText("Nivel: " + nivel_actual);
        limpiar_tablero();

        return v;
    }

    public void gamemode_facil(){
        tvDificultad.setText("Dificultad: Fácil");
        for(int i=0; i< fichas.length; i++){
            asignarOnClick(i, 1);
        }
    }

    public void gamemode_normal(){
        tvDificultad.setText("Dificultad: Media");
        for(int i=0; i< fichas.length; i++){
            asignarOnClick(i, 2);
        }
    }

    public void gamemode_dificil(){
        tvDificultad.setText("Dificultad: Difícil");
        for(int i=0; i<fichas.length; i++){
            asignarOnClick(i, 3);
        }

    }

    public void gamemode_imposible(){
        tvDificultad.setText("Dificultad: Imposible");

        for(int i=0; i< fichas.length; i++){
            fichas[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "En este nivel debes elegir antes con que forma jugar", Toast.LENGTH_SHORT).show();
                }
            });
        }
        lineaAspa.setVisibility(View.INVISIBLE);
        lineaCirculo.setVisibility(View.INVISIBLE);

        btAspa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineaAspa.setVisibility(View.VISIBLE);
                btCirculo.setClickable(false);
                imagenj2 = imgcirculo;
                imagenj1 = imgaspa;

                for(int i=0; i< fichas.length; i++){
                    asignarOnClick(i, 3);
                }
                deshabilitarBotones();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fichas[4].setImageResource(imagenj2);
                        habilitarBotones();
                    }
                }, 1000);
            }
        });
        btCirculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineaCirculo.setVisibility(View.VISIBLE);
                btAspa.setClickable(false);
                imagenj2 = imgaspa;
                imagenj1 = imgcirculo;

                for(int i=0; i< fichas.length; i++){
                    asignarOnClick(i, 3);
                }

                deshabilitarBotones();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fichas[4].setImageResource(imagenj2);
                        habilitarBotones();
                    }
                }, 1000);
            }
        });

    }

    public void gamemode_jvj(){
        tvNivel.setText("JvJ");
        j1.setVisibility(View.VISIBLE);
        j2.setVisibility(View.VISIBLE);
        btCirculo.setClickable(false);
        btAspa.setClickable(false);
        lineaAspa.setVisibility(View.INVISIBLE);
        tvDificultad.setVisibility(View.INVISIBLE);
        //Asignar los onclick
        for(int i=0; i< fichas.length; i++){
            asignarOnClickJvj(i);
        }
        turno = 1;
        listenerNext = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "En JvJ no hay niveles", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void limpiar_tablero(){
        switch (nivel_actual){
            case -1:
                gamemode_jvj();
                break;
            case 1: case 2: case 3: case 4:
                gamemode_facil();
                break;
            case 5: case 6: case 7: case 8:
                gamemode_normal();
                break;
            case 9: case 10: case 11:
                gamemode_dificil();
                break;
            case 12:
                gamemode_imposible();
                break;
        }
        for(int i=0; i< fichas.length; i++){
            fichas[i].setImageDrawable(null);
        }
        turno = 1;
        player1 = true;
    }

    public void asignarOnClick(int num_ficha, int dificultad){
        fichas[num_ficha].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fichas[num_ficha].getDrawable() == null){
                    fichas[num_ficha].setImageResource(imagenj1);

                    if (checkVictory(imagenj1)) {
                        victoria(dificultad);
                    }else{
                        deshabilitarBotones();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                switch (dificultad){
                                    case 1:
                                        respuestaFacil();
                                        break;
                                    case 2:
                                        if(!respuestaMedia()) respuestaFacil();
                                        break;
                                    case 3:
                                        respuestaDificil();
                                        break;
                                }
                                habilitarBotones();
                                if(checkVictory(imagenj2)) {
                                    derrota(dificultad);
                                }
                                boolean empate = true;
                                for(int i=0; i<fichas.length; i++) if(fichas[i].getDrawable() == null) empate = false;

                                if(empate){
                                    dialog = user.alertFinalPartida("Empate.", "Puedes intentarlo de nuevo", 0, 0, 0, getActivity(), listenerReload, listenerNext2, listenerNext, listenerMenu, dialog, getContext());
                                    dialog.show();
                                }
                            }
                        }, 1000);
                    }
                }
            }
        });
    }

    public void victoria(int dificultad){
        int dinero=0, exp=0, estrellas = 6;
        String tit1="", tit2="";
        switch (dificultad){
            case 1:
                tit1 = "Enhorabuena";
                tit2 = "Has superado el nivel facil";
                dinero = 1;
                exp = 2;
                break;
            case 2:
                tit1 = "Victoria!!";
                tit2 = "Has superado el nivel medio";
                dinero = 2;
                exp = 5;
                break;
            case 3:
                tit1 = "Enhorabuena!!!";
                tit2 = "Has superado el nivel más dificil";
                dinero = 5;
                exp = 10;
                break;
        }
        dialog = user.alertFinalPartida(tit1, tit2, estrellas, dinero, exp, getActivity(), listenerReload, listenerNext2, listenerNext, listenerMenu, dialog, getContext());
        dialog.show();

        user.subirNivel(gamemode, nivel_actual);
        user.actualizarEstrellas(3, gamemode, nivel_actual);
        user.incrementarDinero(dinero);
        user.incrementarExperiencia(exp);

    }

    public void derrota(int dificultad){
        String tit1="", tit2="";
        switch (dificultad){
            case 1:
                tit1 = "Has perdido";
                tit2 = "Y este era el nivel más facil...";
                break;
            case 2:
                tit1 = "Has perdido";
                tit2 = "Y ni siquiera era el nivel más dificil...";
                break;
            case 3:
                tit1 = "Has perdido";
                tit2 = "Este es el nivel más dificil";
                break;
        }
        dialog = user.alertFinalPartida(tit1, tit2, 0, 0, 0, getActivity(), listenerReload, listenerNext2, listenerNext, listenerMenu, dialog, getContext());
        dialog.show();
    }

    public void deshabilitarBotones(){
        for(int i=0; i<fichas.length; i++){
            fichas[i].setEnabled(false);
        }
    }

    public void habilitarBotones(){
        for(int i=0; i<fichas.length; i++){
            fichas[i].setEnabled(true);
        }
    }

    public void respuestaFacil(){
        List<ImageView> imagenes = new ArrayList<ImageView>();
        int i;
        for(i=0; i< fichas.length; i++)
            if(fichas[i].getDrawable()==null) imagenes.add(fichas[i]);

        if(imagenes.size()!=0){
            Collections.shuffle(imagenes);
            imagenes.get(0).setImageResource(imagenj2);
        }
    }

    public boolean respuestaMedia(){
        int num2;

        for(int i=0; i<3; i++){
            if((num2=checkLinea(i, imagenj2, false))!=-1){
                fichas[num2].setImageResource(imagenj2);
                return true;
            }
            if((num2=checkLinea(i, imagenj2, true))!=-1){
                fichas[num2].setImageResource(imagenj2);
                return true;
            }
        }

        for(int i=0; i<3; i++){
            if((num2=checkLinea(i, imagenj1, false))!=-1){
                fichas[num2].setImageResource(imagenj2);
                return true;
            }else if((num2=checkLinea(i, imagenj1, true))!=-1){
                fichas[num2].setImageResource(imagenj2);
                return true;
            }
        }
        if((num2=checkDiagonal(imagenj1, true))!=-1){
            fichas[num2].setImageResource(imagenj2);
            return true;
        }
        if((num2=checkDiagonal(imagenj1, false))!=-1){
            fichas[num2].setImageResource(imagenj2);
            return true;
        }
        return false;
    }

    public boolean respuestaDificil(){
        int num = (int)(Math.random()*5+1);
        if(num == 3){
            respuestaFacil();
            return true;
        }else{
            if(!respuestaMedia()){
                if(fichas[0].getDrawable() == null){
                    fichas[0].setImageResource(imagenj2);
                    return true;
                }
                if(fichas[2].getDrawable() == null) {
                    fichas[2].setImageResource(imagenj2);
                    return true;
                }
                if(fichas[6].getDrawable() == null){
                    fichas[6].setImageResource(imagenj2);
                    return true;
                }
                if(fichas[8].getDrawable() == null) {
                    fichas[8].setImageResource(imagenj2);
                    return true;
                }
                respuestaFacil();
                return true;
            }else{
                return false;
            }
        }
    }

    public int checkLinea(int num_linea, int imagen, boolean fila){
        int cont = 0, max, incremento;

        if(fila){
            max = 3;
            incremento = 1;

            if(num_linea == 1) num_linea = 3;
            else if (num_linea == 2) num_linea = 6;
        }else{
            max = 7;
            incremento = 3;
        }

        for(int i=num_linea; i<num_linea + max; i+=incremento){
            try{
                if(getState(fichas[i]) == getResources().getDrawable(imagen).getConstantState()) cont++;
            }catch(NullPointerException e){}
        }
        if(cont == 2){
            for(int i=num_linea; i<(num_linea + max); i+=incremento)
                try{
                    if(fichas[i].getDrawable() == null) return i;
                }catch(NullPointerException e){}
        }
        return -1;
    }

    public int checkDiagonal(int imagen, boolean diagonal1){
        int cont = 0, inicio, maximo, incremento;
        if(diagonal1){
            inicio = 0;
            maximo = fichas.length;
            incremento = 4;
        }else{
            inicio = 2;
            maximo = 7;
            incremento = 2;
        }

        for(int i = inicio; i < maximo; i+=incremento){
            try{
                if(getState(fichas[i]) == getResources().getDrawable(imagen).getConstantState()) cont++;
            }catch(NullPointerException e){}
        }
        if(cont == 2){
            for(int i = inicio; i < maximo; i+=incremento)
                try{
                    if(fichas[i].getDrawable() == null) return i;
                }catch(NullPointerException e){}
        }
        return -1;
    }

    public void asignarOnClickJvj(int num_ficha){
        fichas[num_ficha].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fichas[num_ficha].getDrawable() == null){
                    if(player1){
                        fichas[num_ficha].setImageResource(imagenj1);
                        //Comprobar si el jugador 1 gana con ese movimiento
                        if(turno >= 3 && checkVictory(imagenj1)){
                            dialog = user.alertFinalPartida("Victoria de J1!!", "En JvJ no se gana \ndinero ni estrellas", 0, 0, 0, getActivity(), listenerReload, listenerNext, listenerNext, listenerMenu, dialog, getContext());
                            dialog.show();
                        }
                        player1 = false;
                    } else{
                        fichas[num_ficha].setImageResource(imagenj2);
                        if(turno >= 3 && checkVictory(imagenj2)){
                            dialog = user.alertFinalPartida("Victoria de J2!!", "En JvJ no se gana \ndinero ni estrellas", 0, 0, 0, getActivity(), listenerReload, listenerNext, listenerNext, listenerMenu, dialog, getContext());
                            dialog.show();
                        }
                        player1 = true;
                        turno++;
                        Log.d(TAG, "Turno: " + turno);
                    }
                }else{
                    Toast.makeText(getContext(), "No puedes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkVictory(int imagen){
        int fila = 0, columna = 0;
        Drawable.ConstantState state_imagen = getResources().getDrawable(imagen).getConstantState();
        for(int i=0; i<9; i++){
            //Comprobar las filas
            if(i==3 || i==6) fila = 0;
            if(fichas[i].getDrawable()!=null) if(getState(fichas[i]).equals(state_imagen)) fila++;
            else fila = 0;
            if (fila == 3) return true;

            //Comprobar columnas
            for(int j=0; j<=2; j++){
                for(int k=j; k<fichas.length; k+=3){
                    if(fichas[k].getDrawable()!=null){
                        if(getState(fichas[k]).equals(state_imagen)) columna++;
                        else columna = 0;
                    }
                    if(columna == 3) return true;
                }
                columna = 0;
            }
        }
        //Comprobar diagonales
        try{
            if (getState(fichas[0]).equals(state_imagen) && getState(fichas[4]).equals(state_imagen) && getState(fichas[8]).equals(state_imagen)) return true;
        }catch(NullPointerException e){}
        try{
            if (getState(fichas[2]).equals(state_imagen) && getState(fichas[4]).equals(state_imagen) && getState(fichas[6]).equals(state_imagen)) return true;
        }catch(NullPointerException e){}

        return false;
    }

    public Drawable.ConstantState getState(ImageView img){
        return img.getDrawable().getConstantState();
    }
}