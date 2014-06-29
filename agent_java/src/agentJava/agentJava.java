/**
 * @author wangyifang
 * @date 2014-6-18
 * @description: Main.
 **/
package agentJava;
import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;   

public class agentJava {
	public static void main(String[] args) throws Exception {
		envInit();
		Logger agentLogger = Logger.getLogger("agentLogger");
		PropertyConfigurator.configure("log4j.properties");
		
		if(args.length == 0){
			System.out.println("appManager...");
			agentLogger.info("AppManager...");
			agentApp.appManager();
			return ;
		}
		
		if (args != null && args.length > 0) {
			System.out.println("Get argument...");
			if (args.length != 2) {
				System.out.println("Arg num is not 2...");
				agentLogger.warn("Argument is not 2!");
				System.exit(2);
			}
			
			if (args[0].equalsIgnoreCase("report_state")){
				System.out.println("Report State...");
				if(args[1].contains("--") && args[1].contains("=")){
					int idx = args[1].lastIndexOf("=");
					String appState = args[1].substring(idx+1,args[1].length());
					agentThread.heartBeatThread(appState);
				}
			}
			
			if (args[0].equalsIgnoreCase("recycle_results")){
				System.out.println("Result Recycle...");
				if(args[1].contains("--") && args[1].contains("=")){
					int idx = args[1].lastIndexOf("=");
					String fileLocation = args[1].substring(idx+1,args[1].length());
					System.out.println(fileLocation);
					String resultPath = agentConfig.getConf("app_result_path");
					agentUpload.resultUpload(fileLocation, resultPath);
				}
			}
			System.exit(2);
		}
		 return ;
	}
	
	
	public static void envInit() throws Exception{
		String systemJudge = System.getProperties().getProperty("os.name");
		String ipStr = agentConfig.getConf("ip");
		if(agentConfig.getConf("for_test") == "1")
			return ;
		if(systemJudge.contains("Windows")){
			agentTool.setWindowsIPAndDns( ipStr,"8.8.8.8","8.8.4.4"); //Windows Set Ip
		}
		if(systemJudge.contains("Linux")){
			agentTool.setLinuxIPAndDns( ipStr,"8.8.8.8","8.8.4.4");
		}
	}
}

