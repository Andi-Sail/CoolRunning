package ch.arebsame.coolrunning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity
{
    /*
    SpeedMonitor monitor;
    MediaPlayer music;
    EditText targetSpeed;
    EditText currentSpeed;
    TextView status;
    Button button;
     */
    Spinner dropdown;
    SeekBar difficultyBar;

    Boolean safeTrack = false;

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

        // check Permissions for location
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

        // check permission to read, write to external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

        difficultyBar = (SeekBar) findViewById(R.id.difficultlyBar);
        difficultyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateStartingSpeed(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dropdown = findViewById(R.id.programSpinner);
        RunningMode[] modes = RunningMode.values();
        String[] modesNames = new String[modes.length];
        for (int i = 0; i < modes.length; i++) {
            modesNames[i] = modes[i].name().replace('_', ' ');
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, modesNames);
        dropdown.setAdapter(adapter);
        // set up listener for selection spinner
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedModeString = (String) dropdown.getSelectedItem();
                RunningMode selectedMode = RunningMode.valueOf(selectedModeString.replace(' ', '_'));
                CoolRunningCom.setMode(selectedMode);
                updateStartingSpeed(difficultyBar.getProgress());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        updateStartingSpeed(difficultyBar.getProgress());

        Switch trackSwitch = (Switch) findViewById(R.id.trackSwitch);

        trackSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                CoolRunningCom.setSaveRun(true);
            } else {
                CoolRunningCom.setSaveRun(false);
            }
        }
    });
    }

    private void updateStartingSpeed(int progress) {
        float startingSpeed = progress+1;
        CoolRunningCom.setTargetSpeed(startingSpeed);
        ((TextView)findViewById(R.id.speedVariable)).setText(String.format("%.02f", startingSpeed));
    }

    public void onStartRunningClick(View view) {
        if (CoolRunningCom.getSaveRun()) {
            String trackName = ((TextView)findViewById(R.id.enteredNameEdit)).getText().toString();
            if (trackName.isEmpty())
            {
                Toast.makeText(getBaseContext(), "Please enter a valid name to safe the track", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                CoolRunningCom.setRunName(trackName);
            }
        }

        Intent runningActivity = new Intent(this, ActivityRunning.class);
        startActivity(runningActivity);
    }
}
