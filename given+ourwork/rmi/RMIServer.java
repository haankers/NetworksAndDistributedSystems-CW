 /*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;


import java.rmi.registry.Registry;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
            //initialises the array
        if(receivedMessages == null){
            totalMessages = msg.totalMessages;
            receivedMessages = new int[msg.totalMessages];
        }
	System.out.println("Recieving messages");
        
		// TO-DO: Log receipt of the message
            //verifies a recepit of a message in the array
        receivedMessages[msg.messageNum] = 1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
            //note: that if it didn't receive the last message all the messages would not be logged
        if(msg.messageNum + 1 == totalMessages){
            System.out.println("Listing all lost messages: ");
            int count = 0;
            for(int i=0; i<totalMessages; i++) {
                if (receivedMessages[i] != 1) {
                    count++;
                    System.out.print(i+1 + ", ");
                }
            }
            
            if (count == 0){
                System.out.println("No message was lost");
            }
            System.out.println("Messages sent: " + totalMessages);
            System.out.println("Messages received: " + (totalMessages - count));
            System.out.println("Messages lost: " + count);
	    receivedMessages = null;
        }
	}


	public static void main(String[] args) {

		RMIServer rmiserv = null;

		// TO-DO: Initialise Security Manager
        if (System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }

	try{
		// TO-DO: Instantiate the server class
        rmiserv = new RMIServer();

		// TO-DO: Bind to RMI registry
        rebindServer("RMIServer", rmiserv);
	System.out.println("Binded successfully");
	} catch(Exception e) {
		System.out.println("Error in RMISERV");
	}
	}

	protected static void rebindServer(String serverURL, RMIServer server) {
	try{
		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
        
        LocateRegistry.createRegistry(1099);

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
        
        Naming.rebind(serverURL, server);
	} catch (Exception e){
		System.out.println("Error binding");	
	}
	}
}
