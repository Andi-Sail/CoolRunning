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

            // update and display running time
            CoolRunningCom.updateRunningTime();
            ((TextView) findViewById(R.id.runningValue)).setText(CoolRunningCom.getRunningTimeFormated());

            // diplay current speed and target speed
            ((TextView) findViewById(R.id.speedValue)).setText(String.format("%.02f", CoolRunningCom.getSpeed()));
            ((TextView) findViewById(R.id.targetValue)).setText(String.format("%.02f", CoolRunningCom.getTargetSpeed()));

            // display weahter to speed up or slow down with color
            TextView result = (TextView) findViewById(R.id.resultVariable);
            if (CoolRunningCom.getRunningError() == RunningError.tooFast) {
                result.setText("too fast --> slow down");
                result.setBackgroundColor(getResources().getColor(R.color.runningTooFast));
            } else if (CoolRunningCom.getRunningError() == RunningError.tooSlow) {
                result.setText("too slow --> speed up");
                result.setBackgroundColor(getResources().getColor(R.color.runningTooSlow));
            } else {
                result.setText("correct --> keep going");
                result.setBackgroundColor(getResources().getColor(R.color.runningGood));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        setTitle("CNIT355 Running App");

        // start speed service with GPS API
        speedServiceIntent = new Intent(this, SpeedService.class);
        startService(speedServiceIntent);

        // start speed monitor service to compare current speed and target speed and play beep accordingly
        speedMonitorServiceIntent = new Intent(this, SpeedMonitorService.class);
        startService(speedMonitorServiceIntent);

        // start timer
        CoolRunningCom.setStartTimeNow();
        isRunning = true;

        Thread t = new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    // periodically update User Interface with current data
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

    public void onStopRunningClick(View v) {
        if (isRunning) {
            // stop all services and threads
            isRunning = false;
            stopService(speedServiceIntent);
            stopService(speedMonitorServiceIntent);
            finish();

            // show result activity
            Intent resultActivity = new Intent(this.getApplicationContext(), ActivityResults.class);
            startActivity(resultActivity);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.onStopRunningClick(null);
    }
}
