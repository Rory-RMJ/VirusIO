package com.example.virusio;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.SensorEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.security.AccessController.getContext;

public class VirusIO  extends GameThread{
    private Player player;

 //   private Virus virusClass;
    private List<Virus> virus = new ArrayList<>() ;

    private List<Medicine> health ;

    public VirusIO (GameView gView){
        super(gView);

        player = new Player(gView, gCanvasWidth/2, gCanvasHeight/2, 150);

        //virus = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Random r = new Random();
           // float randX = r.nextFloat();
           // float randY = r.nextFloat();
            System.out.println("virus at" + gCanvasHeight);
            System.out.println("virus at "+ gCanvasWidth);
            virus.add(new Virus(gView, r.nextInt(1000),r.nextInt(1000), 100));
           // virusClass.newDirection(randX, randY);
        }

        health = new ArrayList<>();
        for(int i = 0 ; i < 5; i++){
            Random r = new Random();
            System.out.println("health at " + gCanvasHeight);
            System.out.println("health at " + gCanvasWidth);
            health.add(new Medicine(gView,r.nextInt(1000),r.nextInt(1000),100));

        }

    }

    @Override
    public void setupBeginning(){
        //do nothing
    }

    @Override
    protected void updateGame(float seconds){
        player.update(seconds);

        if(!player.inBounds(gCanvasWidth, gCanvasHeight)){

        }


        for(Virus v : virus){
            if(isColliding(v) && v.getVisible()){
                v.setVisible(false);
                increaseScore(10);

            }
        }

        if (getScore() > 40){
            for(Medicine m: health){
                if(isColliding(m) && m.getVisible()){
                    m.setVisible(false);
                    decreaseScore(10);
                }
            }
        }
    }

    /**
     * checks for collisions with the player
     */
    private boolean isColliding(VirusIOObject obj){
        if(Rect.intersects(player.getBounds(), obj.getBounds()))
            return true;
        return false;
    }

    @Override
    public void setSurfaceSize(int x, int y){
        super.setSurfaceSize(x,y);
    }

    /**
     * draw the images to screen
     */
    public void doDraw(Canvas canvas){
        super.doDraw(canvas);

        player.drawPlayer(canvas);


        for(Virus v : virus){
            v.drawVirus(canvas);
        }

        if(getScore() > 40){
            for(Medicine m : health)
                m.drawHealth(canvas);
        }
    }

    @Override
    public boolean onTouch(MotionEvent e){
        return super.onTouch(e);
    }

    @Override
    public void actionOnTouch(float x, float y){
        player.newDirection(x,y);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        super.onSensorChanged(event);
    }

    @Override
    protected void actionWhenPhoneMoved(float x, float y, float z){
        super.actionWhenPhoneMoved(x,y,z);
    }

    @Override
    public void pause(){
        super.pause();
    }

    @Override
    public void unpause(){
        super.unpause();
    }

    @Override
    public void setState(int mode){
        super.setState(mode);
    }

    @Override
    public void setState(int mode, CharSequence msg){
        super.setState(mode, msg);
    }

    @Override
    public void setSurfaceHolder(SurfaceHolder holder){
        super.setSurfaceHolder(holder);
    }

    @Override
    public boolean isRunning(){
        return super.isRunning();
    }

    @Override
    public void setRunning(boolean running){
        super.setRunning(running);
    }

    @Override
    public int getMode(){
        return super.getMode();
    }

    @Override
    public void setMode(int mode){
        super.setMode(mode);
    }

    @Override
    public float getScore(){
        return super.getScore();
    }

    @Override
    public void setScore(long score){
        super.setScore(score);
    }

    @Override
    public void increaseScore(long score){
        super.increaseScore(score);
    }

    @Override
    public void decreaseScore(long score){
        super.decreaseScore(score);
    }

    @Override
    protected CharSequence getScoreString(){
        return super.getScoreString();
    }


}


