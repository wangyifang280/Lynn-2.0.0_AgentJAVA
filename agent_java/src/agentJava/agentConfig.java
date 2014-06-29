/**
 * @author wangyifang
 * @date 2014-6-18
 * @description: To get configuration.
 **/
package agentJava;

import org.apache.commons.configuration.Configuration;  
import org.apache.commons.configuration.PropertiesConfiguration;   
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.File;
//import java.util.ArrayList; import java.util.List;

public class agentConfig {
	private static String CONFLOCATION;
	static Logger agentLogger = Logger.getLogger("agentLogger");
	
	static {
		String systemJudge = System.getProperties().getProperty("os.name");
		if(systemJudge.contains("Windows")){
			System.out.println("windows");
			CONFLOCATION =  "d:\\conf";
		}
		if(systemJudge.contains("Linux")){
//			CONFLOCATION =  "/usr/local/lynn/conf";  //For Test(Linux)
			File configExist = new File("/media/CDROM/conf");
			if(configExist.exists()){
				CONFLOCATION =  "/media/CDROM/conf";
			}
			else{
				CONFLOCATION = "/usr/local/lynn/conf";//For Test
			}
		}
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@SuppressWarnings("finally")
	public static String getConf(String keyValue) throws Exception {
	    System.out.println("Now in config getting:"+keyValue);
		String retValue = "";
	  try {
		    Configuration config = new PropertiesConfiguration( CONFLOCATION);   
		    retValue = config.getString(keyValue);
	  } catch (Exception e) {
//	TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	  finally
	  {
//	   System.out.println("SUCCESS");
		  if(retValue.isEmpty()){
			  agentLogger.warn("This config value is NULL:"+keyValue);
		  }
	     return retValue;
	  }
	}
}