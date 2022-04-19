package com.example.tfg_nd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class test extends Fragment {

    User uc = new User();
    EditText username;
    EditText edDinero;
    Button agregar;
    Button verDinero;
    TextView tvDinero;

    User user1;

    public test() {

    }

    // TODO: Rename and change types and number of parameters
    public static test newInstance(String param1, String param2) {
        test fragment = new test();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test, container, false);

        username = v.findViewById(R.id.etUsername);
        edDinero = v.findViewById(R.id.etDinero);
        agregar = v.findViewById(R.id.btAgregar);
        tvDinero = v.findViewById(R.id.tvDinero);
        verDinero = v.findViewById(R.id.btVerDinero);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = username.getText().toString();
                user1 = new User(nombre);
                user1.setDinero(10);
            }
        });


        verDinero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDinero.setText(user1.getDinero()+"");
            }
        });

        return v;
    }
}