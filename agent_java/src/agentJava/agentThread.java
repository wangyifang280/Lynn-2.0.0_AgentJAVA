/**
 * @author wangyifang
 * @date 2014-6-23
 * @description: To send heartbeat. 
 **/
package agentJava;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

//import org.apache.thrift.*;  
import org.apache.thrift.protocol.*;  
import org.apache.thrift.transport.*; 
//import org.apache.thrift.TException;    
import gen.Executor;


public class agentThread {
	private static int appStateInt = 0;
	static Logger agentLogger = Logger.getLogger("agentLogger");
	static{
		PropertyConfigurator.configure("log4j.properties");
	}
	
	public static void heartBeatThread(String appStateRT) throws Exception {
	    System.out.println("Now in heartbeat thread...");
		//App State Transfer
		if (appStateRT.equalsIgnoreCase("APP_RUNNING")) 
			appStateInt = 2;
		else if (appStateRT.equalsIgnoreCase("APP_FINISHED")) 
			appStateInt = 3;
		else if (appStateRT.equalsIgnoreCase("APP_FAILED")) 
			appStateInt = 4;
		else {
			agentLogger.error("Unknow App State");

		}

		String jobIdStr = agentConfig.getConf("job_id");
		String taskIdStr = agentConfig.getConf("task_id");
		String executorEndpoint = agentConfig.getConf("executor_endpoint");
		//To do: Get usage of machine
		String cpuUsageStr = agentTool.cpuUsage();
		String memUsageStr = agentTool.memUsage();
		String bytesIn = agentTool.bytesIn();
		String bytesOut = agentTool.bytesOut();
		//组装心跳信息
		String heartBeatString = "[ " +"JOB_ID = "+jobIdStr +"; " +
		"TASK_ID = "+ taskIdStr+
		"; VMHB_CPU = "+cpuUsageStr+
		"; VMHB_MEMORY = "+memUsageStr+
		"; VMHB_BYTES_IN = "+bytesIn+
		"; VMHB_BYTES_OUT = "+bytesOut+
		"; VMHB_STATE = "+Integer.toString(appStateInt) +" ]";
		
		System.out.println(heartBeatString);
		String executorIP = executorEndpoint.split(":")[0];
		String executorPortStr = executorEndpoint.split(":")[1];
		int executorPort = Integer.parseInt(executorPortStr);
		TTransport transport = new TSocket(executorIP,executorPort ); 
        TProtocol protocol = new TBinaryProtocol(transport);  
        Executor.Client client =new Executor.Client(protocol);  
        transport.open();  
        System.out.println("Send heartbeat...");  
        boolean retVal = client.SendVMHeartbeat(heartBeatString);  
        System.out.print(retVal);
        transport.close();
		//发送心跳
	}
}
