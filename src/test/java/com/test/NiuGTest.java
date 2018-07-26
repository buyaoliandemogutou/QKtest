package com.test;

import java.io.IOException;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.base.AbstractBase;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import javax.servlet.ServletException;    


public class NiuGTest extends AbstractBase{
		public String urlBase="https://zyapi.qktz.com.cn/api/user/index?callback=jQuery183043148598985648_1532414181476&";
		/*
		 * 精准测股
		 */
		@Test(dataProvider="inputData")
		public void  CegTest(String gp,String mobilePhone,String code) throws JSONException, IOException, InterruptedException, ServletException{			
			beforeClass("http://ad.cjs.com.cn/template/html/81/5b46bbcbcb081.html");
			String title=driver.getTitle();
			String sourceType=getReAndSot("\"sid\":");			
			String reffer=getReAndSot("\"reffer\":");			
			clearAndSendkeys(By.id("gp"), gp);
			clickElement(By.id("btnSave"));
			clearAndSendkeys(By.id("mobilePhone"), mobilePhone);
			clickElement(By.className("getinfo"));			
			String url=urlBase+sourceType+"&"+reffer+"&bz=&MobilePhone="+mobilePhone+"&Title="+title+"&_=1532414292748&stockCode="+gp;		
			JSONObject json=readJsonFromUrl(url);						
			String jsonValue=json.toString();
			Assert.assertEquals( jsonValue.startsWith(code),true);
		}
		
		@Test(dataProvider="mobilePhone")
		public void RenxTest(String mobilePhone,String code) throws InterruptedException, JSONException, IOException, ServletException{
			beforeClass("http://ad.cjs.com.cn/template/html/42/5b46bbc2afd42.html");
			String title=driver.getTitle();
			String sourceType=getReAndSot("\"sid\":");			
			String reffer=getReAndSot("\"reffer\":");	
			clearAndSendkeys(By.id("mobilePhone"), mobilePhone);
			clickElement(By.className("phoneBtn"));						
			String url=urlBase+sourceType+"&"+reffer+"&bz=&MobilePhone="+mobilePhone+"&Title="+title+"&_=1532414292748";
			JSONObject json=readJsonFromUrl(url);						
			String jsonValue=json.toString();
			Assert.assertEquals(jsonValue.startsWith(code),true);			
		}
		@AfterMethod
		public void aftermethod(){
			driver.close();
			driver.quit();
		}
		
		@DataProvider(name="inputData")
		public Object[][] inputData(){
			Object[][] storageList=new Object[][]{
				{"600000","18109045175","{\"code\":false"},{"600000","18583246119","{\"code\":true"}
			};		
			return storageList;
		}
		@DataProvider(name="mobilePhone")
		public Object[][] mobilePhone(){
			Object[][] storageList=new Object[][]{
				{"18109045175","{\"code\":false"},{"18583246119","{\"code\":true"}
			};		
			return storageList;
		}
		
}
