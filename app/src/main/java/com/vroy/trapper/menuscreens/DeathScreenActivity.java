package com.vroy.trapper.menuscreens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.vroy.trapper.R;
import com.vroy.trapper.gameContent.GameActivity;
import com.vroy.trapper.widgets.AutoResizeTextView;

public class DeathScreenActivity extends AppCompatActivity {



    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        makeFullScreen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_death_screen);
        score = (getIntent().getExtras().getInt("gameScore"));
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


    @Override
    protected void onResume() {
        makeFullScreen();
        super.onResume();
        setMenu();
    }

    private void setMenu() {

        SharedPreferences prefs = getSharedPreferences("trapperPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;


        AutoResizeTextView deathMessage =  (AutoResizeTextView) findViewById(R.id.death_text);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(screenWidth,screenHeight/3);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        deathMessage.setLayoutParams(lp);
        deathMessage.setMaxLines(1);
        deathMessage.setTextSize(500);
        deathMessage.setTextColor(Color.WHITE);
        deathMessage.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BOLD));



        int btnWidth = screenWidth/3;

        RelativeLayout.LayoutParams restartBtnParams = new RelativeLayout.LayoutParams(btnWidth*2,btnWidth/3);
        restartBtnParams.addRule(RelativeLayout.ABOVE, R.id.deathBackBtn);
        restartBtnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        Button restartBtn =  (Button) findViewById(R.id.replayBtn);
        restartBtn.setLayoutParams(restartBtnParams);
        restartBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (btnWidth / 5.5));
        restartBtn.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BOLD));


        Button backBtn = (Button) findViewById(R.id.deathBackBtn);
        RelativeLayout.LayoutParams backBtnParams = new RelativeLayout.LayoutParams(btnWidth*2,btnWidth/3);
        backBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        backBtnParams.setMargins(0, btnWidth / 5, 0, 0);
        backBtnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        backBtn.setLayoutParams(backBtnParams);
        backBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (btnWidth / 5.5));
        backBtn.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BOLD));



        AutoResizeTextView dScoreMsgPT1 = (AutoResizeTextView) findViewById(R.id.DEATH_SCORE_MSG_PT1);
        dScoreMsgPT1.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BOLD));
        RelativeLayout.LayoutParams dParamsPT1 = new RelativeLayout.LayoutParams(screenWidth/2,btnWidth/2);
        dParamsPT1.addRule(RelativeLayout.BELOW, R.id.deathBackBtn);
        dParamsPT1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        dParamsPT1.setMargins(0, btnWidth / 5, 0, 0);
        dScoreMsgPT1.setLayoutParams(dParamsPT1);
        dScoreMsgPT1.setMaxLines(1);
        dScoreMsgPT1.setTextSize(500);
        dScoreMsgPT1.setTextColor(Color.WHITE);
        dScoreMsgPT1.setGravity(Gravity.CENTER_HORIZONTAL);


        AutoResizeTextView dScoreMsgPT2 = (AutoResizeTextView) findViewById(R.id.DEATH_SCORE_MSG_PT2);
        RelativeLayout.LayoutParams dParamsPT2 = new RelativeLayout.LayoutParams( (int) (screenWidth/1.7),btnWidth/2);
        dScoreMsgPT2.invalidate();
        dScoreMsgPT2.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BOLD));
        dParamsPT2.addRule(RelativeLayout.BELOW, R.id.DEATH_SCORE_MSG_PT1);
        dParamsPT2.addRule(RelativeLayout.CENTER_HORIZONTAL);
       // dParamsPT2.setMargins(0, btnWidth / 5, 0, 0);
        dScoreMsgPT2.setMaxLines(1);
        dScoreMsgPT2.setTextSize(500);
        dScoreMsgPT2.setTextColor(Color.WHITE);
        dScoreMsgPT2.setLayoutParams(dParamsPT2);
        dScoreMsgPT2.setGravity(Gravity.CENTER_HORIZONTAL);


        AutoResizeTextView tip = (AutoResizeTextView) findViewById(R.id.death_screen_tip);
        RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams( screenWidth, (int) (btnWidth/1.8));
        tip.invalidate();
        tip.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BOLD));
        tParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        tParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tip.setMaxLines(2);
        tip.setTextSize(500);
        tip.setTextColor(Color.WHITE);
        tip.setLayoutParams(tParams);
        tip.setGravity(Gravity.CENTER_HORIZONTAL);


        String scoreMsgDifParam;
        scoreMsgDifParam = "DIFFICULTY IS NOT ASSIGNED";


        switch (prefs.getInt("DIF",1)) {
            case 0: scoreMsgDifParam = "Easy";
                if(score > prefs.getInt("HSCORE-E",0)) {
                editor.putInt("HSCORE-E",score);
                    editor.commit();
                }
                break;
            case 1: scoreMsgDifParam = "Medium";
                if(score > prefs.getInt("HSCORE-M",0)) {
                    editor.putInt("HSCORE-M",score);
                    editor.commit();
                }
                break;
            case 2: scoreMsgDifParam = "Hard";
                if(score > prefs.getInt("HSCORE-H",0)) {
                    editor.putInt("HSCORE-H",score);
                    editor.commit();
                }
                break;
            case 3: scoreMsgDifParam = "Insane";
                if(score > prefs.getInt("HSCORE-I",0)) {
                    editor.putInt("HSCORE-I",score);
                    editor.commit();
                }
                break;
        }

        String difscoreMsg = getResources().getString(R.string.dif_score_message_PT1, scoreMsgDifParam);
        dScoreMsgPT1.setText(difscoreMsg);

        String scoreMsg = getResources().getString(R.string.death_score_message_PT2,score);
        dScoreMsgPT2.setText(scoreMsg);




    }

    public void playBtnPressed(View view) {
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(intent);
    }

    public void backBtnPressed(View view) {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }



}
