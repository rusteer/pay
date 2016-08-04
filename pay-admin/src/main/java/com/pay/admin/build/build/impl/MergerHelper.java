package com.pay.admin.build.build.impl;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class MergerHelper {
    public static String getPayList(String projectPath, String sdkName) throws IOException {
        File file = new File(projectPath + "/pay-info/" + sdkName + "/paylist.txt");
        if (!file.exists()) {
            file = new File(projectPath + "/assets/pi/paylist.txt");
        }
        return FileUtils.readFileToString(file);
    }
}
