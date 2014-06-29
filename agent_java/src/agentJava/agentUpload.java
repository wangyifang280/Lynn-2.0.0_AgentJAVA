/**
 * @author wangyifang
 * @date 2014-6-19
 * @description: To upload the result. 
 **/
package agentJava;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class agentUpload {
	private static String appInstallPath;
	static Logger agentLogger = Logger.getLogger("agentLogger");
	static {
		String systemJudge = System.getProperties().getProperty("os.name");
		if(systemJudge.contains("Windows")){
			appInstallPath = "C:/lynn/app/";
		}
		if(systemJudge.contains("Linux")){
			appInstallPath = "/usr/local/lynn/app/";
		}
		PropertyConfigurator.configure("log4j.properties");
	}
	
	public static void resultUpload(String localFilePath, String hdfsPath) throws Exception {
		  agentLogger.info("Result Upload...");
		  String hdfsEndpoint = agentConfig.getConf("hdfs_endpoint");
//		  String dst = "hdfs://192.168.56.101:9000/test/ipmsg.zip";
		  String resultName = localFilePath.substring(localFilePath.lastIndexOf("/")+1, localFilePath.length());
		  String jobId = agentConfig.getConf("job_id");
		  String taskId = agentConfig.getConf("task_id");
		  String dst = "hdfs://" + hdfsEndpoint + hdfsPath+"/"+jobId+"/"+taskId+"_"+resultName; //加上localFilePath的文件名
		  System.out.println(appInstallPath);
		  System.out.println("Upload starting...");
		  String localResultPath = appInstallPath + localFilePath;
		  InputStream in = new BufferedInputStream(new FileInputStream(localResultPath));
		  Configuration conf = new Configuration();
		  FileSystem fs = FileSystem.get(URI.create(dst), conf);
		  OutputStream out = fs.create(new Path(dst), new Progressable() {
		   public void progress() {
		    System.out.print(".");
		   }
		  });
		    IOUtils.copyBytes(in, out, 4096, true);
		    System.out.print("SUCCESS");
		 }
	}
