package com.example.senseHat;

import org.json.JSONException;
import org.json.JSONObject;

public class MeasurementModel {

    public String mName;
    public double mValue;
    public String mUnit;

    public MeasurementModel(String name, double value, String unit)
    {
        mName = name;
        mValue = value;
        mUnit = unit;
    }

    public MeasurementModel(JSONObject data) throws JSONException {
        try {
            mName = data.getString("name");
            mValue= data.getDouble("value");
            mUnit = data.getString("unit");
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
