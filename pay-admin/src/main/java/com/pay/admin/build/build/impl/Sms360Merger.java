package com.pay.admin.build.build.impl;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import com.pay.admin.build.build.LogUtil;
import com.pay.admin.build.build.PathUtil;
import com.pay.admin.build.build.utils.AES;
import com.pay.admin.build.build.utils.Helper;
import com.pay.admin.build.build.utils.Md5Util;
import com.pay.admin.utils.EncodedSdkConstants;
import com.pay.admin.utils.SdkConstants;

/**
 * MM支付SDK
 *
 */
public class Sms360Merger {
    String projectPath;
    String packageName;
    String sdkName = SdkConstants.SMS360;
    String encodedSdkName = EncodedSdkConstants.SMS360;
    public Sms360Merger(String projectName, String projectPath, String packageName) {
        this.projectPath = projectPath;
        this.packageName = packageName;
    }
    private String getTargetSdkPath() {
        return PathUtil.DISK_PREFIX + "/workspace/pay/code/sdk/paylib-"+sdkName;
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
        String content = MergerHelper.getPayList(projectPath, sdkName);
        File targetFile = new File(projectPath + "/assets/pi/" + encodedSdkName + ".db");
        String encocedContent = AES.encode(content, Helper.getPassword(projectPath));
        FileUtils.write(targetFile, encocedContent);
        LogUtil.info(targetFile + " created");
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
        Helper.copyDirectory(targetSdkPath + "/assets", projectPath + "/assets");
    }
    private void mergeConfigFile() throws IOException, Exception {
        String targetSdkPath = this.getTargetSdkPath();
        File configFile = new File(projectPath + "/AndroidManifest.xml");
        String configContent = FileUtils.readFileToString(configFile);
        //permissions
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
        //components
        JSONObject sdkInfo = new JSONObject(FileUtils.readFileToString(new File(projectPath + "/pay-info/"+sdkName+"/info.json")));
        String appId = sdkInfo.getString("appId");
        String appKey = sdkInfo.getString("appKey");
        String appSecret = sdkInfo.getString("appSecret");
        String privatekey = Md5Util.MD5(appSecret + "#" + appKey);
        String sdkComponents = FileUtils.readFileToString(new File(targetSdkPath + "/components.txt"));
        sdkComponents = sdkComponents.replace("#APP_ID#", appId);
        sdkComponents = sdkComponents.replace("#APP_KEY#", appKey);
        sdkComponents = sdkComponents.replace("#PRIVATE_KEY#", privatekey);
        configContent = configContent.replace("</application>", sdkComponents + "\n\t</application>");
        //
        FileUtils.write(configFile, configContent);
        LogUtil.info(configFile + " updated");
    }
    private JSONObject getProjectInfo() throws Exception {
        return new JSONObject(FileUtils.readFileToString(new File(projectPath + "/pay-info/info.json")));
    }
    private void mergeBizFile() throws Throwable {
        boolean isLandScape = !"portrait".equals(getProjectInfo().getString("screenOrientation"));
        if (isLandScape) {
            String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n\t<string name=\"isLandScape\">true</string>\n</resources>";
            File file = new File(projectPath + "/res/values/sms360pay_string.xml");
            FileUtils.write(file, content);
            LogUtil.info(file.getAbsolutePath() + "  created");
        }
    }
    private void doOtherTasks() throws Throwable {
        File file = new File(projectPath + "/pay-info/"+sdkName+"/replace");
        if (file.exists() && file.isDirectory()) {
            Helper.copyDirectory(file.getAbsolutePath(), projectPath);
        }
    }
    private void mergeProguard() throws Throwable {
        File libProguard = new File(getTargetSdkPath() + "/proguard.txt");
        String content = FileUtils.readFileToString(libProguard);
        content = content.replace("#PACKAGE_NAME#", packageName);
        File projectProguardFile = new File(projectPath + "/proguard.txt");
        FileUtils.write(projectProguardFile, FileUtils.readFileToString(projectProguardFile) + "\n" + content);
    }
}
