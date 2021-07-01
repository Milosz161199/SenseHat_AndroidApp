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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.senseHat.Model.Common;
import com.example.senseHat.R;

public class HomePageActivity extends AppCompatActivity {

    /* BEGIN config data */
    public static String ipAddress = Common.DEFAULT_IP_ADDRESS;
    public static int sampleTime = Common.DEFAULT_SAMPLE_TIME;
    public static int maxNumberOfSamples = Common.DEFAULT_MAX_NUMBER_OF_SAMPLES;
    public static double apiVersion = Common.DEFAULT_API_VERSION;
    /* END config data */

    private Button btnGoToTable;
    private Button btnGoToConfig;
    private Button btnGoToGraphAngle;
    private Button btnGoToGraphEnv;
    private Button btnGoToGraphJoyStick;
    private Button btnGoToLed;

    private TextView textViewAddressIP;
    private TextView textViewSampleTime;
    private TextView textViewMaxNumberOfSamples;
    private TextView textViewApiVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initMenuBar();
        initView();
        menuBarButtons();
        RefreshMenuData();

    }

    /**
     * @brief Called when the user taps the 'Config' button.
     * */
    private void openConfig() {
        Intent openConfigIntent = new Intent(this, ConfigActivity.class);
        Bundle configBundle = new Bundle();
        configBundle.putString(Common.CONFIG_IP_ADDRESS, ipAddress);
        configBundle.putInt(Common.CONFIG_SAMPLE_TIME, sampleTime);
        configBundle.putInt(Common.CONFIG_MAX_NUMBER_OF_SAMPLES, maxNumberOfSamples);
        configBundle.putDouble(Common.CONFIG_API_VERSION, apiVersion);
        openConfigIntent.putExtras(configBundle);
        startActivityForResult(openConfigIntent, Common.REQUEST_CODE_CONFIG);
    }

    private void RefreshMenuData() {
        textViewAddressIP.setText(ipAddress);
        textViewSampleTime.setText(String.valueOf(sampleTime));
        textViewMaxNumberOfSamples.setText(String.valueOf(maxNumberOfSamples));
        textViewApiVersion.setText(String.valueOf(apiVersion));
    }

    private void initView() {
        textViewAddressIP = (TextView) findViewById(R.id.textViewIpAddressHome);
        textViewSampleTime = (TextView) findViewById(R.id.textViewSampleTimeHome);
        textViewMaxNumberOfSamples = (TextView) findViewById(R.id.textViewMaxNumOfSamplesHome);
        textViewApiVersion = (TextView) findViewById(R.id.textViewApiVersionHome);
    }

    private void initMenuBar() {
        btnGoToTable = (Button) findViewById(R.id.btnGoToTable);
        btnGoToConfig = (Button) findViewById(R.id.btnGoToConfig);
        btnGoToGraphAngle = (Button) findViewById(R.id.btnGoToGraphAngle);
        btnGoToGraphEnv = (Button) findViewById(R.id.btnGoToGraphEnv);
        btnGoToGraphJoyStick = (Button) findViewById(R.id.btnGoToGraphJoyStick);
        btnGoToLed = (Button) findViewById(R.id.btnGoToLed);
    }

    private void menuBarButtons() {
        btnGoToConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfig();
                //RefreshMenuData();
            }
        });

        btnGoToTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, DynamicTableActivity.class));
            }
        });

        btnGoToLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, LedActivity.class));
            }
        });

        btnGoToGraphAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, GraphActivityAngle.class));
            }
        });

        btnGoToGraphEnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, GraphActivityEnv.class));
            }
        });

        btnGoToGraphJoyStick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, GraphActivityJoyStick.class));
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

            // Sample time (ms)
            String sampleTimeText = dataIntent.getStringExtra(Common.CONFIG_SAMPLE_TIME);
            textViewSampleTime.setText(sampleTimeText);
            sampleTime = Integer.parseInt(sampleTimeText);

            // Max number of samples
            String maxSampleTimeText = dataIntent.getStringExtra(Common.CONFIG_MAX_NUMBER_OF_SAMPLES);
            textViewMaxNumberOfSamples.setText(maxSampleTimeText);
            maxNumberOfSamples = Integer.parseInt(maxSampleTimeText);

            // Api version
            String apiText = dataIntent.getStringExtra(Common.CONFIG_API_VERSION);
            textViewApiVersion.setText(apiText);
            apiVersion = Double.parseDouble(apiText);
        }
    }


}
