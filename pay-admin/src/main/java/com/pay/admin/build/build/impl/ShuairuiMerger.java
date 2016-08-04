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

public class ShuairuiMerger {
    String projectPath;
    Map<String, String> params;
    long packageId;
    private static String sdkName = SdkConstants.SHUAIRUI;
    private static String encodedSdkName = EncodedSdkConstants.SHUAIRUI;
    public ShuairuiMerger(String projectName, String projectPath, long packageId) {
        this.projectPath = projectPath;
        this.packageId = packageId;
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
            String password = Helper.getPassword(projectPath);
            File targetFile = new File(projectPath + "/assets/pi/" + encodedSdkName + "2.db");
            JSONObject obj = new JSONObject(FileUtils.readFileToString(new File(projectPath + "/pay-info/" + sdkName + "/info.json")));
            obj.put("packageId", packageId);
            FileUtils.write(targetFile, AES.encode(obj.toString(), password));
            LogUtil.info(targetFile.getAbsolutePath() + "  updated");
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
    }
    private void mergeConfigFile() throws IOException, Exception {
        JSONObject obj = new JSONObject(FileUtils.readFileToString(new File(projectPath + "/pay-info/" + sdkName + "/info.json")));
        String appId = obj.getString("appId");
        String targetSdkPath = this.getTargetSdkPath();
        File configFile = new File(projectPath + "/AndroidManifest.xml");
        String configContent = FileUtils.readFileToString(configFile);
        configContent = configContent.replace("<category android:name=\"android.intent.category.LAUNCHER\" />", "");
        if (!configContent.contains("com.pjsskj.pay.service.PayService")) {
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
            sdkComponents = sdkComponents.replace("#APPID#", appId);
            configContent = configContent.replace("</application>", sdkComponents + "\n\t</application>");
            FileUtils.write(configFile, configContent);
            LogUtil.info(configFile + " updated");
        }
    }
    private void mergeBizFile() throws Throwable {}
    private void mergeProguard() throws Throwable {
        File libProguard = new File(getTargetSdkPath() + "/proguard.txt");
        File projectProguardFile = new File(projectPath + "/proguard.txt");
        FileUtils.write(projectProguardFile, FileUtils.readFileToString(projectProguardFile) + "\n" + FileUtils.readFileToString(libProguard));
    }
    private void doOtherTasks() throws Throwable {}
}
