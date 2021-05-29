package com.example.senseHat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Timer;
import java.util.TimerTask;

public class DynamicTableActivity extends AppCompatActivity {

    public static String[] TableName;
    public static String[] TableValue;
    public static String[] TableUnit;

    public static TextView name1;
    public static TextView name2;
    public static TextView name3;
    public static TextView name4;
    public static TextView name5;
    public static TextView name6;
    public static TextView name7;
    public static TextView name8;
    public static TextView name9;

    public static TextView value1;
    public static TextView value2;
    public static TextView value3;
    public static TextView value4;
    public static TextView value5;
    public static TextView value6;
    public static TextView value7;
    public static TextView value8;
    public static TextView value9;

    public static TextView unit1;
    public static TextView unit2;
    public static TextView unit3;
    public static TextView unit4;
    public static TextView unit5;
    public static TextView unit6;
    public static TextView unit7;
    public static TextView unit8;
    public static TextView unit9;




    /* BEGIN config data */
    private String ipAddress = Common.DEFAULT_IP_ADDRESS;
    private int sampleTime = Common.DEFAULT_SAMPLE_TIME;
    /* END config data */

    private RecyclerView recyclerViewDynamicTable;
    private RecyclerView.Adapter tableAdapter;
    private RecyclerView.LayoutManager tableManager;

    private ImageButton btnGoToConfig;
    private ImageButton btnGoToGraphEnv;
    private ImageButton btnGoToGraphAngle;
    private ImageButton btnGoToLed;
    private ImageButton btnGoToTable;

    private Button btnStartTable;
    private Button btnStopTable;

    private CheckBox checkBoxEnvMes;
    private CheckBox checkBoxAngleOrientation;
    private CheckBox checkBoxJoyStick;


    private String onlyEnvMes = "measurements.php?id=[0,1,2]";
    private String onlyAngles = "measurements.php?id=[3,4,5]";
    private String onlyJoy = "measurements.php?id=[6]";
    private String EnvMesAndAngles = "measurements.php?id=[0,1,2,3,4,5]";
    private String EnvMesAndJoy = "measurements.php?id=[3,4,5,6]";
    private String JoyAndAngles = "measurements.php?id=[3,4,5,6]";
    private String AllMes = "measurements.php?id=[0,1,2,3,4,5,6]";


    /* BEGIN request timer */
    private RequestQueue queue;
    private Timer requestTimer;
    private long requestTimerTimeStamp = 0;
    private long requestTimerPreviousTime = -1;
    private boolean requestTimerFirstRequest = true;
    private boolean requestTimerFirstRequestAfterStop;
    private TimerTask requestTimerTask;
    private final Handler handler = new Handler();
    /* END request timer */

    /* Testable module */
    private TestableClass responseHandling = new TestableClass();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_table);

        // Init buttons
        initMenuButtons();



        btnStartTable = (Button) findViewById(R.id.btnStartTable);
        btnStopTable = (Button) findViewById(R.id.btnStopTable);

        checkBoxEnvMes = (CheckBox) findViewById(R.id.checkBoxEnvMes);
        checkBoxAngleOrientation = (CheckBox) findViewById(R.id.checkBoxAngleOrientation);
        checkBoxJoyStick = (CheckBox) findViewById(R.id.checkBoxJoyStick);

        // Initialize Volley request queue
        queue = Volley.newRequestQueue(DynamicTableActivity.this);
    }

    public void initTable(){
        name1 = (TextView) findViewById(R.id.name1);
        name2 = (TextView) findViewById(R.id.name2);
        name3 = (TextView) findViewById(R.id.name3);
        name4 = (TextView) findViewById(R.id.name4);
        name5 = (TextView) findViewById(R.id.name5);
        name6 = (TextView) findViewById(R.id.name6);
        name7 = (TextView) findViewById(R.id.name7);
        name8 = (TextView) findViewById(R.id.name8);
        name9 = (TextView) findViewById(R.id.name9);

        value1 = (TextView) findViewById(R.id.value1);
        value2 = (TextView) findViewById(R.id.value2);
        value3 = (TextView) findViewById(R.id.value3);
        value4 = (TextView) findViewById(R.id.value4);
        value5 = (TextView) findViewById(R.id.value5);
        value6 = (TextView) findViewById(R.id.value6);
        value7 = (TextView) findViewById(R.id.value7);
        value8 = (TextView) findViewById(R.id.value8);
        value9 = (TextView) findViewById(R.id.value9);

        unit1 = (TextView) findViewById(R.id.unit1);
        unit2 = (TextView) findViewById(R.id.unit2);
        unit3 = (TextView) findViewById(R.id.unit3);
        unit4 = (TextView) findViewById(R.id.unit4);
        unit5 = (TextView) findViewById(R.id.unit5);
        unit6 = (TextView) findViewById(R.id.unit6);
        unit7 = (TextView) findViewById(R.id.unit7);
        unit8 = (TextView) findViewById(R.id.unit8);
        unit9 = (TextView) findViewById(R.id.unit9);

    }

    public void initMenuButtons(){
        /* START INIT WIDGETS VIEW */
        btnGoToConfig = (ImageButton) findViewById(R.id.btnGoToConfig);
        btnGoToGraphEnv = (ImageButton) findViewById(R.id.btnGoToGraphEnv);
        btnGoToGraphAngle = (ImageButton) findViewById(R.id.btnGoToGraphAngle);
        btnGoToLed = (ImageButton) findViewById(R.id.btnGoToLed);
        btnGoToTable = (ImageButton) findViewById(R.id.btnGoToTable);
        /* END INIT WIDGETS VIEW */

        btnGoToConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfig();
            }
        });

        btnGoToGraphEnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGraphEnvView();
            }
        });

        btnGoToGraphAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGraphAngleView();
            }
        });

        btnGoToLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLedView();
            }
        });

        btnGoToTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTableView();
            }
        });

    }

    /**
     * @brief Called when the user taps the 'Config' button.
     * */
    private void openConfig() {
        Intent openConfigIntent = new Intent(this, ConfigActivity.class);
        Bundle configBundle = new Bundle();
        configBundle.putString(Common.CONFIG_IP_ADDRESS, Common.DEFAULT_IP_ADDRESS);
        configBundle.putInt(Common.CONFIG_SAMPLE_TIME, Common.DEFAULT_SAMPLE_TIME);
        openConfigIntent.putExtras(configBundle);
        startActivityForResult(openConfigIntent, Common.REQUEST_CODE_CONFIG);
        finish();
    }

    private void openGraphEnvView(){
        Intent openGraphViewIntent = new Intent(this, GraphActivityEnv.class);
        startActivity(openGraphViewIntent);
        finish();
    }

    private void openGraphAngleView(){
        Intent openGraphViewIntent = new Intent(this, GraphActivityAngle.class);
        startActivity(openGraphViewIntent);
        finish();
    }

    private void openLedView(){
        Intent openLedsViewIntent = new Intent(this, LedActivity.class);
        startActivity(openLedsViewIntent);
        finish();
    }

    private void openTableView(){

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        if ((requestCode == Common.REQUEST_CODE_CONFIG) && (resultCode == RESULT_OK)) {

            // IoT server IP address
            ipAddress = dataIntent.getStringExtra(Common.CONFIG_IP_ADDRESS);
            //textViewIP.setText(getIpAddressDisplayText(ipAddress));

            // Sample time (ms)
            //String sampleTimeText = dataIntent.getStringExtra(Common.CONFIG_SAMPLE_TIME);
            String sampleTimeText = "10000";
            sampleTime = Integer.parseInt(sampleTimeText);
            //textViewSampleTime.setText(getSampleTimeDisplayText(sampleTimeText));
        }
    }


    /**
     * @brief Main activity button onClick procedure - common for all upper menu buttons
     * @param v the View (Button) that was clicked
     */
    public void btns_onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartTable: {
                startRequestTimer();
                break;
            }
            case R.id.btnStopTable: {
                stopRequestTimerTask();
                break;
            }
            default: {
                // do nothing
            }
        }
    }

    /**
     * @brief Starts new 'Timer' (if currently not exist) and schedules periodic task.
     */
    private void startRequestTimer() {
        if(requestTimer == null) {
            // set a new Timer
            requestTimer = new Timer();

            // initialize the TimerTask's job
            initializeRequestTimerTask();
            requestTimer.schedule(requestTimerTask, 0, sampleTime);

            // clear error message
            //textViewError.setText("");
        }
    }

    /**
     * @brief Stops request timer (if currently exist)
     * and sets 'requestTimerFirstRequestAfterStop' flag.
     */
    private void stopRequestTimerTask() {
        // stop the timer, if it's not already null
        if (requestTimer != null) {
            requestTimer.cancel();
            requestTimer = null;
            requestTimerFirstRequestAfterStop = true;
        }
    }

    /**
     * @brief Initialize request timer period task with 'Handler' post method as 'sendGetRequest'.
     */
    private void initializeRequestTimerTask() {
        requestTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() { sendGetRequest(); }
                });
            }
        };
    }

    /**
     * @brief Sending GET request to IoT server using 'Volley'.
     */
    private void sendGetRequest()
    {
        // Instantiate the RequestQueue with Volley
        // https://javadoc.io/doc/com.android.volley/volley/1.1.0-rc2/index.html
        String url = getURL(ipAddress, "measurements.php?id=[-1]");
        if(checkBoxEnvMes.isChecked() && !checkBoxAngleOrientation.isChecked() && !checkBoxJoyStick.isChecked())
        {
            url = getURL(ipAddress, onlyEnvMes);
        }
        if(!checkBoxEnvMes.isChecked() && checkBoxAngleOrientation.isChecked() && !checkBoxJoyStick.isChecked())
        {
            url = getURL(ipAddress, onlyAngles);
        }
        if(!checkBoxEnvMes.isChecked() && !checkBoxAngleOrientation.isChecked() && checkBoxJoyStick.isChecked())
        {
            url = getURL(ipAddress, onlyJoy);
        }
        if(checkBoxEnvMes.isChecked() && checkBoxAngleOrientation.isChecked() && !checkBoxJoyStick.isChecked())
        {
            url = getURL(ipAddress, EnvMesAndAngles);
        }
        if(checkBoxEnvMes.isChecked() && !checkBoxAngleOrientation.isChecked() && checkBoxJoyStick.isChecked())
        {
            url = getURL(ipAddress, EnvMesAndJoy);
        }
        if(!checkBoxEnvMes.isChecked() && checkBoxAngleOrientation.isChecked() && checkBoxJoyStick.isChecked())
        {
            url = getURL(ipAddress, JoyAndAngles);
        }
        if(checkBoxEnvMes.isChecked() && checkBoxAngleOrientation.isChecked() && checkBoxJoyStick.isChecked())
        {
            url = getURL(ipAddress, AllMes);
        }

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { responseHandling(response); }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {  }
                });

        // Set longer time for request
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * @brief Validation of client-side time stamp based on 'SystemClock'.
     */
    private long getValidTimeStampIncrease(long currentTime)
    {
        // Right after start remember current time and return 0
        if(requestTimerFirstRequest)
        {
            requestTimerPreviousTime = currentTime;
            requestTimerFirstRequest = false;
            return 0;
        }

        // After each stop return value not greater than sample time
        // to avoid "holes" in the plot
        if(requestTimerFirstRequestAfterStop)
        {
            if((currentTime - requestTimerPreviousTime) > sampleTime)
                requestTimerPreviousTime = currentTime - sampleTime;

            requestTimerFirstRequestAfterStop = false;
        }

        // If time difference is equal zero after start
        // return sample time
        if((currentTime - requestTimerPreviousTime) == 0)
            return sampleTime;

        // Return time difference between current and previous request
        return (currentTime - requestTimerPreviousTime);
    }


    /**
     * @brief Create JSON file URL from IoT server IP.
     * @param ip IP address (string)
     * @param req added part of request (string)
     * @retval GET request URL
     */
    private String getURL(String ip, String req) {
        return ("http://" + ip + "/" + req);
    }



    /**
     * @brief GET response handling - chart data series updated with IoT server data.
     */
    private void responseHandling(String response)
    {
        if(requestTimer != null) {
            // get time stamp with SystemClock
            long requestTimerCurrentTime = SystemClock.uptimeMillis(); // current time
            requestTimerTimeStamp += getValidTimeStampIncrease(requestTimerCurrentTime);

            // get raw data from JSON response to Table View
            initTable();
            clearTable();

            responseHandling.getRawDataFromResponseToTable(response);

            // update plot series
            double timeStamp = requestTimerTimeStamp / 1000.0; // [sec]

            // remember previous time stamp
            requestTimerPreviousTime = requestTimerCurrentTime;
        }
    }


    public static void clearTable()
    {
        name1.clearComposingText();
        name1.clearComposingText();
        name2.clearComposingText();
        name3.clearComposingText();
        name4.clearComposingText();
        name5.clearComposingText();
        name6.clearComposingText();
        name7.clearComposingText();
        name8.clearComposingText();
        name9.clearComposingText();
        value1.clearComposingText();
        value1.clearComposingText();
        value2.clearComposingText();
        value3.clearComposingText();
        value4.clearComposingText();
        value5.clearComposingText();
        value6.clearComposingText();
        value7.clearComposingText();
        value8.clearComposingText();
        value9.clearComposingText();
        unit1.clearComposingText();
        unit2.clearComposingText();
        unit3.clearComposingText();
        unit4.clearComposingText();
        unit5.clearComposingText();
        unit6.clearComposingText();
        unit7.clearComposingText();
        unit8.clearComposingText();
        unit9.clearComposingText();
    }

}
