package com.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
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
	public String expectUrl="http://zy1.dyrt99.com/template/cjs/cjs201807/cjs072601/index.html";
	public String urlBase="https://zyapi.qktz.com.cn/api/user/index?callback=jQuery183043148598985648_1532414181476&";
	public void beforeClass(String url) {
		setDriver();			
		driver.get(url);	
	}
	/*
	 * 需要输入股票代码的模型
	 */
	public void ZhenGTest(String mobilePhone,int i,String gp,String code) throws JSONException, IOException{		
		String url=getUrl();
		clearAndSendkeys(By.id("gp"), gp);
		clickElement(By.id("btnSave"));
		clearAndSendkeys(By.id("mobilePhone"), mobilePhone);
		clickElement(By.className("getinfo"));	
		assertAlert(i);	
		url=url+"&bz=&MobilePhone="+mobilePhone+"&_=1532414292748&stockCode="+gp;
		assertJson(url,code);			
	}
	/*
	 * 只需要输入电话号码的模板
	 */
	public void RenxTest(String mobilePhone,int i,String code) throws InterruptedException, JSONException, IOException, ServletException{		
		String url=getUrl();	
		clearAndSendkeys(By.id("mobilePhone"), mobilePhone);
		clickElement(By.className("phoneBtn"));
		assertAlert(i);
		url=url+"&bz=&MobilePhone="+mobilePhone+"&_=1532414292748";			
		assertJson(url, code);			
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
	/*
	 * 获取当前地址，与期望URL对比
	 */
	public void assertCurrentUrl(String expectUrl){
		String currentUrl=driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, expectUrl);		
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
	/*
	 * 获取网页源代码，从中提取出想要的数据
	 * @parameter code 想要查找的数据标识
	 * @parameter i 从标识开始，取出标识的前i位
	 */
	public String getPageSource(String code,int i){
		String pageSource=driver.getPageSource();
		int SourceTypeid=pageSource.indexOf(code);		
		String SourceType=pageSource.substring(SourceTypeid, SourceTypeid+i);		
		return SourceType;		
	}
	/*
	 * 获取到的sid和reffer需要格式化
	 * sid:替换为SourceType=
	 * reffer:替换为reffer=
	 * 目前只获取sid reffer  所以写死，后期需要再加	
	 */
	public String getReAndSot(String code){
		if(code.equals("\"sid\":")){
			String sourceType=getPageSource("\"sid\":", 9);
			code=sourceType.replace("\"sid\":", "SourceType=");			
		}else if(code.equals("\"reffer\":")){
			String reffer=getPageSource("\"reffer\":", 10);
			code=reffer.replace("\"reffer\":", "reffer=");			
		}
		return code;	
	}
	/*
	 * 传入参数，获取指定字段的值，拼接接口地址
	 * 目前只获取sid reffer  所以写死，后期需要再加	 
	 */
	public String getUrl(){
		String title=driver.getTitle();
		String sourceType=getReAndSot("\"sid\":");			
		String reffer=getReAndSot("\"reffer\":");
		String url=urlBase+sourceType+"&"+reffer+"&Title="+title;
		return url;		
	}	
	/*
	 * 根据正则表达式获取指定字符串间的数据
	 * @parameter soap 对比源字符串
	 * @parameter rgex 正则表达式
	 */
	public static List<String> getSubUtil(String soap,String rgex){  
        List<String> list = new ArrayList<String>();  
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式  
        Matcher m = pattern.matcher(soap);  
        while (m.find()) {  
            int i = 1;  
            list.add(m.group(i));  
            i++;  
        }  
        return list;  
    } 
	/*
	 * 获取接口的数据
	 * 根据正则获取想要的数据
	 * interfaceUrl 接口地址
	 * regex 获取数据的正则
	 */	
	public  List<String> getURL(String interfaceUrl,String regex){
		String str = getHttpInterface(interfaceUrl);
		System.out.println(str);
	    List<String> lists = getSubUtil(str,regex);	    
		return lists;
	}
	/*
	 * 捕获当前页面是否有alert弹框
	 */
	public boolean getAlert(){
		boolean flag = false;        
        try {
            new WebDriverWait(driver, 5).until(ExpectedConditions
                    .alertIsPresent());
            flag = true;
            // alert.accept();
        } catch (NoAlertPresentException NofindAlert) {
            // TODO: handle exception
            NofindAlert.printStackTrace();
            assertCurrentUrl(expectUrl);
            // throw NofindAlert;
        }
		return flag;		
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
		   //返回json数据不符格式，去掉前面的 qktzCallBack(,最后的括号
		   jsonText=jsonText.substring(13,jsonText.length()-1);
		   JSONObject json = JSONObject.fromObject(jsonText); 
		   return json; 
		  } finally { 
			  is.close(); 
		  } 
	} 
	/*
	 * 提取出json数据，转字符串，对比传入的期望值
	 */
	public void assertJson(String url,String code) throws JSONException, IOException{		
		JSONObject json=readJsonFromUrl(url);						
		String jsonValue=json.toString();
		Assert.assertEquals(jsonValue.startsWith(code),true);
	}
	
	/*
	 * 根据传参值捕获alert
	 */
	public void assertAlert(int i){
		if(i==1){
			Alert alert = driver.switchTo().alert();
			Assert.assertEquals(alert.getText(), "请输入正确的手机号码！");
		}else if(i==0){
			assertCurrentUrl(expectUrl);
		}			
	}
	
	
	public static String getURLContent() throws Exception {
		String strURL = "";
        URL url = new URL(strURL);
        HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.connect();
            
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
        String line;
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
        	buffer.append(line);
        }
        reader.close();
        httpConn.disconnect();
         
       System.out.println(buffer.toString());
       return buffer.toString();
	}
	
	/**
	 * 程序中访问http数据接口
	 */
	public static String getURLContent(String urlStr) {
		/** 网络的url地址 */
		URL url = null;
		/** http连接 */
		HttpURLConnection httpConn = null;
		/**//** 输入流 */
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();
		try {
			url = new URL(urlStr);
			in = new BufferedReader(new InputStreamReader(url.openStream(), "GBK"));
			String str = null;
			while ((str = in.readLine()) != null) {
				sb.append(str);
			}
		} catch (Exception ex) {
 
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
		String result = sb.toString();
		System.out.println(result);
		return result;
	}
	// 调用http接口获取数据
    public static String getHttpInterface(String path){
        BufferedReader in = null;
        StringBuffer result = null;
        try {
            URL url = new URL(path);
            //打开和url之间的连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", "utf-8");
            connection.connect();
 
            result = new StringBuffer();
            //读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
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
        System.out.println(request.getRemotePort());//得到请求的资源
        System.out.println(request.getMethod());//得到请求的资源
	}
		
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
	}
	
}
	
