package com.quizlyworks.quizapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ResultPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);
        ((TextView)findViewById(R.id.score_text)).setText(Integer.toString(getIntent().getExtras().getInt("score")));
        String attemptedText = "You attempted " + Integer.toString(getIntent().getExtras().getInt("attempted"));
        ((TextView)findViewById(R.id.attempted_text)).setText(attemptedText);
    }

}
