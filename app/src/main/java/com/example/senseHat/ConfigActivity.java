/**
 ******************************************************************************
 * @file    Data Grabber Example/ConfigActivity.java
 * @author  Adrian Wojcik
 * @version V1.0
 * @date    09-Apr-2020
 * @brief   Data grabber example: configuration activity with IP and sample time
 ******************************************************************************
 */

package com.example.senseHat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class ConfigActivity extends AppCompatActivity {

    private ImageButton btnGoToConfig;
    private ImageButton btnGoToGraphEnv;
    private ImageButton btnGoToGraphAngle;
    private ImageButton btnGoToLed;
    private ImageButton btnGoToTable;

    /* BEGIN config TextViews */
    EditText ipEditText;
    EditText sampleTimeEditText;
    /* END config TextViews */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        // Init Buttons
        initMenuButtons();

        // get the Intent that started this Activity
        Intent intent = getIntent();

        // get the Bundle that stores the data of this Activity
        Bundle configBundle = intent.getExtras();

        if(configBundle != null) {
            ipEditText = findViewById(R.id.ipEditTextConfig);
            String ip = configBundle.getString(Common.CONFIG_IP_ADDRESS, Common.DEFAULT_IP_ADDRESS);
            ipEditText.setText(ip);

            sampleTimeEditText = findViewById(R.id.sampleTimeEditTextConfig);
            int st = configBundle.getInt(Common.CONFIG_SAMPLE_TIME, Common.DEFAULT_SAMPLE_TIME);
            sampleTimeEditText.setText(Integer.toString(st));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Common.CONFIG_IP_ADDRESS, ipEditText.getText().toString());
        intent.putExtra(Common.CONFIG_SAMPLE_TIME, sampleTimeEditText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
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

    }

    private void openGraphEnvView(){
        Intent intent = new Intent();
        intent.putExtra(Common.CONFIG_IP_ADDRESS, ipEditText.getText().toString());
        intent.putExtra(Common.CONFIG_SAMPLE_TIME, sampleTimeEditText.getText().toString());
        setResult(RESULT_OK, intent);
        Intent openGraphViewIntent = new Intent(this, GraphActivityEnv.class);
        startActivity(openGraphViewIntent);
        finish();
    }

    private void openGraphAngleView(){
        Intent intent = new Intent();
        intent.putExtra(Common.CONFIG_IP_ADDRESS, ipEditText.getText().toString());
        intent.putExtra(Common.CONFIG_SAMPLE_TIME, sampleTimeEditText.getText().toString());
        setResult(RESULT_OK, intent);
        Intent openGraphViewIntent = new Intent(this, GraphActivityAngle.class);
        startActivity(openGraphViewIntent);
        finish();
    }

    private void openLedView(){
        Intent intent = new Intent();
        intent.putExtra(Common.CONFIG_IP_ADDRESS, ipEditText.getText().toString());
        intent.putExtra(Common.CONFIG_SAMPLE_TIME, sampleTimeEditText.getText().toString());
        setResult(RESULT_OK, intent);
        Intent openLedsViewIntent = new Intent(this, LedActivity.class);
        startActivity(openLedsViewIntent);
        finish();
    }

    private void openTableView(){
        Intent intent = new Intent();
        intent.putExtra(Common.CONFIG_IP_ADDRESS, ipEditText.getText().toString());
        intent.putExtra(Common.CONFIG_SAMPLE_TIME, sampleTimeEditText.getText().toString());
        setResult(RESULT_OK, intent);
        Intent openTableViewIntent = new Intent(this, DynamicTableActivity.class);
        startActivity(openTableViewIntent);
        finish();
    }

}