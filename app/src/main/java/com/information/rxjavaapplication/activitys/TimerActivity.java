package com.information.rxjavaapplication.activitys;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.information.rxjavaapplication.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TimerActivity extends AppCompatActivity {
    Unbinder mUnbinder;
    private int DELAY = 0;
    private int PERIOD = 1000;
    private int count;

    // TimerTask
    private Timer mTimer;

    @BindView(R.id.timer_textView)
    TextView mTextView;
    @BindView(R.id.button)
    Button mButton;
    @BindView(R.id.button2)
    Button mButton2;
    @BindView(R.id.button3)
    Button mButton3;

    public void timerStart() {
        count = 0;
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText(String.valueOf(count++));
                    }
                });
            }
        }, DELAY, PERIOD);
    }

    public void timerStop() {
        if(mTimer!=null)
            mTimer.cancel();
    }

    // CountDownTimer
    private static final int MILLISINFUTURE = 11 * 1000;
    private static final int COUNT_DOWN_INTERVAL = 1000;

    CountDownTimer mCountDownTimer;

    private void initCountDownTask() {
        mCountDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTextView.setText(String.valueOf(count--));
            }

            @Override
            public void onFinish() {
                mTextView.setText("Finish .");
            }
        };
    }

    public void countDownTimerStart() {
        count = 10;
        mCountDownTimer.start();
    }

    public void countDownStop() {
        if(mCountDownTimer!=null)
            mCountDownTimer.cancel();
    }

    private Handler mHandler;
    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            mTextView.setText(String.valueOf(count++));
            mHandler.postDelayed(this, 1000);
        }
    };

    private void initHandler() {
        mHandler = new Handler();
    }

    public void handlerStart() {
        count = 0;
        mHandler.postDelayed(timer, 0);
    }

    public void handlerStop() {
        if(mHandler!=null)
            mHandler.removeCallbacksAndMessages(null);
    }

    @OnClick(R.id.button)
    void timerTask() {
        stop();
        timerStart();
    }

    @OnClick(R.id.button2)
    void countDownTask() {
        stop();
        countDownTimerStart();
    }

    @OnClick(R.id.button3)
    void handler() {
        stop();
        handlerStart();
    }

    public void stop() {
        timerStop();
        countDownStop();
        handlerStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        mUnbinder = ButterKnife.bind(this);

        initCountDownTask();
        initHandler();
    }
}