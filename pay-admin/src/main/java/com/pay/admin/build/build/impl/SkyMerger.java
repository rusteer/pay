package com.pay.admin.build.build.impl;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.pay.admin.build.build.LogUtil;
import com.pay.admin.build.build.PathUtil;
import com.pay.admin.build.build.utils.AES;
import com.pay.admin.build.build.utils.Helper;
import com.pay.admin.utils.EncodedSdkConstants;
import com.pay.admin.utils.SdkConstants;

/**
 * MM支付SDK
 *
 */
public class SkyMerger {
    String projectPath;
    Map<String, String> params;
    String channelName;
    String sdkName = SdkConstants.SKY;
    String encodedSdkName = EncodedSdkConstants.SKY;
    //自有渠道 :daiji_自 定 义
    //冒泡市场:1_zhiyifu_
    //冒泡游戏:9_zhiyifu_
    //指盟推广:88_zhiyifu_
    public SkyMerger(String projectName, String projectPath, JSONObject channel) throws JSONException {
        this.projectPath = projectPath;
        String channelName = "daiji_c" + channel.optLong("id");
        String channelInfo = channel.optString("info");
        if (StringUtils.isNotBlank(channelInfo)) {
            JSONObject obj = new JSONObject(channelInfo);
            String skyChannelName = obj.optString("skyChannelName");
            if (StringUtils.isNotBlank(skyChannelName)) {
                channelName = skyChannelName;
            }
        }
        this.channelName = channelName;
    }
    private String getTargetSdkPath() {
        return PathUtil.DISK_PREFIX + "/workspace/pay/code/sdk/paylib-sky";
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
        //LogUtil.info(configContent);
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
            String password = Helper.getPassword(projectPath);
            File targetFile = new File(projectPath + "/assets/pi/" + encodedSdkName + "2.db");
            JSONObject info = new JSONObject(FileUtils.readFileToString(new File(projectPath + "/pay-info/" + sdkName + "/info.json")));
            info.put("channelName", channelName);
            LogUtil.info("info.json:" + info.toString());
            FileUtils.write(targetFile, AES.encode(info.toString(), password));
            LogUtil.info(targetFile.getAbsolutePath() + "  updated");
        }
    }
    private void copySdkSourceFiles() throws Throwable {
        Helper.copyDirectory(getTargetSdkPath() + "/src", projectPath + "/src");
    }
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
        Helper.copyDirectory(targetSdkPath + "/assets/skypayfile", projectPath + "/assets/skypayfile");
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
        FileUtils.write(projectProguardFile, FileUtils.readFileToString(projectProguardFile) + "\n" + FileUtils.readFileToString(libProguard));
    }
    
    
    private void mergeBizFile() throws Throwable {}
    private void doOtherTasks() throws Throwable {
        Helper.deleteFile(projectPath + "/res/values/sky_appname.xml");
        File file = new File(projectPath + "/pay-info/" + sdkName + "/replace");
        if (file.exists() && file.isDirectory()) {
            Helper.copyDirectory(file.getAbsolutePath(), projectPath);
        }
    }
}
