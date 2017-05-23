package com.techm.ProMunchies.MessageModel;




import java.io.Serializable;


public class FileDetailsModel implements Serializable {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private String userName;
	private String hostName;
	
	public FileDetailsModel() {
		// TODO Auto-generated constructor stub
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public FileDetailsModel(String fileName, String hostName, String userName) {
		this.fileName=fileName;
		this.hostName=hostName;
		this.userName=userName;
	}
	//@Override
	//public String toString() {
	//	return "User [fileName=" + fileName + ", userName=" + userName + ", hostName=" + hostName + "]";
	//}
	
	
}
