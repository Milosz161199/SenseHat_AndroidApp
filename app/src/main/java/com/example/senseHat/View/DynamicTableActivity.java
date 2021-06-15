/**
 ******************************************************************************
 * @file    Sense Hat/DynamicTableActivity.java
 * @author  Milosz Plutowski
 * @version V1.0
 * @date    15-06-2021
 * @brief   Sense Hat: Table of measurements activity
 ******************************************************************************
 */

package com.example.senseHat.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.senseHat.Model.Common;
import com.example.senseHat.Model.MeasurementModel;
import com.example.senseHat.R;
import com.example.senseHat.Model.TestableClass;
import com.example.senseHat.ViewModel.MeasurementViewModel;
import com.example.senseHat.ViewModel.TableAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DynamicTableActivity extends AppCompatActivity {

    private ArrayList<MeasurementModel> mList;

    private List<MeasurementViewModel> measurements;


    /* BEGIN config data */
    private String ipAddress = Common.DEFAULT_IP_ADDRESS;
    private int sampleTime = Common.DEFAULT_SAMPLE_TIME;
    /* END config data */

    private RecyclerView recyclerViewDynamicTable;
    private RecyclerView.Adapter tableAdapter;
    private RecyclerView.LayoutManager tableManager;

    private Button btnStartTable;
    private Button btnStopTable;

    private CheckBox checkBoxEnvMes;
    private CheckBox checkBoxAngleOrientation;
    private CheckBox checkBoxJoyStick;
    private CheckBox checkBoxCompass;

    private Switch swChangeUnit;

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

    private RecyclerView dynamicTableRecyclerView;
    private RecyclerView.Adapter dynamicTableRecyclerViewAdapter;
    private RecyclerView.LayoutManager dynamicTableRecyclerViewManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_table);

        /**************** INIT RECYCLER VIEW - DYNAMIC TABLE *****************/
        initTableList();

        /**************** INIT WIDGETS *****************/
        initView();



        // Initialize Volley request queue
        queue = Volley.newRequestQueue(DynamicTableActivity.this);
    }

    public void initView(){
        btnStartTable = (Button) findViewById(R.id.btnStartTable);
        //btnStopTable = (Button) findViewById(R.id.btnStopTable);

        checkBoxEnvMes = (CheckBox) findViewById(R.id.checkBoxEnvMes);
        checkBoxAngleOrientation = (CheckBox) findViewById(R.id.checkBoxAngleOrientation);
        checkBoxJoyStick = (CheckBox) findViewById(R.id.checkBoxJoyStick);
        checkBoxCompass = (CheckBox) findViewById(R.id.checkBoxCompass);

        swChangeUnit = (Switch) findViewById(R.id.swChangeUnit);

        dynamicTableRecyclerView = (RecyclerView) findViewById(R.id.dynamicTableRecyclerView);

        initRecyclerView();
    }

    private void initRecyclerView() {
        dynamicTableRecyclerViewManager = new LinearLayoutManager(this);
        dynamicTableRecyclerView.setLayoutManager(dynamicTableRecyclerViewManager);

        dynamicTableRecyclerView.setHasFixedSize(true);

        dynamicTableRecyclerViewAdapter = new TableAdapter(this, mList);
        dynamicTableRecyclerView.setAdapter(dynamicTableRecyclerViewAdapter);
    }

    private void initTableList()
    {
        mList = new ArrayList<MeasurementModel>() {};
        MeasurementModel m = new MeasurementModel("Temperature", 1.2 , "°C", "QFQFQFQ");
        mList.add(m);
        m = new MeasurementModel("Temperature", 1000.2 , "F", "SENSOR 1");
        mList.add(m);
        m = new MeasurementModel("Pressure", 1000.2 , "hPa", "QFQFQFQFQ");
        mList.add(m);
        m = new MeasurementModel("Roll", 100.2 , "rad", "QFQFQFQQ");
        mList.add(m);
        m = new MeasurementModel("Yall", 100.2 , "deg", "QFQFQFQ");
        mList.add(m);
        m = new MeasurementModel("Roll", 1.2 , "rad", "QFQFQFQQ");
        mList.add(m);
        m = new MeasurementModel("Yall", 1.2 , "rad", "QFQFQFQ");
        mList.add(m);
        m = new MeasurementModel("Humidity", 10.2 , "%", "QFQFQFQ");
        mList.add(m);
        m = new MeasurementModel("Humidity", 10.2 , "[-]]", "QFQFQFQ");
        mList.add(m);
        m = new MeasurementModel("Pitch", 100.2 , "deg", "QFQFQFQ");
        mList.add(m);
        m = new MeasurementModel("Counter_middle", 10 , "[-]", "JOY");
        mList.add(m);
        m = new MeasurementModel("Counter_y", 10 , "[-]", "JOY");
        mList.add(m);
        m = new MeasurementModel("Counter_x", 10 , "[-]", "JOY");
        mList.add(m);
        m = new MeasurementModel("North", 10 , "[-]", "COMPASS");
        mList.add(m);
        m = new MeasurementModel("X", 10 , "[-]", "COMPASS_RAW");
        mList.add(m);
        m = new MeasurementModel("Y", 10 , "[-]", "COMPASS_RAW");
        mList.add(m);
        m = new MeasurementModel("Z", 10 , "[-]", "COMPASS_RAW");
        mList.add(m);
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
            //case R.id.btnStopTable: {
            //    stopRequestTimerTask();
            //    break;
            //}
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
            //initTable();
            //clearTable();

            responseHandling.getRawDataFromResponseToTable(response);

            // update plot series
            double timeStamp = requestTimerTimeStamp / 1000.0; // [sec]

            // remember previous time stamp
            requestTimerPreviousTime = requestTimerCurrentTime;
        }
    }

}
