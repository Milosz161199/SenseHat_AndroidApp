/**
 * *****************************************************************************
 *
 * @file Sense Hat/ConfigModel.java
 * @author Milosz Plutwoski
 * @version V1.0
 * @date 15-06-2021
 * @brief Sense Hat: Config data from json file
 * *****************************************************************************
 */

package com.example.senseHat.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfigModel {
    public String mIpAddress;
    public int mSampleTimeMs;
    public int mMaxNumOfSamples;
    public double mApiVersion;

    public ConfigModel(String IpAddress, int SampleTimeMs, int MaxNumOfSamples, double ApiVersion) {
        mIpAddress = IpAddress;
        mSampleTimeMs = SampleTimeMs;
        mMaxNumOfSamples = MaxNumOfSamples;
        mApiVersion = ApiVersion;
    }

    public ConfigModel(JSONObject data) throws JSONException {
        try {
            mIpAddress = data.getString("ipAddress");
            mSampleTimeMs = data.getInt("sampleTime");
            mMaxNumOfSamples = data.getInt("maxNumOfSamples");
            mApiVersion = data.getDouble("apiVersion");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException("Json Object to Config Data parse error");
        }
    }
}
