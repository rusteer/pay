package com.pay.admin.build.build.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.pay.admin.build.build.LogUtil;

public class CmdUtil {
    public static int exeCmd(String cmd) {
        LogUtil.info("start cmd:" + cmd);
        int result = 0;
        try {
            Runtime run = Runtime.getRuntime();
            Process p = run.exec(cmd);
            BufferedReader inBr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                LogUtil.info(lineStr);
            }
            while ((lineStr = stdError.readLine()) != null) {
                System.err.println(lineStr);
            }
            if (p.waitFor() != 0) {
                result = p.exitValue();
                if (result != 0) {
                    System.err.println("Error occurs while executing cmd {" + cmd + "} and the return value is " + result);
                }
            }
            inBr.close();
            stdError.close();
        } catch (Exception e) {
            LogUtil.error(e);
        }
        LogUtil.info("end cmd:" + cmd);
        return result;
    }
}
