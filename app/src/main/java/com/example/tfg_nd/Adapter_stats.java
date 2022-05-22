package com.example.tfg_nd;



import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Adapter_stats extends RecyclerView.Adapter<Adapter_stats.ViewHolder> {

    private List<String> nombre;
    private List<String> numero;
    private List<String> imagen;
    private LayoutInflater mInflater;

    //Constructor
    public Adapter_stats(Context context, List<String> nombre, List<String> numero, List<String> imagen){
        this.mInflater= LayoutInflater.from(context);
        this.nombre = nombre;
        this.numero = numero;
        this.imagen = imagen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.row_stats, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder (ViewHolder holder, int position){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = "";
        if(user != null) email = user.getEmail();

        String nom = nombre.get(position);
        holder.tvNombre.setText(nom);

        String num = numero.get(position);
        holder.tvNumero.setText(num);

        if(email.equals(nom)) {
            holder.tvNombre.setTextColor(-16776961);
            holder.tvNumero.setTextColor(-16776961);
        }

        int img = Integer.parseInt(imagen.get(position));
        holder.img.setImageResource(img);

        holder.tvNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = nombre.get(position);
                Toast.makeText(mInflater.getContext(), "Esto te llevará al perfil del jugador: " + nom, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount(){
        return nombre.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombre;
        TextView tvNumero;
        ImageView img;
        LinearLayout plantilla;
        ViewHolder (View itenView){
            super(itenView);
            tvNombre = itenView.findViewById(R.id.row_name);
            tvNumero = itenView.findViewById(R.id.row_num);
            img = itenView.findViewById(R.id.row_img);
            plantilla = itenView.findViewById(R.id.plantilla);
        }
    }

}

