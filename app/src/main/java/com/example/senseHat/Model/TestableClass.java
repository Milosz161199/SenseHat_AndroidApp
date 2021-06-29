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
     * @brief Reading raw chart data from JSON response.
     * @param response IoT server JSON response as string
     * @retval new chart data
     */
    public double[] getRawDataFromResponse(String response) {
        // Create generic JSON object form string

        JSONArray jArray;
        double[] x = new double[6];

        try {
            jArray = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return x;
        }

        // iterate through JSON Array
        for (int i = 0; i < jArray.length(); i++) {
            try {
                /* get measurement model from JSON data */
                MeasurementModel measurement = new MeasurementModel(jArray.getJSONObject(i));

                if (measurement.mName.matches("temperature")) {
                    x[0] = measurement.mValue;
                }
                if (measurement.mName.matches("humidity")) {
                    x[1] = measurement.mValue;
                }
                if (measurement.mName.matches("pressure")) {
                    x[2] = measurement.mValue;
                }
                if (measurement.mName.matches("roll")) {
                    x[3] = measurement.mValue;
                }
                if (measurement.mName.matches("pitch")) {
                    x[4] = measurement.mValue;
                }
                if (measurement.mName.matches("yaw")) {
                    x[5] = measurement.mValue;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return x;
    }


    private static final String valueFormat = "%.4f";

    /**
     * @brief Reading raw chart data from JSON response.
     * @param response IoT server JSON response as string
     * @retval new chart data
     */
    public MeasurementModel getRawDataFromResponseToTable(String response) {
        // Create generic JSON object form string

        JSONArray jArray = null;
        MeasurementModel measurementModel = new MeasurementModel("-", 0, "-", "-");

        try {
            jArray = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // iterate through JSON Array
        for (int i = 0; i < jArray.length(); i++) {
            try {
                /* get measurement model from JSON data */
                measurementModel = new MeasurementModel(jArray.getJSONObject(i));

                /*
                if(i == 0 )
                {
                    DynamicTableActivity.name1.setText(measurement.mName);
                    DynamicTableActivity.value1.setText(String.format(valueFormat, measurement.mValue));
                    DynamicTableActivity.unit1.setText(measurement.mUnit);
                }
                if(i == 1 )
                {
                    DynamicTableActivity.name2.setText(measurement.mName);
                    DynamicTableActivity.value2.setText(String.format(valueFormat, measurement.mValue));
                    DynamicTableActivity.unit2.setText(measurement.mUnit);
                }
                if(i == 2 )
                {
                    DynamicTableActivity.name3.setText(measurement.mName);
                    DynamicTableActivity.value3.setText(String.format(valueFormat, measurement.mValue));
                    DynamicTableActivity.unit3.setText(measurement.mUnit);
                }
                if(i == 3 )
                {
                    DynamicTableActivity.name4.setText(measurement.mName);
                    DynamicTableActivity.value4.setText(String.format(valueFormat, measurement.mValue));
                    DynamicTableActivity.unit4.setText(measurement.mUnit);
                }
                if(i == 4 )
                {
                    DynamicTableActivity.name5.setText(measurement.mName);
                    DynamicTableActivity.value5.setText(String.format(valueFormat, measurement.mValue));
                    DynamicTableActivity.unit5.setText(measurement.mUnit);
                }
                if(i == 5 )
                {
                    DynamicTableActivity.name6.setText(measurement.mName);
                    DynamicTableActivity.value6.setText(String.format(valueFormat, measurement.mValue));
                    DynamicTableActivity.unit6.setText(measurement.mUnit);
                }
                if(i == 6 )
                {
                    DynamicTableActivity.name7.setText(measurement.mName);
                    DynamicTableActivity.value7.setText(String.format(valueFormat, measurement.mValue));
                    DynamicTableActivity.unit7.setText(measurement.mUnit);
                }
                if(i == 7 )
                {
                    DynamicTableActivity.name8.setText(measurement.mName);
                    DynamicTableActivity.value8.setText(String.format(valueFormat, measurement.mValue));
                    DynamicTableActivity.unit8.setText(measurement.mUnit);
                }
                if(i == 8 )
                {
                    DynamicTableActivity.name9.setText(measurement.mName);
                    DynamicTableActivity.value9.setText(String.format(valueFormat, measurement.mValue));
                    DynamicTableActivity.unit9.setText(measurement.mUnit);
                }
                */
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        return measurementModel;
    }


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
        MeasurementModel measurementModel = null;
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

        return measurementModel;
    }


}
