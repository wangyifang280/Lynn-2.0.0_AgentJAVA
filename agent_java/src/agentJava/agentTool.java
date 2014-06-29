/**
 * @author wangyifang
 * @date 2014-6-18
 * @description: Some tools for agent.
 **/
package agentJava;

public class agentTool {
	public static String cpuUsage(){
		return "0";
	}
public static String memUsage(){
		return "0";
    }
public static String bytesIn(){
	return "0";
    }
public static String bytesOut(){
	return "0";
   }

static void setWindowsIPAndDns(String newip, String dns1, String dns2)
		   throws Exception {
		String gateWay;
		if(newip.contains("172.16.60")){
			gateWay = "172.16.0.254";
		}
		else if(newip.contains("172.17.60")){
			gateWay = "172.17.0.254";
		}
		else{
			gateWay = "192.168.122.1";
		}
		  Runtime.getRuntime().exec(
		    "netsh interface ip set addr \"本地连接\" static " + newip
		      + " 255.255.255.0 "+gateWay+" 1");

		  Runtime.getRuntime().exec(
		    "netsh interface ip set dns \"本地连接\" static " + dns1);


		Runtime.getRuntime().exec("netsh interface ip add dns name=\"本地连接\" addr=" + dns2 + " index=2 ");

		  }
/*
		 private static void setIPAndDnsNull() throws Exception {
		  Runtime.getRuntime().exec(
		  //这个只是留着备用的
		    "netsh interface ip set address \"本地连接\" dhcp");

		  Runtime.getRuntime().exec("netsh interface ip delete dns \"本地连接\" all");
		  
		 }
	*/	 
		 static void setLinuxIPAndDns(String newip, String dns1, String dns2)
				   throws Exception {
				String gateWay;
				if(newip.contains("172.16.60")){
					gateWay = "172.16.0.254";
				}
				else if(newip.contains("172.17.60")){
					gateWay = "172.17.0.254";
				}
				else{
					gateWay = "192.168.122.1";
				}
				  Runtime.getRuntime().exec( "route add default gw "+gateWay);//set gateway
				  String interfaceStr = agentConfig.getConf("interface");
				  Runtime.getRuntime().exec("ifconfig " + interfaceStr + " " + newip +" netmask "+"255.255.255.0");//set IP
				  Runtime.getRuntime().exec("echo "+"nameserver 8.8.8.8"+" >/etc/resolv.conf");
				 Runtime.getRuntime().exec("ifconfig " + interfaceStr+" up");

				  }
}
