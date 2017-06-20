package com.example.administrator.game2048;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static MainActivity mainActivity = null;

    private TextView textView;

    private int score = 0;

    public MainActivity() {
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public void clearScore() {
        score=0;
        showScore();
    }

    public void showScore(){
        textView.setText(score+"");
    }

    public void addScore(int score) {
        this.score += score;
        showScore();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.game_score);

    }
}
