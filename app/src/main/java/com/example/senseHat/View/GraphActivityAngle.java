/**
 * *****************************************************************************
 *
 * @file Sense Hat/GraphActivityAngle.java
 * @author Milosz Plutowski
 * @version V1.0
 * @date 15-06-2021
 * @brief Sense Hat: Angles measurements activity with data charts
 * *****************************************************************************
 */

package com.example.senseHat.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Double.isNaN;

public class GraphActivityAngle<mList> extends AppCompatActivity {

    /* BEGIN config data */
    private String ipAddress = Common.DEFAULT_IP_ADDRESS;
    private int sampleTime = Common.DEFAULT_SAMPLE_TIME;
    /* END config data */


    /* BEGIN widgets */

    private Switch swRoll;
    private Switch swPitch;
    private Switch swYaw;

    private boolean angleRollBoolean = false;
    private boolean anglePitchBoolean = false;
    private boolean angleYawBoolean = false;

    //private LineGraphSeries[] dataSeries;

    private TextView textViewIP;
    private TextView textViewSampleTime;
    private TextView textViewError;
    private GraphView dataGraph;
    private GraphView dataGraphRoll;
    private GraphView dataGraphYaw;
    private GraphView dataGraphPitch;
    private LineGraphSeries<DataPoint> dataSeriesRoll;
    private LineGraphSeries<DataPoint> dataSeriesPitch;
    private LineGraphSeries<DataPoint> dataSeriesYaw;
    private final int dataGraphMaxDataPointsNumber = 1000;
    private final double dataGraphMaxX = 10.0d;
    private final double dataGraphMinX = 0.0d;
    private final double dataGraphMaxY = 360.0d;
    private final double dataGraphMinY = 0.0d;
    private AlertDialog.Builder configAlterDialog;
    /* END widgets */

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

    public GraphActivityAngle() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_angle);

        /* BEGIN initialize widgets */
        /* BEGIN initialize Switches */
        swRoll = (Switch) findViewById(R.id.swRoll);
        swPitch = (Switch) findViewById(R.id.swPitch);
        swYaw = (Switch) findViewById(R.id.swYaw);

        /* BEGIN initialize TextViews */
        textViewIP = findViewById(R.id.textViewIP);
        textViewIP.setText(getIpAddressDisplayText(ipAddress));

        textViewSampleTime = findViewById(R.id.textViewSampleTime);
        textViewSampleTime.setText(getSampleTimeDisplayText(Integer.toString(sampleTime)));

        textViewError = findViewById(R.id.textViewErrorMsg);
        textViewError.setText("");
        /* END initialize TextViews */


        /* BEGIN initialize GraphView */
        InitGraphView();
        /* END initialize GraphView */

        /* BEGIN config alter dialog */
        configAlterDialog = new AlertDialog.Builder(GraphActivityAngle.this);
        configAlterDialog.setTitle("This will STOP data acquisition. Proceed?");
        configAlterDialog.setIcon(android.R.drawable.ic_dialog_alert);
        configAlterDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                stopRequestTimerTask();
                openConfig();
            }
        });
        configAlterDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        /* END config alter dialog */
        /* END initialize widgets */

        // Initialize Volley request queue
        queue = Volley.newRequestQueue(GraphActivityAngle.this);


        swRoll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "+Roll...", Toast.LENGTH_SHORT).show();
                    angleRollBoolean = true;
                } else {
                    Toast.makeText(getApplicationContext(), "-Roll...", Toast.LENGTH_SHORT).show();
                    angleRollBoolean = false;
                }
            }
        });

        swPitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "+Pitch...", Toast.LENGTH_SHORT).show();
                    anglePitchBoolean = true;
                } else {
                    Toast.makeText(getApplicationContext(), "-Pitch...", Toast.LENGTH_SHORT).show();
                    anglePitchBoolean = false;
                }
            }
        });

        swYaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "+Yaw...", Toast.LENGTH_SHORT).show();
                    angleYawBoolean = true;
                } else {
                    Toast.makeText(getApplicationContext(), "-Yaw...", Toast.LENGTH_SHORT).show();
                    angleYawBoolean = false;
                }
            }
        });

    }


    private void InitGraphView() {
        // https://github.com/jjoe64/GraphView/wiki
        dataGraphRoll = (GraphView) findViewById(R.id.dataGraphRoll);
        dataGraphPitch = (GraphView) findViewById(R.id.dataGraphPitch);
        dataGraphYaw = (GraphView) findViewById(R.id.dataGraphYaw);

        dataSeriesRoll = new LineGraphSeries<>(new DataPoint[]{});
        dataSeriesPitch = new LineGraphSeries<>(new DataPoint[]{});
        dataSeriesYaw = new LineGraphSeries<>(new DataPoint[]{});

        dataGraphRoll.addSeries(dataSeriesRoll);
        dataGraphPitch.addSeries(dataSeriesPitch);
        dataGraphYaw.addSeries(dataSeriesYaw);

        dataGraphRoll.getViewport().setXAxisBoundsManual(true);
        dataGraphRoll.getViewport().setYAxisBoundsManual(true);
        dataGraphRoll.getViewport().setMinX(dataGraphMinX);
        dataGraphRoll.getViewport().setMaxX(dataGraphMaxX);
        dataGraphRoll.getViewport().setMinY(dataGraphMinY);
        dataGraphRoll.getViewport().setMaxY(dataGraphMaxY);

        dataGraphRoll.getViewport().setScalable(true);
        dataGraphRoll.getViewport().setScrollable(true);

        dataGraphPitch.getViewport().setXAxisBoundsManual(true);
        dataGraphPitch.getViewport().setYAxisBoundsManual(true);
        dataGraphPitch.getViewport().setMinX(dataGraphMinX);
        dataGraphPitch.getViewport().setMaxX(dataGraphMaxX);
        dataGraphPitch.getViewport().setMinY(dataGraphMinY);
        dataGraphPitch.getViewport().setMaxY(dataGraphMaxY);

        dataGraphPitch.getViewport().setScalable(true);
        dataGraphPitch.getViewport().setScrollable(true);

        dataGraphYaw.getViewport().setXAxisBoundsManual(true);
        dataGraphYaw.getViewport().setYAxisBoundsManual(true);
        dataGraphYaw.getViewport().setMinX(dataGraphMinX);
        dataGraphYaw.getViewport().setMaxX(dataGraphMaxX);
        dataGraphYaw.getViewport().setMinY(dataGraphMinY);
        dataGraphYaw.getViewport().setMaxY(dataGraphMaxY);

        dataGraphYaw.getViewport().setScalable(true);
        dataGraphYaw.getViewport().setScrollable(true);


        // refresh chart
        dataGraphRoll.onDataChanged(true, true);

        dataGraphRoll.getLegendRenderer().setVisible(true);
        dataGraphRoll.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        dataGraphRoll.getLegendRenderer().setTextSize(30);

        dataGraphRoll.getGridLabelRenderer().setTextSize(20);
        dataGraphRoll.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Roll [deg]");
        dataGraphRoll.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Time [s]");
        dataGraphRoll.getGridLabelRenderer().setNumHorizontalLabels(9);
        dataGraphRoll.getGridLabelRenderer().setNumVerticalLabels(7);
        dataGraphRoll.getGridLabelRenderer().setPadding(35);

        dataGraphPitch.onDataChanged(true, true);

        dataGraphPitch.getLegendRenderer().setVisible(true);
        dataGraphPitch.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        dataGraphPitch.getLegendRenderer().setTextSize(30);

        dataGraphPitch.getGridLabelRenderer().setTextSize(20);
        dataGraphPitch.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Pitch [deg]");
        dataGraphPitch.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Time [s]");
        dataGraphPitch.getGridLabelRenderer().setNumHorizontalLabels(9);
        dataGraphPitch.getGridLabelRenderer().setNumVerticalLabels(7);
        dataGraphPitch.getGridLabelRenderer().setPadding(35);

        dataGraphYaw.onDataChanged(true, true);

        dataGraphYaw.getLegendRenderer().setVisible(true);
        dataGraphYaw.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        dataGraphYaw.getLegendRenderer().setTextSize(30);

        dataGraphYaw.getGridLabelRenderer().setTextSize(20);
        dataGraphYaw.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Yaw [deg]");
        dataGraphYaw.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Time [s]");
        dataGraphYaw.getGridLabelRenderer().setNumHorizontalLabels(9);
        dataGraphYaw.getGridLabelRenderer().setNumVerticalLabels(7);
        dataGraphYaw.getGridLabelRenderer().setPadding(35);
    }

    /**
     * Index :
     * 0 -> Temperature
     * 1 -> Humidity
     * 2 -> Pressure
     * 3 -> Roll
     * 4 -> Pitch
     * 5 -> Yaw
     **/
    boolean[] tabOfMeasurements = new boolean[6];

    private void madeRequestRollPitch() {
        sendGetRequest(Common.REQ_ROLL_DEG);
        sendGetRequest(Common.REQ_PITCH_DEG);
    }

    private void madeRequestRollYaw() {
        sendGetRequest(Common.REQ_ROLL_DEG);
        sendGetRequest(Common.REQ_YAW_DEG);
    }

    private void madeRequestYawPitch() {
        sendGetRequest(Common.REQ_YAW_DEG);
        sendGetRequest(Common.REQ_PITCH_DEG);
    }

    private void madeRequestRollYawPitch() {
        sendGetRequest(Common.REQ_ROLL_DEG);
        sendGetRequest(Common.REQ_PITCH_DEG);
        sendGetRequest(Common.REQ_YAW_DEG);
    }

    private void madeRequest() {
        if (angleRollBoolean && !anglePitchBoolean && !angleYawBoolean) {
            sendGetRequest(Common.REQ_ROLL_DEG);
        }
        if (!angleRollBoolean && anglePitchBoolean && !angleYawBoolean) {
            sendGetRequest(Common.REQ_PITCH_DEG);
        }
        if (!angleRollBoolean && !anglePitchBoolean && angleYawBoolean) {
            sendGetRequest(Common.REQ_YAW_DEG);
        }
        if (angleRollBoolean && anglePitchBoolean && !angleYawBoolean) {
            madeRequestRollPitch();
        }
        if (!angleRollBoolean && anglePitchBoolean && angleYawBoolean) {
            madeRequestRollYaw();
        }
        if (angleRollBoolean && !anglePitchBoolean && angleYawBoolean) {
            madeRequestYawPitch();
        }
        if (angleRollBoolean && anglePitchBoolean && angleYawBoolean) {
            madeRequestRollYawPitch();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        if ((requestCode == Common.REQUEST_CODE_CONFIG) && (resultCode == RESULT_OK)) {

            // IoT server IP address
            ipAddress = dataIntent.getStringExtra(Common.CONFIG_IP_ADDRESS);
            textViewIP.setText(getIpAddressDisplayText(ipAddress));

            // Sample time (ms)
            String sampleTimeText = dataIntent.getStringExtra(Common.CONFIG_SAMPLE_TIME);
            sampleTime = Integer.parseInt(sampleTimeText);
            textViewSampleTime.setText(getSampleTimeDisplayText(sampleTimeText));
        }
    }

    /**
     * @param v the View (Button) that was clicked
     * @brief Main activity button onClick procedure - common for all upper menu buttons
     */
    public void btns_onClick(View v) {
        switch (v.getId()) {
            case R.id.configBtn: {
                if (requestTimer != null)
                    configAlterDialog.show();
                else
                    openConfig();
                break;
            }
            case R.id.startBtn: {
                startRequestTimer();
                break;
            }
            case R.id.stopBtn: {
                stopRequestTimerTask();
                break;
            }
            default: {
                // do nothing
            }
        }
    }

    /**
     * @param ip IP address (string)
     * @brief Create display text for IoT server IP address
     * @retval Display text for textViewIP widget
     */
    private String getIpAddressDisplayText(String ip) {
        return ("IP: " + ip);
    }

    /**
     * @param st Sample time in ms (string)
     * @brief Create display text for requests sample time
     * @retval Display text for textViewSampleTime widget
     */
    private String getSampleTimeDisplayText(String st) {
        return ("Sample time: " + st + " ms");
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
     * @param n Number of spaces.
     * @retval String with 'n' spaces.
     */
    private String Space(int n) {
        return new String(new char[n]).replace('\0', ' ');
    }

    /**
     * @param errorCode local error codes, see: COMMON
     * @brief Handles application errors. Logs an error and passes error code to GUI.
     */
    private void errorHandling(int errorCode) {
        switch (errorCode) {
            case Common.ERROR_TIME_STAMP:
                textViewError.setText("ERR #1");
                Log.d("errorHandling", "Request time stamp error.");
                break;
            case Common.ERROR_NAN_DATA:
                textViewError.setText("ERR #2");
                Log.d("errorHandling", "Invalid JSON data.");
                break;
            case Common.ERROR_RESPONSE:
                textViewError.setText("ERR #3");
                Log.d("errorHandling", "GET request VolleyError.");
                break;
            default:
                textViewError.setText("ERR ??");
                Log.d("errorHandling", "Unknown error.");
                break;
        }
    }

    /**
     * @brief Called when the user taps the 'Config' button.
     */
    private void openConfig() {
        Intent openConfigIntent = new Intent(this, ConfigActivity.class);
        Bundle configBundle = new Bundle();
        configBundle.putString(Common.CONFIG_IP_ADDRESS, ipAddress);
        configBundle.putInt(Common.CONFIG_SAMPLE_TIME, sampleTime);
        openConfigIntent.putExtras(configBundle);
        startActivityForResult(openConfigIntent, Common.REQUEST_CODE_CONFIG);
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
            textViewError.setText("");
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
                        madeRequest();
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
        String url = getURL(ipAddress, u);

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
                        errorHandling(Common.ERROR_RESPONSE);
                    }
                });

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
     * @brief GET response handling - chart data series updated with IoT server data.
     */
    private void responseHandling(String response) {


        if (requestTimer != null) {
            // get time stamp with SystemClock
            long requestTimerCurrentTime = SystemClock.uptimeMillis(); // current time
            requestTimerTimeStamp += getValidTimeStampIncrease(requestTimerCurrentTime);

            // get raw data from JSON response
            MeasurementModel m = new MeasurementModel("-", 0, "-", "-");
            m = responseHandling.getRawDataFromResponseToDynamicTable(response);

            // update chart
            if (isNaN(m.mValue)) {
                errorHandling(Common.ERROR_NAN_DATA);

            } else {
                // update plot series
                double timeStamp = requestTimerTimeStamp / 1000.0; // [sec]
                boolean scrollGraph = (timeStamp > dataGraphMaxX);
                if (m.mName.equals("roll")) {
                    dataSeriesRoll.appendData(new DataPoint(timeStamp, m.mValue), scrollGraph, dataGraphMaxDataPointsNumber);
                    dataSeriesRoll.setTitle("Roll [deg]");
                    dataSeriesRoll.setColor(Color.YELLOW);
                }

                if (m.mName.equals("pitch")) {
                    dataSeriesPitch.appendData(new DataPoint(timeStamp, m.mValue), scrollGraph, dataGraphMaxDataPointsNumber);
                    dataSeriesPitch.setTitle("Pitch [deg]");
                    dataSeriesPitch.setColor(Color.GRAY);
                }

                if (m.mName.equals("yaw")) {
                    dataSeriesYaw.appendData(new DataPoint(timeStamp, m.mValue), scrollGraph, dataGraphMaxDataPointsNumber);
                    dataSeriesYaw.setTitle("Yaw [deg]");
                    dataSeriesYaw.setColor(Color.MAGENTA);

                }


                // refresh chart
                // refresh chart
                dataGraphRoll.onDataChanged(true, true);

                dataGraphRoll.getLegendRenderer().setVisible(true);
                dataGraphRoll.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                dataGraphRoll.getLegendRenderer().setTextSize(30);

                dataGraphRoll.getGridLabelRenderer().setTextSize(20);
                dataGraphRoll.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Roll [deg]");
                dataGraphRoll.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Time [s]");
                dataGraphRoll.getGridLabelRenderer().setNumHorizontalLabels(9);
                dataGraphRoll.getGridLabelRenderer().setNumVerticalLabels(7);
                dataGraphRoll.getGridLabelRenderer().setPadding(35);

                dataGraphPitch.onDataChanged(true, true);

                dataGraphPitch.getLegendRenderer().setVisible(true);
                dataGraphPitch.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                dataGraphPitch.getLegendRenderer().setTextSize(30);

                dataGraphPitch.getGridLabelRenderer().setTextSize(20);
                dataGraphPitch.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Pitch [deg]");
                dataGraphPitch.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Time [s]");
                dataGraphPitch.getGridLabelRenderer().setNumHorizontalLabels(9);
                dataGraphPitch.getGridLabelRenderer().setNumVerticalLabels(7);
                dataGraphPitch.getGridLabelRenderer().setPadding(35);

                dataGraphYaw.onDataChanged(true, true);

                dataGraphYaw.getLegendRenderer().setVisible(true);
                dataGraphYaw.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                dataGraphYaw.getLegendRenderer().setTextSize(30);

                dataGraphYaw.getGridLabelRenderer().setTextSize(20);
                dataGraphYaw.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Yaw [deg]");
                dataGraphYaw.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Time [s]");
                dataGraphYaw.getGridLabelRenderer().setNumHorizontalLabels(9);
                dataGraphYaw.getGridLabelRenderer().setNumVerticalLabels(7);
                dataGraphYaw.getGridLabelRenderer().setPadding(35);
            }

            // remember previous time stamp
            requestTimerPreviousTime = requestTimerCurrentTime;
        }

    }
}
