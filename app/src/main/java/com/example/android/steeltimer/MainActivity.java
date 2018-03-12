package com.example.android.steeltimer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 201600000;
    private static final long BURN_START_IN_MILLIS = 43200000;
    private static final long COAL_START_IN_MILLIS = 4800000;

    private int coalAmount;

    private TextView mTextViewCountDown;
    private TextView mTextViewCountDownTwo;
    private TextView mTextViewCountDownThree;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private Button mButtonAddCoal;

    private CountDownTimer mCountDownTimer;
    private CountDownTimer mCurrentBurnTimer;
    private CountDownTimer mCurrentCoalTimer;

    private boolean mTimerRunning;
    private boolean mCurrentBurnRunning;
    private boolean mCurrentCoalRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mCurrentBurnInMillis = BURN_START_IN_MILLIS;
    private long mCurrentCoalInMillis = COAL_START_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mTextViewCountDownTwo = findViewById(R.id.text_view_countdown_two);
        mTextViewCountDownThree = findViewById(R.id.text_view_countdown_three);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

        mButtonAddCoal = findViewById(R.id.button_add_coal);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();

                }
                if (mCurrentBurnRunning) {
                    pauseBurnTimer();
                } else {
                    startCurrentBurnTimer();
                }
                if (mCurrentCoalRunning) {
                    pauseCoalTimer();
                } else {
                    startCurrentCoalTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        mButtonAddCoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coalAmount += 1;
            }
        });

        updateCountDownText();
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("Start");
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonAddCoal.setVisibility(View.VISIBLE);
    }

    private void startCurrentBurnTimer() {
        mCurrentBurnTimer = new CountDownTimer(mCurrentBurnInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCurrentBurnInMillis = millisUntilFinished;
                updateCurrentBurnText();
            }

            @Override
            public void onFinish() {
                resetTimer();
            }
        }.start();

        mCurrentBurnRunning = true;
    }

    private void startCurrentCoalTimer() {
        mCurrentCoalTimer = new CountDownTimer(mCurrentCoalInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCurrentCoalInMillis = millisUntilFinished;
                updateCurrentCoalText();
            }

            @Override
            public void onFinish() {
                if (mTimerRunning) {
                    restartCoalTimer();
                } else {
                    pauseCoalTimer();
                }
            }
        }.start();

        mCurrentCoalRunning = true;
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void pauseBurnTimer() {
        mCurrentBurnTimer.cancel();
        mCurrentBurnRunning = false;
        mButtonAddCoal.setVisibility(View.INVISIBLE);
    }

    private void pauseCoalTimer() {
        mCurrentCoalTimer.cancel();
        mCurrentCoalRunning = false;
    }

    private void resetTimer() {
        coalAmount = 0;
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        mCurrentBurnInMillis = BURN_START_IN_MILLIS;
        mCurrentCoalInMillis = COAL_START_IN_MILLIS;
        updateCountDownText();
        updateCurrentBurnText();
        updateCurrentCoalText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonAddCoal.setVisibility(View.INVISIBLE);
        mCurrentBurnRunning = false;
        mCurrentCoalRunning = false;

    }

    private void restartCoalTimer() {
        startCurrentCoalTimer();
    }

    private void updateCountDownText() {
        int days = (int) (mTimeLeftInMillis / 1000) / 86400;
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600 % 24;
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60 % 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d:%02d", days, hours, minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateCurrentBurnText() {
        int hours = (int) ((mCurrentBurnInMillis + (coalAmount * COAL_START_IN_MILLIS)) / 1000) / 3600 % 24;
        int minutes = (int) ((mCurrentBurnInMillis + (coalAmount * COAL_START_IN_MILLIS)) / 1000) / 60 % 60;
        int seconds = (int) ((mCurrentBurnInMillis + (coalAmount * COAL_START_IN_MILLIS)) / 1000) % 60;

        String currentBurnLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        mTextViewCountDownTwo.setText(currentBurnLeftFormatted);
    }

    private void updateCurrentCoalText() {
        int hours = (int) (mCurrentCoalInMillis  / 1000) / 3600 % 24;
        int minutes = (int) (mCurrentCoalInMillis / 1000) / 60 % 60;
        int seconds = (int) (mCurrentCoalInMillis / 1000) % 60;

        String currentBurnCoalFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        mTextViewCountDownThree.setText(currentBurnCoalFormatted);
    }

}