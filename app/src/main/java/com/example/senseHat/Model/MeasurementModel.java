package com.example.senseHat.Model;

import com.example.senseHat.ViewModel.MeasurementViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class MeasurementModel {

    public String mName;
    public double mValue;
    public String mUnit;
    public String mSensor;

    public MeasurementModel(String name, double value, String unit, String sensor)
    {
        mName = name;
        mValue = value;
        mUnit = unit;
        mSensor = sensor;
    }

    public MeasurementModel(JSONObject data) throws JSONException {
        try {
            mName = data.getString("name");
            mValue= data.getDouble("value");
            mUnit = data.getString("unit");
            mSensor = data.getString("sensor");
        }
        catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException("Json Object to Measurement Data parse error");
        }
    }

    public MeasurementViewModel toVM()
    {
        return new MeasurementViewModel(this);
    }
}
