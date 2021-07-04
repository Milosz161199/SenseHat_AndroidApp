/**
 * *****************************************************************************
 *
 * @file Sense Hat/GraphActivityJoyStick.java
 * @author Milosz Plutowski
 * @version V1.0
 * @date 15-06-2021
 * @brief Sense Hat: Joy-stick activity with data chart
 * *****************************************************************************
 */

package com.example.senseHat.View;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.senseHat.Model.JoyStickModel;
import com.example.senseHat.Model.TestableClass;
import com.example.senseHat.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Double.isNaN;

public class GraphActivityJoyStick extends AppCompatActivity {

    /* BEGIN config data */
    private String ipAddress = HomePageActivity.ipAddress;
    private int sampleTime = HomePageActivity.sampleTime;
    /* END config data */

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

    /* Joy-stick Model */
    private JoyStickModel joyStickModel = new JoyStickModel();

    /* BEGIN Buttons */
    private Button btnRefreshCounters;
    private Button btnStartCounters;
    private Button btnStopCounters;
    private TextView textViewCounterMiddle;
    /* END Buttons */

    /* BEGIN GRAPH PARAMS */
    private GraphView dataGraphJoy;
    private PointsGraphSeries<DataPoint> dataSeriesPoint;
    private final int dataGraphMaxX = 100;
    private final int dataGraphMinX = -100;
    private final int dataGraphMaxY = 100;
    private final int dataGraphMinY = -100;
    /* END GRAPH PARAMS */

    private String text;
    private boolean modeOfRequest = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_joystick);

        // Initialize Volley request queue
        queue = Volley.newRequestQueue(GraphActivityJoyStick.this);

        // Initialize View
        initView();

        // Initialize Graph View
        InitGraphView();

        /* BEGIN BUTTONS ONCLICK METHOD */
        btnRefreshCounters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGetRequest();
            }
        });
        btnStartCounters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeOfRequest = false;
                startRequestTimer();
            }
        });
        btnStopCounters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeOfRequest = true;
                stopRequestTimerTask();
            }
        });
        /* END BUTTONS ONCLICK METHOD */
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
                        sendGetRequest();
                    }
                });
            }
        };
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
     * @param ip IP address (string)
     * @brief Create JSON file URL from IoT server IP.
     * @retval GET request URL
     */
    private String getURL(String ip) {
        return ("http://" + ip + Common.PHP_COMMAND_TAKE_DATA_JOY_STICK);
    }

    /**
     * @brief Sending GET request to IoT server using 'Volley'.
     */
    private void sendGetRequest() {
        // Instantiate the RequestQueue with Volley
        // https://javadoc.io/doc/com.android.volley/volley/1.1.0-rc2/index.html
        String url = getURL(ipAddress);

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (modeOfRequest) {
                            responseHandling(response);
                        } else {
                            responseHandlingWithTimer(response);
                        }
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
     * @param errorCode local error codes, see: COMMON
     * @brief Handles application errors. Logs an error and passes error code to GUI.
     */
    private void errorHandling(int errorCode) {
        switch (errorCode) {
            case Common.ERROR_TIME_STAMP:
                //textViewError.setText("ERR #1");
                Log.d("errorHandling", "Request time stamp error.");
                break;
            case Common.ERROR_NAN_DATA:
                //textViewError.setText("ERR #2");
                Log.d("errorHandling", "Invalid JSON data.");
                break;
            case Common.ERROR_RESPONSE:
                //textViewError.setText("ERR #3");
                Log.d("errorHandling", "GET request VolleyError.");
                break;
            default:
                //textViewError.setText("ERR ??");
                Log.d("errorHandling", "Unknown error.");
                break;
        }
    }

    /**
     * @brief GET response handling - chart data series updated with IoT server data.
     */
    private void responseHandling(String response) {
        // get raw data from JSON response
        joyStickModel = responseHandling.getRawDataFromResponseToJoyStick(response);

        // update chart
        if (isNaN(joyStickModel.getCounterX()) || isNaN(joyStickModel.getCounterY()) || isNaN(joyStickModel.getCounterMiddle())) {
            errorHandling(Common.ERROR_NAN_DATA);

        } else {

            // update plot series
            dataSeriesPoint.resetData(new DataPoint[]{new DataPoint(joyStickModel.getCounterX(), joyStickModel.getCounterY())});

            text = "(" + String.valueOf(joyStickModel.getCounterX()) + ", " + String.valueOf(joyStickModel.getCounterY()) + ")";
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            dataSeriesPoint.setTitle("Joy-stick");
            dataSeriesPoint.setColor(Color.MAGENTA);
            dataSeriesPoint.setShape(PointsGraphSeries.Shape.RECTANGLE);

            textViewCounterMiddle.setText(String.valueOf(joyStickModel.getCounterMiddle()));

            // refresh chart
            dataGraphJoy.onDataChanged(true, true);

            dataGraphJoy.getLegendRenderer().setVisible(true);
            dataGraphJoy.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
            dataGraphJoy.getLegendRenderer().setTextSize(30);

            dataGraphJoy.getGridLabelRenderer().setTextSize(20);
            dataGraphJoy.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Counter_Y [-]");
            dataGraphJoy.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Counter_X [-]");
            dataGraphJoy.getGridLabelRenderer().setNumHorizontalLabels(9);
            dataGraphJoy.getGridLabelRenderer().setNumVerticalLabels(7);
            dataGraphJoy.getGridLabelRenderer().setPadding(35);
        }
    }

    /**
     * @brief GET response handling - chart data series updated with IoT server data (using timer).
     */
    private void responseHandlingWithTimer(String response) {
        if (requestTimer != null) {
            // get time stamp with SystemClock
            long requestTimerCurrentTime = SystemClock.uptimeMillis(); // current time
            requestTimerTimeStamp += getValidTimeStampIncrease(requestTimerCurrentTime);

            // get raw data from JSON response
            joyStickModel = responseHandling.getRawDataFromResponseToJoyStick(response);

            // update chart
            if (isNaN(joyStickModel.getCounterX()) || isNaN(joyStickModel.getCounterY()) || isNaN(joyStickModel.getCounterMiddle())) {
                errorHandling(Common.ERROR_NAN_DATA);

            } else {
                dataSeriesPoint.resetData(new DataPoint[]{new DataPoint(joyStickModel.getCounterX(), joyStickModel.getCounterY())});

                text = "(" + String.valueOf(joyStickModel.getCounterX()) + ", " + String.valueOf(joyStickModel.getCounterY()) + ")";
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                dataSeriesPoint.setTitle("Joy-stick");
                dataSeriesPoint.setColor(Color.MAGENTA);
                dataSeriesPoint.setShape(PointsGraphSeries.Shape.RECTANGLE);

                textViewCounterMiddle.setText(String.valueOf(joyStickModel.getCounterMiddle()));

                // refresh chart
                dataGraphJoy.onDataChanged(true, true);

                dataGraphJoy.getLegendRenderer().setVisible(true);
                dataGraphJoy.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                dataGraphJoy.getLegendRenderer().setTextSize(30);

                dataGraphJoy.getGridLabelRenderer().setTextSize(20);
                dataGraphJoy.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Counter_Y [-]");
                dataGraphJoy.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Counter_X [-]");
                dataGraphJoy.getGridLabelRenderer().setNumHorizontalLabels(9);
                dataGraphJoy.getGridLabelRenderer().setNumVerticalLabels(7);
                dataGraphJoy.getGridLabelRenderer().setPadding(35);
            }

            // remember previous time stamp
            requestTimerPreviousTime = requestTimerCurrentTime;
        }
    }

    /**
     * @param n Number of spaces.
     * @retval String with 'n' spaces.
     */
    private String Space(int n) {
        return new String(new char[n]).replace('\0', ' ');
    }

    /**
     * @brief Init widgets.
     */
    public void initView() {
        dataGraphJoy = (GraphView) findViewById(R.id.dataGraphJoy);
        btnRefreshCounters = (Button) findViewById(R.id.btnRefreshCounters);
        btnStartCounters = (Button) findViewById(R.id.btnStartCounters);
        btnStopCounters = (Button) findViewById(R.id.btnStopCounters);
        textViewCounterMiddle = (TextView) findViewById(R.id.textViewCounterMiddle);
    }

    /**
     * @brief Init graph view.
     */
    private void InitGraphView() {
        dataSeriesPoint = new PointsGraphSeries<>(new DataPoint[]{});

        dataGraphJoy.addSeries(dataSeriesPoint);

        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        dataSeriesPoint.setTitle("Joy-stick");
        dataSeriesPoint.setColor(Color.MAGENTA);
        dataSeriesPoint.setShape(PointsGraphSeries.Shape.RECTANGLE);

        dataGraphJoy.getViewport().setScalable(false);
        dataGraphJoy.getViewport().setScrollable(false);

        dataGraphJoy.getViewport().setXAxisBoundsManual(true);
        dataGraphJoy.getViewport().setYAxisBoundsManual(true);
        dataGraphJoy.getViewport().setMinX(dataGraphMinX);
        dataGraphJoy.getViewport().setMaxX(dataGraphMaxX);
        dataGraphJoy.getViewport().setMinY(dataGraphMinY);
        dataGraphJoy.getViewport().setMaxY(dataGraphMaxY);

        // refresh chart
        dataGraphJoy.onDataChanged(true, true);

        dataGraphJoy.getLegendRenderer().setVisible(true);
        dataGraphJoy.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        dataGraphJoy.getLegendRenderer().setTextSize(30);
        dataGraphJoy.getGridLabelRenderer().setTextSize(20);
        dataGraphJoy.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Counter_Y [-]");
        dataGraphJoy.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Counter_X [-]");
        dataGraphJoy.getGridLabelRenderer().setNumHorizontalLabels(9);
        dataGraphJoy.getGridLabelRenderer().setNumVerticalLabels(7);
        dataGraphJoy.getGridLabelRenderer().setPadding(35);
    }
}
