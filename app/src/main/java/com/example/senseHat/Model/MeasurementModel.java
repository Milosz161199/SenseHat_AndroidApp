/**
 * *****************************************************************************
 *
 * @file Sense Hat/MeasurementModel.java
 * @author Milosz Plutwoski
 * @version V1.0
 * @date 15-06-2021
 * @brief Sense Hat: Measurements model
 * *****************************************************************************
 */

package com.example.senseHat.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class MeasurementModel {

    public String mName;
    public double mValue;
    public String mUnit;
    public String mSensor;
    public String mDate;

    public MeasurementModel(String name, double value, String unit, String sensor) {
        mName = name;
        mValue = value;
        mUnit = unit;
        mSensor = sensor;
    }

    public MeasurementModel(JSONObject data) throws JSONException {
        try {
            mName = data.getString("name");
            mValue = data.getDouble("value");
            mUnit = data.getString("unit");
            mSensor = data.getString("sensor");

        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException("Json Object to Measurement Data parse error");
        }
    }
}
