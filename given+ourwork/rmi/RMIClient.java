package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.rmi.RMISecurityManager;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {
		System.out.println("Attempting to send messages with client");
		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length != 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
            //sets security manager
        System.setSecurityManager(new RMISecurityManager());
        
        try{
	System.out.print("Attempting to bind with server:");
		// TO-DO: Bind to RMIServer
            //checks the url for exceptions and bings the lookup server
        iRMIServer = (RMIServerI) Naming.lookup(urlServer);
	System.out.println("Successful");
        

		// TO-DO: Attempt to send messages the specified number of times
        for( int i=0; i<numMessages; i++){
            //creates a new message and calls RMI server receive this message
            MessageInfo msg = new MessageInfo(numMessages,i);
            iRMIServer.receiveMessage(msg);
        }
} catch (Exception e) {
System.out.println("Error in client");
}
        
	}
}
