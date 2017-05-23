package com.techm.ProMunchies.MessageSendingCtrl;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import com.techm.ProMunchies.MessageModel.FileDetailsModel;

public class MunchieClientMsgCtrl {
	final static int SERVER_PORT = 3333;
//	final static String SERVER_ADDR = "127.0.0.1";
	final static String SERVER_ADDR = "10.10.113.1";
	
	public void sendMessage(String fileName, String hostName, String userName) {
		FileDetailsModel fd1 = new FileDetailsModel(fileName, hostName, userName);
		Socket socketToServer = null;
		ObjectOutputStream out = null;
	    try {  
	    	socketToServer = new Socket(SERVER_ADDR, SERVER_PORT);
	    	out = new ObjectOutputStream(socketToServer.getOutputStream());
	    	System.out.println("Sent Server request to get File information");
	    	System.out.println("File name: " +fileName+ " Hostname: "+ hostName+ " User name: "+userName);
	    	out.writeObject(fd1);
	    	out.flush();
	    	out.close();
	    	socketToServer.close();
	    } catch(Exception e){
	    		System.out.println(e);
	    }
	}
	
	public MunchieClientMsgCtrl(){
//		try {
//			receiveMessage();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	};
	
	public void receiveMessage() throws UnknownHostException, IOException {
		String serverAddress = JOptionPane.showInputDialog(
			"Enter IP Address of a machine that is\n" +
			"running the date service on port 9090:");
		Socket s = new Socket(serverAddress, 3333);
		BufferedReader input =
			new BufferedReader(new InputStreamReader(s.getInputStream()));
		String answer = input.readLine();
		if(answer != null){
			setMsg(answer);
		}
		JOptionPane.showMessageDialog(null, answer);
		s.close();
		System.exit(0);	
	}
	
	String msg = "";
	public void setMsg(String msg){};
	public String getMsg(){
		return msg;
	};
}


