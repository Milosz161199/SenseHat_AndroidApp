/**
 * *****************************************************************************
 *
 * @file Sense Hat/MeasurementsViewModel.java
 * @author Milosz Plutowski
 * @version V1.0
 * @date 15-06-2021
 * @brief Sense Hat: View model for measurements
 * *****************************************************************************
 */

package com.example.senseHat.ViewModel;

// MODELS

import com.example.senseHat.Model.MeasurementModel;

public class MeasurementViewModel {

    private MeasurementModel model;
    private static final String valueFormat = "%.4f";

    public MeasurementViewModel(MeasurementModel measurement) {
        model = measurement;
    }

    public String getName() {
        return model.mName;
    }

    public String getValue() {
        return String.format(valueFormat, model.mValue);
    }

    public String getUnit() {
        return model.mUnit;
    }

    public String getSensor() {
        return model.mSensor;
    }
}
