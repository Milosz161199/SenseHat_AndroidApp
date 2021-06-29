/**
 * *****************************************************************************
 *
 * @file Sense Hat/JoyStickModel.java
 * @author Milosz Plutwoski
 * @version V1.0
 * @date 15-06-2021
 * @brief Sense Hat: Joy-stick model
 * *****************************************************************************
 */

package com.example.senseHat.Model;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class JoyStickModel extends AppCompatActivity {

    private int mCounterX;
    private int mCounterY;
    private int mCounterMiddle;


    public int getCounterX() {
        return mCounterX;
    }

    public void setCounterX(int x) {
        this.mCounterX = x;
    }

    public int getCounterY() {
        return mCounterY;
    }

    public void setCounterY(int y) {
        this.mCounterY = y;
    }

    public int getCounterMiddle() {
        return mCounterMiddle;
    }

    public void setCounterMiddle(int z) {
        this.mCounterMiddle = z;
    }


    public JoyStickModel() {
    }

    public JoyStickModel(JSONObject data) throws JSONException {
        try {
            this.mCounterX = data.getInt("counter_x");
            this.mCounterY = data.getInt("counter_y");
            this.mCounterMiddle = data.getInt("counter_middle");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException("Json Object to Measurement Data parse error");
        }
    }

}
