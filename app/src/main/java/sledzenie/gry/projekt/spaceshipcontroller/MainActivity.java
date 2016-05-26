package sledzenie.gry.projekt.spaceshipcontroller;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity  implements SensorEventListener, View.OnClickListener{


    //view
    private TextView xData;
    private TextView yData;
    private TextView zData;
    private EditText ipEditText;
    private EditText portEditText;
    private CheckBox connectedCheck;
    private boolean connectedShow = false;

    //sensor
    private SensorManager mSensorManager;
    private Sensor mSensor;

    //connector
    private Connector connector;
    private boolean connectionReady = false;

    private int deltaSpeed = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xData = (TextView) findViewById(R.id.XtextView);
        yData = (TextView) findViewById(R.id.YtextView);
        zData = (TextView) findViewById(R.id.ZtextView);
        ipEditText = (EditText) findViewById(R.id.IpEditText);
        portEditText = (EditText) findViewById(R.id.editTextPort);

        connectedCheck = (CheckBox) findViewById(R.id.checkBox);


        mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(this);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        xData.setText("X: " + event.values[0]);
        yData.setText("Y: " + event.values[1]);
        zData.setText("Z: " + event.values[2]);

        if (connectionReady && connector != null){
            try {
                connector.send("X: " + event.values[0]+", "+"Y: " + event.values[1]+", "+"Z: " + event.values[2]+", DELTASPEED: "+deltaSpeed+" END");
                if (connectedShow == false){
                    connectedShow = true;
                    connectedCheck.setChecked(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong...!", Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        mSensorManager.unregisterListener(this);
        connectionReady = false;
        if (connector !=  null){
            connector.closeConnection();
        }
    }

    @Override
    public void onClick(View v) {
        if(connectionReady){
            Toast.makeText(this, "Connection probably already established!", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            connector = new Connector(ipEditText.getText().toString(), portEditText.getText().toString());
            connectionReady = true;
            Toast.makeText(this, "Connection probably established...", Toast.LENGTH_LONG).show();
        } catch (SocketException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (KeyEvent.KEYCODE_VOLUME_UP == keyCode ){
            deltaSpeed++;
            Toast.makeText(this, "Speed up", Toast.LENGTH_SHORT).show();


        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            deltaSpeed--;
            Toast.makeText(this, "Speed down", Toast.LENGTH_SHORT).show();


        }
        return true;
    }
}
