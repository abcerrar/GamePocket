package com.example.tfg_nd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>
{

    private List<Card> mdata;
    private LayoutInflater minflater;
    public int pos;

    public Adapter(Context context, List<Card> mdata) {
        this.mdata = mdata;
        this.minflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = minflater.inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String textocarta = mdata.get(position).getCard();
        int imagencarta = mdata.get(position).getImageId();
        holder.texto.setText(textocarta);
        holder.imagen.setImageResource(imagencarta);
        pos= position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView texto;
        ImageView imagen;

        public ViewHolder(View itemView) {
            super(itemView);
            texto = itemView.findViewById(R.id.textoCarta);
            imagen = itemView.findViewById(R.id.imagenCarta);
        }
    }
}
