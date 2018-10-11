package com.test.demo.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.test.demo.utils.ParseProperties;

public class BaseParpare {

	public WebDriver driver;

	private String projectpath = System.getProperty("user.dir");
	private ParseProperties driverData = new ParseProperties(System.getProperty("user.dir") + "/config/browserdriver.properties");
	
	public WebDriver getDriver() {
		return driver;
	}

	@BeforeClass
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", projectpath + driverData.getValue("chromedriver"));
		ChromeOptions option = new ChromeOptions();
		option.addArguments("disable-infobars");
		driver = new ChromeDriver(option);
		driver.manage().window().maximize();
		driver.navigate().to("http://www.baidu.com");
	}

	@AfterClass
	public void tearDown() {
		driver.close();
		driver.quit();
	}

}
