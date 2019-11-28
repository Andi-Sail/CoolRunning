package ch.arebsame.coolrunning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityResults extends AppCompatActivity
{
    TextView totalValue;
    TextView scoreValue;
    Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        setTitle("CNIT355 Running App");

        totalValue = findViewById(R.id.totalValue);
        scoreValue = findViewById(R.id.scoreValue);
        finishButton = findViewById(R.id.finishButton);

        totalValue.setText(CoolRunningCom.getRunningTimeFormated());
        scoreValue.setText(String.valueOf(CoolRunningCom.getScore()));
    }

    public void onFinishClick(View view)
    {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    public void onShowMapClick(View view)
    {
        Intent mapActivity = new Intent(this.getApplicationContext(), MapsActivity.class);
        startActivity(mapActivity);
    }
}
