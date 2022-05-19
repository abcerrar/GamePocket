package com.example.tfg_nd;

public class Card {
    private String suit;
    private int num;
    private int imageId = 0;
    private Boolean upwards = false;
    private Byte color;


    public Card(String card, int num, int imageId) {
        this.suit = card;
        this.num = num;
        this.imageId = imageId;
        if(card.contains("Clubs")||card.contains("Spades")){
            color = 0;
        }
        else{
            color = 1;
        }
    }

    public int flip(){
        upwards = true;
        return imageId;
    }

    public String getSuit() {
        return suit;
    }

    public String getCard(){
        return num+" "+suit;
    }

    public void setSuit(String card) {
        this.suit = card;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Boolean getUpwards() {
        return upwards;
    }

    public void setUpwards(Boolean upwards) {
        this.upwards = upwards;
    }

    public Byte getColor() {
        return color;
    }

    public int getNum() {
        return num;
    }
}

