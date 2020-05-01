package com.example.virusio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class VirusIOObject {
    protected int gSize ;
    protected Bitmap virusImage ;
    protected Bitmap medicineImage;
    protected Bitmap playerImage;
    protected float x, y;
    protected float speedX, speedY ;
    protected boolean visible;

    public VirusIOObject(GameView gView, float x, float y, int size){
        //initialise virus image
        this.virusImage =BitmapFactory.decodeResource(gView.getContext().getResources(),R.drawable.virus);

        //initialise medicine image
        this.medicineImage = BitmapFactory.decodeResource(gView.getContext().getResources(),R.drawable.health);

        //initialise player image
        this.playerImage = BitmapFactory.decodeResource(gView.getContext().getResources(),R.drawable.player);

        //initialise x
        this.x = x;

        //initialise y
        this.y = y;

        //initialise x speed
        this.speedX = 0;

        //initialise y speed
        this.speedY = 0 ;

        //initialise size
        this.gSize = size ;

        //initialise visible
        this.visible = true ;

        //scale virus
        scaleVirus(gSize);

        //scale health
        scaleHealth(gSize);

        //scale player
        scalePlayer(gSize);
    }

    //Update game
    public void update(float seconds){
        //Move the player around
        this.x = this.x + seconds * this.speedX;
        this.y = this.y + seconds * this.speedY ;
    }

    /**
     * draw virus
     */
    public void drawVirus(Canvas canvas){
        if(visible){
            canvas.drawBitmap(this.getVirusImage(), this.getX() - this.getVirusImage().getWidth()/2, this.getY() - this.getVirusImage().getHeight()/2, null);

        }
    }

    /**
     * draw health
     */
    public void drawHealth(Canvas canvas){
        if(visible){
            canvas.drawBitmap(this.getHealthImage(),this.getX() - this.getHealthImage().getWidth()/2, this.getY() - this.getHealthImage().getHeight()/2,null);
        }
    }

    /**
     * draw player
     */
    public void drawPlayer(Canvas canvas){
        if(visible){
            canvas.drawBitmap(this.getPlayerImage(),this.getX() - this.getPlayerImage().getWidth()/2, this.getY() - this.getPlayerImage().getHeight(),null);
        }
    }

    /**
     * scale virus image
     */
    public void scaleVirus(int size){
        this.gSize = size ;
        this.virusImage = Bitmap.createScaledBitmap(this.virusImage,this.gSize,this.gSize,false);
    }

    /**
     * scale health image
     */
    public void scaleHealth(int size){
        this.gSize = size;
        this.medicineImage = Bitmap.createScaledBitmap(this.medicineImage,this.gSize,this.gSize,false);
    }

    /**
     * scale player image
     */
    public void scalePlayer(int size){
        this.gSize = size ;
        this.playerImage = Bitmap.createScaledBitmap(this.playerImage,this.gSize,this.gSize,false);
    }

    /**
     * check to see if all the objects are in bounds
     */
    public boolean inBounds(int xMax, int yMax){
        if(this.y >= yMax){
            System.out.println("Object collided with top");
            return false;
        }
        if(this.y<= 0){
            System.out.println("Object collided with bottom bound");
            return false;
        }
        if(this.x >= xMax){
            System.out.println("Object collided with right bound");
            return false ;
        }
        if(this.x<= 0){
            System.out.println("Object collided with left bound");
            return false;
        }
        return true;
    }


    public Rect getBounds(){
        return new Rect(
                (int) this.getX(),
                (int) this.getY(),
                (int)this.getX()+this.gSize/2,
                (int)this.getY()+this.gSize/2);

    }

    public Bitmap getVirusImage(){
        return virusImage;
    }

    /**
     * set virus image
     */
    public void setVirusImage(Bitmap image){
        this.virusImage = image;
    }

    /**
     * get health image
     */
    public Bitmap getHealthImage(){
        return medicineImage;
    }

    /**
     * set health image
     */
    public void setHealthImage(Bitmap image){
        this.medicineImage = image;
    }

    /**
     * get player image
     */
    public Bitmap getPlayerImage(){
        return playerImage;
    }

    /**
     * set player image
     */
    public void setPlayerImage(Bitmap image){
        this.playerImage = image;
    }

    /**
     * get x
     */
    public float getX(){
        return x;
    }

    /**
     * set x
     */
    public void setX(float x){
        this.x = x;
    }

    /**
     * get y
     */
    public float getY(){
        return y;
    }

    /**
     * set y
     */
    public void setY(float y){
        this.y = y;
    }

    /**
     * get x speed
     */
    public double getXSpeed(){
        return speedX;
    }

    /**
     * set x speed
     */
    public void setXSpeed(float xSpeed){
        this.speedX = xSpeed;
    }

    /**
     * get y speed
     */
    public float getYSpeed(){
        return speedY;
    }

    /**
     * set y speed
     */
    public void setYSpeed(float ySpeed){
        this.speedY = ySpeed;
    }

    /**
     * get game size
     */
    public int getSize(){
        return gSize;
    }

    /**
     * set size
     */
    public void setSize(int size){
        this.gSize = size;
    }

    /**
     * get visible
     */
    public boolean getVisible(){
        return visible;
    }

    /**
     * set visible
     */
    public void setVisible(boolean visible){
        this.visible = visible;
    }

}
