package com.example.tfg_nd;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class puzzle_tresraya extends Fragment {

    //Constantes utiles
    private final String TAG = "puzzle_tresraya.java";
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private manejadorPreferencias mPref;
    private final String gamemode = "tresraya";
    private String email;
    private User user;
    private AlertDialog dialog;
    private int nivel_actual = 0;
    private View.OnClickListener listenerReload, listenerMenu, listenerNext;
    private TextView lineaAspa, lineaCirculo, j1, j2;

    //Constantes juego
    private ImageView fichas[];
    private int turno = 1;
    private ImageView btAspa, btCirculo;

    //Variables pve
    private int imagenj1 = R.drawable.aspa, imagenj2 = R.drawable.circulo;

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
        mPref = new manejadorPreferencias("pref", getActivity());
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            email = currentUser.getEmail();
            user = new User(email);
        }
        nivel_actual = Integer.parseInt(mPref.get("nivel", "0"));

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
        j1 = v.findViewById(R.id.j1);
        j2 = v.findViewById(R.id.j2);

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
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.niveles);
                dialog.dismiss();
            }
        };
        listenerNext = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Aun no", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        };
        btAspa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenj2 = R.drawable.circulo;
                imagenj1 = R.drawable.aspa;
                lineaAspa.setVisibility(View.VISIBLE);
                lineaCirculo.setVisibility(View.INVISIBLE);
            }
        });
        btCirculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenj2 = R.drawable.aspa;
                imagenj1 = R.drawable.circulo;
                lineaAspa.setVisibility(View.INVISIBLE);
                lineaCirculo.setVisibility(View.VISIBLE);
            }
        });

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
            case 9: case 10: case 11: case 12:
                gamemode_dificil();
                break;
        }

        return v;
    }

    public void gamemode_facil(){
        for(int i=0; i< fichas.length; i++){
            asignarOnClick(i, 1);
        }
        turno = 1;
    }
    public void gamemode_normal(){
        for(int i=0; i< fichas.length; i++){
            asignarOnClick(i, 2);
        }
        turno = 1;
    }
    public void gamemode_dificil(){}
    public void gamemode_jvj(){
        j1.setVisibility(View.VISIBLE);
        j2.setVisibility(View.VISIBLE);
        btCirculo.setClickable(false);
        btAspa.setClickable(false);
        lineaAspa.setVisibility(View.INVISIBLE);
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
                        dialog = user.alertFinalPartida("Victoria!!", "Enhorabuena!", 6, 0, 0, getActivity(), listenerReload, listenerNext, listenerNext, listenerMenu, dialog, getContext());
                        dialog.show();
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
                                        respuestaMedia();
                                        break;
                                }
                                habilitarBotones();
                                if(checkVictory(imagenj2)) {
                                    dialog = user.alertFinalPartida("Has perdido...", "Y este era el más fácil", 0, 0, 0, getActivity(), listenerReload, listenerNext, listenerNext, listenerMenu, dialog, getContext());
                                    dialog.show();
                                }
                            }
                        }, 1000);
                    }
                    boolean empate = true;
                    for(int i=0; i<fichas.length; i++) if(fichas[i].getDrawable() == null) empate = false;

                    if(empate){
                        dialog = user.alertFinalPartida("Empate.", "Puedes intentarlo de nuevo", 0, 0, 0, getActivity(), listenerReload, listenerNext, listenerNext, listenerMenu, dialog, getContext());
                        dialog.show();
                    }
                }
            }
        });
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

    public void respuestaMedia(){
        int num = (int)(Math.random()*3), num2;

        if((num2=checkLinea(0, imagenj1, true))!=-1) fichas[num2].setImageResource(imagenj2);
    }

    public int checkLinea(int num_linea, int imagen, boolean fila){
        int cont = 0, incremento;

        if(fila) incremento = 3;
        else incremento = 7;

        if(fila){
            if(num_linea == 1) num_linea = 3;
            else if (num_linea == 2) num_linea = 6;
        }

        for(int i=num_linea; i<num_linea + incremento; i++){
            try{
                if(getState(fichas[i]) == getResources().getDrawable(imagen).getConstantState()) cont++;
            }catch(NullPointerException e){}
        }
        if(cont == 2){
            for(int i=num_linea; i<(num_linea + incremento); i++)
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
            else return false;
        }catch(NullPointerException e){}
        try{
            if (getState(fichas[2]).equals(state_imagen) && getState(fichas[4]).equals(state_imagen) && getState(fichas[6]).equals(state_imagen)) return true;
            else return false;
        }catch(NullPointerException e){}

        return false;
    }
    public Drawable.ConstantState getState(ImageView img){
        return img.getDrawable().getConstantState();
    }
}