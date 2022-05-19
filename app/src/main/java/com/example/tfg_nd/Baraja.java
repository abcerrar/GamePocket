package com.example.tfg_nd;


import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Baraja {


    private int [] number = {1,2,3,4,5,6,7,8,9,10,11,12,13};
    //private String [] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
    private String [] suits = {"Clubs", "Clubs", "Diamonds", "Diamonds"};
    private int [] images = {R.drawable.iceace, R.drawable.ice2,R.drawable.ice3,R.drawable.ice4,R.drawable.ice5,
            R.drawable.ice6,R.drawable.ice7,R.drawable.ice8,R.drawable.ice9,R.drawable.ice10,
            R.drawable.icejack,R.drawable.icequeen,R.drawable.iceking,
            R.drawable.mothace,R.drawable.moth2,R.drawable.moth3,R.drawable.moth4,R.drawable.moth5,
            R.drawable.moth6,R.drawable.moth7,R.drawable.moth8,R.drawable.moth9,R.drawable.moth10,
            R.drawable.mothjack,R.drawable.mothqueen,R.drawable.mothking};

    private ArrayList<Card> mazo = new ArrayList<Card>(52);
    private ArrayList<Card> waste = new ArrayList<Card>();
    private String gameMode = "";
    private Card currentCard;

    public Baraja() {
        for (int i = 0; i < suits.length; i++) {
            for (int j = 0; j < number.length; j++) {
                if(i>1){
                    mazo.add(new Card(suits[i], number[j],images[j+13]));
                }
                else{
                    mazo.add(new Card(suits[i], number[j],images[j]));
                }
            }
        }
        Collections.shuffle(mazo);
    }

    public ArrayList[] repartoKlondike(){

        gameMode= "klondike";

        ArrayList<Card> pile1 = new ArrayList<Card>();
        ArrayList<Card> pile2 = new ArrayList<Card>();
        ArrayList<Card> pile3 = new ArrayList<Card>();
        ArrayList<Card> pile4 = new ArrayList<Card>();
        ArrayList<Card> pile5 = new ArrayList<Card>();
        ArrayList<Card> pile6 = new ArrayList<Card>();
        ArrayList<Card> pile7 = new ArrayList<Card>();
        ArrayList<Card> stack1 = new ArrayList<Card>();
        ArrayList<Card> stack2 = new ArrayList<Card>();
        ArrayList<Card> stack3 = new ArrayList<Card>();
        ArrayList<Card> stack4 = new ArrayList<Card>();


        //stack1.add(new Card("Hearts", 0,R.drawable.emptypileblue));
        //stack2.add(new Card("Spades", 0,R.drawable.yellowemptypile));
        //stack3.add(new Card("Diamonds", 0,R.drawable.emptypileblue));
        //stack4.add(new Card("Clubs", 0,R.drawable.yellowemptypile));
        stack1.add(new Card("Clubs", 0,R.drawable.emptypileblue));
        stack2.add(new Card("Diamonds", 0,R.drawable.yellowemptypile));
        stack3.add(new Card("Clubs", 0,R.drawable.emptypileblue));
        stack4.add(new Card("Diamonds", 0,R.drawable.yellowemptypile));

        ArrayList[] piles = {pile1, pile2, pile3, pile4, pile5, pile6, pile7, stack1, stack2, stack3, stack4, waste};

        for (int i = 0; i < 7; i++) {

            piles[i].add(new Card("Empty", -1, R.drawable.empty));

            for (int j = 0; j <= i; j++) {

                piles[i].add(mazo.get(0));
                mazo.remove(0);

            }
        }

        return piles;
    }

    public Card draw(){
        if(!mazo.isEmpty()){
            waste.add(mazo.get(0));
            mazo.remove(0);
            return waste.get(waste.size()-1);
        }
        else{
            if (!waste.isEmpty()){
                mazo.addAll(waste);
                waste.clear();
                waste.add(mazo.get(0));
                mazo.remove(0);
                currentCard = waste.get(waste.size()-1);
                return currentCard;
            }
            else{
                return new Card("Empty", -2, R.drawable.empty);
            }
        }
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public ArrayList<Card> getWaste() {
        return waste;
    }
}
