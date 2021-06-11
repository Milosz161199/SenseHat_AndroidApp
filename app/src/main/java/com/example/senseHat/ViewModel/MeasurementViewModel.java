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

    public String getSensor() { return model.mSensor; }
}
