/**
 ******************************************************************************************************************
 * @file    Sense Hat/ConfigActivity.java
 * @author  Milosz Plutowski
 * @version V1.0
 * @date    15-06-2021
 * @brief   Sense Hat: configuration activity with: IP, socket, sample time, max number of samples and api version
 ******************************************************************************************************************
 */

package com.example.senseHat.View;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.senseHat.Model.Common;
import com.example.senseHat.R;

public class ConfigActivity extends AppCompatActivity {


    /* BEGIN config TextViews */
    private EditText ipEditText;
    private EditText socketEditText;
    private EditText sampleTimeEditText;
    private EditText maxNumberOfSamplesTimeEditText;
    private EditText apiVersionEditText;
    /* END config TextViews */


    private Button btnSetNewConfig;
    private Button btnSetDefaultConfig;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);


        // get the Intent that started this Activity
        Intent intent = getIntent();

        // get the Bundle that stores the data of this Activity
        Bundle configBundle = intent.getExtras();

        if(configBundle != null) {
            ipEditText = (EditText) findViewById(R.id.ipEditTextConfig);
            String ip = configBundle.getString(Common.CONFIG_IP_ADDRESS, Common.DEFAULT_IP_ADDRESS);
            ipEditText.setText(ip);

            socketEditText = (EditText) findViewById(R.id.socketEditTextConfig);
            int soc = configBundle.getInt(Common.CONFIG_SOCKET, Common.DEFAULT_SOCKET);
            socketEditText.setText(Integer.toString(soc));

            sampleTimeEditText = (EditText) findViewById(R.id.sampleTimeEditTextConfig);
            int st = configBundle.getInt(Common.CONFIG_SAMPLE_TIME, Common.DEFAULT_SAMPLE_TIME);
            sampleTimeEditText.setText(Integer.toString(st));

            maxNumberOfSamplesTimeEditText = (EditText) findViewById(R.id.maxNumOfSamplesEditTextConfig);
            int maxSamples = configBundle.getInt(Common.CONFIG_MAX_NUMBER_OF_SAMPLES, Common.DEFAULT_MAX_NUMBER_OF_SAMPLES);
            maxNumberOfSamplesTimeEditText.setText(Integer.toString(maxSamples));

            apiVersionEditText = (EditText) findViewById(R.id.apiVersionEditTextConfig);
            double api = configBundle.getDouble(Common.CONFIG_API_VERSION, Common.DEFAULT_API_VERSION);
            apiVersionEditText.setText(Double.toString(api));
        }


        btnSetNewConfig = (Button) findViewById(R.id.btnSetConfig);
        btnSetDefaultConfig = (Button) findViewById(R.id.btnSetDefaultConfig);

        btnSetNewConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSetNewConfig.setBackgroundColor(Color.rgb(0,255,0));
                Intent intent = new Intent();
                intent.putExtra(Common.CONFIG_IP_ADDRESS, ipEditText.getText().toString());
                intent.putExtra(Common.CONFIG_SOCKET, socketEditText.getText().toString());
                intent.putExtra(Common.CONFIG_SAMPLE_TIME, sampleTimeEditText.getText().toString());
                intent.putExtra(Common.CONFIG_MAX_NUMBER_OF_SAMPLES, maxNumberOfSamplesTimeEditText.getText().toString());
                intent.putExtra(Common.CONFIG_API_VERSION, apiVersionEditText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnSetDefaultConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSetDefaultConfig.setBackgroundColor(Color.rgb(0,255,0));
                Intent intent = new Intent();
                intent.putExtra(Common.CONFIG_IP_ADDRESS, Common.DEFAULT_IP_ADDRESS);
                intent.putExtra(Common.CONFIG_SOCKET, String.valueOf(Common.DEFAULT_SOCKET));
                intent.putExtra(Common.CONFIG_SAMPLE_TIME, String.valueOf(Common.DEFAULT_SAMPLE_TIME));
                intent.putExtra(Common.CONFIG_MAX_NUMBER_OF_SAMPLES, String.valueOf(Common.DEFAULT_MAX_NUMBER_OF_SAMPLES));
                intent.putExtra(Common.CONFIG_API_VERSION, String.valueOf(Common.DEFAULT_API_VERSION));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Common.CONFIG_IP_ADDRESS, ipEditText.getText().toString());
        intent.putExtra(Common.CONFIG_SOCKET, socketEditText.getText().toString());
        intent.putExtra(Common.CONFIG_SAMPLE_TIME, sampleTimeEditText.getText().toString());
        intent.putExtra(Common.CONFIG_MAX_NUMBER_OF_SAMPLES, maxNumberOfSamplesTimeEditText.getText().toString());
        intent.putExtra(Common.CONFIG_API_VERSION, apiVersionEditText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

}