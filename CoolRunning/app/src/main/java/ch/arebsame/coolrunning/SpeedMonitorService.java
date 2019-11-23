package ch.arebsame.coolrunning;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class SpeedMonitorService extends Service {
    private TargetSpeedUpdater targetSpeedUpdater;
    private SpeedMonitor monitor;
    private Thread speedMonitorThread;

    private MediaPlayer slowDownMusic;
    private MediaPlayer speedUpMusic;

    private boolean isRunning = false;

    public SpeedMonitorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.targetSpeedUpdater = new TargetSpeedUpdater();
        this.targetSpeedUpdater.Start();
        this.monitor = new SpeedMonitor();

        this.slowDownMusic = MediaPlayer.create(this.getBaseContext(), R.raw.slow_down);
        this.slowDownMusic.setLooping(false);
        this.speedUpMusic = MediaPlayer.create(this.getBaseContext(), R.raw.speed_up);
        this.speedUpMusic.setLooping(false);

        this.isRunning = true;

        this.speedMonitorThread = new Thread() {
            @Override
            public void run() {

                while (isRunning) {
                    while (speedUpMusic.isPlaying() || slowDownMusic.isPlaying()) {
                        // wait to make sure nothing is playing
                    }

                    //monitor.compareSpeed(CoolRunningCom.getAverageSpeed(), CoolRunningCom.getTargetSpeed());
                    monitor.compareSpeed(CoolRunningCom.getSpeed(), CoolRunningCom.getTargetSpeed());

                    if (monitor.getRunningError() == RunningError.tooSlow) {
                        speedUpMusic.start();
                        CoolRunningCom.setRunningError(RunningError.tooSlow);
                    } else if (monitor.getRunningError() == RunningError.tooFast) {
                        slowDownMusic.start();
                        CoolRunningCom.setRunningError(RunningError.tooFast);
                    } else {
                        CoolRunningCom.setRunningError(RunningError.correct);
                    }

                    // pass some time
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        speedMonitorThread.start();
    }

    private void monitorSpeed() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;
        this.targetSpeedUpdater.Stop();
        CoolRunningCom.setScore(monitor.getCurrentScore());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
