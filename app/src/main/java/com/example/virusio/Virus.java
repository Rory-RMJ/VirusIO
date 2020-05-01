package com.example.virusio;

import android.content.Context;

public class Virus extends VirusIOObject {
    private int speed ;

    public Virus(GameView gView, float x, float y, int size){
        super(gView, x,y,size);
        setYSpeed(10);
        speed = 100 ;
    }

    public void newDirection(float x, float y){
        float differenceX = this.getX() - x ;
        float differenceY = this.getY() - y ;
        float magnitude = (float)Math.sqrt((differenceX * differenceX + differenceY * differenceY));
        this.setXSpeed(-differenceX/magnitude*speed);
        this.setYSpeed(-differenceY/magnitude*speed);
    }
}
