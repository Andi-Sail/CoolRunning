package ch.arebsame.coolrunning;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    SpeedMonitor monitor;
    MediaPlayer music;
    EditText targetSpeed;
    EditText currentSpeed;
    TextView status;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monitor = new SpeedMonitor();

        music = MediaPlayer.create(this, R.raw.hello);
        music.setLooping(true);

        currentSpeed =  findViewById(R.id.editText1);               // Second line of the activity
        targetSpeed = findViewById(R.id.editText2);                 // First line of the activity
        status = findViewById(R.id.textView4);

        button = findViewById(R.id.button1);

        /*
         * When pressed, calculates the difference and makes alarm sound.
         */

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                monitor.compareSpeed(Float.parseFloat(currentSpeed.getText().toString()), Float.parseFloat(targetSpeed.getText().toString()));

                if(monitor.getResult() <= -5.0f)
                {
                    music.start();
                    status.setText("Too Slow");
                    music.setLooping(false);
                }
                else if (monitor.getResult() >= 5.0f)
                {
                    music.start();
                    status.setText("Too Fast");
                    music.setLooping(false);
                }
                else
                {
                    status.setText("");
                }
            }
        });

        //currentSpeed.setText("10.00");
        //targetSpeed.setText("5.00");
    }
}
