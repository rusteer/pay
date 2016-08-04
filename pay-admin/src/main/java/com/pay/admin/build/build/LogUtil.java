package com.pay.admin.build.build;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    protected static Logger logger = LoggerFactory.getLogger("build");
    public static void info(Object o) {
        logger.info(o == null ? "null" : o.toString());
    }
    public static void error(Throwable e) {
        logger.error(e.getMessage(), e);
    }
}
