package com.test;

import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.base.AbstractBase;

/*
 * 验证所有连接后缀带#test的地址，
 * 手机号码验证通过跳转到指定的地址
 * 验证不通过就提示手机号码不正确
 */
public class MobilePhoneTest extends AbstractBase{
	public String expectUrl="http://zy1.dyrt99.com/template/cjs/cjs201807/cjs072601/index.html";
	/*
	 *电话号码正则 (13[0-9]|15[012356789]|17[0134678]|19[89]|18[0-9]|14[57])[0-9]{8}
	 */
	@Test(dataProvider="mobilePhone")
	public void ZhenGTest(String mobilePhone,int i){
		beforeClass("http://ad.cjs.com.cn/template/html/81/5b46bbcbcb081.html#test");
		clearAndSendkeys(By.id("gp"), "600000");
		clickElement(By.id("btnSave"));
		clearAndSendkeys(By.id("mobilePhone"), mobilePhone);
		clickElement(By.className("getinfo"));		
		assertAlert(i);		
	}
	
	@AfterMethod
	public void vloseDriver(){
		driver.close();
		driver.quit();
	}
	
	@DataProvider(name="mobilePhone")
	public Object[][] mobilePhone(){ 
		Object[][] storageList=new Object[][]{
			{"13108225698",0},{"15225632565",0},{"17066986598",0},{"19983246119",0},{"19883246119",0},{"18583246119",0},{"14700253698",0},{"14803699856",1},{"16639859865",1},{"17256896325",1}
		};		
		return storageList;
	}
}
