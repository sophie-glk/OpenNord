package main;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JList;

public class connector {
	
	public static Process p;
	
	public static void connect(MouseEvent event) throws Exception{
		String rights = "gksudo";
		
		@SuppressWarnings("unchecked")
		JList<String> j = (JList<String>) event.getSource();
		String value = j.getSelectedValue();
		String s = "auth-user-pass /opt/opennord/client.conf";
		int patched = 0;
		BufferedReader br = new BufferedReader(new FileReader("/opt/opennord/"+ value));
		    String line;
		    while ((line = br.readLine()) != null) {
		       if(line.contains(s)){
		          System.out.println("patching...");
		    	   patched = 1;
		       }
		 
		    }
            br.close();
		if(patched != 1){
		    Files.write(Paths.get("/opt/opennord/"+ value), s.getBytes(), StandardOpenOption.APPEND);
		
		}
		else{
			   System.out.println("File is patched!");
		}
		 
		Process kill = Runtime.getRuntime().exec(rights+" killall openvpn");
		
			System.out.println("Trying to kill openvpn...");

	      
				kill.waitFor();
		       
		         
		         Thread thread = new Thread(){
		        	    public void run(){
		        	    	 
								try {
									p = Runtime.getRuntime().exec("gksudo openvpn /opt/opennord/"+ value);
								} catch (IOException e1) {
									e1.printStackTrace();
									System.out.println("Command failed");
								}
							
		        	    	   String line;
		        	    	InputStream in = p.getInputStream();
		       			 BufferedReader input = new BufferedReader(new InputStreamReader(in));
		       			 
							try {
								while ((line = input.readLine()) != null) {
								    System.out.println(line);
								
    	    }
							} catch (IOException e) {
								e.printStackTrace();
							}
		        	  };
		         };
		thread.start();
			
			
		} 
	
	public static void connect(JList<String> list) throws Exception{
		String rights = "gksudo";
		JList<String> j = list;
		String value = j.getSelectedValue();
		String s = "auth-user-pass /opt/opennord/client.conf";
		int patched = 0;
		BufferedReader br = new BufferedReader(new FileReader("/opt/opennord/"+ value));
		    String line;
		    while ((line = br.readLine()) != null) {
		       if(line.contains(s)){
		          System.out.println("patching...");
		    	   patched = 1;
		       }
		 
		    }
            br.close();
		if(patched != 1){
		
		    Files.write(Paths.get("/opt/opennord/"+ value), s.getBytes(), StandardOpenOption.APPEND);
		
		}
		else{
			   System.out.println("File is patched!");
		}
		 
		
		Process k = Runtime.getRuntime().exec(rights+" killall openvpn");
		
			System.out.println("Trying to kill openvpn...");

	      
				k.waitFor();
		       
		         
		         Thread thread = new Thread(){
		        	    public void run(){
		        	    	 
								try {
									p = Runtime.getRuntime().exec("gksudo openvpn /opt/opennord/"+ value);
								} catch (IOException e1) {
									e1.printStackTrace();
									System.out.println("Command failed");
								}
							
		        	    	   String line;
		        	    	InputStream in = p.getInputStream();
		       			 BufferedReader input = new BufferedReader(new InputStreamReader(in));
		       			 
							try {
								while ((line = input.readLine()) != null) {
								    System.out.println(line);
								
    	    }
							} catch (IOException e) {
								e.printStackTrace();
							}
		        	  };
		         };
		thread.start();
			
			
		} 
	}
	
	

