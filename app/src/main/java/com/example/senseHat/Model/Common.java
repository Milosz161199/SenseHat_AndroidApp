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
    public final static String DEFAULT_IP_ADDRESS = "192.168.0.104";

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
    private final String CMD_TEMPERATURE_C_1 = "get_temp_c_1";
    private final String CMD_TEMPERATURE_C_2 = "get_temp_c_2";
    private final String CMD_TEMPERATURE_C_3 = "get_temp_c_3";
    private final String CMD_TEMPERATURE_F_1 = "get_temp_f_1";
    private final String CMD_TEMPERATURE_F_2 = "get_temp_f_2";
    private final String CMD_TEMPERATURE_F_3 = "get_temp_f_3";
    private final String CMD_PRESSURE_HPA = "get_press_hpa";
    private final String CMD_PRESSURE_MM_HG = "get_press_mm_hg";
    private final String CMD_HUMIDITY_P = "get_hum_p";
    private final String CMD_HUMIDITY_ = "get_hum_";
    private final String CMD_ORIENT_ROLL_DEG = "get_roll_deg";
    private final String CMD_ORIENT_ROLL_RAD = "get_roll_rad";
    private final String CMD_ORIENT_PITCH_DEG = "get_pitch_deg";
    private final String CMD_ORIENT_PITCH_RAD = "get_pitch_rad";
    private final String CMD_ORIENT_YAW_DEG = "get_yaw_deg";
    private final String CMD_ORIENT_YAW_RAD = "get_yaw_rad";
    private final String CMD_COMPASS_NORTH = "get_compass_north";
    private final String CMD_COMPASS_X = "get_compass_x";
    private final String CMD_COMPASS_Y = "get_compass_y";
    private final String CMD_COMPASS_Z = "get_compass_z";
    private final String CMD_GYROSCOPE_ROLL = "get_gyroscope_roll";
    private final String CMD_GYROSCOPE_PITCH = "get_gyroscope_pitch";
    private final String CMD_GYROSCOPE_YAW = "get_gyroscope_yaw";
    private final String CMD_GYROSCOPE_X = "get_gyroscope_x";
    private final String CMD_GYROSCOPE_Y = "get_gyroscope_y";
    private final String CMD_GYROSCOPE_Z = "get_gyroscope_z";
    private final String CMD_ACCELEROMETER_ROLL = "get_accelerometer_roll";
    private final String CMD_ACCELEROMETER_PITCH = "get_accelerometer_pitch";
    private final String CMD_ACCELEROMETER_YAW = "get_accelerometer_yaw";
    private final String CMD_ACCELEROMETER_X = "get_accelerometer_x";
    private final String CMD_ACCELEROMETER_Y = "get_accelerometer_y";
    private final String CMD_ACCELEROMETER_Z = "get_accelerometer_z";


    private final String CMD_RPY_DEG = "get_rpy_deg";
    private final String CMD_RPY_RAD = "get_rpy_rad";
    private final String CMD_ENV_1 = "get_env_1";
    private final String CMD_ENV_2 = "get_env_2";
    private final String CMD_COMPASS = "get_compass";
    private final String CMD_JOY_STICK = "get_joy_stick";
    private final String CMD_ALL_MEASUREMENTS = "get_all";


    /* END COMMAND TO SERVER  */

    /* BEGIN REQUEST COMMAND TO SERVER */
    public static String REQ_TEST_FILE = "/PROJECT/OneByOne/TEST_file.json";
    public static String REQ_COMPASS_X = "/PROJECT/OneByOne/compass_x.json";
    public static String REQ_COMPASS_Y = "/PROJECT/OneByOne/compass_y.json";
    public static String REQ_COMPASS_Z = "/PROJECT/OneByOne/compass_z.json";
    public static String REQ_COMPASS_NORTH = "/PROJECT/OneByOne/compass_north.json";
    public static String REQ_COUNTER_X = "/PROJECT/OneByOne/counter_x.json";
    public static String REQ_COUNTER_Y = "/PROJECT/OneByOne/counter_y.json";
    public static String REQ_COUNTER_MIDDLE = "/PROJECT/OneByOne/counter_middle.json";
    public static String REQ_HUM_ = "/PROJECT/OneByOne/hum_.json";
    public static String REQ_HUM_P = "/PROJECT/OneByOne/hum_p.json";
    public static String REQ_TEMP_C_1 = "/PROJECT/OneByOne/temp_C_1.json";
    public static String REQ_TEMP_C_2 = "/PROJECT/OneByOne/temp_C_2.json";
    public static String REQ_TEMP_C_3 = "/PROJECT/OneByOne/temp_C_3.json";
    public static String REQ_TEMP_F_1 = "/PROJECT/OneByOne/temp_F_1.json";
    public static String REQ_TEMP_F_2 = "/PROJECT/OneByOne/temp_F_2.json";
    public static String REQ_TEMP_F_3 = "/PROJECT/OneByOne/temp_F_3.json";
    public static String REQ_PRES_HPA = "/PROJECT/OneByOne/pres_hpa.json";
    public static String REQ_PRES_MM_HG = "/PROJECT/OneByOne/pres_mm_hg.json";
    public static String REQ_ROLL_RAD = "/PROJECT/OneByOne/roll_rad.json";
    public static String REQ_ROLL_DEG = "/PROJECT/OneByOne/roll_deg.json";
    public static String REQ_PITCH_RAD = "/PROJECT/OneByOne/pitch_rad.json";
    public static String REQ_PITCH_DEG = "/PROJECT/OneByOne/pitch_deg.json";
    public static String REQ_YAW_RAD = "/PROJECT/OneByOne/yaw_rad.json";
    public static String REQ_YAW_DEG = "/PROJECT/OneByOne/yaw_deg.json";
    public static String REQ_GYROSCOPE_ROLL = "/PROJECT/OneByOne/gyroscope_roll.json";
    public static String REQ_GYROSCOPE_PITCH = "/PROJECT/OneByOne/gyroscope_pitch.json";
    public static String REQ_GYROSCOPE_WAY = "/PROJECT/OneByOne/gyroscope_yaw.json";
    public static String REQ_GYROSCOPE_X = "/PROJECT/OneByOne/gyroscope_x.json";
    public static String REQ_GYROSCOPE_Y = "/PROJECT/OneByOne/gyroscope_y.json";
    public static String REQ_GYROSCOPE_Z = "/PROJECT/OneByOne/gyroscope_z.json";
    public static String REQ_ACCELEROMETER_ROLL = "/PROJECT/OneByOne/accelerometer_roll.json";
    public static String REQ_ACCELEROMETER_PITCH = "/PROJECT/OneByOne/accelerometer_pitch.json";
    public static String REQ_ACCELEROMETER_WAY = "/PROJECT/OneByOne/accelerometer_yaw.json";
    public static String REQ_ACCELEROMETER_X = "/PROJECT/OneByOne/accelerometer_x.json";
    public static String REQ_ACCELEROMETER_Y = "/PROJECT/OneByOne/accelerometer_y.json";
    public static String REQ_ACCELEROMETER_Z = "/PROJECT/OneByOne/accelerometer_z.json";
    /* END REQUEST COMMAND TO SERVER */

    public static List<MeasurementModel> measurementModelsList;


}
