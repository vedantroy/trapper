package com.vroy.trapper.menuscreens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.vroy.trapper.R;
import com.vroy.trapper.gameContent.GameActivity;
import com.vroy.trapper.widgets.AutoResizeTextView;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //E,M,H,I





    }



    @Override
    protected void onResume() {


        makeFullScreen();
        setMenuButtons();
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("trapperPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if(prefs.getInt("DIF", -1) == -1) {
            editor.putInt("DIF",1);
            editor.commit();
        }

        if(prefs.getInt("HSCORE-E", -1) == 0) {
            editor.putInt("HSCORE-E",0);
            editor.commit();
        }
        if(prefs.getInt("HSCORE-M", -1) == 0) {
            editor.putInt("HSCORE-M",0);
            editor.commit();
        }
        if(prefs.getInt("HSCORE-H", -1) == 0) {
            editor.putInt("HSCORE-H",0);
            editor.commit();
        }
        if(prefs.getInt("HSCORE-I", -1) == 0) {
            editor.putInt("HSCORE-I",0);
            editor.commit();
        }
    }

    private  void setMenuButtons() {

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int playBtnWidth = screenWidth/3;

        AutoResizeTextView titleText =  (AutoResizeTextView) findViewById(R.id.title_text);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(screenWidth*4/5,screenHeight/4);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        titleText.setLayoutParams(lp);
        titleText.setMaxLines(1);
        titleText.setTextSize(500);
        titleText.setTextColor(Color.WHITE);
        titleText.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BOLD));

        //This creates the menu button
        Button playBtn = (Button) findViewById(R.id.playBtn);
        RelativeLayout.LayoutParams playBtnParams = new RelativeLayout.LayoutParams(playBtnWidth * 2,playBtnWidth / 3);
        playBtnParams.addRule(RelativeLayout.BELOW,R.id.title_text);
        playBtnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        playBtnParams.setMargins(0,playBtnWidth * 2/3,0,0);
        playBtn.setLayoutParams(playBtnParams);
        playBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,  (float) (playBtnWidth / 5.5));
        playBtn.setTextColor(Color.WHITE);
        playBtn.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BOLD));


        Button instructionButton = (Button) findViewById(R.id.instruction_screen_button);
        RelativeLayout.LayoutParams instructionBtnParams = new RelativeLayout.LayoutParams(playBtnWidth * 2,playBtnWidth / 3);
        instructionBtnParams.setMargins(0, playBtnWidth * 2 /3, 0,  0);
        instructionBtnParams.addRule(RelativeLayout.BELOW, R.id.playBtn);
        instructionBtnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        instructionButton.setLayoutParams(instructionBtnParams);
        instructionButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(playBtnWidth / 5.5));
        instructionButton.setTextColor(Color.WHITE);
        instructionButton.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BOLD));


        Button difBtn = (Button) findViewById(R.id.difficulty_button);
        RelativeLayout.LayoutParams difBtnParams = new RelativeLayout.LayoutParams(playBtnWidth * 2,playBtnWidth/3);
        difBtnParams.setMargins(0, playBtnWidth * 2/3, 0, 0);
        difBtnParams.addRule(RelativeLayout.BELOW, R.id.instruction_screen_button);
        difBtnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        difBtn.setLayoutParams(difBtnParams);
        difBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (playBtnWidth / 5.5));
        difBtn.setTextColor(Color.WHITE);
        difBtn.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BOLD));




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

    public void playBtnPressed(View view) {

        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(intent);

    }

    public void instructionBtnPressed(View view) {

        Intent intent = new Intent(getApplicationContext(), InstructionScreen.class);
        startActivity(intent);

    }

    public void difficultyButtonPressed(View view) {

        Intent intent = new Intent(getApplicationContext(), DifficultyScreenActivity.class);
        startActivity(intent);

    }
}
