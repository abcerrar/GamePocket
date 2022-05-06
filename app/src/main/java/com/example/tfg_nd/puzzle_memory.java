package com.example.tfg_nd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class puzzle_memory extends Fragment {

    private int nivel_actual = 0;

    private final String TAG = "puzzle_memory.java";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private manejadorPreferencias mPref;
    private final int total_cartas = 20;

    private ImageView[] cartas;
    private TextView tvMovimientos;
    private final String gamemode = "memory";
    private String email;
    private User user;

    private int movimientos = 0, max_parejas, parejas_completadas = 0;

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
        tvMovimientos = v.findViewById(R.id.tvMovimientos);

        tvMovimientos.setText("Movimientos: 0");

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
        cartas[0].setVisibility(View.INVISIBLE);
        cartas[1].setVisibility(View.INVISIBLE);
        cartas[2].setVisibility(View.INVISIBLE);
        cartas[3].setVisibility(View.INVISIBLE);
        cartas[16].setVisibility(View.INVISIBLE);
        cartas[17].setVisibility(View.INVISIBLE);
        cartas[18].setVisibility(View.INVISIBLE);
        cartas[19].setVisibility(View.INVISIBLE);

        max_parejas = 6;
    }
    public void gamemode2(){
        cartas[0].setVisibility(View.INVISIBLE);
        cartas[1].setVisibility(View.INVISIBLE);
        cartas[2].setVisibility(View.INVISIBLE);
        cartas[3].setVisibility(View.INVISIBLE);

        max_parejas = 8;
    }
    public void gamemode3(){
        max_parejas = 10;

    }



}