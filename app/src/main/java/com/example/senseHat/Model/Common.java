/**
 ******************************************************************************
 * @file    Data Grabber Example/Common.java
 * @author  Adrian Wojcik
 * @version V1.0
 * @date    09-Apr-2020
 * @brief   Data grabber example: common app information
 ******************************************************************************
 */

package com.example.senseHat.Model;

import com.example.senseHat.Model.MeasurementModel;

import java.util.List;

public class Common {
    // activities request codes
    public final static int REQUEST_CODE_CONFIG = 1;

    // configuration info: names and default values
    public final static String CONFIG_IP_ADDRESS = "ipAddress";
    public final static String DEFAULT_IP_ADDRESS = "192.168.0.104";

    public final static String CONFIG_SAMPLE_TIME = "sampleTime";
    public final static int DEFAULT_SAMPLE_TIME = 500;

    public final static String CONFIG_MAX_NUMBER_OF_SAMPLES = "maxNumOfSamples";
    public final static int DEFAULT_MAX_NUMBER_OF_SAMPLES = 100;

    public final static String CONFIG_API_VERSION = "apiVersion";
    public final static double DEFAULT_API_VERSION = 1.0;

    // error codes
    public final static int ERROR_TIME_STAMP = -1;
    public final static int ERROR_NAN_DATA = -2;
    public final static int ERROR_RESPONSE = -3;

    // IoT server data
    public static String FILE_NAME = "measurements.php?id=";

    public static String PHP_COMMAND_TAKING_DATA_TO_TABLE = "";
    public static String PHP_COMMAND_TAKING_DATA_TO_GRAPH = "";
    public static String PHP_COMMAND_TAKING_DATA_TO_CONFIG = "";


    public static List<MeasurementModel> measurementModelsList;


}
