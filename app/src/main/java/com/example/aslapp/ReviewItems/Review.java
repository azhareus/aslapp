package com.example.aslapp.ReviewItems;

public class Review {

    private int sign;
    private String translation;

    public Review(int sign, String translation){
        this.sign = sign;
        this.translation = translation;
    }

    public int getSign() {
        return sign;
    }
    public String getTranslation() {
        return translation;
    }



}

