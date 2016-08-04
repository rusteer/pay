package com.pay.admin.build.build.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import com.pay.admin.build.build.LogUtil;
import com.pay.admin.build.build.PathUtil;

public abstract class Helper {
    public static String getSdkRoot() {
        return PathUtil.DISK_PREFIX + "/workspace/pay/code/PaysdkHelper/sdks";
    }
    public static void copyFile(String from, String to) throws Throwable {
        FileUtils.copyFile(new File(from), new File(to));
        LogUtil.info(from + " copied to " + to);
    }
    public static void copyDirectory(String from, String to) throws Throwable {
        FileUtils.copyDirectory(new File(from), new File(to), true);
        LogUtil.info(from + " copied to " + to);
    }
    public static void deleteDirectory(String from) throws Throwable {
        FileUtils.deleteDirectory(new File(from));
        LogUtil.info(from + " deleted ");
    }
    public static void deleteFile(String from) throws Throwable {
        FileUtils.deleteQuietly(new File(from));
        LogUtil.info(from + " deleted ");
    }
    public static String searchEnclosed(String target, String left, String right) {
        return searchEnclosed(target, left, right, 0);
    }
    public static String searchEnclosed(String target, String left, String right, int matcherIndex) {
        if (!StringUtils.isEmpty(target) && !StringUtils.isEmpty(left) && !StringUtils.isEmpty(right)) {
            int index = 0;
            int leftPlace = 0;
            int rightPlace = 0;
            int fromPlace = 0;
            while (leftPlace >= 0 && rightPlace >= 0) {
                leftPlace = target.indexOf(left, fromPlace);
                if (leftPlace >= 0) {
                    fromPlace = leftPlace + left.length() + 1;
                    rightPlace = target.indexOf(right, fromPlace);
                    if (rightPlace > 0 && (matcherIndex == -1 || matcherIndex == index++)) {//
                        return target.substring(leftPlace + left.length(), rightPlace);
                    }
                    fromPlace = rightPlace + right.length() + 1;
                }
            }
        }
        return null;
    }
    public static void replaceAttributes(String filePath, ReplaceModel... listArray) {
        List<ReplaceModel> list = new ArrayList<ReplaceModel>();
        for (ReplaceModel model : listArray) {
            list.add(model);
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedreader = null;
        try {
            bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String line;
            while ((line = bufferedreader.readLine()) != null) {
                if (!list.isEmpty()) {
                    for (int i = list.size() - 1; i >= 0; --i) {
                        ReplaceModel model = list.get(i);
                        int beginIndex = line.indexOf(model.begin);
                        if (beginIndex >= 0) {
                            int endIndex = line.indexOf(model.end, beginIndex + model.begin.length() + 1);
                            if (endIndex > beginIndex) {
                                // LogUtil.info(line);
                                line = line.substring(0, beginIndex) + model.begin + model.value + line.substring(endIndex);
                                // LogUtil.info(line);
                                list.remove(i);
                            }
                        }
                    }
                }
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            LogUtil.error(e);
            return;
        } finally {
            if (bufferedreader != null) try {
                bufferedreader.close();
            } catch (IOException e) {
                LogUtil.error(e);
            }
        }
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
            out.write(sb.toString());
            out.flush();
        } catch (IOException e) {
            LogUtil.error(e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                LogUtil.error(e);
            }
        }
    }
    public static String getPassword(String projectPath) throws Throwable {
        File configFile = new File(projectPath + "/AndroidManifest.xml");
        String configContent = FileUtils.readFileToString(configFile);
        String packageName = Helper.searchEnclosed(configContent, "package=\"", "\"");
        return getPasswordByPackageName(packageName);
    }
    private static String getPasswordByPackageName(String packageName) {
        if (packageName.length() >= 16) {
            return packageName.substring(0, 16);
        } else {
            while (packageName.length() < 16) {
                packageName += "w";
            }
            return packageName;
        }
    }
}
