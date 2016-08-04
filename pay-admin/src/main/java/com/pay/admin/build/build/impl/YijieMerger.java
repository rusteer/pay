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
 * 易接支付
 *
 */
public class YijieMerger {
    String projectPath;
    Map<String, String> params;
    int channelId;
    private static String sdkName = SdkConstants.YIJIE;
    private static String encodedSdkName = EncodedSdkConstants.YIJIE;
    public YijieMerger(String projectName, String projectPath ) {
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
    
    private void doOtherTasks() throws Throwable {
        JSONObject projectInfo = new JSONObject(FileUtils.readFileToString(new File(projectPath + "/pay-info/info.json")));
        {
            String mainApplication = projectInfo.getString("mainApplication");
            File mainApplicationJava = new File(projectPath + "/src/" + mainApplication.replace('.', '/') + ".java");
            String from = "extends Application";
            String to = "extends com.snowfish.cn.ganga.offline.helper.SFOfflineApplication";
            String content = FileUtils.readFileToString(mainApplicationJava);
            content = content.replace(from, to);
            FileUtils.write(mainApplicationJava, content);
            LogUtil.info(mainApplicationJava + " updated");
        }
        {
            String mainActivity = projectInfo.getString("mainActivity");
            File file = new File(projectPath + "/res/values/yijie_strings.xml");
            String content = FileUtils.readFileToString(file).replace("#MAIN-ACTIVITY#", mainActivity);
            FileUtils.write(file, content);
            LogUtil.info(file.getAbsolutePath() + "  updated");
        }
        {
            File file = new File(projectPath + "/pay-info/" + sdkName + "/replace");
            if (file.exists() && file.isDirectory()) {
                Helper.copyDirectory(file.getAbsolutePath(), projectPath);
            }
        }
    }
}
