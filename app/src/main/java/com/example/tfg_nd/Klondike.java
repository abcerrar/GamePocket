package com.example.tfg_nd;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Klondike extends AppCompatActivity {
    ArrayList<Card>[] piles = new ArrayList[12];
    //TextView drawView, wasteView, stack1View, stack2View, stack3View, stack4View, pile1View, pile2View, pile3View, pile4View, pile5View, pile6View, pile7View;
    ImageView drawView, wasteView, stack1View, stack2View, stack3View, stack4View, pile1View, pile2View, pile3View, pile4View, pile5View, pile6View, pile7View;
    ImageView movingCard = null;
    public Context context = this;
    TextView tvCarta;
    Chronometer cronometro;
    AlertDialog dialog;
    private View.OnClickListener listenerReload, listenerMenu, listenerNext;
    //  TextView [] onClickSetter = {waste, stack1, stack2, stack3, stack4, pile1, pile2, pile3, pile4, pile5, pile6, pile7};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klondike);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        cronometro = (Chronometer) findViewById(R.id.chronometerExample);
        cronometro.start();

        Baraja baraja = new Baraja();
        piles = baraja.repartoKlondike();
        listenerMenu = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Klondike.this.startActivity(new Intent(getApplicationContext(), HomeMenuActivity.class));
            }
        };
        listenerReload = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Klondike.this.startActivity(new Intent(getApplicationContext(), Klondike.class));
            }
        };
        listenerNext = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "En este modo de juego no hay más niveles", Toast.LENGTH_SHORT).show();
            }
        };




        pile1View = findViewById(R.id.pile1);
        pile2View = findViewById(R.id.pile2);
        pile3View = findViewById(R.id.pile3);
        pile4View = findViewById(R.id.pile4);
        pile5View = findViewById(R.id.pile5);
        pile6View = findViewById(R.id.pile6);
        pile7View = findViewById(R.id.pile7);
        drawView= findViewById(R.id.draw);
        wasteView = findViewById(R.id.waste);
        stack1View = findViewById(R.id.stack1);
        stack2View = findViewById(R.id.stack2);
        stack3View = findViewById(R.id.stack3);
        stack4View = findViewById(R.id.stack4);
        tvCarta = findViewById(R.id.tvCarta);

        pile1View.setImageResource(piles[0].get(piles[0].size()-1).getImageId());
        pile2View.setImageResource(piles[1].get(piles[1].size()-1).getImageId());
        pile3View.setImageResource(piles[2].get(piles[2].size()-1).getImageId());
        pile4View.setImageResource(piles[3].get(piles[3].size()-1).getImageId());
        pile5View.setImageResource(piles[4].get(piles[4].size()-1).getImageId());
        pile6View.setImageResource(piles[5].get(piles[5].size()-1).getImageId());
        pile7View.setImageResource(piles[6].get(piles[6].size()-1).getImageId());
        stack1View.setImageResource(piles[7].get(0).getImageId());
        stack2View.setImageResource(piles[8].get(0).getImageId());
        stack3View.setImageResource(piles[9].get(0).getImageId());
        stack4View.setImageResource(piles[10].get(0).getImageId());

        tvCarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                victoria();
            }
        });

        ImageView [] onClickSetter = {pile1View, pile2View, pile3View, pile4View, pile5View, pile6View, pile7View, stack1View, stack2View, stack3View, stack4View};


        //Click para el mazo
        drawView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card drawn = baraja.draw();
                wasteView.setImageResource(drawn.getImageId());
                if(drawn.getNum()==0){
                    v.setEnabled(false);
                    ((ImageView) (v)).setImageResource(R.drawable.empty);
                }
            }
        });


        //Click para el robo
        wasteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Comprobar so hay carta para robar y cogerla en caso afirmativo
                 *Cancelar el movimiento al volver a clicar
                 */
                if(movingCard == null&&!((ImageView) v).getDrawable().equals(R.drawable.empty)){
                    //Toast.makeText(v.getContext(),"Robo", Toast.LENGTH_SHORT).show();

                    movingCard = findViewById(v.getId());
                    tvCarta.setText(piles[(Integer.parseInt(movingCard.getTag().toString()))].get(piles[(Integer.parseInt(movingCard.getTag().toString()))].size()-1).getCard());
                }
                else{
                    Toast.makeText(v.getContext(),"Cancelo Robo", Toast.LENGTH_SHORT).show();
                    movingCard=null;
                    tvCarta.setText("Ninguna");
                }
            }
        });



        //Clicks para las pilas
        for (int i = 0; i < onClickSetter.length-4; i++) {

            onClickSetter[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /* Comprobar si hay carta para robar
                     * o si se está moviendo un rey a la posición vacía
                     */
                    if(piles[Integer.parseInt(v.getTag().toString())].get(piles[Integer.parseInt(v.getTag().toString())].size()-1).getNum()!=-1){


                        /* Si no se está moviendo ninguna carta seleccionar la primera de la pila
                         * Si se está moviendo una carta y se clica la misma pila deshabilitar el movimiento
                         * Si se clica otra pila comprobar que la carta sea la carta visible menos uno pero de distinto color
                         */
                        if(movingCard==null){
                            Toast toast = Toast.makeText(v.getContext(),"entro en click set moving card", Toast.LENGTH_SHORT);
                            toast.show();
                            movingCard= findViewById(v.getId());
                            tvCarta.setText(piles[(Integer.parseInt(movingCard.getTag().toString()))].get(piles[(Integer.parseInt(movingCard.getTag().toString()))].size()-1).getCard());

                        }
                        else if(movingCard!=null&&movingCard.getId() == v.getId()){
                            Toast toast = Toast.makeText(v.getContext(),"entro en click deshabilitar", Toast.LENGTH_SHORT);
                            toast.show();
                            tvCarta.setText("Ninguna");
                            movingCard=null;
                        }
                        else if(movingCard!=null){

                            int pila = Integer.parseInt(movingCard.getTag().toString());
                            int otrapila = Integer.parseInt(v.getTag().toString());

                            if(piles[pila].size()-1>=0){

                                Card carta1 = piles[pila].get(piles[pila].size()-1);
                                Card carta2 = piles[otrapila].get(piles[otrapila].size()-1);

                                int valorClickado = carta1.getNum();
                                int valorComparado = carta2.getNum();


                                Toast.makeText(v.getContext(),carta1.getColor() +" "+valorClickado, Toast.LENGTH_SHORT).show();

                                Toast.makeText(v.getContext(),carta2.getColor() +" "+valorComparado, Toast.LENGTH_SHORT).show();



                                if (valorClickado==valorComparado-1 && carta1.getColor()!=carta2.getColor()){
                                    piles[otrapila].add(carta1);
                                    piles[pila].remove(carta1);
                                    ((ImageView) findViewById(v.getId())).setImageDrawable(movingCard.getDrawable());
                                    try {
                                        movingCard.setImageResource(piles[pila].get(piles[pila].size()-1).getImageId());
                                    }
                                    catch(Exception e){
                                        movingCard.setImageResource(R.drawable.empty);
                                    }

                                    movingCard = null;
                                    tvCarta.setText("Ninguna");
                                }
                            }
                        }
                    }
                    else {
                        if (movingCard != null) {

                            int pila = Integer.parseInt(movingCard.getTag().toString());
                            int otrapila = Integer.parseInt(v.getTag().toString());

                            Card carta1 = piles[pila].get(piles[pila].size() - 1);
                            Card carta2 = piles[otrapila].get(piles[otrapila].size() - 1);

                            int valorClickado = carta1.getNum();
                            int valorComparado = carta2.getNum();

                            if (valorComparado == -1 && valorClickado == 13) {
                                piles[otrapila].add(carta1);
                                piles[pila].remove(carta1);

                                ((ImageView) findViewById(v.getId())).setImageDrawable(movingCard.getDrawable());

                                movingCard.setImageResource(piles[pila].get(piles[pila].size() - 1).getImageId());
                                movingCard = null;
                                tvCarta.setText("Ninguna");
                            }
                        }
                    }
                }
            });
        }

        //Clicks para los palos
        for (int i = onClickSetter.length-4; i < onClickSetter.length; i++) {

            onClickSetter[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(movingCard!=null){

                        int pila = Integer.parseInt(movingCard.getTag().toString());
                        int otrapila = Integer.parseInt(v.getTag().toString());

                        Card carta1 = piles[pila].get(piles[pila].size()-1);
                        Card carta2 = piles[otrapila].get(piles[otrapila].size()-1);

                        int valorClickado = carta1.getNum();
                        int valorComparado = carta2.getNum();
                        String paloClickado = carta1.getSuit();
                        String paloComparado = carta2.getSuit();

                        Toast.makeText(v.getContext(),carta1.getColor() +" "+valorClickado, Toast.LENGTH_SHORT).show();

                        Toast.makeText(v.getContext(),carta2.getColor() +" "+valorComparado, Toast.LENGTH_SHORT).show();


                        if (valorClickado==valorComparado+1&&paloClickado==paloComparado){
                            piles[otrapila].add(piles[pila].get(piles[pila].size()-1));
                            piles[pila].remove(piles[pila].get(piles[pila].size()-1));

                            ((ImageView) findViewById(v.getId())).setImageDrawable(movingCard.getDrawable());

                            movingCard.setImageResource(piles[pila].get(piles[pila].size()-1).getImageId());
                            movingCard = null;
                            tvCarta.setText("Ninguna");
                            winCondition(piles[7],piles[8],piles[9],piles[10]);
                        }
                    }


                }
            });
        }

    }

    public void winCondition(ArrayList stack1,ArrayList stack2,ArrayList stack3,ArrayList stack4){
        if (stack1.size()==12&&stack2.size()==12&&stack3.size()==12&&stack4.size()==12){
            Toast.makeText(getApplicationContext(),"Yay you win", Toast.LENGTH_SHORT).show();
        }
        else{

        }
    }

    public void victoria(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        try{
            String email = mAuth.getCurrentUser().getEmail();
            User user = new User(email);

            String tiempo = cronometro.getText()+"";
            Toast.makeText(Klondike.this, "Tiempo tardado: " + tiempo, Toast.LENGTH_SHORT).show();
            cronometro.stop();
            int tiempo_tardado = 0;
            try{
                tiempo_tardado = Integer.parseInt(tiempo.substring(0,2));
            }catch (NumberFormatException ex){
                Toast.makeText(Klondike.this, "Error al pasar el timempo tardado a int", Toast.LENGTH_SHORT).show();
            }

            int estrellas, dinero, experiencia;
            String titulo, titutlo2;

            if(tiempo_tardado < 2){
                estrellas = 3;
                dinero = 30;
                experiencia = 60;
                titulo = "Enhorabuena!!";
                titutlo2 = "Te lo has pasado en menos\n de 2 minútos";
            }else if(tiempo_tardado < 5){
                estrellas = 2;
                dinero = 15;
                experiencia = 30;
                titulo = "No está mal!!";
                titutlo2 = "Te lo has pasado en menos\n de 5 minútos";
            }else if(tiempo_tardado < 8){
                estrellas = 1;
                dinero = 5;
                experiencia = 10;
                titulo = "Por poco";
                titutlo2 = "Te lo has pasado en menos\n de 8 minútos";
            }else{
                estrellas = 0;
                dinero = 0;
                experiencia = 0;
                titulo = "No lo has superado";
                titutlo2 = tiempo_tardado + "minutos es demasiado para recibir recompensas";
            }

            user.incrementarExperiencia(experiencia);
            user.incrementarDinero(dinero);
            user.incrementarTiempoRecord(tiempo_tardado);
            dialog = user.alertFinalPartida(titulo, titutlo2, estrellas*2, dinero, experiencia, this, listenerReload, listenerNext, listenerNext, listenerMenu, dialog, this);
            dialog.show();

        }catch(NullPointerException ex){
            Toast.makeText(Klondike.this, "No recibes recompensas porque no estás logeado", Toast.LENGTH_SHORT).show();
        }
    }

    public void seeMore(View v){
        int pila = Integer.parseInt(v.getTag().toString());
        ArrayList<Card> pile = new ArrayList<Card>(piles[pila]);
        pile.remove(0);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Klondike.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.activity_card_view, null);
        alertDialog.setView(convertView);


        RecyclerView list = convertView.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);

        Adapter adapter = new Adapter(context, pile);
        list.setAdapter(adapter);
        alertDialog.setView(convertView);

        AlertDialog dialog = alertDialog.create();


        Toast.makeText(Klondike.this, "Tiempo: " + cronometro.getText(), Toast.LENGTH_SHORT).show();

        dialog.show();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels/4;
        int width = (int) (displayMetrics.widthPixels);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        dialog.getWindow().setLayout(width , height);

        adapter.notifyDataSetChanged();
    }

    public void exit(View v){
        finish();
    }
}