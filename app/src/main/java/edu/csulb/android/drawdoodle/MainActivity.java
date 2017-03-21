package edu.csulb.android.drawdoodle;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

    private CanvasDraw customCanvas;
    BroadcastReceiver receiver = new Broadcastlistner();
    private SensorManager sensorManager;
    private long lastUpdate;
MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_NORMAL);



        //registerReceiver(receiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
        mediaPlayer = MediaPlayer.create(this, R.raw.eraser);
        mediaPlayer.setLooping(false);
        lastUpdate = System.currentTimeMillis();
        customCanvas = (CanvasDraw) findViewById(R.id.signature_canvas);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            checkShake(event);
        }
    }







    private void checkShake(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[0];
        float z = event.values[0];

        float accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();
        if (accelationSquareRoot >= 2) {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            Toast.makeText(this, "Dont shake me!", Toast.LENGTH_SHORT).show();
            Thread t = new Thread() {
                public void run() {
                   mediaPlayer.start();
                }
            };
            t.start();
            customCanvas.clearCanvas();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

    }

    public void clearCanvas(View v) {
        customCanvas.clearCanvas();
    }




}
