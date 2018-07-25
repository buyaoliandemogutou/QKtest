package com.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverBase {
	public static WebDriver driver;
	public void setDriver(){
		System.setProperty("webdriver.chrome.driver", "F:/BaiduNetdiskDownload/aboutjava/chromedriver.exe");
		driver=new ChromeDriver();
		driver.manage().window().maximize();		
	}
}
