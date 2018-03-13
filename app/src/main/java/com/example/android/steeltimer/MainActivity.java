package com.example.android.steeltimer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //Base values of all three timers
    private static final long START_TIME_IN_MILLIS = 201600000;
    private static final long BURN_START_IN_MILLIS = 43200000;
    private static final long COAL_START_IN_MILLIS = 4800000;

    private int coalAmount;

    private TextView mTextViewCountDown;
    private TextView mTextViewCountDownTwo;
    private TextView mTextViewCountDownThree;

    private Button mButtonStartReset;
    private Button mButtonAddCoal;

    private CountDownTimer mCountDownTimer;
    private CountDownTimer mCurrentBurnTimer;
    private CountDownTimer mCurrentCoalTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mCurrentBurnInMillis = BURN_START_IN_MILLIS;
    private long mCurrentCoalInMillis = COAL_START_IN_MILLIS;

    //values for onSaveInstanceState
    private Calendar calender = Calendar.getInstance();

    private long startTime = 0;
    private long finishTime = 0;
    private long currentFinishTime = 0;
    private long coalFinishTime = 0;

    private String FINISH_TIME = "whenFullTimerRunsOutInMillis";
    private String FINAL_TIMER_RUNNING = "booleanValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recovering the instance state
        if (savedInstanceState != null) {
            super.onCreate(savedInstanceState);
            mTimerRunning = savedInstanceState.getBoolean(FINAL_TIMER_RUNNING);
            if (mTimerRunning) {
                finishTime = savedInstanceState.getLong(FINISH_TIME);
                startTimer();
            }
        }

        //View Id Assignments
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mTextViewCountDownTwo = findViewById(R.id.text_view_countdown_two);
        mTextViewCountDownThree = findViewById(R.id.text_view_countdown_three);

        mButtonStartReset = findViewById(R.id.button_start_pause);
        mButtonAddCoal = findViewById(R.id.button_add_coal);

        //Initializing Start Button Listener
        mButtonStartReset.setOnClickListener(new View.OnClickListener() {

            /**
             * onClick method that handles the startReset button.
             * @param v startReset Button
             */
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    resetTimer();
                } else {
                    startTime = calender.getTimeInMillis();
                    finishTime = startTime + START_TIME_IN_MILLIS;
                    currentFinishTime = startTime + BURN_START_IN_MILLIS;
                    coalFinishTime = startTime + COAL_START_IN_MILLIS;
                    startTimer();
                }

            }

        });

//         Method that adds coal to the fuel load
        mButtonAddCoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coalAmount += 1;
            }
        });

        updateCountDownText();
    }

    /**
     * Method that handles the timers.
     */
    private void startTimer() {

//         Starting the final countdown timer
        mTimeLeftInMillis = finishTime - calender.getTimeInMillis();
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartReset.setText("Start");
            }
        }.start();

        mTimerRunning = true;
        mButtonStartReset.setText("Reset");
        mButtonAddCoal.setVisibility(View.VISIBLE);

//         Starting the current burn countdown timer
        mTimeLeftInMillis = currentFinishTime - calender.getTimeInMillis();
        mCurrentBurnTimer = new CountDownTimer(mCurrentBurnInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCurrentBurnInMillis = millisUntilFinished;
                updateCurrentBurnText();
            }

            @Override
            public void onFinish() {
                resetTimer();
                String RUN_OUT_TEXT = "Forge went out :(";
                mTextViewCountDownTwo.setText(RUN_OUT_TEXT);
            }
        }.start();


//        Starting the current coal countdown timer
        mTimeLeftInMillis = coalFinishTime - calender.getTimeInMillis();
        mCurrentCoalTimer = new CountDownTimer(mCurrentCoalInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCurrentCoalInMillis = millisUntilFinished;
                updateCurrentCoalText();
            }

            @Override
            public void onFinish() {
                if (mTimerRunning) {
                    mCurrentCoalTimer.start();
                }
            }
        }.start();

    }

    /**
     * Method for resetting all three timers.
     */
    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        mCurrentBurnInMillis = BURN_START_IN_MILLIS;
        mCurrentCoalInMillis = COAL_START_IN_MILLIS;

        updateCountDownText();
        updateCurrentBurnText();
        updateCurrentCoalText();

        mButtonStartReset.setVisibility(View.VISIBLE);
        mButtonAddCoal.setVisibility(View.INVISIBLE);

        mTimerRunning = false;
    }


    /**
     * Method handling the Final Countdown Timer
     */
    private void updateCountDownText() {
        int days = (int) (mTimeLeftInMillis / 1000) / 86400;
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600 % 24;
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60 % 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        //Setting current burn time
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    /**
     * Method handling the Current Burn Countdown Timer
     */
    private void updateCurrentBurnText() {
        int hours = (int) ((mCurrentBurnInMillis + (coalAmount * COAL_START_IN_MILLIS)) / 1000) / 3600 % 24;
        int minutes = (int) ((mCurrentBurnInMillis + (coalAmount * COAL_START_IN_MILLIS)) / 1000) / 60 % 60;
        int seconds = (int) ((mCurrentBurnInMillis + (coalAmount * COAL_START_IN_MILLIS)) / 1000) % 60;

        //Setting coal burn time
        String currentBurnLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        mTextViewCountDownTwo.setText(currentBurnLeftFormatted);
    }

    /**
     * Method handling the Current Coal Countdown Timer
     */
    private void updateCurrentCoalText() {
        int hours = (int) (mCurrentCoalInMillis / 1000) / 3600 % 24;
        int minutes = (int) (mCurrentCoalInMillis / 1000) / 60 % 60;
        int seconds = (int) (mCurrentCoalInMillis / 1000) % 60;

        String currentBurnCoalFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        mTextViewCountDownThree.setText(currentBurnCoalFormatted);
    }

    /**
     * onSaveInstanceState override to save important values.
     * ~needs more concrete data saving, so to make sure it's never lost until deletion.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        //Saving important values
        outState.putLong(FINISH_TIME, finishTime);

        outState.putBoolean(FINAL_TIMER_RUNNING, mTimerRunning);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

}