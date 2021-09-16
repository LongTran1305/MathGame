package com.longtran.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final long START_TIMER_IN_MILIS = 30000;
    Boolean timer_running ;

    TextView tvScore,tvLife,tvTime,tvQuestion;
    EditText answer;
    Button btnOk,btnNextQuestion;
    Random random = new Random();
    int number1,number2,userAnswer,realAnswer;
    int userScore = 0;
    int userLife = 3;
    long time_left_in_miliseconds = START_TIMER_IN_MILIS;
    CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvScore = findViewById(R.id.tvScore);
        tvLife = findViewById(R.id.tvLife);
        tvTime = findViewById(R.id.tvTime);
        tvQuestion = findViewById(R.id.tvQuestion);
        btnOk = findViewById(R.id.btnOK);
        btnNextQuestion = findViewById(R.id.btnNextQuestion);
        answer = findViewById(R.id.editTextAnswer);
        gameContinue();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAnswer = Integer.valueOf(answer.getText().toString());
                pauseTimer();
                if(userAnswer == realAnswer){
                    userScore += 10;
                    tvQuestion.setText("Your answer is correct");
                    tvScore.setText("" + userScore);
                }else{
                    userLife -= 1;
                    tvQuestion.setText("Your answer is WRONG");
                }
            }
        });

        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer.setText("");
                resetTimer();
                if(userLife <= 0){
                    Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GameActivity.this,ResultActivity.class);
                    intent.putExtra("Score",userScore);
                    startActivity(intent);
                    finish();
                }else{
                    gameContinue();
                }
            }
        });
    }

    public void gameContinue(){
        number1 = random.nextInt(100);
        number2 = random.nextInt(100);
        tvQuestion.setText(number1 + " " + number2);
        realAnswer = number1 + number2;
        startTimer();
    }

    public void startTimer(){
        timer = new CountDownTimer(time_left_in_miliseconds,1000) {
            @Override
            public void onTick(long l) {
                time_left_in_miliseconds = l;
                updateText();
            }

            @Override
            public void onFinish() {
                timer_running = false;
                pauseTimer();
                resetTimer();
                updateText();
                userLife -= 1;
                tvQuestion.setText("Oops,Timeout");
            }
        }.start();
        timer_running = true;
    }

    public void updateText() {
        int second = (int) ((time_left_in_miliseconds / 100)%60);
        String time_left = String.format(Locale.getDefault(),"%02d",second);
        tvTime.setText(time_left);
    }

    public void pauseTimer(){
        timer.cancel();
        timer_running = false;
    }

    public void resetTimer(){
        time_left_in_miliseconds = START_TIMER_IN_MILIS;
        updateText();
    }
}