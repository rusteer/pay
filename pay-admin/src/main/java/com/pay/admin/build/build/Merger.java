package com.pay.admin.build.build;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import com.pay.admin.build.build.impl.BaiduMerger;
import com.pay.admin.build.build.impl.BbPayMerger;
import com.pay.admin.build.build.impl.DemoMerger;
import com.pay.admin.build.build.impl.DianwoMerger;
import com.pay.admin.build.build.impl.DisableMerger;
import com.pay.admin.build.build.impl.DtAdsMerger;
import com.pay.admin.build.build.impl.DuotuiMerger;
import com.pay.admin.build.build.impl.EhooMerger;
import com.pay.admin.build.build.impl.FengyiMerger;
import com.pay.admin.build.build.impl.FreeMerger;
import com.pay.admin.build.build.impl.GulongAdsMerger;
import com.pay.admin.build.build.impl.HongchiMerger;
import com.pay.admin.build.build.impl.HurrayMerger;
import com.pay.admin.build.build.impl.HyPayMerger;
import com.pay.admin.build.build.impl.JDMerger;
import com.pay.admin.build.build.impl.JJMerger;
import com.pay.admin.build.build.impl.JPayMerger;
import com.pay.admin.build.build.impl.JhtcMerger;
import com.pay.admin.build.build.impl.LetuPayMerger;
import com.pay.admin.build.build.impl.LinkyunMerger;
import com.pay.admin.build.build.impl.MmMerger;
import com.pay.admin.build.build.impl.MyEpayMerger;
import com.pay.admin.build.build.impl.QihooMerger;
import com.pay.admin.build.build.impl.ShuairuiMerger;
import com.pay.admin.build.build.impl.SkyMerger;
import com.pay.admin.build.build.impl.Sms360Merger;
import com.pay.admin.build.build.impl.UniPayMerger;
import com.pay.admin.build.build.impl.WPayMerger;
import com.pay.admin.build.build.impl.YijieMerger;
import com.pay.admin.build.build.impl.YousdkMerger;
import com.pay.admin.build.build.impl.ZPayMerger;
import com.pay.admin.build.build.utils.AES;
import com.pay.admin.build.build.utils.CmdUtil;
import com.pay.admin.build.build.utils.ConstantReplacer;
import com.pay.admin.build.build.utils.Helper;
import com.pay.admin.build.build.utils.JsonUtil;
import com.pay.admin.build.build.utils.ReplaceModel;
import com.pay.admin.utils.SdkConstants;

public class Merger {
    public static String getRandomString(int length) {
        //String content = "abceghjklmnopqrstuvxyzABCEHKMXYZ";
        String content = "تسعتسعينسعين";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(content.charAt(random.nextInt(content.length())));
        }
        return sb.toString();
    }
    private static void updateAR(String projectPath, String packageName) throws Exception {
        File file = new File(projectPath + "/src/biz/AR.java");
        StringBuilder sb = new StringBuilder();
        boolean replaced = false;
        if (file.exists()) {
            List<String> contents = FileUtils.readLines(file);
            for (String line : contents) {
                if (!replaced && line.contains("import")) {
                    sb.append("import " + packageName + ".R;").append("\n");
                    replaced = true;
                } else {
                    sb.append(line).append("\n");
                }
            }
        }
        FileUtils.write(file, sb.toString());
    }
    public static String doMerge(String hostName, JSONObject pkg, JSONObject channel, JSONObject product, String[] sdkArray) throws Throwable {
        LogUtil.info(hostName);
        LogUtil.info(pkg);
        LogUtil.info(channel);
        LogUtil.info(product);
        LogUtil.info(sdkArray);
        String projectName = product.getString("projectName");
        String packageName = pkg.getString("packageName");
        String name = pkg.getString("name");
        String appName = pkg.getString("appName");
        String versionName = pkg.getString("versionName");
        int versionCode = pkg.getInt("versionCode");
        int channelId = pkg.getInt("channelId");
        String pkgInfo = pkg.optString("info");
        //String channelShortName = channel.getString("shortName");
        String channelDisplayName = channel.getString("displayName");
        int packageId = pkg.getInt("id");
        //-----------------------------
        List<String> list = new ArrayList<String>();
        StringBuilder encodedSdks = new StringBuilder();
        StringBuilder projectSuffix = new StringBuilder("-build-");
        for (String sdk : sdkArray) {
            list.add(sdk);
        }
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            String sdk = list.get(i);
            encodedSdks.append(SdkConstants.getEncodedSdk(sdk));
            projectSuffix.append(sdk);
            if (i < sdkArray.length - 1) {
                encodedSdks.append(",");
                projectSuffix.append("-");
            }
        }
        projectSuffix.append("-").append(channelId).append("-").append(packageId);
        String projectRoot = product.getString("codePath");
        String path1 = PathUtil.DISK_PREFIX + "/workspace/games/";
        if (new File(path1).exists()) {
            CmdUtil.exeCmd("svn up " + path1);
        }
        String path2 = PathUtil.DISK_PREFIX + "/workspace/pay/code/projects/";
        if (new File(path2).exists()) {
            CmdUtil.exeCmd("svn up " + path2);
        }
        CmdUtil.exeCmd("svn up " + PathUtil.DISK_PREFIX + "/workspace/pay/code/sdk");
        String newProjectName = projectName + projectSuffix.toString();
        // String projectPath = projectRoot + "/branches/" + newProjectName;
        String projectPath = PathUtil.DISK_PREFIX + "/mnt/tmp/game-build/" + newProjectName;
        {
            Helper.deleteDirectory(projectPath + "/src");
            Helper.deleteDirectory(projectPath + "/libs");
            Helper.deleteDirectory(projectPath + "/assets");
            Helper.deleteDirectory(projectPath + "/res");
        }
        Helper.copyDirectory(projectRoot + "/trunk", projectPath);
        {
            Helper.deleteDirectory(projectPath + "/resources");
            Helper.deleteDirectory(projectPath + "/bin");
            Helper.deleteDirectory(projectPath + "/gen");
        }
        //replace project name in .project
        {
            File file = new File(projectPath + "/.project");
            String content = FileUtils.readFileToString(file);
            content = content.replace(projectName, newProjectName);
            FileUtils.write(file, content);
        }
        File src = new File(projectPath + "/src");
        if (!src.exists()) src.mkdir();
        Helper.copyDirectory(PathUtil.DISK_PREFIX + "/workspace/pay/code/sdk/paylib/src", src.getAbsolutePath());
        Helper.copyDirectory(PathUtil.DISK_PREFIX + "/workspace/pay/code/sdk/paylib/runtime-src", src.getAbsolutePath());
        String configFilePath = projectPath + "/AndroidManifest.xml";
        if (StringUtils.isNotBlank(versionName)) {
            Helper.replaceAttributes(configFilePath, new ReplaceModel("android:versionName=\"", "\"", versionName));
            LogUtil.info("versionName updated");
        }
        if (versionCode > 0) {
            Helper.replaceAttributes(configFilePath, new ReplaceModel("android:versionCode=\"", "\"", versionCode + ""));
            LogUtil.info("versionCode updated");
        }
        if (StringUtils.isNotBlank(packageName)) {
            Helper.replaceAttributes(configFilePath, new ReplaceModel("package=\"", "\"", packageName));
            LogUtil.info("packageName updated");
        }
        String bizFile = projectPath + "/res/values/biz_strings.xml";
        if (StringUtils.isNotBlank(appName)) {
            Helper.replaceAttributes(bizFile, new ReplaceModel("<string name=\"app_name\">", "</string>", appName));
            LogUtil.info("appName updated");
        }
        {
            String infoString = channel.optString("info");
            if (StringUtils.isNotBlank(infoString)) {
                JSONObject info = new JSONObject(infoString);
                boolean showLuckyDraw = info.optBoolean("showLuckyDraw");
                if (showLuckyDraw) {
                    String bizContent = FileUtils.readFileToString(new File(bizFile));
                    bizContent = bizContent.replace("</resources>", "<string name=\"sld\">true</string>\n</resources>");
                    FileUtils.write(new File(bizFile), bizContent);
                }
                boolean showActivate = info.optBoolean("showActivate");
                if (showActivate) {
                    String bizContent = FileUtils.readFileToString(new File(bizFile));
                    bizContent = bizContent.replace("</resources>", "<string name=\"sa\">true</string>\n</resources>");
                    FileUtils.write(new File(bizFile), bizContent);
                }
            }
        }
        Helper.replaceAttributes(projectPath + "/res/values/biz_strings.xml", new ReplaceModel("<string name=\"channel_name\">", "</string>", channelId + ""));
        LogUtil.info("channelName updated");
        //
        {
            File constFile = new File(projectPath + "/src/com/thumb/payapi/utils/Constants.java");
            String content = FileUtils.readFileToString(constFile);
            content = content.replace("228213228213L", packageId + "L");
            //content = content.replace("SHORTSHORTSHORT", channelShortName);
            content = content.replace("MYHOSTNAME", hostName);
            FileUtils.write(constFile, content);
            LogUtil.info(constFile + "  updated");
        }
        //
        Helper.copyDirectory(PathUtil.DISK_PREFIX + "/workspace/pay/code/sdk/paylib/libs", projectPath + "/libs");
        {
            File file = new File(projectPath + "/assets/pi/pcdata.db");
            FileUtils.write(file, AES.encode(encodedSdks.toString(), Helper.getPassword(projectPath)));
            LogUtil.info(file.getAbsolutePath() + "  created");
        }
        {
            File proguardKeyFile = new File(projectPath + "/proguard-keys.txt");
            StringBuilder sb = new StringBuilder();
            int lines = 5000;
            Random random = new Random();
            while ((lines--) > 0) {
                sb.append(getRandomString(50 + random.nextInt(50))).append("\n");
            }
            FileUtils.write(proguardKeyFile, sb.toString());
        }
        {
            JSONObject info = JsonUtil.readJson(projectPath + "/pay-info/info.json");
            String soName = info.optString("soName");
            if (StringUtils.isNotEmpty(soName)) {
                String toFile = projectPath + "/libs/armeabi/" + soName;
                String fromFile = projectPath + "/package-libs/" + packageName + ".so";
                Helper.copyFile(fromFile, toFile);
            }
        }
        if (list.contains(SdkConstants.MM)) {
            new MmMerger(projectName, projectPath).merge();
        }
        if (list.contains(SdkConstants.SKY)) {
            new SkyMerger(projectName, projectPath, channel).merge();
        }
        if (list.contains(SdkConstants.JD)) {
            new JDMerger(projectName, projectPath).merge();
        }
        if (list.contains(SdkConstants.QIHOO)) {
            new QihooMerger(projectName, projectPath, packageName).merge();
        }
        if (list.contains(SdkConstants.BAIDU)) {
            new BaiduMerger(projectName, projectPath).merge();
        }
        if (list.contains(SdkConstants.JJ)) {
            new JJMerger(projectName, projectPath, channelId).merge();
        }
        if (list.contains(SdkConstants.DEMO)) {
            new DemoMerger(projectName, projectPath).merge();
        }
        if (list.contains(SdkConstants.MYEPAY)) {
            new MyEpayMerger(projectName, projectPath).merge();
        }
        if (list.contains(SdkConstants.YIJIE)) {
            new YijieMerger(projectName, projectPath).merge();
        }
        if (list.contains(SdkConstants.DISABLE)) {
            new DisableMerger(projectName, projectPath).merge();
        }
        if (list.contains(SdkConstants.LINKYUN)) {
            new LinkyunMerger(projectName, projectPath).merge();
        }
        if (list.contains(SdkConstants.HURRAY)) {
            new HurrayMerger(projectName, projectPath, channelId).merge();
        }
        if (list.contains(SdkConstants.WPAY)) {
            new WPayMerger(projectName, projectPath, channel).merge();
        }
        if (list.contains(SdkConstants.DUOTUI)) {
            new DuotuiMerger(projectName, projectPath, packageId).merge();
        }
        if (list.contains(SdkConstants.FREE)) {
            new FreeMerger(projectName, projectPath).merge();
        }
        if (list.contains(SdkConstants.JPAY)) {
            new JPayMerger(projectName, projectPath, channelId).merge();
        }
        if (list.contains(SdkConstants.HYPAY)) {
            new HyPayMerger(projectName, projectPath, channelId).merge();
        }
        if (list.contains(SdkConstants.HONGCHI)) {
            new HongchiMerger(projectName, projectPath, channelId, pkgInfo).merge();
        }
        if (list.contains(SdkConstants.FENGYI)) {
            new FengyiMerger(projectName, projectPath, pkgInfo).merge();
        }
        if (list.contains(SdkConstants.DIANWO)) {
            new DianwoMerger(projectName, projectPath, channelId).merge();
        }
        if (list.contains(SdkConstants.YOUSDK)) {
            new YousdkMerger(projectName, projectPath, channelId).merge();
        }
        if (list.contains(SdkConstants.BBPAY)) {
            new BbPayMerger(projectName, projectPath, channelId).merge();
        }
        if (list.contains(SdkConstants.LETU)) {
            new LetuPayMerger(projectName, projectPath, packageId).merge();
        }
        if (list.contains(SdkConstants.SHUAIRUI)) {
            new ShuairuiMerger(projectName, projectPath,packageId).merge();
        }
        if (list.contains(SdkConstants.UNIPAY)) {
            new UniPayMerger(projectName, projectPath).merge();
        }
        if (list.contains(SdkConstants.JHTC)) {
            new JhtcMerger(projectName, projectPath, pkgInfo).merge();
        }
        if (list.contains(SdkConstants.ZPAY)) {
            new ZPayMerger(projectName, projectPath, pkgInfo).merge();
        }
        if (list.contains(SdkConstants.EHOO)) {
            new EhooMerger(projectName, projectPath, channelId).merge();
        }
        if (list.contains(SdkConstants.GULONG)) {
            new GulongAdsMerger(projectName, projectPath).merge();
        }
        if (list.contains(SdkConstants.SMS360)) {
            new Sms360Merger(projectName, projectPath, packageName).merge();
        }
        if (list.contains(SdkConstants.DTADS)) {
            new DtAdsMerger(projectName, projectPath, packageId).merge();
        }
        {
            Helper.deleteFile(projectPath + "/assets/pi/paylist.txt");
        }
        updateAR(projectPath, packageName);
        ConstantReplacer.replace(projectPath);
        return build(appName, packageId, channelId, sdkArray, projectPath, channelDisplayName, name);
        //return null;
    }
    private static String build(String appName, int packageId, int channelId, String[] sdkArray, String projectPath, String channelDisplayName, String pkgAlias) throws IOException {
        if (!PathUtil.OS_WIN) {
            String localPropertiesFile = projectPath + "/local.properties";
            FileUtils.write(new File(localPropertiesFile), "sdk.dir=/usr/local/android/sdk");
            CmdUtil.exeCmd("sh " + projectPath + "/build.sh");
        } else {
            CmdUtil.exeCmd(projectPath + "/build.bat");
        }
        String apkPath = getApkPathSuffix(appName, channelDisplayName, sdkArray, packageId, pkgAlias);
        String finalPath = PathUtil.DISK_PREFIX + PathUtil.APK_PATH_PREFIX + apkPath;
        FileUtils.copyFile(new File(projectPath + "/bin/main-release.apk"), new File(finalPath));
        LogUtil.info(finalPath + " generated");
        return apkPath;
    }
    public static final String getApkPathSuffix(String appName, String channelDisplayName, String sdkArray[], int pkgId, String pkgAlias) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < sdkArray.length; i++) {
            sb.append(sdkArray[i]);
            if (i < sdkArray.length - 1) {
                sb.append("-");
            }
        }
        sb.append(")");
        String finalPath = "/" + appName + "-" + channelDisplayName + "-" + pkgAlias + "-p" + pkgId + "-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date())
                + sb.toString() + ".apk";
        return finalPath;
    }
}
