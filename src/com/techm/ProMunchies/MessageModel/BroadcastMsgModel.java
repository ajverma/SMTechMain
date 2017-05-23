package com.techm.ProMunchies.MessageModel;



import java.io.Serializable;
import java.util.ArrayList;


public class BroadcastMsgModel implements Serializable {
  
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private String fileName;
	private ArrayList<String> HostArrayList;
	private ArrayList<String> UserArrayList;
	
	//public BroadcastMsgModel() {
		// TODO Auto-generated constructor stub
	//}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public ArrayList<String> getHostArrayList() {
		return HostArrayList;
	}
	public void setHostArrayList(ArrayList<String> HostArrayList) {
		this.HostArrayList = HostArrayList;
	}
	
	public ArrayList<String> getUserArrayList() {
		return UserArrayList;
	}
	public void setUserArrayList(ArrayList<String> UserArrayList) {
		this.UserArrayList = UserArrayList;
	}
	
	public BroadcastMsgModel(String fileName, ArrayList<String> HostArrayList, ArrayList<String> UserArrayList) {
		this.fileName=fileName;
		this.HostArrayList=HostArrayList;
		this.UserArrayList=UserArrayList;
	}
	
	
}
