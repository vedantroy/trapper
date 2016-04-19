package com.vroy.trapper.gameContent;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.vroy.trapper.R;
import com.vroy.trapper.widgets.AutoResizeTextView;

import java.util.Timer;

public class GameActivity extends AppCompatActivity  {

    private static Context mContext;
    private static AutoResizeTextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        mContext = this;
        score =  (AutoResizeTextView) findViewById(R.id.score_text);
        setScore();
    }

    @Override
    protected void onResume() {
        makeFullScreen();
        super.onResume();

        GameView.TIMER.cancel();
        GameView.TIMER.purge();
        GameView.TIMER = new Timer();
        GameView.TIMER.schedule(new GameView.FriendlyOrbTimerTask(), 1000);
        GameView.TIMER.schedule(new GameView.EnemyOrbTimerTask(), 1000);


        for(GameView.Orb o : GameView.friendlyOrbs ) {
            o.startTime = (SystemClock.uptimeMillis() - GameView.pauseStartTime) + o.startTime;
        }
        for(GameView.Orb o : GameView.enemyOrbs) {
            o.startTime = (SystemClock.uptimeMillis() - GameView.pauseStartTime) + o.startTime;
        }

        setMenu();

        SharedPreferences prefs = getSharedPreferences("trapperPrefs",MODE_PRIVATE);
        int difficulty = prefs.getInt("DIF",1);

        switch (difficulty) {
            case 0: GameView.totalOrbs = 5;
                GameView.startOrbs = 2;
                break;
            case 1: GameView.totalOrbs = 7;
                GameView.startOrbs = 3;
                break;
            case 2: GameView.totalOrbs = 10;
                GameView.startOrbs = 4;
                break;
            case 3: GameView.totalOrbs = 12;
                GameView.startOrbs = 5;
                break;
        }


    }

    @Override
    protected  void onPause() {
        super.onPause();
        GameView.pauseStartTime = SystemClock.uptimeMillis();

        GameView.TIMER.cancel();
        GameView.TIMER.purge();
        GameView.TIMER = new Timer();
    }

    private void makeFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    /*
    @Override
    public void onBackPressed() {
        callOnDraw = false;
        Context ctx = getContext();
        Intent intent = new Intent(ctx, DeathScreenActivity.class);
        intent.putExtra("gameScore", forbsKilled); //Might need to update later
        ctx.startActivity(intent);
    }
    */

    private void setMenu() {

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;


        AutoResizeTextView score = (AutoResizeTextView) findViewById(R.id.score_text);
        RelativeLayout.LayoutParams scoreParams = new RelativeLayout.LayoutParams(screenWidth,screenHeight / 40 + screenHeight/240);
        scoreParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        scoreParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        score.setLayoutParams(scoreParams);
        score.setTextColor(Color.WHITE);
        score.setTextSize(500);
        score.setMaxLines(1);
    }

    public  static void setScore() {

        String scoreMsg = mContext.getResources().getString(R.string.score, GameView.forbsKilled);
        score.setText(scoreMsg);

    }



}
