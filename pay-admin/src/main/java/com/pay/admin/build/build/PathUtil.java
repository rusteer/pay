package com.pay.admin.build.build;

public class PathUtil {
    public static final boolean OS_WIN = System.getProperty("os.name").toLowerCase().contains("win");
    public static final String DISK_PREFIX = OS_WIN ? "E:" : "";
    public static final String APK_PATH_PREFIX = "/mnt/apks";
}
