package com.pay.admin.build.build.impl;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import com.pay.admin.build.build.LogUtil;
import com.pay.admin.build.build.PathUtil;
import com.pay.admin.build.build.utils.AES;
import com.pay.admin.build.build.utils.Helper;
import com.pay.admin.utils.EncodedSdkConstants;
import com.pay.admin.utils.SdkConstants;

/**
 * 点我支付
 *
 */
public class DianwoMerger {
    String projectPath;
    Map<String, String> params;
    private int channelId;
    private static String sdkName = SdkConstants.DIANWO;
    private static String encodedSdkName = EncodedSdkConstants.DIANWO;
    public DianwoMerger(String projectName, String projectPath, int channelId) {
        this.projectPath = projectPath;
        this.channelId = channelId;
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
        mergeProguard();
    }
    private void encodePayInfoDB() throws Throwable {
        {
            String content = MergerHelper.getPayList(projectPath, sdkName);
            File targetFile = new File(projectPath + "/assets/pi/" + encodedSdkName + ".db");
            String encocedContent = AES.encode(content, Helper.getPassword(projectPath));
            FileUtils.write(targetFile, encocedContent);
            LogUtil.info(targetFile + " created");
        }
        {
            JSONObject obj = new JSONObject(FileUtils.readFileToString(new File(projectPath + "/pay-info/" + sdkName + "/info.json")));
            File file = new File(projectPath + "/assets/config.properties");
            String content = String.format("QY_APP_KEY = %s\nQY_CHANNEL_ID = XXXXX", obj.getString("appId"));
            FileUtils.write(file, content);
            LogUtil.info(file.getAbsolutePath() + " updated");
        }
    }
    private void copySdkSourceFiles() throws Throwable {
        Helper.copyDirectory(getTargetSdkPath() + "/src", projectPath + "/src");
    }
    //
    private void valid() throws Exception {
        String targetSdkPath = this.getTargetSdkPath();
        File file = new File(projectPath);
        if (!file.exists() || !file.isDirectory()) throw new Exception("Project path not exists:" + projectPath);
        File targetSDKFolder = new File(targetSdkPath);
        File projectLibsFolder = new File(file.getAbsolutePath(), "libs");
        if (!projectLibsFolder.exists()) projectLibsFolder.mkdir();
        if (!targetSDKFolder.exists() || !targetSDKFolder.isDirectory()) throw new Exception("SDK path not exists:" + targetSDKFolder.getAbsolutePath());
    }
    private void copyResources() throws Throwable {
        String targetSdkPath = this.getTargetSdkPath();
        Helper.copyDirectory(targetSdkPath + "/libs", projectPath + "/libs");
        Helper.copyDirectory(targetSdkPath + "/res", projectPath + "/res");
        Helper.copyDirectory(targetSdkPath + "/assets", projectPath + "/assets");
        
        File file=new File(projectPath + "/libs/armeabi-v7a");
        if(file.exists()){
            Helper.copyDirectory(projectPath + "/libs/armeabi", projectPath + "/libs/armeabi-v7a");
        }
    }
    private void mergeConfigFile() throws IOException, Exception {
        String targetSdkPath = this.getTargetSdkPath();
        File configFile = new File(projectPath + "/AndroidManifest.xml");
        String configContent = FileUtils.readFileToString(configFile);
        List<String> permissions = FileUtils.readLines(new File(targetSdkPath + "/permissions.txt"));
        StringBuilder sb = new StringBuilder();
        for (String permission : permissions) {
            if (StringUtils.isNotBlank(permission)) {
                permission = permission.trim();
                if (!configContent.contains(permission)) {
                    sb.append("<uses-permission android:name=\"").append(permission).append("\" />\n\t");
                }
            }
        }
        configContent = configContent.replace("</application>", "</application>\n\t" + sb.toString());
        String sdkComponents = FileUtils.readFileToString(new File(targetSdkPath + "/components.txt"));
        configContent = configContent.replace("</application>", sdkComponents + "\n\t</application>");
        FileUtils.write(configFile, configContent);
        LogUtil.info(configFile + " updated");
    }
    private void mergeProguard() throws Throwable {
        File libProguard = new File(getTargetSdkPath() + "/proguard.txt");
        File projectProguardFile = new File(projectPath + "/proguard.txt");
        String content = FileUtils.readFileToString(projectProguardFile) + "\n" + FileUtils.readFileToString(libProguard);
        FileUtils.write(projectProguardFile, content);
    }
    private void mergeBizFile() throws Throwable {}
    private void doOtherTasks() throws Throwable {
        File file = new File(projectPath + "/pay-info/" + sdkName + "/replace");
        if (file.exists() && file.isDirectory()) {
            Helper.copyDirectory(file.getAbsolutePath(), projectPath);
        }
    }
}
