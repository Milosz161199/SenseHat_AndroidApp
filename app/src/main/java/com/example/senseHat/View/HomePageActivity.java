package com.example.senseHat.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.senseHat.Model.Common;
import com.example.senseHat.R;

public class HomePageActivity extends AppCompatActivity {

    private Button imageButtonGoToTable;
    private Button imageButtonGoToConfig;
    private Button imageButtonGoToGraphAngle;
    private Button imageButtonGoToGraphEnv;
    private Button imageButtonGoToGraphJoyStick;
    private Button imageButtonGoToLed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initMenuBar();
        menuBarButtons();


    }

    /**
     * @brief Called when the user taps the 'Config' button.
     * */
    private void openConfig() {
        Intent openConfigIntent = new Intent(this, ConfigActivity.class);
        Bundle configBundle = new Bundle();
        //configBundle.putString(Common.CONFIG_IP_ADDRESS, ipAddress);
        //configBundle.putString(Common.CONFIG_IP_SOCKET, ipSocket);
        //configBundle.putInt(Common.CONFIG_SAMPLE_TIME, sampleTime);
        openConfigIntent.putExtras(configBundle);
        startActivityForResult(openConfigIntent, Common.REQUEST_CODE_CONFIG);
    }

    private void initMenuBar(){
        imageButtonGoToTable = (Button) findViewById(R.id.btnGoToTable);
        imageButtonGoToConfig = (Button) findViewById(R.id.btnGoToConfig);
        imageButtonGoToGraphAngle = (Button) findViewById(R.id.btnGoToGraphAngle);
        imageButtonGoToGraphEnv = (Button) findViewById(R.id.btnGoToGraphEnv);
        imageButtonGoToGraphJoyStick = (Button) findViewById(R.id.btnGoToGraphJoyStick);
        imageButtonGoToLed = (Button) findViewById(R.id.btnGoToLed);
    }

    private void menuBarButtons() {
        imageButtonGoToConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfig();
            }
        });

        imageButtonGoToTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, DynamicTableActivity.class));
            }
        });

        imageButtonGoToLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, LedActivity.class));
            }
        });

        imageButtonGoToGraphAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, GraphActivityAngle.class));
            }
        });

        imageButtonGoToGraphEnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, GraphActivityEnv.class));
            }
        });

        imageButtonGoToGraphJoyStick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, GraphActivityJoyStick.class));
            }
        });

    }

}
