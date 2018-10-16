package com.test.demo.testcases;

import org.openqa.selenium.By;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.test.demo.base.BaseParpare;

import io.qameta.allure.Description;
import ru.yandex.qatools.allure.annotations.Title;

public class CaseDemo extends BaseParpare {

	@Title("APP-授信申请 case01_app_credit")
	@Description("打开APP，进行注册登录等操作")
	@Test
	public void webTestCase1() {
		// ExtentReports报告中输出log
        Reporter.log("执行webTestCase1");
		driver.findElement(By.id("kw")).sendKeys("selenium");
	}

	@Test
	public void webTestCase2() {
		// ExtentReports报告中输出log
        Reporter.log("执行webTestCase2");
		driver.findElement(By.id("su1")).click();
	}

	@Test
	public void webTestCase3() {
		// ExtentReports报告中输出log
        Reporter.log("执行webTestCase3");
		System.out.println(driver.getTitle());
	}

}
