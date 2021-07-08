/**
 * *****************************************************************************
 *
 * @file Sense Hat/GraphActivityEnv.java
 * @author Milosz Plutowski
 * @version V1.0
 * @date 15-06-2021
 * @brief Sense Hat: Environmental measurements activity with data charts
 * *****************************************************************************
 */

package com.example.senseHat.View;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Double.isNaN;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class GraphEnvView extends AppCompatActivity {

    /* BEGIN config data */
    private String ipAddress = Common.DEFAULT_IP_ADDRESS;
    private int sampleTime = Common.DEFAULT_SAMPLE_TIME;
    private int dataGraphMaxDataPointsNumber = Common.DEFAULT_MAX_NUMBER_OF_SAMPLES;
    /* END config data */

    /* BEGIN widgets */
    private Switch swTemperature;
    private Switch swHumidity;
    private Switch swPressure;

    /* BEGIN booleans */
    private boolean tempBoolean = true;
    private boolean humBoolean = true;
    private boolean presBoolean = true;
    /* END booleans */

    private TextView textViewIP;
    private TextView textViewSampleTime;
    private TextView textViewError;
    private GraphView dataGraphTemp;
    private GraphView dataGraphHum;
    private GraphView dataGraphPres;
    private LineGraphSeries<DataPoint> dataSeriesTemperature;
    private LineGraphSeries<DataPoint> dataSeriesHumidity;
    private LineGraphSeries<DataPoint> dataSeriesPressure;

    private final double dataGraphMaxX = 10.0d;
    private final double dataGraphMinX = 0.0d;
    private final double dataGraphMaxY = 100.0d;
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

    public GraphEnvView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_env);

        /* BEGIN initialize widgets */
        /* BEGIN initialize Switches */
        swTemperature = findViewById(R.id.swTemperature);
        swHumidity = findViewById(R.id.swHumidity);
        swPressure = findViewById(R.id.swPressure);


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
        configAlterDialog = new AlertDialog.Builder(GraphEnvView.this);
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
        queue = Volley.newRequestQueue(GraphEnvView.this);


        /* BEGIN SWITCH METHOD */
        swTemperature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "+Temperature...", Toast.LENGTH_SHORT).show();
                    tempBoolean = true;
                } else {
                    Toast.makeText(getApplicationContext(), "-Temperature...", Toast.LENGTH_SHORT).show();
                    tempBoolean = false;
                }
            }
        });

        swHumidity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "+Humidity...", Toast.LENGTH_SHORT).show();
                    humBoolean = true;
                } else {
                    Toast.makeText(getApplicationContext(), "-Humidity...", Toast.LENGTH_SHORT).show();
                    humBoolean = false;
                }
            }
        });

        swPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "+Pressure...", Toast.LENGTH_SHORT).show();
                    presBoolean = true;
                } else {
                    Toast.makeText(getApplicationContext(), "-Pressure...", Toast.LENGTH_SHORT).show();
                    presBoolean = false;
                }
            }
        });
        /* END SWITCH METHOD */
    }


    /**
     * @brief Init GraphViews.
     */
    private void InitGraphView() {
        // https://github.com/jjoe64/GraphView/wiki
        dataGraphTemp = (GraphView) findViewById(R.id.dataGraphTemp);
        dataGraphHum = (GraphView) findViewById(R.id.dataGraphHum);
        dataGraphPres = (GraphView) findViewById(R.id.dataGraphPres);

        dataSeriesTemperature = new LineGraphSeries<>(new DataPoint[]{});
        dataSeriesHumidity = new LineGraphSeries<>(new DataPoint[]{});
        dataSeriesPressure = new LineGraphSeries<>(new DataPoint[]{});


        // GRAPH TEMPERATURE
        dataGraphTemp.addSeries(dataSeriesTemperature);
        dataGraphHum.addSeries(dataSeriesHumidity);
        dataGraphPres.addSeries(dataSeriesPressure);

        dataGraphTemp.getViewport().setXAxisBoundsManual(true);
        dataGraphTemp.getViewport().setYAxisBoundsManual(true);
        dataGraphTemp.getViewport().setMinX(0);
        dataGraphTemp.getViewport().setMaxX(10);
        dataGraphTemp.getViewport().setMinY(20);
        dataGraphTemp.getViewport().setMaxY(40);

        dataGraphTemp.getViewport().setScalable(true);
        dataGraphTemp.getViewport().setScrollable(true);

        // GRAPH HUMIDITY
        dataGraphHum.getViewport().setXAxisBoundsManual(true);
        dataGraphHum.getViewport().setYAxisBoundsManual(true);
        dataGraphHum.getViewport().setMinX(0);
        dataGraphHum.getViewport().setMaxX(10);
        dataGraphHum.getViewport().setMinY(0);
        dataGraphHum.getViewport().setMaxY(100);

        dataGraphHum.getViewport().setScalable(true);
        dataGraphHum.getViewport().setScrollable(true);

        // GRAPH PRESSURE
        dataGraphPres.getViewport().setXAxisBoundsManual(true);
        dataGraphPres.getViewport().setYAxisBoundsManual(true);
        dataGraphPres.getViewport().setMinX(0);
        dataGraphPres.getViewport().setMaxX(10);
        dataGraphPres.getViewport().setMinY(980);
        dataGraphPres.getViewport().setMaxY(1030);

        dataGraphPres.getViewport().setScalable(true);
        dataGraphPres.getViewport().setScrollable(true);

        // refresh chart
        dataGraphTemp.onDataChanged(true, true);

        dataGraphTemp.getLegendRenderer().setVisible(true);
        dataGraphTemp.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        dataGraphTemp.getLegendRenderer().setTextSize(20);

        dataGraphTemp.getGridLabelRenderer().setTextSize(14);
        dataGraphTemp.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Temperature [C]");
        dataGraphTemp.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Time [s]");
        dataGraphTemp.getGridLabelRenderer().setNumHorizontalLabels(9);
        dataGraphTemp.getGridLabelRenderer().setNumVerticalLabels(7);
        dataGraphTemp.getGridLabelRenderer().setPadding(35);

        dataGraphHum.onDataChanged(true, true);

        dataGraphHum.getLegendRenderer().setVisible(true);
        dataGraphHum.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        dataGraphHum.getLegendRenderer().setTextSize(20);

        dataGraphHum.getGridLabelRenderer().setTextSize(14);
        dataGraphHum.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Humidity [-]");
        dataGraphHum.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Time [s]");
        dataGraphHum.getGridLabelRenderer().setNumHorizontalLabels(9);
        dataGraphHum.getGridLabelRenderer().setNumVerticalLabels(7);
        dataGraphHum.getGridLabelRenderer().setPadding(35);

        dataGraphPres.onDataChanged(true, true);

        dataGraphPres.getLegendRenderer().setVisible(true);
        dataGraphPres.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        dataGraphPres.getLegendRenderer().setTextSize(20);

        dataGraphPres.getGridLabelRenderer().setTextSize(14);
        dataGraphPres.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Pressure [hPa]");
        dataGraphPres.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Time [s]");
        dataGraphPres.getGridLabelRenderer().setNumHorizontalLabels(9);
        dataGraphPres.getGridLabelRenderer().setNumVerticalLabels(7);
        dataGraphPres.getGridLabelRenderer().setPadding(35);
    }

    /**
     * @brief Request for temperature and humidity.
     */
    private void madeRequestTempHum() {
        sendGetRequest(Common.REQ_TEMP_C_1);
        sendGetRequest(Common.REQ_HUM_P);
    }

    /**
     * @brief Request for pressure and humidity.
     */
    private void madeRequestHumPres() {
        sendGetRequest(Common.REQ_HUM_P);
        sendGetRequest(Common.REQ_PRES_HPA);
    }

    /**
     * @brief Request for temperature and pressure.
     */
    private void madeRequestTempPres() {
        sendGetRequest(Common.REQ_TEMP_C_1);
        sendGetRequest(Common.REQ_PRES_HPA);
    }

    /**
     * @brief Request for temperature, humidity and pressure.
     */
    private void madeRequestTempHumPres() {
        sendGetRequest(Common.REQ_TEMP_C_1);
        sendGetRequest(Common.REQ_PRES_HPA);
        sendGetRequest(Common.REQ_HUM_P);
    }

    /**
     * @brief Creating full request for measurements.
     */
    private void madeRequest() {
        if (tempBoolean && !humBoolean && !presBoolean) {
            sendGetRequest(Common.REQ_TEMP_C_1);
        }
        if (!tempBoolean && humBoolean && !presBoolean) {
            sendGetRequest(Common.REQ_HUM_P);
        }
        if (!tempBoolean && !humBoolean && presBoolean) {
            sendGetRequest(Common.REQ_PRES_HPA);
        }
        if (tempBoolean && humBoolean && !presBoolean) {
            madeRequestTempHum();
        }
        if (!tempBoolean && humBoolean && presBoolean) {
            madeRequestHumPres();
        }
        if (tempBoolean && !humBoolean && presBoolean) {
            madeRequestTempPres();
        }
        if (tempBoolean && humBoolean && presBoolean) {
            madeRequestTempHumPres();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        if ((requestCode == Common.REQUEST_CODE_CONFIG) && (resultCode == RESULT_OK)) {

            // IoT server IP address
            ipAddress = dataIntent.getStringExtra(Common.CONFIG_IP_ADDRESS);
            textViewIP.setText(getIpAddressDisplayText(ipAddress));
            Common.DEFAULT_IP_ADDRESS = ipAddress;

            // Sample time (ms)
            String sampleTimeText = dataIntent.getStringExtra(Common.CONFIG_SAMPLE_TIME);
            sampleTime = Integer.parseInt(sampleTimeText);
            textViewSampleTime.setText(getSampleTimeDisplayText(sampleTimeText));
            Common.DEFAULT_SAMPLE_TIME = sampleTime;

            // Sample time (ms)
            String sampleSText = dataIntent.getStringExtra(Common.CONFIG_MAX_NUMBER_OF_SAMPLES);
            dataGraphMaxDataPointsNumber = Integer.parseInt(sampleSText);
            Common.DEFAULT_MAX_NUMBER_OF_SAMPLES = dataGraphMaxDataPointsNumber;
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
        Intent openConfigIntent = new Intent(this, ConfigView.class);
        Bundle configBundle = new Bundle();
        configBundle.putString(Common.CONFIG_IP_ADDRESS, ipAddress);
        configBundle.putInt(Common.CONFIG_SAMPLE_TIME, sampleTime);
        configBundle.putInt(Common.CONFIG_MAX_NUMBER_OF_SAMPLES, dataGraphMaxDataPointsNumber);
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
                if (m.mName.equals("temperature")) {
                    dataSeriesTemperature.appendData(new DataPoint(timeStamp, m.mValue), scrollGraph, dataGraphMaxDataPointsNumber);
                    dataSeriesTemperature.setTitle("Temperature");
                    dataSeriesTemperature.setColor(Color.BLUE);
                }
                if (m.mName.equals("humidity")) {
                    dataSeriesHumidity.appendData(new DataPoint(timeStamp, m.mValue), scrollGraph, dataGraphMaxDataPointsNumber);
                    dataSeriesHumidity.setTitle("Humidity");
                    dataSeriesHumidity.setColor(Color.GREEN);
                }
                if (m.mName.equals("pressure")) {
                    dataSeriesPressure.appendData(new DataPoint(timeStamp, m.mValue), scrollGraph, dataGraphMaxDataPointsNumber);
                    dataSeriesPressure.setTitle("Pressure");
                    dataSeriesPressure.setColor(Color.RED);
                }

                // refresh chart
                dataGraphTemp.onDataChanged(true, true);

                dataGraphTemp.getLegendRenderer().setVisible(true);
                dataGraphTemp.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                dataGraphTemp.getLegendRenderer().setTextSize(30);

                dataGraphTemp.getGridLabelRenderer().setTextSize(20);
                dataGraphTemp.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Temperature [C]");
                dataGraphTemp.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Time [s]");
                dataGraphTemp.getGridLabelRenderer().setNumHorizontalLabels(9);
                dataGraphTemp.getGridLabelRenderer().setNumVerticalLabels(7);
                dataGraphTemp.getGridLabelRenderer().setPadding(35);

                dataGraphHum.onDataChanged(true, true);

                dataGraphHum.getLegendRenderer().setVisible(true);
                dataGraphHum.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                dataGraphHum.getLegendRenderer().setTextSize(30);

                dataGraphHum.getGridLabelRenderer().setTextSize(20);
                dataGraphHum.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Humidity [%]");
                dataGraphHum.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Time [s]");
                dataGraphHum.getGridLabelRenderer().setNumHorizontalLabels(9);
                dataGraphHum.getGridLabelRenderer().setNumVerticalLabels(7);
                dataGraphHum.getGridLabelRenderer().setPadding(35);

                dataGraphPres.onDataChanged(true, true);

                dataGraphPres.getLegendRenderer().setVisible(true);
                dataGraphPres.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                dataGraphPres.getLegendRenderer().setTextSize(30);

                dataGraphPres.getGridLabelRenderer().setTextSize(20);
                dataGraphPres.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Pressure [hPa]");
                dataGraphPres.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Time [s]");
                dataGraphPres.getGridLabelRenderer().setNumHorizontalLabels(9);
                dataGraphPres.getGridLabelRenderer().setNumVerticalLabels(7);
                dataGraphPres.getGridLabelRenderer().setPadding(35);
            }
            // remember previous time stamp
            requestTimerPreviousTime = requestTimerCurrentTime;
        }
    }
}
