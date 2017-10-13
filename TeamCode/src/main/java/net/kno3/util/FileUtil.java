package net.kno3.util;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jaxon on 12/12/2016.
 */

public class FileUtil {
    public static void writeJSON(String fileName, JSONObject json) {
        try {
            FileWriter file = new FileWriter("/data/data/" + FtcRobotControllerActivity.instance.getApplicationContext().getPackageName() + "/" + fileName);
            file.write(json.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject readJSON(String filename) {
        try {
            File f = new File("/data/data/" + FtcRobotControllerActivity.instance.getPackageName() + "/" + filename);
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new JSONObject(new String(buffer));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
