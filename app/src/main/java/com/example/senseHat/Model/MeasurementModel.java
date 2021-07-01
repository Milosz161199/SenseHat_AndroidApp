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

import com.example.senseHat.ViewModel.MeasurementViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

        // SET TIME OF MEASUREMENT
        //Date currentTime = Calendar.getInstance().getTime();
        //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
        //String formattedDate = df.format(currentTime);
        //mDate = formattedDate;
    }

    public MeasurementModel(JSONObject data) throws JSONException {
        try {
            mName = data.getString("name");
            mValue = data.getDouble("value");
            mUnit = data.getString("unit");
            mSensor = data.getString("sensor");

            // SET TIME OF MEASUREMENT
            //Date currentTime = Calendar.getInstance().getTime();
            //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
            //String formattedDate = df.format(currentTime);
            //mDate = formattedDate;

        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException("Json Object to Measurement Data parse error");
        }
    }

    public MeasurementViewModel toVM() {
        return new MeasurementViewModel(this);
    }
}
