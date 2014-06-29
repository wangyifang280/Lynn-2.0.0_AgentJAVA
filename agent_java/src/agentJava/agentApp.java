/**
 * @author wangyifang
 * @date 2014-6-20
 * @description: To deal with app. Include: ①.Download app ②Run app.
 **/
package agentJava;

//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.IOUtils;
//import org.apache.hadoop.util.Progressable;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.URI;

import java.io.BufferedReader;
import java.io.File;
//import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;   
   
import java.util.zip.ZipEntry;   
import java.util.zip.ZipInputStream; 
import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;   

public class agentApp {
	/*------------------------SYSCONF--------------------------*/
	private static String appInstallPath;
	static Logger agentLogger = Logger.getLogger("agentLogger");
	/*--------------需根据平台不同配置的变量--------------------*/
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
	
    private static final int buffer = 2048;    
    
	public static int appManager() throws Exception{
		System.out.println("Now in appManager...");
		appInstall();
		appRun();
		return 0;
	}
    
    
	private static void downFromHdfs() throws Exception {
		  String appFileEndpoint = agentConfig.getConf("hdfs_endpoint");
		  String appHdfsPath = agentConfig.getConf("app_src_path");

//		  String appOutFile = appInstallPath + agentConfig.getConf("app_name");
		  int idx = appHdfsPath.lastIndexOf("/");
		  String appName = appHdfsPath.substring(idx+1,appHdfsPath.length());
		  String appOutFile = appInstallPath + appName;
		  //To Do:确定用Appname还是从路径中提取名字
		  
		  //eg: String dst = "hdfs://10.128.85.5:9000/lynn/common/app/getURL/ipmsg.exe";  
		  String dst = "hdfs://"+ appFileEndpoint +"/"+ appHdfsPath;
		  
		  downLoadFromHdfsBasic(dst, appOutFile);
		  
		  //unzip
		  if(dst.endsWith(".zip"))
			  unZip(appOutFile);
		  
		  //down input
		  String timeStamp = agentConfig.getConf("timestamp");
		  String resultPathStr = agentConfig.getConf("app_result_path");
		  String inputFilePath = resultPathStr.substring(0, resultPathStr.indexOf("/result/"))+"/input/"+timeStamp+'/';
		  //To do down a dir :1、list a dir；2、down it
		  if(agentConfig.getConf("is_input_file").equalsIgnoreCase("1")){
			  agentLogger.info("Download input...");
			  System.out.println(inputFilePath);
			  getInputFromHdfs(inputFilePath);
		  }
		 }
	
	public static void appInstall() throws Exception{
		System.out.println("Now Install App...");
		//download
		downFromHdfs();
	
	}
	
	public static void appRun() throws Exception{
		System.out.println("Now Run App...");
		agentLogger.info("App Run...");
		
		String appHdfsPath = agentConfig.getConf("app_src_path");
		int idx = appHdfsPath.lastIndexOf("/");
		String appName = appHdfsPath.substring(idx+1,appHdfsPath.length());

//        String installFileName = agentConfig.getConf("app_name");
        File fileInAllPath=new File(appInstallPath+"/"+appName);
        
        if(!fileInAllPath.exists()){
        	System.out.println("No File To Run...");
        	//To do Log
        	agentLogger.error("Needed app is no exist...");
        	return ;
        }
    	//To do加可执行权限	
        fileInAllPath.setExecutable(true);
		//运行
        File installDir = new File(appInstallPath);
        String appCmd =  agentConfig.getConf("exe");
        Runtime runCmd = Runtime.getRuntime(); 
        Process p = runCmd.exec(appCmd, null, installDir); 
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream())); 
//        StringBuffer sb = new StringBuffer(); 
        String inline; 
        
        ReadThread rd = new ReadThread(new InputStreamReader(p.getErrorStream()));
        Thread errorThread = new Thread(rd);
        errorThread.start();
  //这三条语句得加上，否则得话，在无法发送心跳的时候，程序会卡死。
        
        while (null != (inline = br.readLine())) { 
    //       sb.append(inline).append("\n"); 
    	   System.out.println(inline);
    //		这句一定得加，否则缓冲区溢出，程序憋shi了
       } 
        p.waitFor();
	}
	 
	
	
    public static void unZip(String path)    
    {   
    	agentLogger.info("unzip...");
        int count = -1;   
        int index = -1;   
      	System.out.println("UnZiping...");       
        String savepath = "";   
           
  //      savepath = path.substring(0, path.lastIndexOf("\\")) + "\\";   
        savepath = path.substring(0, path.lastIndexOf("/")) + "/";  
        try    
        {   
            BufferedOutputStream bos = null;   
            ZipEntry entry = null;   
            FileInputStream fis = new FileInputStream(path);    
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));   
               
            while((entry = zis.getNextEntry()) != null)    
            {   
                byte data[] = new byte[buffer];    
  
                String temp = entry.getName();   
                   
                index = temp.lastIndexOf("/");   
                if(index > -1)   
                    temp = temp.substring(index+1);   
                temp = savepath + temp;   
                   
                File f = new File(temp);   
                f.createNewFile();   
  
                FileOutputStream fos = new FileOutputStream(f);   
                bos = new BufferedOutputStream(fos, buffer);   
                   
                while((count = zis.read(data, 0, buffer)) != -1)    
                {   
                    bos.write(data, 0, count);   
                }   
  
                bos.flush();   
                bos.close();   
            }   
  
            zis.close();   
  
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
    }   
    
    private static void downLoadFromHdfsBasic(String dst,String outFile) throws Exception {
		  System.out.println("Read starting...");
		  Configuration conf = new Configuration();  
		  FileSystem fs = FileSystem.get(URI.create(dst), conf);
		  FSDataInputStream hdfsInStream = fs.open(new Path(dst));
		  OutputStream out = new FileOutputStream(outFile); 
		  byte[] ioBuffer = new byte[1024];
		  int readLen = hdfsInStream.read(ioBuffer);

		  while(-1 != readLen){
		  out.write(ioBuffer, 0, readLen);  
		  readLen = hdfsInStream.read(ioBuffer);
		  }
		  out.close();
		  hdfsInStream.close();
		  fs.close();
    }    
    
    private static void getInputFromHdfs(String dst) throws Exception {
    	System.out.println("Get input...");
    //	String dst = "hdfs://192.168.56.101:9000/test/";
    	
          String inputPath = "hdfs://" + agentConfig.getConf("hdfs_endpoint") +"/"+dst;
          System.out.println(inputPath);
    	  Configuration conf = new Configuration();  
    	  FileSystem fs = FileSystem.get(URI.create(inputPath), conf);
    	  FileStatus fileList[] = fs.listStatus(new Path(inputPath));
    	  int size = fileList.length;
    	  for(int i = 0; i < size; i++){
    	  System.out.println("name:" + fileList[i].getPath().getName() + "/t/tsize:" + fileList[i].getLen());
    	  if(fileList[i].getLen()!=0){
    //		readFromHdfs(fileList[i].getPath().getName());
    		  String inputOutFile = appInstallPath + fileList[i].getPath().getName();
    	      downLoadFromHdfsBasic(inputPath+"/" + fileList[i].getPath().getName(),inputOutFile);
    	  }
    	  }
    	  System.out.println("Get input end...");
    	  fs.close();
    	 } 
    
}
