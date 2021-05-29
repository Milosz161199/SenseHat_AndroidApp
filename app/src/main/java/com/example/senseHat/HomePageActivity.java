package com.example.senseHat;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity {

    private ImageButton imageButtonGoToTable;
    private ImageButton imageButtonGoToConfig;
    private ImageButton imageButtonGoToGraphAngle;
    private ImageButton imageButtonGoToGraphEnv;
    private ImageButton imageButtonGoToGraphJoyStick;
    private ImageButton imageButtonGoToLed;



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
        imageButtonGoToTable = (ImageButton) findViewById(R.id.btnGoToTable);
        imageButtonGoToConfig = (ImageButton) findViewById(R.id.btnGoToConfig);
        imageButtonGoToGraphAngle = (ImageButton) findViewById(R.id.btnGoToGraphAngle);
        imageButtonGoToGraphEnv = (ImageButton) findViewById(R.id.btnGoToGraphEnv);
        imageButtonGoToGraphJoyStick = (ImageButton) findViewById(R.id.btnGoToGraphJoyStick);
        imageButtonGoToLed = (ImageButton) findViewById(R.id.btnGoToLed);
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
