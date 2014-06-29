package agentJava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadThread implements Runnable{

	public ReadThread(InputStreamReader p){
		this.p =p;
	}
	public InputStreamReader p;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(p); 
//      StringBuffer sb = new StringBuffer(); 
       String inline; 
//       Thread errorThread = new Thread(target);
      try {
		while (null != (inline = br.readLine())) {  
		   System.out.println(inline);
		 }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	}
	

}
