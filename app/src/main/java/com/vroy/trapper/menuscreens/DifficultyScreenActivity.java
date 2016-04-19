package com.vroy.trapper.menuscreens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.vroy.trapper.R;
import com.vroy.trapper.widgets.AutoResizeTextView;

public class DifficultyScreenActivity extends AppCompatActivity {


    AutoResizeTextView hScoreMsgPT1;
    AutoResizeTextView hScoreMsgPT2;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Toast difMsg;
    private Drawable selectBtn;
    private Drawable nonselectBtn;
    private Button easyBtn;
    private Button mediumBtn;
    private Button hardBtn;
    private Button insaneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_screen);
    }


    @Override
    protected void onResume() {
        makeFullScreen();
        super.onResume();
        setMenuButtons();

        prefs = getSharedPreferences("trapperPrefs", MODE_PRIVATE);
        editor = prefs.edit();
        difMsg = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        selectBtn = ContextCompat.getDrawable(this, R.drawable.selected_rounded_rect_btn);
        nonselectBtn = ContextCompat.getDrawable(this,R.drawable.rounded_rect_btn);
        setHscoreText();

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

    private void setMenuButtons() {



        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        int difBtnWidth = screenWidth / 3;
        int btnHeight = screenHeight/5;
        int difBarHorizontalMargins = screenWidth / 7;

        LinearLayout topBtnBar = (LinearLayout) findViewById(R.id.TbtnBar);
        RelativeLayout.LayoutParams TbtnBarParams = new RelativeLayout.LayoutParams(screenWidth, screenHeight / 3);
        TbtnBarParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        TbtnBarParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        topBtnBar.setLayoutParams(TbtnBarParams);

        LinearLayout BBtnBar = (LinearLayout) findViewById(R.id.BbtnBar);
        RelativeLayout.LayoutParams BbtnBarParams = new RelativeLayout.LayoutParams(screenWidth, screenHeight / 4);
        BbtnBarParams.addRule(RelativeLayout.BELOW, R.id.TbtnBar);
        BbtnBarParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        BBtnBar.setLayoutParams(BbtnBarParams);

        //Left,Top,Right,Bottom

         easyBtn = (Button) findViewById(R.id.easyDifBtn);
        LinearLayout.LayoutParams easyParams = new LinearLayout.LayoutParams(difBtnWidth, btnHeight);
        easyParams.setMargins(difBarHorizontalMargins, screenHeight / 10, 0, 0);
        easyBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnHeight / 5);
        easyBtn.setLayoutParams(easyParams);

         mediumBtn = (Button) findViewById(R.id.mediumDifBtn);
        LinearLayout.LayoutParams mediumParams = new LinearLayout.LayoutParams(difBtnWidth, btnHeight);
        mediumParams.setMargins(screenHeight / 30,screenHeight /10,difBarHorizontalMargins,0);
        mediumBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnHeight / 5);
        mediumBtn.setLayoutParams(mediumParams);

         hardBtn = (Button) findViewById(R.id.hardDifBtn);
        LinearLayout.LayoutParams hardParams = new LinearLayout.LayoutParams(difBtnWidth, btnHeight);
        hardParams.setMargins(difBarHorizontalMargins,0,screenWidth/18,0);
        hardBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnHeight / 5);
        hardBtn.setLayoutParams(hardParams);

         insaneBtn = (Button) findViewById(R.id.insaneDifBtn);
        LinearLayout.LayoutParams insaneParams = new LinearLayout.LayoutParams(difBtnWidth, btnHeight);
        insaneParams.setMargins(0, 0, difBarHorizontalMargins, 0);
        insaneBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnHeight / 5);
        insaneBtn.setLayoutParams(insaneParams);

        int btnWidth = screenWidth/3;
        Button backBtn = (Button) findViewById(R.id.difficulty_screen_backBtn);
        RelativeLayout.LayoutParams backBtnParams = new RelativeLayout.LayoutParams(btnWidth * 2, btnWidth / 3);
        backBtnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        backBtnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        backBtnParams.setMargins(0, 0, 0, screenHeight / 20);
        backBtn.setLayoutParams(backBtnParams);
        backBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (btnWidth / 5.5));
        backBtn.setTextColor(Color.WHITE);
        backBtn.setTypeface(Typer.set(this).getFont(Font.ROBOTO_MEDIUM));


        hScoreMsgPT1 = (AutoResizeTextView) findViewById(R.id.DIF_SCREEN_HSCOREMSGPT1);
        RelativeLayout.LayoutParams curDifParams = new RelativeLayout.LayoutParams(screenWidth/2, screenHeight / 16);
        curDifParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        curDifParams.addRule(RelativeLayout.ABOVE, R.id.DIF_SCREEN_HSCOREMSGPT2);
        curDifParams.setMargins(0, 0, 0, 0);
        hScoreMsgPT1.setLayoutParams(curDifParams);
        hScoreMsgPT1.setMaxLines(1);
        hScoreMsgPT1.setTextSize(500);
        hScoreMsgPT1.setTextColor(Color.WHITE);
        hScoreMsgPT1.setTypeface(Typer.set(this).getFont(Font.ROBOTO_MEDIUM));
        hScoreMsgPT1.setGravity(Gravity.CENTER_HORIZONTAL);


        hScoreMsgPT2 = (AutoResizeTextView) findViewById(R.id.DIF_SCREEN_HSCOREMSGPT2);
        RelativeLayout.LayoutParams curDifParamsTwo = new RelativeLayout.LayoutParams((int) (screenWidth/1.9), screenHeight / 14);
        curDifParamsTwo.addRule(RelativeLayout.CENTER_HORIZONTAL);
        curDifParamsTwo.addRule(RelativeLayout.ABOVE, R.id.difficulty_screen_backBtn);
        curDifParamsTwo.setMargins(0, 0, 0, (int) (btnWidth/1.4));
        hScoreMsgPT2.setLayoutParams(curDifParamsTwo);
        hScoreMsgPT2.setMaxLines(1);
        hScoreMsgPT2.setTextSize(500);
        hScoreMsgPT2.setTextColor(Color.WHITE);
        hScoreMsgPT2.setTypeface(Typer.set(this).getFont(Font.ROBOTO_MEDIUM));
        hScoreMsgPT2.setGravity(Gravity.CENTER_HORIZONTAL);


    }

    public void easyDifBtnPressed(View view) {
        editor.putInt("DIF", 0);
        editor.commit();
        setHscoreText();
        difMsg.cancel();
        difMsg = Toast.makeText(this, "" , Toast.LENGTH_SHORT);
        difMsg.setText("You are on: \"Easy\" difficulty");
        difMsg.show();
    }

    public void mediumDifBtnPressed(View view) {
        editor.putInt("DIF", 1);
        editor.commit();
        setHscoreText();
        difMsg.cancel();
        difMsg = Toast.makeText(this, "" , Toast.LENGTH_SHORT);
        difMsg.setText("You are on: \"Medium\" difficulty");
        difMsg.show();
    }

    public void hardDifBtnPressed(View view) {
        editor.putInt("DIF", 2);
        editor.commit();
        setHscoreText();
        difMsg.cancel();
        difMsg = Toast.makeText(this, "" , Toast.LENGTH_SHORT);
        difMsg.setText("You are on: \"Hard\" difficulty");
        difMsg.show();
    }

    public void insaneDifBtnPressed(View view) {
        editor.putInt("DIF", 3);
        editor.commit();
        setHscoreText();
        difMsg.cancel();
        difMsg = Toast.makeText(this, "" , Toast.LENGTH_SHORT);
        difMsg.setText("You are on: \"Insane\" difficulty");
        difMsg.show();
    }

    public void backBtnPressed(View view) {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }

    private void resetAllBackgrounds() {
        easyBtn.setBackground(nonselectBtn);
        mediumBtn.setBackground(nonselectBtn);
        hardBtn.setBackground(nonselectBtn);
        insaneBtn.setBackground(nonselectBtn);

        easyBtn.invalidate();
        mediumBtn.invalidate();
        hardBtn.invalidate();
        insaneBtn.invalidate();
    }


    private void setHscoreText() {


        String curDif = "DIFFICULTY NOT ASSIGNED";
        int difHscore = -100;



        switch (prefs.getInt("DIF", 1)) {
            case 0:
                curDif = "Easy";
                difHscore = prefs.getInt("HSCORE-E", 0);
                resetAllBackgrounds();
                easyBtn.setBackground(selectBtn);
                break;
            case 1:
                curDif = "Medium";
                difHscore = prefs.getInt("HSCORE-M", 0);
                resetAllBackgrounds();
                mediumBtn.setBackground(selectBtn);
                break;
            case 2:
                curDif = "Hard";
                difHscore = prefs.getInt("HSCORE-H", 0);
                resetAllBackgrounds();
                hardBtn.setBackground(selectBtn);
                break;
            case 3:
                curDif = "Insane";
                difHscore = prefs.getInt("HSCORE-I", 0);
                resetAllBackgrounds();
                insaneBtn.setBackground(selectBtn);
                break;
        }



        String currentDif = getResources().getString(R.string.dif_score_message_PT1 , curDif);
        String hScoreMsg = getResources().getString(R.string.high_score_message_PT2, difHscore);
        hScoreMsgPT1.setText(currentDif);
        hScoreMsgPT2.setText(hScoreMsg);


    }
}
