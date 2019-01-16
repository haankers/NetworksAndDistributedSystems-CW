package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import java.io.*;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;

	private void run() throws SocketTimeoutException{
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever

		while(!close){
			//pacsize is randomly chosen has to match client.java we thought 2000 would be enough
			pacSize = 60000;
			pacData = new byte[60000];

			pac = new DatagramPacket(pacData, pacSize);
			try {
				//thought this would be 30 seconds as it's in milliseconds
				recvSoc.setSoTimeout(30000);
				recvSoc.receive(pac);
			} catch (IOException e) {
				System.out.println("Error receiving packet");
				System.exit(-1);
			}
			processMessage(pac.getData());
		}
	}

	public void processMessage(byte[] data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		ObjectInputStream ois;

		try {
			ois = new ObjectInputStream(new BufferedInputStream(byteStream));
			msg = (MessageInfo) ois.readObject();
			ois.close();
		} catch (Exception e) {
			System.out.println("Error creating the stream");
			System.exit(-1);
		}

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (receivedMessages == null) {
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		//note won't print if the last message is lost
		if (msg.messageNum + 1 == msg.totalMessages) {
			close = true;

			System.out.println("Listing all lost messages: ");
			int count = 0;
			for (int i = 0; i < totalMessages; i++) {
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


	public UDPServer(int portno) {
		// TO-DO: Initialise UDP socket for receiving data
		try {
			recvSoc = new DatagramSocket(portno);
		} catch (SocketException e) {
			System.out.println("Error creating the server port");
			System.exit(-1);
		}
		System.out.println("UDPServer initialized");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length != 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer udpserv = new UDPServer(recvPort);
		try {
			System.err.println("running the server");
			udpserv.run();
		} catch (SocketTimeoutException e) {
			System.err.println("Error Socket Timeout");
		}
	}

}
