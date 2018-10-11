package com.test.demo.extentReports;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.log4testng.Logger;

/**
 * 设置报错重试执行次数
 */
public class TestngRetry implements IRetryAnalyzer {
    private static Logger logger = Logger.getLogger(TestngRetry.class);
    private static int maxRetryCount = 0; //最大重试次数（0为关闭重复执行，大于0为打开重复执行）
    private int retryCount = 1; //重试次数

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount <= maxRetryCount) {
            String message = "Running retry for '" + result.getName()
                    + "' on class " + this.getClass().getName() + " Retrying "
                    + retryCount + " times";
            logger.info(message);
            Reporter.setCurrentTestResult(result);
            Reporter.log("RunCount=" + (retryCount + 1));
            retryCount++;
            return true;
        }
        return false;
    }
}
