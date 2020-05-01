package com.example.virusio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private Button closeBtn;
    private Button levelBtn;
    private Button levelTwoBtn ;
    private Button levelThreeBtn ;
    private Button leaderboardBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        closeBtn = (Button) findViewById(R.id.closeBtn);

        closeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
             systemClose();
            }
        });

        levelBtn = (Button) findViewById(R.id.levelBtn);
        levelBtn.setOnClickListener(new View.OnClickListener(){
            @Override  public void onClick(View v){
                openLevelOne();
            }
        });



        leaderboardBtn = (Button)findViewById(R.id.leaderboardBtn);
        leaderboardBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("Button Pressed");
                System.out.println("Opening Leaderboard");
                openLeaderboard();
            }
        });
    }

    public void systemClose(){

    }

    public void openLevelOne(){

        Intent levelOne = new Intent(this,LevelOne.class);
        startActivity(levelOne);
    }



    public void openLeaderboard(){
        Intent leaderboard = new Intent(this, Leaderboard.class);
        startActivity(leaderboard);
    }
}
