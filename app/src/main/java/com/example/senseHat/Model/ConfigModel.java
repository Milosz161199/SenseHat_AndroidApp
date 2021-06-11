package com.example.senseHat.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfigModel {
    public String mIpAddress;
    public double mSampleTimeMs;
    public double mMaxNumOfSamples;
    public double mApiVersion;

    public ConfigModel(String IpAddress, double SampleTimeMs, double MaxNumOfSamples, double ApiVersion)
    {
        mIpAddress = IpAddress;
        mSampleTimeMs = SampleTimeMs;
        mMaxNumOfSamples = MaxNumOfSamples;
        mApiVersion = ApiVersion;
    }

    public ConfigModel(JSONObject data) throws JSONException {
        try {
            mIpAddress = data.getString("ipAddress");
            mSampleTimeMs= data.getDouble("sampleTimeMs");
            mMaxNumOfSamples = data.getDouble("maxNumOfSamples");
            mApiVersion = data.getDouble("apiVersion");
        }
        catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException("Json Object to Measurement Data parse error");
        }
    }
}
