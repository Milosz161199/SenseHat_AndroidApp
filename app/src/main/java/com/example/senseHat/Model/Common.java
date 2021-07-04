/**
 * *****************************************************************************
 *
 * @file Sense Hat/Common.java
 * @author Milosz Plutwoski
 * @version V1.0
 * @date 15-06-2021
 * @brief Sense Hat: common app information
 * *****************************************************************************
 */

package com.example.senseHat.Model;

import com.example.senseHat.Model.MeasurementModel;

import java.util.List;

public class Common {
    // activities request codes
    public final static int REQUEST_CODE_CONFIG = 1;

    // configuration info: names and default values
    public final static String CONFIG_IP_ADDRESS = "ipAddress";
    public final static String DEFAULT_IP_ADDRESS = "192.168.100.19";


    public final static String CONFIG_SAMPLE_TIME = "sampleTime";
    public final static int DEFAULT_SAMPLE_TIME = 500;

    public final static String CONFIG_MAX_NUMBER_OF_SAMPLES = "maxNumOfSamples";
    public final static int DEFAULT_MAX_NUMBER_OF_SAMPLES = 100;

    public final static String CONFIG_API_VERSION = "apiVersion";
    public final static double DEFAULT_API_VERSION = 6.0;

    // error codes
    public final static int ERROR_TIME_STAMP = -1;
    public final static int ERROR_NAN_DATA = -2;
    public final static int ERROR_RESPONSE = -3;


    /* BEGIN REQUEST COMMAND TO SERVER */

    // IoT server data
    public static String PHP_COMMAND_TAKE_DATA_TO_CONFIG = "/PROJECT/config_test_file.json";
    public static String PHP_COMMAND_SEND_DATA_TO_CONFIG = "/PROJECT/config_test_file.json";
    public static String PHP_COMMAND_TAKE_DATA_JOY_STICK = "/PROJECT/joy_stick_test_file.json";
    public static String PHP_COMMAND_TAKE_DATA_LED_MATRIX = "/PROJECT/led_display_test_file.json";
    public static String PHP_COMMAND_SEND_DATA_LED_MATRIX = "/PROJECT/led_display.php";

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
    public static String REQ_GYROSCOPE_YAW = "/PROJECT/OneByOne/gyroscope_yaw.json";
    public static String REQ_GYROSCOPE_X = "/PROJECT/OneByOne/gyroscope_x.json";
    public static String REQ_GYROSCOPE_Y = "/PROJECT/OneByOne/gyroscope_y.json";
    public static String REQ_GYROSCOPE_Z = "/PROJECT/OneByOne/gyroscope_z.json";
    public static String REQ_ACCELEROMETER_ROLL = "/PROJECT/OneByOne/accelerometer_roll.json";
    public static String REQ_ACCELEROMETER_PITCH = "/PROJECT/OneByOne/accelerometer_pitch.json";
    public static String REQ_ACCELEROMETER_YAW = "/PROJECT/OneByOne/accelerometer_yaw.json";
    public static String REQ_ACCELEROMETER_X = "/PROJECT/OneByOne/accelerometer_x.json";
    public static String REQ_ACCELEROMETER_Y = "/PROJECT/OneByOne/accelerometer_y.json";
    public static String REQ_ACCELEROMETER_Z = "/PROJECT/OneByOne/accelerometer_z.json";
    /* END REQUEST COMMAND TO SERVER */
}
