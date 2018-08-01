package com.test;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.base.AbstractBase;
import net.sf.json.JSONException;

/*
 * 验证所有连接后缀带#test的地址，
 * 手机号码验证通过跳转到指定的地址
 * 验证不通过就提示手机号码不正确
 */
public class MobilePhoneTest extends AbstractBase{
	/*
	 *电话号码正则 (13[0-9]|15[012356789]|17[0134678]|19[89]|18[0-9]|14[57])[0-9]{8}
	 */
	@Test(dataProvider="gp")
	public void testGp(String mobilePhone,int init,String gp,String code) throws JSONException, IOException{
		String urlRgex="\\{\"date\":(.*?),\"hs300\"";
		String typeRgex="\\{\"date\":(.*?),\"hs300\"";
		String inUrl="https://iseedemo.yjifs.com/tools/tools/get_industry_lines";
		List<String> urlLists=getURL(inUrl, urlRgex);
		List<String> typeLists=getURL(inUrl, typeRgex);
		for (int i=0;i<urlLists.size();i++) {
			if("0".equals(typeLists.get(i))){
				String url=urlLists.get(i);
				beforeClass(url);
				ZhenGTest(mobilePhone, init, gp, code);
			}
		}
	}
	@Test(dataProvider="mobilePhone")
	public void testN(String mobilePhone,int init,String code) throws JSONException, IOException, InterruptedException, ServletException{
		String urlRgex="\\{\"date\":(.*?),\"hs300\"";
		String typeRgex="\\{\"date\":(.*?),\"hs300\"";
		String inUrl="https://iseedemo.yjifs.com/tools/tools/get_industry_lines";
		List<String> urlLists=getURL(inUrl, urlRgex);
		List<String> typeLists=getURL(inUrl, typeRgex);
		for (int i=0;i<urlLists.size();i++) {
			if("1".equals(typeLists.get(i))){
				String url=urlLists.get(i);
				beforeClass(url);
				RenxTest(mobilePhone, init, code);
			}		
		}
	}
	
	
	@AfterMethod
	public void vloseDriver(){
		driver.close();
		driver.quit();
	}
	//需要输入股票代码、电话号码
	@DataProvider(name="gp")
	public Object[][] gp(){ 
		Object[][] storageList=new Object[][]{
			{"13108225698",0,"600000","{\"code\":true"},{"15225632565",0,"600000","{\"code\":true"},{"17066986598",0,"600000","{\"code\":true"},{"19983246119",0,"600000","{\"code\":true"},
			{"19883246119",0,"600000","{\"code\":true"},{"18583246119",0,"600000","{\"code\":true"},{"14700253698",0,"600000","{\"code\":true"},{"14803699856",1,"600000","{\"code\":false"},
			{"16639859865",1,"600000","{\"code\":false"},{"17256896325",1,"600000","{\"code\":false"}
		};		
		return storageList;
	}
	//需要输入电话号码
	@DataProvider(name="mobilePhone")
	public Object[][] mobilePhone(){
		Object[][] storageList=new Object[][]{
			{"13108225698",0,"{\"code\":true"},{"15225632565",0,"{\"code\":true"},{"17066986598",0,"{\"code\":true"},{"19983246119",0,"{\"code\":true"},
			{"19883246119",0,"{\"code\":true"},{"18583246119",0,"{\"code\":true"},{"14700253698",0,"{\"code\":true"},{"14803699856",1,"{\"code\":false"},
			{"16639859865",1,"{\"code\":false"},{"17256896325",1,"{\"code\":false"}
		};		
		return storageList;
	}
}
