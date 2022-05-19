package com.example.tfg_nd;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class klondike<ImageView> extends Fragment {

    ArrayList<Card>[] piles = new ArrayList[12];
    //TextView drawView, wasteView, stack1View, stack2View, stack3View, stack4View, pile1View, pile2View, pile3View, pile4View, pile5View, pile6View, pile7View;
    android.widget.ImageView drawView, wasteView, stack1View, stack2View, stack3View, stack4View, pile1View, pile2View, pile3View, pile4View, pile5View, pile6View, pile7View;
    android.widget.ImageView movingCard = null;
    android.widget.ImageView more, more2, more3, more4, more5, more6, more7;
    public klondike() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static klondike newInstance(String param1, String param2) {
        klondike fragment = new klondike();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_klondike, container, false);

        Baraja baraja = new Baraja();
        piles = baraja.repartoKlondike();

        pile1View =  v.findViewById(R.id.pile1);
        pile2View =  v.findViewById(R.id.pile2);
        pile3View =  v.findViewById(R.id.pile3);
        pile4View =  v.findViewById(R.id.pile4);
        pile5View =  v.findViewById(R.id.pile5);
        pile6View =  v.findViewById(R.id.pile6);
        pile7View =  v.findViewById(R.id.pile7);
        drawView=  v.findViewById(R.id.draw);
        wasteView = v.findViewById(R.id.waste);
        stack1View = v.findViewById(R.id.stack1);
        stack2View = v.findViewById(R.id.stack2);
        stack3View = v.findViewById(R.id.stack3);
        stack4View = v.findViewById(R.id.stack4);
        more = v.findViewById(R.id.more);
        more2 = v.findViewById(R.id.more2);
        more3 = v.findViewById(R.id.more3);
        more4 = v.findViewById(R.id.more4);
        more5 = v.findViewById(R.id.more5);
        more6 = v.findViewById(R.id.more6);
        more7 = v.findViewById(R.id.more7);

        android.widget.ImageView[]moresetter = {more, more2, more3, more4, more5, more6, more7};

        pile2View.setImageResource(piles[1].get(piles[1].size()-1).getImageId());
        pile1View.setImageResource(piles[0].get(piles[0].size()-1).getImageId());
        pile3View.setImageResource(piles[2].get(piles[2].size()-1).getImageId());
        pile4View.setImageResource(piles[3].get(piles[3].size()-1).getImageId());
        pile5View.setImageResource(piles[4].get(piles[4].size()-1).getImageId());
        pile6View.setImageResource(piles[5].get(piles[5].size()-1).getImageId());
        pile7View.setImageResource(piles[6].get(piles[6].size()-1).getImageId());
        stack1View.setImageResource(piles[7].get(0).getImageId());
        stack2View.setImageResource(piles[8].get(0).getImageId());
        stack3View.setImageResource(piles[9].get(0).getImageId());
        stack4View.setImageResource(piles[10].get(0).getImageId());

        android.widget.ImageView [] onClickSetter = {pile1View, pile2View, pile3View, pile4View, pile5View, pile6View, pile7View, stack1View, stack2View, stack3View, stack4View};


        //Click para el mazo
        drawView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card drawn = baraja.draw();
                wasteView.setImageResource(drawn.getImageId());
                if(drawn.getNum()==0){
                    v.setEnabled(false);
                    ((android.widget.ImageView) (v)).setImageResource(R.drawable.empty);
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
                if(movingCard == null&&!((android.widget.ImageView) v).getDrawable().equals(R.drawable.empty)){
                    Toast.makeText(v.getContext(),"Robo", Toast.LENGTH_SHORT).show();
                    movingCard = v.findViewById(v.getId());
                }
                else{
                    Toast.makeText(v.getContext(),"Cancelo Robo", Toast.LENGTH_SHORT).show();
                    movingCard=null;
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
                    if(!((android.widget.ImageView) v).getDrawable().equals(R.drawable.empty)){


                        /* Si no se está moviendo ninguna carta seleccionar la primera de la pila
                         * Si se está moviendo una carta y se clica la misma pila deshabilitar el movimiento
                         * Si se clica otra pila comprobar que la carta sea la carta visible menos uno pero de distinto color
                         */
                        if(movingCard==null){
                            Toast toast = Toast.makeText(v.getContext(),"entro en click set moving card", Toast.LENGTH_SHORT);
                            toast.show();
                            movingCard= v.findViewById(v.getId());
                        }
                        else if(movingCard!=null&&movingCard.getId() == v.getId()){
                            Toast toast = Toast.makeText(v.getContext(),"entro en click deshabilitar", Toast.LENGTH_SHORT);
                            toast.show();

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
                                    ((android.widget.ImageView) v.findViewById(v.getId())).setImageDrawable(movingCard.getDrawable());
                                    try {
                                        movingCard.setImageResource(piles[pila].get(piles[pila].size()-1).getImageId());
                                    }
                                    catch(Exception e){
                                        movingCard.setImageResource(R.drawable.empty);
                                    }

                                    movingCard = null;
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

                                ((android.widget.ImageView) v.findViewById(v.getId())).setImageDrawable(movingCard.getDrawable());

                                movingCard.setImageResource(piles[pila].get(piles[pila].size() - 1).getImageId());

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

                            ((android.widget.ImageView) v.findViewById(v.getId())).setImageDrawable(movingCard.getDrawable());

                            movingCard.setImageResource(piles[pila].get(piles[pila].size()-1).getImageId());
                            movingCard = null;
                        }
                    }


                }
            });
        }


        for (int i = 0; i < moresetter.length; i++) {
            moresetter[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seeMore(v);
                }
            });
        }

        // Inflate the layout for this fragment
        return v;
    }

    public void seeMore(View v){
        Context context = getActivity();
        int pila = Integer.parseInt(v.getTag().toString());
        ArrayList<Card> pile = piles[pila];

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.activity_card_view, null);
        alertDialog.setView(convertView);


        RecyclerView list = convertView.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(context));
        list.setHasFixedSize(true);

        Adapter adapter = new Adapter(context, pile);
        list.setAdapter(adapter);
        alertDialog.setView(convertView);

        AlertDialog dialog = alertDialog.create();




        dialog.show();


        int width = HomeMenuActivity.width;
        int height = HomeMenuActivity.height;
        dialog.getWindow().setLayout(width , height);

        adapter.notifyDataSetChanged();
    }


}