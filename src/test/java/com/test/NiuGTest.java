package com.test;

import java.io.IOException;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.base.AbstractBase;
import net.sf.json.JSONException;
import javax.servlet.ServletException;    


public class NiuGTest extends AbstractBase{		
		/*
		 * 精准测股
		 */
		@Test(dataProvider="inputData")
		public void  CegTest(String gp,String mobilePhone,String code) throws JSONException, IOException, InterruptedException, ServletException{			
			beforeClass("http://ad.cjs.com.cn/template/html/81/5b46bbcbcb081.html");
			String url=getUrl();		
			clearAndSendkeys(By.id("gp"), gp);
			clickElement(By.id("btnSave"));
			clearAndSendkeys(By.id("mobilePhone"), mobilePhone);
			clickElement(By.className("getinfo"));			
			url=url+"&bz=&MobilePhone="+mobilePhone+"&_=1532414292748&stockCode="+gp;		
			assertJson(url, code);			
		}
		
		@Test(dataProvider="mobilePhone")
		public void RenxTest(String mobilePhone,String code) throws InterruptedException, JSONException, IOException, ServletException{
			beforeClass("http://ad.cjs.com.cn/template/html/42/5b46bbc2afd42.html");
			String url=getUrl();	
			clearAndSendkeys(By.id("mobilePhone"), mobilePhone);
			clickElement(By.className("phoneBtn"));						
			url=url+"&bz=&MobilePhone="+mobilePhone+"&_=1532414292748";			
			assertJson(url, code);			
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
