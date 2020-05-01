package com.example.virusio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {

    private Button backBtn ;
    private Button readBtn ;
    private Button writeBtn;
    private TextView highScoresTxt  ;
    ListView readList;
    FirebaseDatabase firebaseDatabase ;
    DatabaseReference databaseReference;
    ArrayList<String> dataList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("VirusIO");

        backBtn = (Button) findViewById(R.id.exit);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("Button pressed");
                System.out.println("Returning to Menu");
                back();
            }
        });

        readBtn = (Button) findViewById(R.id.read);
        readBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("Button Pressed");
                System.out.println("Retrieving data from Firebase");
                read();
            }
        });

        writeBtn = (Button)findViewById(R.id.writeBtn);
        writeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("Button Pressed");
                System.out.println("Saving data");
                write();
            }
        });

    }



    public void back(){
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);
    }

    public void read(){


        readList = (ListView) findViewById(R.id.readList);
        Toast.makeText(this,"reading...", Toast.LENGTH_SHORT).show();
        databaseReference.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnap, String s){
                String value = dataSnap.getValue(String.class);
                dataList.add(value);
                arrayAdapter = new ArrayAdapter<String>(Leaderboard.this,android.R.layout.simple_list_item_1,dataList);
                readList.setAdapter(arrayAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot,String s){

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot){

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s){

            }

            @Override
            public void onCancelled(DatabaseError error){

            }
        });
    }

    public void write(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Leaderboard");
        highScoresTxt = (TextView)findViewById(R.id.writeTxt);
        String highScores = highScoresTxt.getText().toString();
        databaseReference.child("Leaderboard").setValue(highScores);
        Toast.makeText(this,"Saving...",Toast.LENGTH_SHORT).show();
    }
}
