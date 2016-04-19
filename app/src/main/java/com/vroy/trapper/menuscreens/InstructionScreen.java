package com.vroy.trapper.menuscreens;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.vroy.trapper.widgets.AutoResizeTextView;
import com.vroy.trapper.R;

public class InstructionScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_screen);
    }

    @Override
    protected void onResume() {
        makeFullScreen();
        super.onResume();
        setMenuButtons();
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

    public void backBtnPressed(View view) {

        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }

    private  void setMenuButtons() {
        Button backBtn = (Button) findViewById(R.id.instruction_screen_back_button);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;


        int backBtnWidth = screenWidth/3;
        RelativeLayout.LayoutParams instructBtnParams = new RelativeLayout.LayoutParams(backBtnWidth * 2,backBtnWidth/3);
        instructBtnParams.addRule(RelativeLayout.BELOW,R.id.instruction_text);
        instructBtnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        backBtn.setLayoutParams(instructBtnParams);
        backBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (backBtnWidth / 5.5));
        backBtn.setTextColor(Color.WHITE);
        backBtn.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BOLD));

        AutoResizeTextView instructTitle = (AutoResizeTextView) findViewById(R.id.instruction_screen_title);
        RelativeLayout.LayoutParams textparamsTitle = new RelativeLayout.LayoutParams(screenWidth,screenHeight/10);
        textparamsTitle.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        textparamsTitle.setMargins(screenWidth / 15, screenHeight / 30, screenWidth / 15, 0);
        instructTitle.setLayoutParams(textparamsTitle);
        instructTitle.setLayoutParams(textparamsTitle);
        instructTitle.setTextColor(Color.WHITE);
        instructTitle.setTextSize(500);
        instructTitle.setMaxLines(1);
        instructTitle.setMinLines(1);
        instructTitle.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BOLD));


        AutoResizeTextView instructText = (AutoResizeTextView) findViewById(R.id.instruction_text);
        RelativeLayout.LayoutParams textparams = new RelativeLayout.LayoutParams(screenWidth,screenHeight*2/4);
        textparams.addRule(RelativeLayout.BELOW, R.id.instruction_screen_title);
        textparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        textparams.setMargins(screenWidth / 15, screenHeight / 30, screenWidth / 15, screenHeight / 30);
        instructText.setLayoutParams(textparams);
        instructText.setTextColor(Color.WHITE);
        instructText.setTextSize(500);
        instructText.setMaxLines(100);
        instructText.setMinLines(1);
        instructText.setTypeface(Typer.set(this).getFont(Font.ROBOTO_REGULAR));
        instructText.setGravity(Gravity.CENTER_HORIZONTAL);



    }
}
