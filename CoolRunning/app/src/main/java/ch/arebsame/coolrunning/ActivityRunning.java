package ch.arebsame.coolrunning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityRunning extends AppCompatActivity {

    Intent speedServiceIntent;
    Intent speedMonitorServiceIntent;
    boolean isRunning = false;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            CoolRunningCom.updateRunningTime();
            ((TextView) findViewById(R.id.runningValue)).setText(CoolRunningCom.getRunningTimeFormated());

            ((TextView) findViewById(R.id.speedValue)).setText(String.format("%.02f", CoolRunningCom.getSpeed()));
            ((TextView) findViewById(R.id.targetValue)).setText(String.format("%.02f", CoolRunningCom.getTargetSpeed()));

            TextView result = (TextView) findViewById(R.id.resultVariable);
            Log.d("current speed", String.format("%.02f", CoolRunningCom.getSpeed()));
            if (CoolRunningCom.getRunningError() == RunningError.tooFast) {
                result.setText("too fast --> slow down");
                result.setBackgroundColor(getResources().getColor(R.color.runningTooFast));
                Log.d("speed", "too fast --> slow down");
            } else if (CoolRunningCom.getRunningError() == RunningError.tooSlow) {
                result.setText("too slow --> speed up");
                result.setBackgroundColor(getResources().getColor(R.color.runningTooSlow));
                Log.d("speed", "too slow --> speed up");
            } else {
                result.setText("correct --> keep going");
                Log.d("speed", "correct --> keep going");
                result.setBackgroundColor(getResources().getColor(R.color.runningGood));
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        setTitle("CNIT355 Running App");

        speedServiceIntent = new Intent(this, SpeedService.class);
        startService(speedServiceIntent);

        speedMonitorServiceIntent = new Intent(this, SpeedMonitorService.class);
        startService(speedMonitorServiceIntent);

        // start timer
        CoolRunningCom.setStartTimeNow();
        isRunning = true;

        Thread t = new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    handler.sendEmptyMessage(0);

                    // pass some time
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    void onStopRunningClick(View v) {
        isRunning = false;
        stopService(speedServiceIntent);
        stopService(speedMonitorServiceIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.onStopRunningClick(null);
    }
}
