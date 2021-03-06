/**
 * *****************************************************************************
 *
 * @file Sense Hat/HomePageActivity.java
 * @author Milosz Plutowski
 * @version V1.0
 * @date 15-06-2021
 * @brief Sense Hat: main page activity with data and main menu
 * *****************************************************************************
 */

package com.example.senseHat.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.senseHat.Model.Common;
import com.example.senseHat.Model.ConfigModel;
import com.example.senseHat.Model.TestableClass;
import com.example.senseHat.R;

import static java.lang.Double.isNaN;

public class HomePageView extends AppCompatActivity {

    /* BEGIN config data */
    public static String ipAddress = Common.DEFAULT_IP_ADDRESS;
    public static int sampleTime = Common.DEFAULT_SAMPLE_TIME;
    public static int maxNumberOfSamples = Common.DEFAULT_MAX_NUMBER_OF_SAMPLES;
    public static double apiVersion = Common.DEFAULT_API_VERSION;
    /* END config data */

    /* BEGIN Buttons */
    private Button btnGoToTable;
    private Button btnGoToConfig;
    private Button btnGoToGraphAngle;
    private Button btnGoToGraphEnv;
    private Button btnGoToGraphJoyStick;
    private Button btnGoToLed;
    /* END Buttons */

    /* BEGIN TextViews */
    private TextView textViewAddressIP;
    private TextView textViewSampleTime;
    private TextView textViewMaxNumberOfSamples;
    private TextView textViewApiVersion;
    /* END TextViews */

    private boolean getConfigParams = true;
    private RequestQueue queue;

    /* Testable module */
    private TestableClass responseHandling = new TestableClass();

    /* Config module */
    private ConfigModel configModel = new ConfigModel(ipAddress, sampleTime, maxNumberOfSamples, apiVersion);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Initialize Volley request queue
        queue = Volley.newRequestQueue(HomePageView.this);

        initMenuBar();
        initView();
        menuBarButtons();
        RefreshMenuData();

        if (getConfigParams) {
            sendGetRequest();
            getConfigParams = false;
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
        configBundle.putInt(Common.CONFIG_MAX_NUMBER_OF_SAMPLES, maxNumberOfSamples);
        configBundle.putDouble(Common.CONFIG_API_VERSION, apiVersion);
        openConfigIntent.putExtras(configBundle);
        startActivityForResult(openConfigIntent, Common.REQUEST_CODE_CONFIG);
    }

    /**
     * @brief Refresh params.
     */
    private void RefreshMenuData() {
        textViewAddressIP.setText(ipAddress);
        textViewSampleTime.setText(String.valueOf(sampleTime));
        textViewMaxNumberOfSamples.setText(String.valueOf(maxNumberOfSamples));
        textViewApiVersion.setText(String.valueOf(apiVersion));
    }

    /**
     * @brief Init basics view.
     */
    private void initView() {
        textViewAddressIP = (TextView) findViewById(R.id.textViewIpAddressHome);
        textViewSampleTime = (TextView) findViewById(R.id.textViewSampleTimeHome);
        textViewMaxNumberOfSamples = (TextView) findViewById(R.id.textViewMaxNumOfSamplesHome);
        textViewApiVersion = (TextView) findViewById(R.id.textViewApiVersionHome);
    }

    /**
     * @brief Init basics view - menu bar buttons.
     */
    private void initMenuBar() {
        btnGoToTable = (Button) findViewById(R.id.btnGoToTable);
        btnGoToConfig = (Button) findViewById(R.id.btnGoToConfig);
        btnGoToGraphAngle = (Button) findViewById(R.id.btnGoToGraphAngle);
        btnGoToGraphEnv = (Button) findViewById(R.id.btnGoToGraphEnv);
        btnGoToGraphJoyStick = (Button) findViewById(R.id.btnGoToGraphJoyStick);
        btnGoToLed = (Button) findViewById(R.id.btnGoToLed);
    }

    /**
     * @brief Called when the user taps the one of buttons in menu bar.
     */
    private void menuBarButtons() {
        btnGoToConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfig();
            }
        });

        btnGoToTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageView.this, DynamicTableView.class));
            }
        });

        btnGoToLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageView.this, LedView.class));
            }
        });

        btnGoToGraphAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageView.this, GraphAngleView.class));
            }
        });

        btnGoToGraphEnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageView.this, GraphEnvView.class));
            }
        });

        btnGoToGraphJoyStick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageView.this, GraphJoyStickView.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        if ((requestCode == Common.REQUEST_CODE_CONFIG) && (resultCode == RESULT_OK)) {

            // server IP address
            ipAddress = dataIntent.getStringExtra(Common.CONFIG_IP_ADDRESS);
            textViewAddressIP.setText(ipAddress);
            Common.DEFAULT_IP_ADDRESS = ipAddress;

            // Sample time (ms)
            String sampleTimeText = dataIntent.getStringExtra(Common.CONFIG_SAMPLE_TIME);
            textViewSampleTime.setText(sampleTimeText);
            sampleTime = Integer.parseInt(sampleTimeText);
            Common.DEFAULT_SAMPLE_TIME = sampleTime;

            // Max number of samples
            String maxSampleTimeText = dataIntent.getStringExtra(Common.CONFIG_MAX_NUMBER_OF_SAMPLES);
            textViewMaxNumberOfSamples.setText(maxSampleTimeText);
            maxNumberOfSamples = Integer.parseInt(maxSampleTimeText);
            Common.DEFAULT_MAX_NUMBER_OF_SAMPLES = maxNumberOfSamples;

            // Api version
            String apiText = dataIntent.getStringExtra(Common.CONFIG_API_VERSION);
            textViewApiVersion.setText(apiText);
            apiVersion = Double.parseDouble(apiText);
            Common.DEFAULT_API_VERSION = apiVersion;
        }
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
        configModel = responseHandling.getRawDataFromResponseToConfig(response);

        // update chart
        if (isNaN(configModel.mSampleTimeMs)) {
            errorHandling(Common.ERROR_NAN_DATA);

        } else {
            //ipAddress = configModel.mIpAddress;
            sampleTime = configModel.mSampleTimeMs;
            maxNumberOfSamples = configModel.mMaxNumOfSamples;
            apiVersion = configModel.mApiVersion;
            RefreshMenuData();
        }
    }

    /**
     * @param ip IP address (string)
     * @brief Create JSON file URL from IoT server IP.
     * @retval GET request URL
     */
    private String getURL(String ip) {
        return ("http://" + ip + Common.PHP_COMMAND_SEND_DATA_TO_CONFIG);
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
                        responseHandling(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //errorHandling(Common.ERROR_RESPONSE);
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
