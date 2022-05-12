package com.example.tfg_nd;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class puzzle_tresraya extends Fragment {

    //Constantes utiles
    private final String TAG = "puzzle_tresraya.java";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private manejadorPreferencias mPref;
    private final String gamemode = "tresraya";
    private String email;
    private User user;
    private AlertDialog dialog;
    private int nivel_actual = 0;

    //Constantes juego
    private ImageView fichas[];

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

    public void gamemode_facil(){}
    public void gamemode_normal(){}
    public void gamemode_dificil(){}
    public void gamemode_jvj(){
        //Asignar los onclick
        for(int i=0; i< fichas.length; i++){
            asignarOnClickJvj(i);
        }
    }

    public void limpiar_tablero(){
        for(int i=0; i< fichas.length; i++){
            fichas[i].setImageDrawable(null);
        }
    }

    public void asignarOnClickJvj(int num_ficha){
        fichas[num_ficha].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fichas[num_ficha].getDrawable() == null){
                    if(player1){
                        fichas[num_ficha].setImageResource(R.drawable.aspa);
                        player1 = false;
                    } else{
                        fichas[num_ficha].setImageResource(R.drawable.circulo);
                        player1 = true;
                    }
                }else{
                    Toast.makeText(getContext(), "No puedes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}