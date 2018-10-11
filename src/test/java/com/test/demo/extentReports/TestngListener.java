package com.test.demo.extentReports;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.log4testng.Logger;

import com.test.demo.base.BaseParpare;

/**
 * 用TestNG的一个监听器来监听错误时截图
 */
public class TestngListener extends TestListenerAdapter {
	private static Logger logger = Logger.getLogger(TestngListener.class);
	protected ITestContext testContext = null;

	@Override
	public void onStart(ITestContext testContext) {
		this.testContext = testContext;
		super.onStart(testContext);
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		super.onTestFailure(tr);
		logger.warn(tr.getInstanceName() + "-" + tr.getName() + " 测试用例执行失败！");

//		try {			
//			BaseParpareApp bp = (BaseParpareApp) tr.getInstance();
//			WebDriver driver = bp.getDriver();
//			takeScreenShot(tr, driver);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//		}

		try {
			BaseParpare bp = (BaseParpare) tr.getInstance();
			WebDriver driver2 = bp.getDriver();
			takeScreenShot2(tr, driver2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		super.onTestSkipped(tr);
		logger.warn(tr.getInstanceName() + "-" + tr.getName() + " 测试用例由于某些原因被跳过！");

//		try {
//			BaseParpareApp bp = (BaseParpareApp) tr.getInstance();
//			WebDriver driver = bp.getDriver();
//			// takeScreenShot(tr, driver);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//		}

		try {
			BaseParpare bp = (BaseParpare) tr.getInstance();
			WebDriver driver2 = bp.getDriver();
			// takeScreenShot2(tr, driver2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		super.onTestSuccess(tr);
		logger.info(tr.getInstanceName() + "-" + tr.getName() + " 测试用例执行成功！");
	}

	@Override
	public void onTestStart(ITestResult tr) {
		super.onTestStart(tr);
		logger.info(tr.getInstanceName() + "-" + tr.getName() + " 测试用例开始执行！");
	}

	@Override
	public void onFinish(ITestContext testContext) {
		super.onFinish(testContext);

		// List of test results which we will delete later
		ArrayList<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();
		// collect all id's from passed test
		Set<Integer> passedTestIds = new HashSet<Integer>();
		for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
			logger.info("执行成功的用例 = " + passedTest.getName());
			passedTestIds.add(getId(passedTest));
		}

		// Eliminate the repeat methods
		Set<Integer> skipTestIds = new HashSet<Integer>();
		for (ITestResult skipTest : testContext.getSkippedTests().getAllResults()) {
			logger.info("被跳过的用例 = " + skipTest.getName());
			// id = class + method + dataprovider
			int skipTestId = getId(skipTest);

			if (skipTestIds.contains(skipTestId) || passedTestIds.contains(skipTestId)) {
				testsToBeRemoved.add(skipTest);
			} else {
				skipTestIds.add(skipTestId);
			}
		}

		// Eliminate the repeat failed methods
		Set<Integer> failedTestIds = new HashSet<Integer>();
		for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {
			logger.info("执行失败的用例 = " + failedTest.getName());
			// id = class + method + dataprovider
			int failedTestId = getId(failedTest);

			// if we saw this test as a failed test before we mark as to be
			// deleted
			// or delete this failed test if there is at least one passed
			// version
			if (failedTestIds.contains(failedTestId) || passedTestIds.contains(failedTestId)) {
				testsToBeRemoved.add(failedTest);
			} else {
				failedTestIds.add(failedTestId);
			}
		}

		// finally delete all tests that are marked
		for (Iterator<ITestResult> iterator = testContext.getFailedTests().getAllResults().iterator(); iterator
				.hasNext();) {
			ITestResult testResult = iterator.next();
			if (testsToBeRemoved.contains(testResult)) {
				logger.info("移除重复失败的用例 = " + testResult.getName());
				iterator.remove();
			}
		}

	}

	private int getId(ITestResult result) {
		int id = result.getTestClass().getName().hashCode();
		id = id + result.getMethod().getMethodName().hashCode();
		id = id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
		return id;
	}

	/**
	 * 自动截图，保存图片到本地以及html结果文件中
	 * 
	 * @param tr
	 * @param driver
	 */
	private void takeScreenShot(ITestResult tr, WebDriver driver) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String mDateTime = formatter.format(new Date());
		String screenName = mDateTime + "_" + tr.getInstanceName() + "-" + tr.getName() + ".jpg";
		File dir = new File("result/test-report/snapshot");
		if (!dir.exists())
			dir.mkdirs();
		String screenPath = dir.getAbsolutePath() + "/" + screenName;
		try {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(screenPath));
			System.out.println("[" + screenName + "] 截图成功，保存在：" + "[ " + screenPath + " ]");
		} catch (IOException e) {
			// screenPath = "[" + screenName + "]" + " 截图失败，原因：" +
			// e.getMessage();
			// logger.error(screenPath);
		}
		Reporter.setCurrentTestResult(tr);
		// 将\转为/
		String Path = screenPath.replaceAll("\\\\", "/");
		Reporter.log("报错截图地址：" + Path);

		// String[] screenPathLink= new String[2];
		// screenPathLink = Path.split("test-report");
		// // Link 图片链接
		// Reporter.log("<a href=." + screenPathLink[1] + " target=_blank>Failed
		// Screen Shot</a>", true);

		String[] screenPathImg = new String[2];
		screenPathImg = Path.split("test-report");
		// Img 这里实现把图片链接直接输出到结果文件中，通过邮件发送结果则可以直接显示图片
		// Reporter.log("<img src=\"." + screenPathImg[1] + "\" width='50%'
		// />");
		// ExtentTestNGIReporterListener方法会吧<转义成&lt; >转义成&gt; 将#a标记为< 将a#标记为>
		Reporter.log("#aimg src=\"." + screenPathImg[1] + "\" width='60%'/a#");
	}

	/**
	 * 自动截图，保存图片到本地以及html结果文件中
	 * 
	 * @param tr
	 * @param driver
	 */
	private void takeScreenShot2(ITestResult tr, WebDriver driver2) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String mDateTime = formatter.format(new Date());
		String screenName = mDateTime + "_" + tr.getInstanceName() + "-" + tr.getName() + ".jpg";
		File dir = new File("result/test-report/snapshot");
		if (!dir.exists())
			dir.mkdirs();
		String screenPath = dir.getAbsolutePath() + "/" + screenName;
		try {
			File scrFile = ((TakesScreenshot) driver2).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(screenPath));
			System.out.println("[" + screenName + "] 截图成功，保存在：" + "[ " + screenPath + " ]");
		} catch (IOException e) {
			// screenPath = "[" + screenName + "]" + " 截图失败，原因：" +
			// e.getMessage();
			// logger.error(screenPath);
		}
		Reporter.setCurrentTestResult(tr);
		// 将\转为/
		String Path = screenPath.replaceAll("\\\\", "/");
		Reporter.log("报错截图地址：" + Path);

		// String[] screenPathLink= new String[2];
		// screenPathLink = Path.split("test-report");
		// // Link 图片链接
		// Reporter.log("<a href=." + screenPathLink[1] + " target=_blank>Failed
		// Screen Shot</a>", true);

		String[] screenPathImg = new String[2];
		screenPathImg = Path.split("test-report");
		// Img 这里实现把图片链接直接输出到结果文件中，通过邮件发送结果则可以直接显示图片
		// Reporter.log("<img src=\"." + screenPathImg[1] + "\" width='50%'
		// />");
		// ExtentTestNGIReporterListener方法会吧<转义成&lt; >转义成&gt; 将#a标记为< 将a#标记为>
		Reporter.log("#aimg src=\"." + screenPathImg[1] + "\" /a#");
	}
}
