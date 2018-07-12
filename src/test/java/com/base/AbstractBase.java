package com.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class AbstractBase extends DriverBase{
	public void beforeClass(String url) {
		setDriver();			
		driver.get(url);	
	}
	
	public void  closeDriver(){
		driver.close();
	}
	
	public void clickElement(By by){
		driver.findElement(by).click();
		new WebDriverWait(driver, 1);		
	}
	
	public void clearAndSendkeys(By by,String key){
		driver.findElement(by).clear();
		driver.findElement(by).sendKeys(key);
	}
	
	public String getElementValue(By by){		
		return  driver.findElement(by).getText();
	}
	
	public void assertEquals(String elementType,String elementPath,String expectValue){
		if("xpath".equals(elementType)){
			Assert.assertEquals(( getElementValue(By.xpath(elementPath) )), expectValue);	
		}else if ("cssSelector".equals(elementType)) {
			Assert.assertEquals(( getElementValue(By.cssSelector(elementPath) )), expectValue);
		}else if ("linkText".equals(elementType)) {
			Assert.assertEquals(( getElementValue(By.linkText(elementPath) )), expectValue);
		}else if("className".equals(elementType))	{
			Assert.assertEquals(( getElementValue(By.className(elementPath) )), expectValue);
		}
	}
	
	public void assertUrl(String elementPath,String expectValue) throws InterruptedException{		
		clickElement(By.xpath(elementPath));
		Thread.sleep(1000);
		Set<String> winHandles=driver.getWindowHandles();
		List<String> it=new ArrayList<String>(winHandles);
		driver.switchTo().window(it.get(1));		
		Assert.assertEquals(( driver.getCurrentUrl()), expectValue);		
		driver.close();
		driver.switchTo().window(it.get(0));
	}
	
	public void assertCurrentUrl(String actual, String expected){		
		Assert.assertEquals(actual, expected);
	}

	public void assertEquals(By by,String expected){
		boolean actual=getElementValue(by).contains(expected);
	    Assert.assertEquals(actual, true);
	}
	
	public static String getCellText(WebDriver driver, By by, int row, int cell) {
		WebElement table = driver.findElement(by);
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		WebElement theRow = rows.get(row);
		String text = getCell(theRow, cell).getText();
		return text;
	}
	public static WebElement clickCellButton(WebDriver driver, By by, int row, int cell){
		WebElement table = driver.findElement(by);
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		WebElement theRow = rows.get(row);
		theRow.click();
		return null;
	}
	private static WebElement getCell(WebElement row, int cell) {
		List<WebElement> cells;
		if (row.findElements(By.tagName("td")).size() > 0) {
			cells = row.findElements(By.tagName("td"));//<tr> 标签定义 HTML 表格中的行
			return cells.get(cell);
		}
		if (row.findElements(By.tagName("th")).size() > 0) {
			cells = row.findElements(By.tagName("th"));
			return cells.get(cell);
		}
		return null;
	}
	
	//鼠标悬停选择菜单
	public void moveToElement(By by){
		WebElement element=driver.findElement(by);
		Actions actions=new Actions(driver);
		actions.moveToElement(element).perform();
	}
	
	//设置等待时间直到期望元素出现
	public void visibilityOfelement(int time,By by){
		  new WebDriverWait(driver, time).until(ExpectedConditions.invisibilityOfAllElements(driver.findElements(by)));
	}
}
