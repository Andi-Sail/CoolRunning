package ch.arebsame.coolrunning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity
{
    SpeedMonitor monitor;
    MediaPlayer music;
    EditText targetSpeed;
    EditText currentSpeed;
    TextView status;
    Button button;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            ((TextView) findViewById(R.id.speedVariable)).setText(String.format("%.02f", CoolRunningCom.getSpeed()));
            Log.d("current speed", String.format("%.02f", CoolRunningCom.getSpeed()));
            if (CoolRunningCom.getRunningError() == RunningError.tooFast) {
                ((EditText) findViewById(R.id.enteredNameEdit)).setText("too fast --> slow down");
                Log.d("speed", "too fast --> slow down");
            } else if (CoolRunningCom.getRunningError() == RunningError.tooSlow) {
                ((EditText) findViewById(R.id.enteredNameEdit)).setText("too slow --> speed up");
                Log.d("speed", "too slow --> speed up");
            } else {
                ((EditText) findViewById(R.id.enteredNameEdit)).setText("correct --> keep going");
                Log.d("speed", "correct --> keep going");
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("CNIT355 Running App");

        Spinner dropdown = findViewById(R.id.programSpinner);
        String[] items = new String[]{"Interval", "Increasing Speed", "Constant Speed", "Random Speed"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions((Activity) this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions((Activity) this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        }

        // TODO: only start on start button with Running Activity Intent
        // the following is only temporary
        Intent speedServiceIntent = new Intent(this, SpeedService.class);
        startService(speedServiceIntent);

        Intent speedMonitorServiceIntent = new Intent(this, SpeedMonitorService.class);
        startService(speedMonitorServiceIntent);

        CoolRunningCom.setMode(RunningMode.Constant_Speed);
        CoolRunningCom.setTargetSpeed(3);

        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
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

    public void onStartRunningClick(View vies) {

    }
}
