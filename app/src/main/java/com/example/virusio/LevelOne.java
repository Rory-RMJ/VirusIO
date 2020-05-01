package com.example.virusio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

public class LevelOne extends AppCompatActivity {

    private static final int MENU_RESUME = 1;
    private static final int MENU_START = 2;
    private static final int MENU_STOP = 3;
    private static final int MENU_EXIT = 4;

    int currentFrame = 0 ;
    private GameThread gameThread;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_level);

        gameView= (GameView) findViewById(R.id.gamearea);
        gameView.setStatusView((TextView) findViewById(R.id.text));
        gameView.setScoreView((TextView)findViewById(R.id.score));
        this.startGame(gameView, null, savedInstanceState);
    }

    /**
     * start game
     */
    private void startGame(GameView gView, GameThread gThread, Bundle savedInstanceState){
        //set up a new game
        gameThread = new VirusIO(gameView);
        gameView.setThread(gameThread);
        gameThread.setState(GameThread.STATE_READY);
        gameView.startSensor((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        //Virus = new Virus.newDirection();
    }

    /**
     * state functions
     */
    @Override
    protected void onPause(){
        super.onPause();

        if(gameThread.getMode()==GameThread.STATE_RUNNING)
            gameThread.setState(GameThread.STATE_PAUSE);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        gameView.cleanup();
        gameView.removeSensor((SensorManager)getSystemService(Context.SENSOR_SERVICE));
        gameThread = null ;
        gameView = null;
    }

    /**
     * UI Functions
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        menu.add(0,MENU_START,0,R.string.menu_start);
        menu.add(0,MENU_STOP,0,R.string.menu_stop);
        menu.add(0,MENU_RESUME,0,R.string.menu_resume);
        menu.add(0,MENU_EXIT,0,R.string.menu_exit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case MENU_START:
                gameThread.doStart();
                return true;
            case MENU_STOP:
                gameThread.setState(GameThread.STATE_LOSE, getText(R.string.message_stopped));
                return true;
            case MENU_RESUME:
                gameThread.unpause();
                return true;
            case MENU_EXIT:
                gameThread.cleanup();
                back();
                return true;
        }
        return false;
    }

    public void back(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onNothingSelected(AdapterView<?> arg0){

    }
}
