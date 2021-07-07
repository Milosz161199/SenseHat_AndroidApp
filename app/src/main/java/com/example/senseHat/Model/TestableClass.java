/**
 * *****************************************************************************
 *
 * @file Sense Hat/TestableClass.java
 * @author Milosz Plutowski
 * @version V1.0
 * @date 15-06-2021
 * @brief Sense Hat: testable class for JSON parsing
 * *****************************************************************************
 */

package com.example.senseHat.Model;

import com.example.senseHat.Model.MeasurementModel;
import com.example.senseHat.View.DynamicTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestableClass {
    /**
     * @brief Reading raw joy-stick data from JSON response.
     * @param response IoT server JSON response as string
     * @retval new JoyStickModel data
     */
    public JoyStickModel getRawDataFromResponseToJoyStick(String response) {
        // Create generic JSON object form string
        JSONObject jsonObject = null;
        JoyStickModel joyStickModel = null;
        try {
            jsonObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            /* get joy-stick model from JSON data */
            joyStickModel = new JoyStickModel(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return joyStickModel;
    }

    /**
     * @brief Reading raw joy-stick data from JSON response.
     * @param response IoT server JSON response as string
     * @retval new JoyStickModel data
     */
    public MeasurementModel getRawDataFromResponseToDynamicTable(String response) {
        // Create generic JSON object form string
        JSONObject jsonObject = null;
        //MeasurementModel measurementModel = null;
        MeasurementModel measurementModel = new MeasurementModel("Error", 0, "-", "-");
        if(!response.isEmpty()) {
            try {
                jsonObject = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                /* get joy-stick model from JSON data */
                measurementModel = new MeasurementModel(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return measurementModel;
    }

    /**
     * @brief Reading raw joy-stick data from JSON response.
     * @param response IoT server JSON response as string
     * @retval new Config data
     */
    public ConfigModel getRawDataFromResponseToConfig(String response) {
        // Create generic JSON object form string
        JSONObject jsonObject = null;
        ConfigModel configModel = null;
        if(!response.isEmpty()) {
            try {
                jsonObject = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                /* get joy-stick model from JSON data */
                configModel = new ConfigModel(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return configModel;
    }
}
