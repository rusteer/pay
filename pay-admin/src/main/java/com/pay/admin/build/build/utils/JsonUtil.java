package com.pay.admin.build.build.utils;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
    public static JSONObject readJson(String filePath) throws JSONException, IOException {
        return new JSONObject(FileUtils.readFileToString(new File(filePath)));
    }
}
