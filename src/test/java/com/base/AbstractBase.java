package com.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


public class AbstractBase extends DriverBase{
	HttpServletRequest request;
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
	
 
	public JSONObject readJsonFromUrl(String url) throws IOException, JSONException { 
		  InputStream is = new URL(url).openStream(); 
		  try { 
		   BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8"))); 
		   StringBuilder sb = new StringBuilder(); 
		   int cp; 
		   while ((cp = rd.read()) != -1) { 
			   sb.append((char) cp); 
		   } 
		   String jsonText = sb.toString();
		   //返回json数据不符格式，去掉前面的 qktzCallBack(
		   jsonText=jsonText.substring(13);
		   JSONObject json = JSONObject.fromObject(jsonText); 
		   return json; 
		  } finally { 
			  is.close(); 
		  } 
	} 
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /**
         * URL是URI的子集。
         * URI用来标识一个资源。
         * URL用来标识互联网上的一个资源。
         */		
		String callback=request.getParameter("callback");
		System.out.println(callback);
        System.out.println(request.getRequestURL());//得到请求URL地址
        System.out.println(request.getRequestURI());//得到请求的资源
        System.out.println(request.getQueryString());
        System.out.println(request.getRemoteAddr());//得到来访者IP
        System.out.println(request.getRemoteHost());
        //由于没有在dns上注册所以打印结果还是127.0.0.1,如果是百度访问这个程序，则打印www.baidu.com
        System.out.println(request.getRemotePort());//得到请求的资源
        System.out.println(request.getMethod());//得到请求的资源
	}
		
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
	}
	
}
	
