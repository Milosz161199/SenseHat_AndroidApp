/**
 ******************************************************************************
 * @file    Sense Hat/Common.java
 * @author  Milosz Plutwoski
 * @version V1.0
 * @date    15-06-2021
 * @brief   Sense Hat: common app information
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
    public final static String DEFAULT_IP_ADDRESS = "192.168.0.103";

    public final static String CONFIG_SOCKET = "socketAddress";
    public final static int DEFAULT_SOCKET = 21567;

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

    public static String PHP_COMMAND_TAKE_DATA_TO_TABLE = "";
    public static String PHP_COMMAND_TAKE_DATA_TO_GRAPH = "";
    public static String PHP_COMMAND_TAKE_DATA_TO_CONFIG = "/PROJECT/config_data_test_file.json";
    public static String PHP_COMMAND_TAKE_DATA_JOY_STICK = "/PROJECT/joy_stick_test_file.json";
    public static String PHP_COMMAND_TAKE_DATA_LED_MATRIX = "/PROJECT/led_display_test_file.json";
    public static String PHP_COMMAND_SEND_DATA_LED_MATRIX = "/PROJECT/led_display.php";


    /* BEGIN COMMAND TO SERVER */
    private final String CMD_TEMPERATURE_C = "get_temp_c";
    private final String CMD_TEMPERATURE_F = "get_temp_f";
    private final String CMD_PRESSURE_HPA = "get_press_hpa";
    private final String CMD_PRESSURE_MM_HG = "get_press_mm_hg";
    private final String CMD_HUMIDITY_P = "get_hum_p";
    private final String CMD_HUMIDITY_ = "get_hum_";
    private final String CMD_ROLL_DEG = "get_roll_deg";
    private final String CMD_ROLL_RAD = "get_roll_rad";
    private final String CMD_PITCH_DEG = "get_pitch_deg";
    private final String CMD_PITCH_RAD = "get_pitch_rad";
    private final String CMD_YAW_DEG = "get_yaw_deg";
    private final String CMD_YAW_RAD = "get_yaw_rad";


    private final String CMD_RPY_DEG = "get_rpy_deg";
    private final String CMD_RPY_RAD = "get_rpy_rad";
    private final String CMD_ENV_1 = "get_env_1";
    private final String CMD_ENV_2 = "get_env_2";
    private final String CMD_COMPASS = "get_compass";
    private final String CMD_JOY_STICK = "get_joy_stick";
    private final String CMD_ALL_MEASUREMENTS = "get_all";


    /* END COMMAND TO SERVER  */

    public static List<MeasurementModel> measurementModelsList;


}
