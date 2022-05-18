package com.example.tfg_nd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class tienda extends Fragment {

    private ImageView[] productos_memory;
    private ImageView[] productos_porcentajes;
    private ImageView[] productos_tresraya;

    public tienda() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tienda, container, false);



        return v;
    }
}