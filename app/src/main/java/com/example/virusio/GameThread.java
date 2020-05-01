package com.example.virusio;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

public abstract class GameThread extends Thread {
    public static final int STATE_LOSE = 1;
    public static final int STATE_PAUSE = 2 ;
    public static final int STATE_READY = 3;
    public static final int STATE_RUNNING = 4;
    public static final int STATE_WIN = 5;

    //control variable for the mode of the game
    protected int gMode = 1 ;

    //Control variable of the actual running inside run()
    private boolean gRun = false ;

    //the surface this thread writes on
    private SurfaceHolder gSurfaceHolder ;

    //message handler to the game
    private Handler gHandler;

    //android context - stores all information we need
    private Context gContext;

    //the view
    public GameView gView ;

    //canvas height and width
    protected int gCanvasHeight = 1 ;
    protected int gCanvasWidth = 1 ;

    //previous time we updated game physics
    protected long gLastTime = 0 ;

    //background image
    protected Bitmap gBackgroundImage ;

    //stores the score
    protected long score = 0 ;

    //time keeping
    private long now;
    private float  elapsed ;

    //Rotation vectors when change in orientation
    float[] gGravity;
    float[] gGeomagnetic;

    //ensures appropriate threading
    static final Integer monitor = 1 ;

    public GameThread(GameView gameView) {
        gView = gameView;

        gSurfaceHolder = gameView.getHolder();
        gHandler = gameView.getgHandler();
        gContext = gameView.getContext();

        gBackgroundImage = BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.background);
    }

    /*
     * Called when app is destroyed, so not really that important here
     * But if (later) the game involves more thread, we might need to stop a thread, and then we would need this
     * Dare I say memory leak...
     */
    public void cleanup() {
        this.gContext = null;
        this.gView = null;
        this.gHandler = null;
        this.gSurfaceHolder = null;
    }

    //Pre-begin a game
    abstract public void setupBeginning();

    //Starting up the game
    public void doStart() {
        synchronized(monitor) {

            setupBeginning();

            gLastTime = System.currentTimeMillis() + 100;

            setState(STATE_RUNNING);

            setScore(0);
        }
    }

    //The thread start
    @Override
    public void run() {
        Canvas canvasRun;
        while (gRun) {
            canvasRun = null;
            try {
                canvasRun = gSurfaceHolder.lockCanvas(null);
                synchronized (monitor) {
                    if (gMode == STATE_RUNNING) {
                        updatePhysics();
                    }
                    doDraw(canvasRun);
                }
            }
            finally {
                if (canvasRun != null) {
                    if(gSurfaceHolder != null)
                        gSurfaceHolder.unlockCanvasAndPost(canvasRun);
                }
            }
        }
    }

    /*
     * Surfaces and drawing
     */
    public void setSurfaceSize(int width, int height) {
        synchronized (monitor) {
            gCanvasWidth = width;
            gCanvasHeight = height;

            // don't forget to resize the background image
            gBackgroundImage = Bitmap.createScaledBitmap(gBackgroundImage, width, height, true);
        }
    }


    protected void doDraw(Canvas canvas) {

        if(canvas == null) return;

        if(gBackgroundImage != null) canvas.drawBitmap(gBackgroundImage, 0, 0, null);
    }

    private void updatePhysics() {
        now = System.currentTimeMillis();
        elapsed = (now - gLastTime) / 1000.0f;

        updateGame(elapsed);

        gLastTime = now;
    }

    abstract protected void updateGame(float secondsElapsed);

    /*
     * Control functions
     */

    //Finger touches the screen
    public boolean onTouch(MotionEvent e) {
        if(e.getAction() != MotionEvent.ACTION_DOWN) return false;

        if(gMode == STATE_READY || gMode == STATE_LOSE || gMode == STATE_WIN) {
            doStart();
            return true;
        }

        if(gMode == STATE_PAUSE) {
            unpause();
            return true;
        }

        synchronized (monitor) {
            this.actionOnTouch(e.getRawX(), e.getRawY());
        }

        return false;
    }

    protected void actionOnTouch(float x, float y) {
        //Override to do something
    }

    //The Orientation has changed
    @SuppressWarnings("deprecation")
    public void onSensorChanged(SensorEvent event) {
        synchronized (monitor) {

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                gGravity = event.values;
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                gGeomagnetic = event.values;
            if (gGravity != null && gGeomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, gGravity, gGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    actionWhenPhoneMoved(orientation[2],orientation[1],orientation[0]);
                }
            }
        }
    }

    protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {
        //Override to do something
    }

    /*
     * Game states
     */
    public void pause() {
        synchronized (monitor) {
            if (gMode == STATE_RUNNING) setState(STATE_PAUSE);
        }
    }

    public void unpause() {
        // Move the real time clock up to now
        synchronized (monitor) {
            gLastTime = System.currentTimeMillis();
        }
        setState(STATE_RUNNING);
    }

    //Send messages to View/Activity thread
    public void setState(int mode) {
        synchronized (monitor) {
            setState(mode, null);
        }
    }

    public void setState(int mode, CharSequence message) {
        synchronized (monitor) {
            gMode = mode;

            if (gMode == STATE_RUNNING) {
                Message msg = gHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("text", "");
                b.putInt("viz", View.INVISIBLE);
                b.putBoolean("showAd", false);
                msg.setData(b);
                gHandler.sendMessage(msg);
            }
            else {
                Message msg = gHandler.obtainMessage();
                Bundle b = new Bundle();

                Resources res = gContext.getResources();
                CharSequence str = "";
                if (gMode == STATE_READY)
                    str = res.getText(R.string.mode_ready);
                else
                if (gMode == STATE_PAUSE)
                    str = res.getText(R.string.mode_pause);
                else
                if (gMode == STATE_LOSE)
                    str = res.getText(R.string.mode_lose);
                else
                if (gMode == STATE_WIN) {
                    str = res.getText(R.string.mode_win);
                }

                if (message != null) {
                    str = message + "\n" + str;
                }

                b.putString("text", str.toString());
                b.putInt("viz", View.VISIBLE);

                msg.setData(b);
                gHandler.sendMessage(msg);
            }
        }
    }

    /*
     * Getter and setter
     */
    public void setSurfaceHolder(SurfaceHolder h) {
        gSurfaceHolder = h;
    }

    public boolean isRunning() {
        return gRun;
    }

    public void setRunning(boolean running) {
        gRun = running;
    }

    public int getMode() {
        return gMode;
    }

    public void setMode(int mMode) {
        this.gMode = mMode;
    }


    /* ALL ABOUT SCORES */

    //Send a score to the View to view

    public void setScore(long score) {
        this.score = score;

        synchronized (monitor) {
            Message msg = gHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putBoolean("score", true);
            b.putString("text", getScoreString().toString());
            msg.setData(b);
            gHandler.sendMessage(msg);
        }
    }

    public float getScore() {
        return score;
    }

    public void increaseScore(long score) {
        this.setScore(this.score + score);
    }

    public void decreaseScore(long score){
        this.setScore(this.score - score);
    }


    protected CharSequence getScoreString() {
        return Long.toString(Math.round(this.score));
    }
}
