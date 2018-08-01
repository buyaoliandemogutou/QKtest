package QKtest.QKtest;

import java.util.List;
import com.base.AbstractBase;

/**
 * Unit test for simple App.
 */
public class AppTest extends AbstractBase    
{
	
	public static void main(String[] args) {
		 String str = getHttpInterface("https://iseedemo.yjifs.com/tools/tools/get_industry_lines");
		 System.out.println(str);
		 String rgex="\\{\"date\":(.*?),\"hs300\"";
	     List<String> lists = getSubUtil(str,rgex);
	     for (int i=0;i<lists.size();i++) {
	    	String url=lists.get(i);
			System.out.println(url);
		}
//		    System.out.println(getSubUtilSimple(str, rgex));  		
	}
}
