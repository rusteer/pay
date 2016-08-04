package com.pay.admin.build.build.impl;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import com.pay.admin.build.build.LogUtil;
import com.pay.admin.build.build.PathUtil;
import com.pay.admin.build.build.utils.AES;
import com.pay.admin.build.build.utils.Helper;
import com.pay.admin.utils.EncodedSdkConstants;
import com.pay.admin.utils.SdkConstants;

/**
 * 易接支付
 *
 */
public class FreeMerger {
    String projectPath;
    Map<String, String> params;
    private static String sdkName = SdkConstants.FREE;
    private static String encodedSdkName = EncodedSdkConstants.FREE;
    public FreeMerger(String projectName, String projectPath) {
        this.projectPath = projectPath;
    }
    private String getTargetSdkPath() {
        return PathUtil.DISK_PREFIX + "/workspace/pay/code/sdk/paylib-" + sdkName;
    }
    public void merge() throws Throwable {
        valid();
        copyResources();
        mergeConfigFile();
        copySdkSourceFiles();
        mergeBizFile();
        encodePayInfoDB();
        doOtherTasks();
        //LogUtil.info(configContent);
    }
    private void encodePayInfoDB() throws Throwable {
        String content = MergerHelper.getPayList(projectPath, sdkName);
        File targetFile = new File(projectPath + "/assets/pi/" + encodedSdkName + ".db");
        String encocedContent = AES.encode(content, Helper.getPassword(projectPath));
        FileUtils.write(targetFile, encocedContent);
        LogUtil.info(targetFile + " created");
    }
    private void copySdkSourceFiles() throws Throwable {
        Helper.copyDirectory(getTargetSdkPath() + "/src", projectPath + "/src");
    }
    private void valid() throws Exception {}
    private void copyResources() throws Throwable {}
    private void mergeConfigFile() throws IOException, Exception {}
    private void mergeBizFile() throws Throwable {}
    private void doOtherTasks() throws Throwable {
        File file = new File(projectPath + "/pay-info/" + sdkName + "/replace");
        if (file.exists() && file.isDirectory()) {
            Helper.copyDirectory(file.getAbsolutePath(), projectPath);
        }
    }
}
