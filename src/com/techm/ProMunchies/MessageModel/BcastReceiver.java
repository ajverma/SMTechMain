package com.techm.ProMunchies.MessageModel;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.techm.ProMunchies.MessageModel.BroadcastMsgModel;


public class BcastReceiver {
	
	private final static Logger LOGGER = Logger.getLogger(BcastReceiver.class.getName());
	
    final static String INET_ADDR = "224.0.0.1";
    final static int PORT = 4444;
    
    
	
	public static void main(String[] args) {
		
		InetAddress address= null;
		
		try {
			address = InetAddress.getByName(INET_ADDR);
		} catch (UnknownHostException e) {
			LOGGER.log(Level.SEVERE, "Cannot get the requested inet address");
			e.printStackTrace();
		}
	

		byte[] buf = new byte[1000];
		
		
		
		try (MulticastSocket clientSocket = new MulticastSocket(PORT)){
            //Joint the Multicast group.
            clientSocket.joinGroup(address);
            
                // Receive the information and print it.
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);
                
              //  System.out.println("Buffer length is "+buf.length);
                //System.out.println("Buffer length is "+new String(buf));
                
                
                
               ByteArrayInputStream bais = new ByteArrayInputStream(buf);
               
            /*   int ascii_val = 0;
               while ((ascii_val = bais.read()) != -1) {  
                   //Conversion of a byte into character  
                   char ch = (char) ascii_val;  
                   System.out.println("ASCII value of Character is:" + ascii_val + "; Special character is: " + ch);  
                 }  */
               
               
                ObjectInputStream ois = null;
                BroadcastMsgModel bmsg = null;
                 
                try{
                	ois = new ObjectInputStream(bais);
                	bmsg =(BroadcastMsgModel) ois.readObject();
                	ois.close();
                	//System.out.println("Filename is "+bmsg.getFileName());
                	
                	//System.out.println(bmsg.toString());
                	
                } catch (EOFException eof){
                	LOGGER.log(Level.SEVERE, "End of file reached, please check the object");
                	eof.printStackTrace();
                }
                
                
                for (String user: bmsg.getUserArrayList())
                {
                	LOGGER.log(Level.INFO, user+" is currently working on "+bmsg.getFileName());                	
                }	
                
             
        } catch (IOException | ClassNotFoundException ex) {
        	LOGGER.log(Level.SEVERE, "Cannnot process the received Datagram packet");
            ex.printStackTrace();

        }
    }
		
		
				
	}



