package com.example.tommo.guess_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button begin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        begin = (Button)findViewById(R.id.Begin_button);
        try {
            if (begin != null) {
                begin.setOnClickListener(this);
            }
        }
        catch(NullPointerException e)
        {
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        if(v == begin)
            startActivity(new Intent(getApplicationContext(), DataListActivity.class));
    }
}
