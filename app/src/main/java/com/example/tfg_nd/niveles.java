package com.example.tfg_nd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class niveles extends Fragment {

    private final int MAX_NIVELES = 12;
    private TextView[] niveles;
    private manejadorPreferencias mPref;

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
        mPref = new manejadorPreferencias("prefs", getActivity());

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



        return v;
    }
}