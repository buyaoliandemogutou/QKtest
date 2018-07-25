package com.test;

import java.io.IOException;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.base.AbstractBase;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class NiuGTest extends AbstractBase{
		public String MobilePhone="18583246119";
		/*
		 * 精准测股
		 */
		@Test
		public void  CegTest() throws JSONException, IOException{
			beforeClass("http://ad.cjs.com.cn/template/html/81/5b46bbcbcb081.html");
			clearAndSendkeys(By.id("gp"), "600000");
			clickElement(By.id("btnSave"));
			clearAndSendkeys(By.id("mobilePhone"), MobilePhone);
			clickElement(By.className("getinfo"));
			JSONObject json=readJsonFromUrl("https://zyapi.qktz.com.cn/api/user/index?callback=jQuery183018257501088084793_1532419260653&SourceType=205&reffer=1&bz=&MobilePhone=18583246119&Title=&_=1532419267978");						
			String jsonValue=json.toString();
			Assert.assertEquals(true, jsonValue.startsWith("{\"code\":true"));
			closeDriver();
		}
		
		@Test
		public void RenxTest() throws InterruptedException, JSONException, IOException{
			beforeClass("http://ad.cjs.com.cn/template/html/42/5b46bbc2afd42.html");
			clearAndSendkeys(By.id("mobilePhone"), MobilePhone);
			clickElement(By.className("phoneBtn"));
			Thread.sleep(1000);
			JSONObject json=readJsonFromUrl("https://zyapi.qktz.com.cn/api/user/index?callback=jQuery183043148598985648_1532414181476&SourceType=205&reffer=1&bz=&MobilePhone=18583246119&Title=&_=1532414292748");						
			String jsonValue=json.toString();
			Assert.assertEquals(true, jsonValue.startsWith("{\"code\":true"));			
			closeDriver();
		}
}
