/**
 * *****************************************************************************
 *
 * @file Sense Hat/DynamicTableActivity.java
 * @author Milosz Plutowski
 * @version V1.0
 * @date 15-06-2021
 * @brief Sense Hat: Table of measurements activity
 * *****************************************************************************
 */

package com.example.senseHat.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.senseHat.ViewModel.TableAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class DynamicTableActivity extends AppCompatActivity {

    /* BEGIN config data */
    private String ipAddress = Common.DEFAULT_IP_ADDRESS;
    private int sampleTime = 5000;
    /* END config data */

    private ArrayList<MeasurementModel> mList;

    private RecyclerView recyclerViewDynamicTable;
    private RecyclerView.Adapter tableAdapter;
    private RecyclerView.LayoutManager tableManager;

    /* BEGIN view Buttons */
    private Button btnRefreshTable;
    private Button btnClearTable;
    private Button btnStartTable;
    private Button btnStopTable;
    private Button btnSet;
    /* END view Buttons */

    /* BEGIN view EditText and variable for sample time */
    private EditText editTextSampleTime;
    private int sampleTimeOfRefresh;
    /* END view EditText and variable for sample time */

    /* BEGIN view CheckBoxes */
    private CheckBox checkBoxList;
    private CheckBox checkBoxEnvMes;
    private CheckBox checkBoxAngleOrientation;
    private CheckBox checkBoxJoyStick;
    private CheckBox checkBoxCompass;
    private CheckBox checkBoxALL;
    /* END view CheckBoxes */

    /* BEGIN view Switch */
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch swChangeUnit;
    /* END view Switch */

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

    /* BEGIN DATA INFO */
    protected Date currentTime;
    protected SimpleDateFormat df;
    protected String formattedDate;
    /* END DATA INFO */

    /* BEGIN RecyclerView variables */
    private RecyclerView dynamicTableRecyclerView;
    private RecyclerView.Adapter dynamicTableRecyclerViewAdapter;
    private RecyclerView.LayoutManager dynamicTableRecyclerViewManager;
    /* END RecyclerView variables */

    /* Testable module */
    private TestableClass responseHandling = new TestableClass();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_table);

        /**************** INIT RECYCLER VIEW - DYNAMIC TABLE *****************/
        mList = new ArrayList<MeasurementModel>() {
        };

        /**************** INIT WIDGETS *****************/
        initView();

        // Initialize Volley request queue
        queue = Volley.newRequestQueue(DynamicTableActivity.this);
    }

    /**
     * @brief Init View of Page.
     */
    public void initView() {
        editTextSampleTime = (EditText) findViewById(R.id.editTextNumberDecimalSampleTime);

        btnRefreshTable = (Button) findViewById(R.id.btnRefreshTable);
        btnClearTable = (Button) findViewById(R.id.btnClearTable);
        btnStartTable = (Button) findViewById(R.id.btnStartTable);
        btnStopTable = (Button) findViewById(R.id.btnStopTable);
        btnSet = (Button) findViewById(R.id.btnSetSample);

        checkBoxList = (CheckBox) findViewById(R.id.checkBoxList);
        checkBoxEnvMes = (CheckBox) findViewById(R.id.checkBoxEnvMes);
        checkBoxAngleOrientation = (CheckBox) findViewById(R.id.checkBoxAngleOrientation);
        checkBoxJoyStick = (CheckBox) findViewById(R.id.checkBoxJoyStick);
        checkBoxCompass = (CheckBox) findViewById(R.id.checkBoxCompass);
        checkBoxALL = (CheckBox) findViewById(R.id.checkBoxAll);

        swChangeUnit = (Switch) findViewById(R.id.swChangeUnit);

        dynamicTableRecyclerView = (RecyclerView) findViewById(R.id.dynamicTableRecyclerView);

        initRecyclerView();
    }

    /**
     * @brief Init View of RecyclerView.
     */
    private void initRecyclerView() {
        dynamicTableRecyclerViewManager = new LinearLayoutManager(this);
        dynamicTableRecyclerView.setLayoutManager(dynamicTableRecyclerViewManager);

        dynamicTableRecyclerView.setHasFixedSize(true);

        dynamicTableRecyclerViewAdapter = new TableAdapter(this, mList);
        dynamicTableRecyclerView.setAdapter(dynamicTableRecyclerViewAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        if ((requestCode == Common.REQUEST_CODE_CONFIG) && (resultCode == RESULT_OK)) {

            // IoT server IP address
            ipAddress = dataIntent.getStringExtra(Common.CONFIG_IP_ADDRESS);
            //textViewIP.setText(getIpAddressDisplayText(ipAddress));
        }
    }

    /**
     * @brief Refreshing data to table by unit and max number of samples
     */
    private void refreshDataToTable() {
        if (mList.size() <= 1000) {
            if (swChangeUnit.isChecked()) {
                //Toast.makeText(getApplicationContext(), "Second type of unit..", Toast.LENGTH_SHORT).show();
                MeasurementsToTableSecondUnit();
            } else {
                //Toast.makeText(getApplicationContext(), "First type of unit..", Toast.LENGTH_SHORT).show();
                MeasurementsToFirstOneUnit();
            }
        }
    }

    /**
     * @brief Request for picked measurements with first unit.
     */
    private void MeasurementsToFirstOneUnit() {
        if (checkBoxJoyStick.isChecked()) {
            if (checkBoxALL.isChecked()) {
                RequestForAllMeasurements();
            } else {
                sendGetRequest(Common.REQ_COUNTER_X);
                sendGetRequest(Common.REQ_COUNTER_Y);
                sendGetRequest(Common.REQ_COUNTER_MIDDLE);
            }
        }

        if (checkBoxEnvMes.isChecked()) {
            if (checkBoxALL.isChecked()) {
                RequestForAllMeasurements();
            } else {
                sendGetRequest(Common.REQ_TEMP_C_1);
                sendGetRequest(Common.REQ_TEMP_C_2);
                sendGetRequest(Common.REQ_TEMP_C_3);
                sendGetRequest(Common.REQ_HUM_P);
                sendGetRequest(Common.REQ_PRES_HPA);
            }
        }

        if (checkBoxAngleOrientation.isChecked()) {
            if (checkBoxALL.isChecked()) {
                RequestForAllMeasurements();
            } else {
                sendGetRequest(Common.REQ_ROLL_DEG);
                sendGetRequest(Common.REQ_PITCH_DEG);
                sendGetRequest(Common.REQ_YAW_DEG);
            }
        }

        if (checkBoxCompass.isChecked()) {
            if (checkBoxALL.isChecked()) {
                RequestForAllMeasurements();
            } else {
                sendGetRequest(Common.REQ_COMPASS_NORTH);
                sendGetRequest(Common.REQ_COMPASS_X);
                sendGetRequest(Common.REQ_COMPASS_Y);
                sendGetRequest(Common.REQ_COMPASS_Z);
            }
        }

        if (checkBoxALL.isChecked() && !(checkBoxEnvMes.isChecked() || checkBoxAngleOrientation.isChecked() || checkBoxJoyStick.isChecked() || checkBoxCompass.isChecked())) {
            RequestForAllMeasurements();
        }
    }

    /**
     * @brief Request for picked measurements with second unit.
     */
    private void MeasurementsToTableSecondUnit() {
        if (checkBoxJoyStick.isChecked()) {
            if (checkBoxALL.isChecked()) {
                RequestForAllMeasurementsOtherUnit();
            } else {
                sendGetRequest(Common.REQ_COUNTER_X);
                sendGetRequest(Common.REQ_COUNTER_Y);
                sendGetRequest(Common.REQ_COUNTER_MIDDLE);
            }
        }

        if (checkBoxEnvMes.isChecked()) {
            if (checkBoxALL.isChecked()) {
                RequestForAllMeasurementsOtherUnit();
            } else {
                sendGetRequest(Common.REQ_TEMP_F_1);
                sendGetRequest(Common.REQ_TEMP_F_2);
                sendGetRequest(Common.REQ_TEMP_F_3);
                sendGetRequest(Common.REQ_HUM_);
                sendGetRequest(Common.REQ_PRES_MM_HG);
            }
        }

        if (checkBoxAngleOrientation.isChecked()) {
            if (checkBoxALL.isChecked()) {
                RequestForAllMeasurementsOtherUnit();
            } else {
                sendGetRequest(Common.REQ_ROLL_RAD);
                sendGetRequest(Common.REQ_PITCH_RAD);
                sendGetRequest(Common.REQ_YAW_RAD);
            }
        }

        if (checkBoxCompass.isChecked()) {
            if (checkBoxALL.isChecked()) {
                RequestForAllMeasurementsOtherUnit();
            } else {
                sendGetRequest(Common.REQ_COMPASS_NORTH);
                sendGetRequest(Common.REQ_COMPASS_X);
                sendGetRequest(Common.REQ_COMPASS_Y);
                sendGetRequest(Common.REQ_COMPASS_Z);
            }
        }

        if (checkBoxALL.isChecked() && !(checkBoxEnvMes.isChecked() || checkBoxAngleOrientation.isChecked() || checkBoxJoyStick.isChecked() || checkBoxCompass.isChecked())) {
            RequestForAllMeasurementsOtherUnit();
        }
    }

    /**
     * @brief Request for all measurements with first unit.
     */
    private void RequestForAllMeasurements() {
        sendGetRequest(Common.REQ_TEMP_C_1);
        sendGetRequest(Common.REQ_TEMP_C_2);
        sendGetRequest(Common.REQ_TEMP_C_3);
        sendGetRequest(Common.REQ_HUM_P);
        sendGetRequest(Common.REQ_PRES_HPA);

        sendGetRequest(Common.REQ_ROLL_DEG);
        sendGetRequest(Common.REQ_PITCH_DEG);
        sendGetRequest(Common.REQ_YAW_DEG);

        sendGetRequest(Common.REQ_ACCELEROMETER_PITCH);
        sendGetRequest(Common.REQ_ACCELEROMETER_ROLL);
        sendGetRequest(Common.REQ_ACCELEROMETER_YAW);

        sendGetRequest(Common.REQ_ACCELEROMETER_X);
        sendGetRequest(Common.REQ_ACCELEROMETER_Y);
        sendGetRequest(Common.REQ_ACCELEROMETER_Z);

        sendGetRequest(Common.REQ_GYROSCOPE_ROLL);
        sendGetRequest(Common.REQ_GYROSCOPE_PITCH);
        sendGetRequest(Common.REQ_GYROSCOPE_YAW);
        sendGetRequest(Common.REQ_GYROSCOPE_X);
        sendGetRequest(Common.REQ_GYROSCOPE_Y);
        sendGetRequest(Common.REQ_GYROSCOPE_Z);

        sendGetRequest(Common.REQ_COMPASS_NORTH);
        sendGetRequest(Common.REQ_COMPASS_X);
        sendGetRequest(Common.REQ_COMPASS_Y);
        sendGetRequest(Common.REQ_COMPASS_Z);
    }

    /**
     * @brief Request for all measurements with second unit.
     */
    private void RequestForAllMeasurementsOtherUnit() {
        sendGetRequest(Common.REQ_TEMP_F_1);
        sendGetRequest(Common.REQ_TEMP_F_2);
        sendGetRequest(Common.REQ_TEMP_F_3);
        sendGetRequest(Common.REQ_HUM_);
        sendGetRequest(Common.REQ_PRES_MM_HG);

        sendGetRequest(Common.REQ_ROLL_RAD);
        sendGetRequest(Common.REQ_PITCH_RAD);
        sendGetRequest(Common.REQ_YAW_RAD);

        sendGetRequest(Common.REQ_ACCELEROMETER_PITCH);
        sendGetRequest(Common.REQ_ACCELEROMETER_ROLL);
        sendGetRequest(Common.REQ_ACCELEROMETER_YAW);

        sendGetRequest(Common.REQ_ACCELEROMETER_X);
        sendGetRequest(Common.REQ_ACCELEROMETER_Y);
        sendGetRequest(Common.REQ_ACCELEROMETER_Z);

        sendGetRequest(Common.REQ_GYROSCOPE_ROLL);
        sendGetRequest(Common.REQ_GYROSCOPE_PITCH);
        sendGetRequest(Common.REQ_GYROSCOPE_YAW);
        sendGetRequest(Common.REQ_GYROSCOPE_X);
        sendGetRequest(Common.REQ_GYROSCOPE_Y);
        sendGetRequest(Common.REQ_GYROSCOPE_Z);

        sendGetRequest(Common.REQ_COMPASS_NORTH);
        sendGetRequest(Common.REQ_COMPASS_X);
        sendGetRequest(Common.REQ_COMPASS_Y);
        sendGetRequest(Common.REQ_COMPASS_Z);
    }

    /**
     * @param v the View (Button) that was clicked
     * @brief Main activity button onClick procedure - common for all upper menu buttons
     */
    public void btns_onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRefreshTable: {
                refreshDataToTable();
                break;
            }
            case R.id.btnClearTable: {
                clearTable();
                break;
            }
            case R.id.btnStartTable: {
                startRequestTimer();
                break;
            }
            case R.id.btnStopTable: {
                stopRequestTimerTask();
                break;
            }
            case R.id.btnSetSample: {
                // Sample Time of refresh graph by timer
                stopRequestTimerTask();
                sampleTimeOfRefresh = Integer.valueOf(editTextSampleTime.getText().toString());
                if (sampleTimeOfRefresh >= 1000)
                    sampleTime = sampleTimeOfRefresh;
                else {
                    Toast.makeText(getApplicationContext(), "Too low value..", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default: {
                // do nothing
            }
        }
    }

    /**
     * @brief Clear table 'mList'.
     */
    private void clearTable() {
        mList.clear();
        initRecyclerView();
    }


    /**
     * @brief Starts new 'Timer' (if currently not exist) and schedules periodic task.
     */
    private void startRequestTimer() {
        if (requestTimer == null) {
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
                    public void run() {
                        if (checkBoxList.isChecked() && mList.size() <= 1000) {
                            refreshDataToTable();
                        } else {
                            clearTable();
                            refreshDataToTable();
                        }
                    }
                });
            }
        };
    }


    /**
     * @brief Sending GET request to IoT server using 'Volley'.
     */
    private void sendGetRequest(String u) {
        // Instantiate the RequestQueue with Volley
        // https://javadoc.io/doc/com.android.volley/volley/1.1.0-rc2/index.html
        String url = getURL(ipAddress, "measurements.php?id=[-1]");
        url = getURL(ipAddress, u);

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responseHandling(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        // Set longer time for request
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(
        //        7000,
        //        0,
        //        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    /**
     * @brief Validation of client-side time stamp based on 'SystemClock'.
     */
    private long getValidTimeStampIncrease(long currentTime) {
        // Right after start remember current time and return 0
        if (requestTimerFirstRequest) {
            requestTimerPreviousTime = currentTime;
            requestTimerFirstRequest = false;
            return 0;
        }

        // After each stop return value not greater than sample time
        // to avoid "holes" in the plot
        if (requestTimerFirstRequestAfterStop) {
            if ((currentTime - requestTimerPreviousTime) > sampleTime)
                requestTimerPreviousTime = currentTime - sampleTime;

            requestTimerFirstRequestAfterStop = false;
        }

        // If time difference is equal zero after start
        // return sample time
        if ((currentTime - requestTimerPreviousTime) == 0)
            return sampleTime;

        // Return time difference between current and previous request
        return (currentTime - requestTimerPreviousTime);
    }


    /**
     * @param ip  IP address (string)
     * @param req added part of request (string)
     * @brief Create JSON file URL from IoT server IP.
     * @retval GET request URL
     */
    private String getURL(String ip, String req) {
        return ("http://" + ip + "/" + req);
    }


    /**
     * @brief GET response handling - chart data series updated with IoT server data.
     */
    private void responseHandling(String response) {
        if (requestTimer != null) {
            // get time stamp with SystemClock
            long requestTimerCurrentTime = SystemClock.uptimeMillis(); // current time
            requestTimerTimeStamp += getValidTimeStampIncrease(requestTimerCurrentTime);

            MeasurementModel m = new MeasurementModel("Error", 0, "-", "-");
            m = responseHandling.getRawDataFromResponseToDynamicTable(response);
            // SET TIME OF MEASUREMENT
            currentTime = Calendar.getInstance().getTime();
            df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
            formattedDate = df.format(currentTime);
            m.mDate = formattedDate;

            mList.add(m);
            initRecyclerView();

            // remember previous time stamp
            requestTimerPreviousTime = requestTimerCurrentTime;
        } else {
            MeasurementModel m = new MeasurementModel("Error", 0, "-", "-");
            m = responseHandling.getRawDataFromResponseToDynamicTable(response);
            // SET TIME OF MEASUREMENT
            currentTime = Calendar.getInstance().getTime();
            df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
            formattedDate = df.format(currentTime);
            m.mDate = formattedDate;

            mList.add(m);
            initRecyclerView();
        }
    }
}
